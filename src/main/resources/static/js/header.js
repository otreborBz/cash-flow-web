function toggleSubMenu() {
    var submenu = document.getElementById("submenu");
    submenu.classList.toggle("active");
}

window.addEventListener('click', function(event) {
    var submenu = document.getElementById("submenu");
    var menu = document.getElementById("menu");
    var target = event.target;

    if (!menu.contains(target) && !submenu.contains(target)) {
        submenu.classList.remove("active");
    }
});
