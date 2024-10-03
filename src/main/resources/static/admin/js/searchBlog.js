
    document.getElementById('searchType').addEventListener('change', function () {
    var searchType = this.value;
    var searchInput = document.getElementById('searchInput');

    if (searchType === 'category') {
    // Fetch the categories via AJAX
    fetch('/api/articles/categories')
    .then(response => response.json())
    .then(categories => {
    // Replace the input field with a select dropdown
    let select = document.createElement('select');
    select.name = 'keyword';
    select.id = 'keyword';
    select.className = 'form-select';

    // Populate select options with categories
    categories.forEach(category => {
    let option = document.createElement('option');
    option.value = category.id; // Assuming category has an id
    option.text = category.name; // Assuming category has a name
    select.appendChild(option);
});

    // Clear the previous input group and append the new select element
    searchInput.innerHTML = '';
    searchInput.appendChild(select);

    // Add event listener to the category select for alert
    select.addEventListener('change', function() {
    // alert('Bạn đã chọn thể loại: ' + this.options[this.selectedIndex].text);
});
    searchInput.innerHTML += `<button type="submit" class="btn btn-primary">Tìm kiếm</button>`;
})
    .catch(error => console.error('Error fetching categories:', error));
} else {
    // Replace the select dropdown with the original text input if a different option is selected
    searchInput.innerHTML = `
                <input type="text" id="keyword" name="keyword" placeholder="Nhập nội dung tìm kiếm" class="form-control"/>
                <button type="submit" class="btn btn-primary">Tìm kiếm</button>
            `;
}
});
