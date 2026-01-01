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

function updateGreeting() {
    const hour = new Date().getHours();
    let greeting;

    if (hour >= 5 && hour < 9) {
        greeting = "早上好";
    } else if (hour >= 9 && hour < 12) {
        greeting = "上午好";
    } else if (hour >= 12 && hour < 14) {
        greeting = "中午好";
    } else if (hour >= 14 && hour < 18) {
        greeting = "下午好";
    } else if (hour >= 18 && hour < 22) {
        greeting = "晚上好";
    } else {
        greeting = "夜深了";
    }

    const greetingElement = document.getElementById("greeting");
    greetingElement.style.opacity = "0";

    setTimeout(() => {
        greetingElement.textContent = greeting;
        greetingElement.style.opacity = "1";
    }, 100);
}

function searchBookmark() {
    const tags = document.querySelectorAll(".category-tab");
    const bookmarks = document.querySelectorAll(".bookmark-card");
    tags.forEach((tab) => {
        tab.addEventListener("click", function () {
            tags.forEach((t) => {
                t.classList.remove("bg-primary/30", "text-white");
                t.classList.add("bg-neutral", "text-dark", "hover:bg-primary/50");
            });
            this.classList.remove(
                "bg-primary/30",
                "text-dark",
                "hover:bg-primary/50"
            );
            this.classList.add("bg-primary/30", "text-white");

            const category = this.getAttribute("data-category");
            bookmarks.forEach((card) => {
                if (
                    !!!category ||
                    card.getAttribute("data-category") === category
                ) {
                    card.style.display = "flex";
                    setTimeout(() => {
                        card.style.opacity = "1";
                        card.style.transform = "translateY(0)";
                    }, 50);
                } else {
                    card.style.opacity = "0";
                    card.style.transform = "translateY(10px)";
                    setTimeout(() => {
                        card.style.display = "none";
                    }, 300);
                }
            });
        });
    });
    const searchInput = document.getElementById("search-input");
    searchInput.addEventListener("input", function () {
        const searchTerm = this.value.toLowerCase().trim();
        bookmarks.forEach((card) => {
            const cardName = card.dataset.name.toLowerCase();
            if (cardName.includes(searchTerm)) {
                card.style.display = "flex";
                card.style.opacity = "1";
                card.style.transform = "translateY(0)";
            } else {
                card.style.opacity = "0";
                card.style.transform = "translateY(10px)";
                setTimeout(() => {
                    card.style.display = "none";
                }, 300);
            }
        });
    });
}

function initThemeSwitch() {
    const themeToggle = document.getElementById('greeting');

    if (localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
        document.documentElement.classList.add('dark');
    } else {
        document.documentElement.classList.remove('dark');
    }

    themeToggle.addEventListener('click', () => {
        document.documentElement.classList.toggle('dark');
        localStorage.theme = document.documentElement.classList.contains('dark') ? 'dark' : 'light';
    });
}

initThemeSwitch();
searchBookmark();
updateGreeting();

// window.addEventListener("scroll", () => {
//     const header = document.getElementById("header");
//     if (window.scrollY > 10) {
//         header.classList.add("py-2", "shadow");
//         header.classList.remove("py-4", "shadow-sm");
//     } else {
//         header.classList.add("py-4", "shadow-sm");
//         header.classList.remove("py-2", "shadow");
//     }
// });

