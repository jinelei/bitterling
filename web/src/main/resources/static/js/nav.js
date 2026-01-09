tailwind.config = {
    darkMode: 'class',
    theme: {
        extend: {
            colors: {
                primary: '#3B82F6',
                secondary: '#10B981',
                dark: {
                    bg: '#1E293B',
                    card: '#334155'
                }
            },
            fontFamily: {
                inter: ['Inter', 'system-ui', 'sans-serif'],
            },
        },
    }
}

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
