// 购物车前端交互：数量修改 / 移除 / 清空（对应 CartServlet action=update/remove/clear）
(function () {
    var ctx = document.body.getAttribute('data-context-path') || '';
    var submitting = false; // 防止重复提交

    function fmt(n) {
        return Number(n).toFixed(2);
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
                    tr.querySelector('.qty-value').textContent = data.quantity;
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
        tr.querySelector('.qty-minus').addEventListener('click', function () {
            var cur = parseInt(tr.querySelector('.qty-value').textContent, 10);
            updateQuantity(tr, pid, cur - 1);
        });
        tr.querySelector('.qty-plus').addEventListener('click', function () {
            var cur = parseInt(tr.querySelector('.qty-value').textContent, 10);
            updateQuantity(tr, pid, cur + 1);
        });
        tr.querySelector('.qty-remove').addEventListener('click', function () {
            removeItem(tr, pid);
        });
    });

    var clearBtn = document.getElementById('cart-clear');
    if (clearBtn) {
        clearBtn.addEventListener('click', clearCart);
    }
})();
