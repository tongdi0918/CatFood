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
                    tr.querySelector('.qty-subtotal').textContent = '¥ ' + fmt(data.subtotal);
                    document.getElementById('cart-total').textContent = '¥ ' + fmt(data.total);
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
                    var remaining = document.querySelectorAll('.cart-table tbody tr').length;
                    if (remaining === 0) {
                        location.reload(); // 空购物车 → 重新渲染服务端空态
                    } else {
                        document.getElementById('cart-total').textContent = '¥ ' + fmt(data.total);
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

    var clearBtn = document.getElementById('cart-clear');
    if (clearBtn) {
        clearBtn.addEventListener('click', clearCart);
    }

    // 提交订单：防止连续点击
    var checkoutForm = document.getElementById('checkout-form');
    var checkoutBtn = document.getElementById('checkout-submit');
    if (checkoutForm && checkoutBtn) {
        checkoutForm.addEventListener('submit', function (e) {
            if (checkoutBtn.disabled) {
                e.preventDefault(); // 已提交中，阻止重复提交
                return;
            }
            checkoutBtn.disabled = true;
        });
    }

    // 加购：商品详情页等通过 window.addToCart(pid, quantity) 调用
    window.addToCart = function (pid, quantity) {
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
})();
