// 购物车前端交互：数量修改 / 移除 / 清空（对应 CartServlet action=update/remove/clear）
(function () {
    var ctx = document.body.getAttribute('data-context-path') || '';
    var submitting = false; // 防止重复提交

    function fmt(n) {
        return Number(n).toFixed(2);
    }

    // 校验数量输入：必须是大于 0 的整数，否则返回 null
    function parseQuantity(value) {
        var text = String(value).trim();
        var n = Number(text);
        if (text === '' || isNaN(n) || Math.floor(n) !== n || n < 1) {
            return null;
        }
        return n;
    }

    // 在页面显示短暂的反馈提示
    function showFeedback(msg) {
        var box = document.getElementById('cart-feedback');
        if (!box) return;
        box.textContent = msg;
        box.style.display = 'block';
        if (window.feedbackTimer) clearTimeout(window.feedbackTimer);
        window.feedbackTimer = setTimeout(function () {
            box.style.display = 'none';
        }, 1500);
    }

    // 根据单价 × 数量计算单个商品小计并更新显示
    function updateSubtotal(tr) {
        var price = parseFloat(tr.getAttribute('data-price'));
        var qty = parseQuantity(tr.querySelector('.qty-value').value);
        if (isNaN(price) || qty === null) return;
        tr.querySelector('.qty-subtotal').textContent = '¥ ' + fmt(price * qty);
    }

    // 根据选中的商品重新计算总价（前端展示用，最终金额以后端结算为准）
    function updateTotal() {
        var total = 0;
        document.querySelectorAll('.cart-item-check').forEach(function (ck) {
            if (!ck.checked) return;
            var tr = ck.closest('tr');
            var price = parseFloat(tr.getAttribute('data-price'));
            var qty = parseQuantity(tr.querySelector('.qty-value').value);
            if (!isNaN(price) && qty !== null) {
                total += price * qty;
            }
        });
        var totalEl = document.getElementById('cart-total');
        if (totalEl) totalEl.textContent = '¥ ' + fmt(total);
    }

    function updateQuantity(tr, pid, quantity) {
        if (submitting) return;   // 避免重复提交
        if (quantity < 1) return; // 非法数量，不发起请求

        var stock = parseInt(tr.getAttribute('data-stock'), 10);
        if (!isNaN(stock) && quantity > stock) {
            alert('库存不足');
            return;
        }

        submitting = true;
        var url = ctx + '/cart?action=update&pid=' + encodeURIComponent(pid)
                + '&quantity=' + encodeURIComponent(quantity);
        fetch(url, { method: 'POST' })
            .then(function (resp) { return resp.json(); })
            .then(function (data) {
                if (data.code === 0) {
                    tr.querySelector('.qty-value').value = data.quantity;
                    updateSubtotal(tr);
                    updateTotal();
                    showFeedback('数量已更新');
                } else {
                    // 展示服务端错误信息；数量保持不变（即恢复原数量）
                    alert(data.msg || '操作失败');
                }
            })
            .catch(function () {
                alert('网络错误，请重试');
            })
            .then(function () {
                submitting = false;
            });
    }

    function removeItem(tr, pid) {
        if (submitting) return; // 防止重复点击
        if (!confirm('确定要删除这个商品吗？')) return;
        submitting = true;
        var url = ctx + '/cart?action=remove&pid=' + encodeURIComponent(pid);
        fetch(url, { method: 'POST' })
            .then(function (resp) { return resp.json(); })
            .then(function (data) {
                if (data.code === 0) {
                    tr.remove();
                    showFeedback('商品已删除');
                    var remaining = document.querySelectorAll('.cart-table tbody tr').length;
                    if (remaining === 0) {
                        location.reload(); // 空购物车 → 重新渲染服务端空态
                    } else {
                        updateTotal();
                    }
                } else {
                    // 失败：保留原行并提示错误
                    alert(data.msg || '操作失败');
                }
            })
            .catch(function () {
                alert('网络错误，请重试');
            })
            .then(function () {
                submitting = false;
            });
    }

    function clearCart() {
        if (submitting) return; // 防止重复点击
        if (!confirm('确认清空购物车？')) return;
        submitting = true;
        fetch(ctx + '/cart?action=clear', { method: 'POST' })
            .then(function (resp) { return resp.json(); })
            .then(function (data) {
                if (data.code === 0) {
                    location.reload(); // 成功后才刷新，服务端渲染空态
                } else {
                    alert(data.msg || '操作失败');
                }
            })
            .catch(function () {
                alert('网络错误，请重试');
            })
            .then(function () {
                submitting = false;
            });
    }

    var rows = document.querySelectorAll('.cart-table tbody tr');
    rows.forEach(function (tr) {
        var pid = tr.getAttribute('data-pid');
        var qtyInput = tr.querySelector('.qty-value');

        tr.querySelector('.qty-minus').addEventListener('click', function () {
            var cur = parseQuantity(qtyInput.value);
            if (cur === null) { alert('数量无效'); return; }
            updateQuantity(tr, pid, cur - 1);
        });
        tr.querySelector('.qty-plus').addEventListener('click', function () {
            var cur = parseQuantity(qtyInput.value);
            if (cur === null) { alert('数量无效'); return; }
            updateQuantity(tr, pid, cur + 1);
        });

        // 直接输入数量：记录上次有效值，非法输入时恢复并提示
        qtyInput.addEventListener('focus', function () {
            qtyInput.setAttribute('data-prev', qtyInput.value);
        });
        qtyInput.addEventListener('change', function () {
            var qty = parseQuantity(qtyInput.value);
            if (qty === null) {
                alert('请输入大于 0 的整数数量');
                qtyInput.value = qtyInput.getAttribute('data-prev');
                return;
            }
            updateQuantity(tr, pid, qty);
        });

        tr.querySelector('.qty-remove').addEventListener('click', function () {
            removeItem(tr, pid);
        });
    });

    // 全选 / 取消全选
    var selectAll = document.getElementById('cart-select-all');
    var itemChecks = document.querySelectorAll('.cart-item-check');
    if (selectAll) {
        selectAll.addEventListener('change', function () {
            itemChecks.forEach(function (ck) {
                ck.checked = selectAll.checked;
            });
            updateTotal();
        });
        itemChecks.forEach(function (ck) {
            ck.addEventListener('change', function () {
                var allChecked = true;
                itemChecks.forEach(function (c) {
                    if (!c.checked) allChecked = false;
                });
                selectAll.checked = allChecked;
                updateTotal();
            });
        });
    }

    var clearBtn = document.getElementById('cart-clear');
    if (clearBtn) {
        clearBtn.addEventListener('click', clearCart);
    }

    // 提交订单：收集勾选商品(pid + 最新数量)写入隐藏域，防止重复点击
    var checkoutForm = document.getElementById('checkout-form');
    var checkoutBtn = document.getElementById('checkout-submit');
    if (checkoutForm && checkoutBtn) {
        checkoutForm.addEventListener('submit', function (e) {
            if (checkoutBtn.disabled) {
                e.preventDefault(); // 已提交中，阻止重复提交
                return;
            }

            // 移除旧的勾选商品隐藏域，按当前勾选重新生成
            checkoutForm.querySelectorAll('input[name="pid"], input[name="quantity"]').forEach(function (el) {
                el.remove();
            });

            var anyChecked = false;
            var invalid = false;
            document.querySelectorAll('.cart-item-check').forEach(function (ck) {
                if (!ck.checked || invalid) return;
                anyChecked = true;
                var tr = ck.closest('tr');
                // 以输入框当前数量为准，避免“改数量后立即提交”的异步竞态
                var qty = parseQuantity(tr.querySelector('.qty-value').value);
                if (qty === null) {
                    invalid = true;
                    return;
                }

                var pidInput = document.createElement('input');
                pidInput.type = 'hidden';
                pidInput.name = 'pid';
                pidInput.value = tr.getAttribute('data-pid');
                checkoutForm.appendChild(pidInput);

                var qtyInput = document.createElement('input');
                qtyInput.type = 'hidden';
                qtyInput.name = 'quantity';
                qtyInput.value = qty;
                checkoutForm.appendChild(qtyInput);
            });

            if (invalid) {
                e.preventDefault();
                showFeedback('勾选商品的数量无效，请重新输入');
                return;
            }
            if (!anyChecked) {
                e.preventDefault();
                showFeedback('请至少勾选一件商品再结算');
                return;
            }
            checkoutBtn.disabled = true;
        });
    }

    // 加购：商品详情页等通过 window.addToCart(pid, quantity, callback) 调用
    // callback：加购成功后执行（例如“立即购买”跳转购物车）
    window.addToCart = function (pid, quantity, callback) {
        if (submitting) return; // 防止重复点击
        quantity = quantity || 1;
        submitting = true;
        fetch(ctx + '/cart?action=add&pid=' + encodeURIComponent(pid)
                + '&quantity=' + encodeURIComponent(quantity), { method: 'POST' })
            .then(function (resp) {
                var ct = resp.headers.get('content-type') || '';
                if (ct.indexOf('application/json') === -1) {
                    window.location.href = ctx + '/login.jsp'; // 会话失效，跳登录
                    return null;
                }
                return resp.json();
            })
            .then(function (data) {
                if (data === null) return;
                if (data.code === 0) {
                    alert('已加入购物车');
                    window.refreshCartCount && window.refreshCartCount();
                    if (typeof callback === 'function') {
                        callback();
                    }
                } else {
                    alert(data.msg || '加入失败');
                }
            })
            .catch(function () {
                alert('网络错误，请重试');
            })
            .then(function () {
                submitting = false;
            });
    };

    // 导航角标：查询购物车商品种类数并更新
    window.refreshCartCount = function () {
        var badge = document.getElementById('cart-count');
        if (!badge) return;
        fetch(ctx + '/cart?action=count', { method: 'POST' })
            .then(function (resp) { return resp.json(); })
            .then(function (data) {
                if (data.code === 0) {
                    var n = data.count || 0;
                    badge.textContent = n > 0 ? n : '';
                    badge.style.display = n > 0 ? 'inline-block' : 'none';
                }
            })
            .catch(function () { /* 查询失败忽略，保持角标原样 */ });
    };
})();
