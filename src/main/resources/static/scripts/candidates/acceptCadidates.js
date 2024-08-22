let currentPage = 0;
let totalPages = 0;

function loadPage(page) {
    fetch(`/apiCheck/candidates/accept?page=${page}`)
        .then(response => response.json())
        .then(data => {
            const candidatesTableBody = document.getElementById('candidates-table-body');
            candidatesTableBody.innerHTML = ''; // Clear existing table rows

            if (data.candidates.length === 0) {
                candidatesTableBody.innerHTML = '<tr><td colspan="2">No candidates found.</td></tr>';
                document.getElementById('page-numbers').innerHTML = '';
                document.getElementById('prev-page').disabled = true;
                document.getElementById('next-page').disabled = true;
                return;
            }

            data.candidates.forEach((candidate, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${candidate.fullName}</td>
                    <td id="events-${index}"></td>
                `;
                candidatesTableBody.appendChild(row);

                // Pass the index to ensure correct targeting
                const eventsContainer = document.getElementById(`events-${index}`);
                renderEvents(eventsContainer, candidate.events);
            });

            currentPage = data.currentPage;
            totalPages = data.totalPages;

            updatePageNumbers();
            updatePaginationControls();
        })
        .catch(error => console.error('Error loading page:', error));
}

function renderEvents(eventsContainer, events) {
    eventsContainer.innerHTML = '';
    events.forEach(event => {
        const img = document.createElement('img');
        img.className = 'event-image';
        img.src = event.imageURL;
        img.alt = event.name;
        img.style.width = '40px';
        img.style.height = '40px';
        eventsContainer.appendChild(img);
    });
}


function updatePageNumbers() {
    const pageNumbersContainer = document.getElementById('page-numbers');
    pageNumbersContainer.innerHTML = '';

    for (let i = 0; i < totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i + 1;
        pageButton.disabled = i === currentPage;
        pageButton.addEventListener('click', () => loadPage(i));
        pageNumbersContainer.appendChild(pageButton);
    }
}

function updatePaginationControls() {
    document.getElementById('prev-page').disabled = currentPage === 0;
    document.getElementById('next-page').disabled = currentPage + 1 === totalPages;

    // Update current page display
    document.getElementById('current-page').textContent = `Page ${currentPage + 1} of ${totalPages}`;
}

// Initial load
loadPage(0);

// Event listeners for pagination buttons
document.getElementById('prev-page').addEventListener('click', () => {
    if (currentPage > 0) loadPage(currentPage - 1);
});

document.getElementById('next-page').addEventListener('click', () => {
    if (currentPage < totalPages - 1) loadPage(currentPage + 1);
});
