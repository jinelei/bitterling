function initNavBarClient() {
    const mobileMenuButton = document.getElementById('mobile-menu-button');
    const mobileMenu = document.getElementById('mobile-menu');

    if (mobileMenuButton && mobileMenu) {
        mobileMenuButton.addEventListener('click', () => {
            mobileMenu.classList.toggle('hidden');
        });
    }

    const dropdownButton = document.getElementById('dropdown-button');
    const dropdownMenu = document.getElementById('dropdown-menu');
    const dropdownIcon = dropdownButton.querySelector('svg');

// 点击按钮切换下拉框
    if (dropdownButton && dropdownMenu && dropdownIcon) {
        dropdownButton.addEventListener('click', () => {
            dropdownMenu.classList.toggle('hidden');
            dropdownIcon.classList.toggle('rotate-180'); // 箭头旋转动画
        });

// 点击页面其他区域关闭下拉框
        document.addEventListener('click', (e) => {
            if (!document.getElementById('dropdown-container').contains(e.target)) {
                dropdownMenu.classList.add('hidden');
                dropdownIcon.classList.remove('rotate-180');
            }
        });
    }
}

function initThemeSwitch() {
    const themeToggle = document.getElementById('theme');
    if (!!themeToggle) {
        if (localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
            document.documentElement.classList.add('dark');
            themeToggle.classList.remove('fa-moon-o');
            themeToggle.classList.add('fa-sun-o');
        } else {
            document.documentElement.classList.remove('dark');
            themeToggle.classList.remove('fa-sun-o');
            themeToggle.classList.add('fa-moon-o');
        }
        themeToggle.addEventListener('click', () => {
            document.documentElement.classList.toggle('dark');
            localStorage.theme = document.documentElement.classList.contains('dark') ? 'dark' : 'light';
            if (localStorage.theme === 'dark') {
                themeToggle.classList.remove('fa-moon-o');
                themeToggle.classList.add('fa-sun-o');
            } else {
                themeToggle.classList.remove('fa-sun-o');
                themeToggle.classList.add('fa-moon-o');
            }
        });
    }
}

initThemeSwitch();
initNavBarClient();
