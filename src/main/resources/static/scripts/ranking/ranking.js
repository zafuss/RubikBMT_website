let currentPage = 0;
let totalPages = 0;

tmpRoundDetailID = "";

function loadEvent(competitionId, eventId) {
    const dataToSend = {
        competitionId: competitionId,
        eventId: eventId
    };
    fetch('/apiRoundBy/allEvent', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dataToSend)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(`HTTP error! Status: ${response.status} - ${response.statusText} - ${text}`);
                });
            }
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return response.json(); // Nếu là JSON thì parse
            } else {
                return response.text().then(text => {
                    throw new Error(`Expected JSON, but received: ${text}`);
                });
            }
        })
        .then(data => {
            displayJson(data);
        })
        .catch(error => {
            console.error('Error loading page:', error);
        });
}
function displayJson(jsonArray) {
    const resultDiv = document.getElementById('result');
    resultDiv.innerHTML = ''; // Xóa nội dung cũ

    jsonArray.forEach(item => {
        // Tạo nút button
        const button = document.createElement('button');
        button.textContent = item.name;
        button.dataset.roundId = item.roundDetailId;
        button.className = 'outline-button'; // Thêm lớp btn và btn-outline-primary của Bootstrap

        // Thêm sự kiện click cho nút
        button.addEventListener('click', () => {
            // Xóa lớp active của tất cả các nút
            document.querySelectorAll('.event-item button').forEach(btn => {
                btn.classList.remove('active');
                btn.classList.add('outline-button');
            });

            // Thêm lớp active cho nút đã chọn
            button.classList.remove('outline-button');
            button.classList.add('active');

            // Gọi hàm loadPage
            loadPage(0, button.dataset.roundId);
        });

        // Tạo phần tử chứa nút và liên kết
        const eventDiv = document.createElement('div');
        eventDiv.className = 'event-item';
        eventDiv.appendChild(button);

        // Thêm phần tử vào kết quả
        resultDiv.appendChild(eventDiv);
    });

    // Tự động chọn nút đầu tiên sau khi hiển thị
    const firstButton = resultDiv.querySelector('.event-item button');
    if (firstButton) {
        firstButton.click(); // Giả lập click vào nút đầu tiên
    }
}


function showJson(boxNumber, btn) {
    const resultDiv = document.getElementById('result');
    resultDiv.innerHTML = ''; // Xóa nội dung cũ
    const table = document.getElementById('round-details-table-body');
    table.innerHTML = '';
    loadEvent('1', boxNumber);
    // Tạo và hiển thị các vòng tròn

    // Loại bỏ lớp 'active' khỏi tất cả các nút

    const buttons = document.querySelectorAll('.box button');
    buttons.forEach(button => button.classList.remove('active'));

    // Thêm lớp 'active' vào nút được nhấn
    btn.classList.add('active');
}

function loadPage(page, roundDetailId) {
    const dataToSend = {
        page: page,
        roundDetailId: roundDetailId,
    };
    tmpRoundDetailID = roundDetailId;
    fetch('/apiRoundBy/Event', {
        method: 'Post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dataToSend)
    })
        .then(response => response.json())
        .then(data => {
            const roundDetailTableBody = document.getElementById('round-details-table-body');
            roundDetailTableBody.innerHTML = ''; // Clear existing table rows

            // console.log(data);
            if (data.roundDetail.length === 0) {
                roundDetailTableBody.innerHTML = '<tr><td colspan="7">Chưa có dữ liệu.</td></tr>';
                document.getElementById('page-numbers').innerHTML = '';
                document.getElementById('prev-page').disabled = true;
                document.getElementById('next-page').disabled = true;
                return;
            }

            data.roundDetail.forEach((detail, index) => {
                const row = document.createElement('tr');

                // Apply CSS class based on rankRound
                if (detail.rankRound === 1) {
                    row.classList.add('rank-1');
                } else if (detail.rankRound === 2) {
                    row.classList.add('rank-2');
                } else if (detail.rankRound === 3) {
                    row.classList.add('rank-3');
                } else if (detail.rankRound != 0 && detail.nextRoundCandidate !== 0 && detail.rankRound <= detail.nextRoundCandidate) {
                    row.classList.add('nextRound')
                }
                // Create a td for the solves
                const solvesTd = document.createElement('td');

                // Create the ul element
                const ul = document.createElement('ul');
                ul.style.listStyleType = 'none';
                ul.style.paddingLeft = '0';
                ul.style.margin = '0';

                // Iterate over the solves and create li elements
                detail.solves.forEach(solve => {
                    const li = document.createElement('li');
                    li.style.display = 'inline';
                    li.style.marginRight = '10px';

                    // Apply your conditions to format the solve text
                    let solveText = '';
                    if (solve.timeDurationString === '10:00:00.00') {
                        solveText = '(DNF)';
                    } else if (solve.timeDurationString === detail.best || solve.timeDurationString === detail.worst) {
                        solveText = `(${solve.timeDurationString})`;
                    } else {
                        solveText = solve.timeDurationString;
                    }

                    li.textContent = solveText;
                    ul.appendChild(li);
                });

                // Append the ul to the td
                solvesTd.appendChild(ul);

                // Set the other td elements
                row.innerHTML = `
                <td>${detail.rankRound === 0 ? '-' : detail.rankRound}</td>
                <td>${detail.fullName}</td>
                <td>${detail.ao5}</td>
                <td>${detail.best}</td>
                <td>${detail.worst}</td>
            `;

                // Append the solves td to the row
                row.appendChild(solvesTd);

                // Append the row to the table body
                roundDetailTableBody.appendChild(row);
            });

            currentPage = data.currentPage;
            totalPages = data.totalPages;

            updatePageNumbers();
            updatePaginationControls();
        })
        .catch(error => console.error('Error loading page:', error));
}

function updatePageNumbers() {
    const pageNumbersContainer = document.getElementById('page-numbers');
    pageNumbersContainer.innerHTML = '';

    for (let i = 0; i < totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i + 1;
        pageButton.disabled = i === currentPage;

        if (i === currentPage) {
            pageButton.classList.add('active');
        } else {
            pageButton.classList.add('outline-button');
        }

        pageButton.addEventListener('click', () => {
            loadPage(i, tmpRoundDetailID);
            setActivePageButton(i);
        });

        pageNumbersContainer.appendChild(pageButton);
    }
}

function setActivePageButton(pageIndex) {
    const pageButtons = document.querySelectorAll('#page-numbers button');
    pageButtons.forEach(button => button.classList.remove('active'));
    pageButtons[pageIndex].classList.add('active');
}

function updatePaginationControls() {
    document.getElementById('prev-page').disabled = currentPage === 0;
    document.getElementById('next-page').disabled = currentPage + 1 === totalPages;

    document.getElementById('current-page').textContent = `Page ${currentPage + 1} of ${totalPages}`;
}

// Event listeners for pagination buttons
document.getElementById('prev-page').addEventListener('click', () => {
    if (currentPage > 0) loadPage(currentPage - 1, tmpRoundDetailID);
});

document.getElementById('next-page').addEventListener('click', () => {
    if (currentPage < totalPages - 1) loadPage(currentPage + 1, tmpRoundDetailID);
});
