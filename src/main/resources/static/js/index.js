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


document.addEventListener('DOMContentLoaded', (event) => {
    const modal = document.getElementById('modal');
    const modalImg = document.getElementById('modal-image');
    const closeModal = document.getElementById('modal-close');
    const images = document.querySelectorAll('.expense-image-container img');

    images.forEach(image => {
        image.addEventListener('click', () => {
            modal.style.display = 'block';
            modalImg.src = image.src;
        });
    });

    closeModal.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
});

