document.querySelector('form').addEventListener('submit', function (e) {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!username) {
        alert('请输入用户名！');
        e.preventDefault();
        return;
    }
    if (!password) {
        alert('请输入密码！');
        e.preventDefault();
        return;
    }
});