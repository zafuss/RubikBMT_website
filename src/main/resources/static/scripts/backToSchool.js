function validateSelection() {
    const checkboxes = document.querySelectorAll('input[type="checkbox"][name="events"]');
    const isChecked = Array.from(checkboxes).some(checkbox => checkbox.checked);

    if (!isChecked) {
        alert('Vui lòng chọn ít nhất một môn thi.');
    }
}

function clearError(errorId) {
    var errorSpan = document.getElementById(errorId);
    if (errorSpan) {
        errorSpan.textContent = '';
        errorSpan.classList.remove('text-danger');
    }
}

