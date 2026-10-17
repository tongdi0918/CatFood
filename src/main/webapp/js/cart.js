// 购物车数量修改：加减控件 + AJAX（对应 CartServlet action=update）
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
    });
})();
