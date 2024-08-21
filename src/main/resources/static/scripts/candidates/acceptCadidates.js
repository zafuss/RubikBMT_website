let currentPage = 0;
let totalPages = 0;

function loadPage(page) {
    fetch(`/apiCheck/candidates/accept?page=${page}`)
        .then(response => response.json())
        .then(data => {
            const candidatesTableBody = document.getElementById('candidates-table-body');
            candidatesTableBody.innerHTML = ''; // Clear existing table rows

            data.candidates.forEach((candidate, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${candidate.fullName}</td>
                    <td>${candidate.phoneNumber}</td>
                    <td id="events-${index}"></td>
                `;
                candidatesTableBody.appendChild(row);

                // Pass the index to ensure correct targeting
                const eventsContainer = document.getElementById(`events-${index}`);
                renderEvents(eventsContainer, candidate.events);
            });

            currentPage = data.currentPage;
            totalPages = data.totalPages;

            document.getElementById('prev-page').disabled = currentPage === 0;
            document.getElementById('next-page').disabled = currentPage + 1 === totalPages;

            updatePageNumbers();
        })
        .catch(error => console.error('Error loading page:', error));
}

function renderEvents(eventsContainer, events) {
    eventsContainer.innerHTML = '';
    events.forEach(event => {
        const eventDiv = document.createElement('div');
        eventDiv.className = 'event-container';
        eventDiv.textContent = event.name;
        eventsContainer.appendChild(eventDiv);
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

// Initial load
loadPage(0);

// Event listeners for pagination buttons
document.getElementById('prev-page').addEventListener('click', () => {
    loadPage(currentPage - 1);
});

document.getElementById('next-page').addEventListener('click', () => {
    loadPage(currentPage + 1);
});