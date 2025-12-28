// src/main/resources/static/js/nav.js
document.addEventListener('DOMContentLoaded', function() {
    // 文件夹点击事件（示例：展开子书签）
    const folderItems = document.querySelectorAll('.bookmark-item.folder');
    folderItems.forEach(folder => {
        folder.addEventListener('click', function() {
            // 此处可扩展：加载文件夹下的子书签并展示
            alert(`展开文件夹：${this.querySelector('.bookmark-name').textContent}`);
        });
    });
});
