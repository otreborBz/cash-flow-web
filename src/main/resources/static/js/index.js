document.addEventListener('DOMContentLoaded', (event) => {
    const modal = document.getElementById('image-modal');
    const modalImg = document.getElementById('modal-image');
    const body = document.querySelector('body');

    function toggleScrollLock() {
        body.style.overflow = body.style.overflow === 'hidden' ? '' : 'hidden';
    }

    document.querySelectorAll('.img_despesas').forEach(img => {
        img.addEventListener('click', function() {
            modal.style.display = "block";
            modalImg.src = this.src;
            toggleScrollLock();
        });
    });

    modal.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
            toggleScrollLock();
        }
    }
});

