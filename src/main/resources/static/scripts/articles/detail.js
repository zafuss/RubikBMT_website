const articleId = /*[[${article.id}]]*/ '0'; // Default value is '0' if no article id

document.getElementById('commentForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const name = document.getElementById('commentName').value;
    const content = document.getElementById('commentContent').value;

    fetch(`/api/articles/${articleId}/addComment`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            name: name,
            content: content
        })
    })
        .then(response => response.json()) // Parse the JSON from the response
        .then(data => {
            // Check if the comment list exists
            let commentList = document.querySelector('.comments-list ul');

            // If it doesn't exist, create a new list
            if (!commentList) {
                const commentsContainer = document.querySelector('.comments-list');
                commentsContainer.innerHTML = '<ul class="list-group"></ul>';
                commentList = commentsContainer.querySelector('ul');
            }

            // Create and insert the new comment along with the reply button and form
            const newComment = `
                <li class="list-group-item">
                    <div class="d-flex justify-content-between">
                        <div>
                            <strong>${data.name}</strong>
                            <p class="text-muted mb-1">${data.createdAt}</p>
                        </div>
                    </div>
                    <p>${data.content}</p>

                    <!-- Reply List (hidden initially) -->
                    <ul class="list-group mt-2 collapse" id="replyList${data.id}"></ul>

                    <!-- Reply Button -->
                    <button class="btn btn-sm btn-link mt-2 reply-button" type="button" data-toggle="collapse" data-target="#replyForm${data.id}">
                        Trả lời
                    </button>

                    <!-- Reply Form (hidden initially) -->
                    <div id="replyForm${data.id}" class="collapse mt-3">
                        <form class="reply-form" data-comment-id="${data.id}">
                            <div class="form-group">
                                <label for="replyName${data.id}">Tên</label>
                                <input type="text" id="replyName${data.id}" name="name" class="form-control" placeholder="Nhập tên của bạn" required>
                            </div>
                            <div class="form-group mt-3">
                                <label for="replyContent${data.id}">Bình luận</label>
                                <textarea id="replyContent${data.id}" name="content" class="form-control" rows="2" placeholder="Nhập bình luận của bạn" required></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary mt-2">Gửi trả lời</button>
                        </form>
                    </div>
                </li>
            `;
            commentList.insertAdjacentHTML('afterbegin', newComment);

            // Reset the comment form
            document.getElementById('commentForm').reset();

            // Re-attach reply form event listener for the newly added comment
            attachReplyFormListener();
        })
        .catch(error => console.error('Error:', error));
});

// Function to attach event listeners to reply forms
function attachReplyFormListener() {
    document.querySelectorAll('.reply-form').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            event.preventDefault();

            const parentCommentId = form.getAttribute('data-comment-id');
            const name = form.querySelector('input[name="name"]').value;
            const content = form.querySelector('textarea[name="content"]').value;

            fetch(`/api/articles/${articleId}/addReply`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({
                    parentCommentId: parentCommentId,
                    name: name,
                    content: content
                })
            })
                .then(response => response.json())
                .then(data => {
                    // Find the reply list for the parent comment
                    let commentList = form.closest('.list-group-item').querySelector(`#replyList${parentCommentId}`);

                    // If no replies exist yet, create a new reply list
                    if (!commentList) {
                        commentList = document.createElement('ul');
                        commentList.classList.add('list-group', 'mt-2');

                        // Insert reply list before the "Reply" button
                        const replyButton = form.closest('.list-group-item').querySelector('.reply-button');
                        form.closest('.list-group-item').insertBefore(commentList, replyButton);
                    }

                    // Add the new reply to the reply list
                    const newReply = `
                        <li class="list-group-item">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <strong>${data.name}</strong>
                                    <p class="text-muted mb-1">Vừa xong</p>
                                </div>
                            </div>
                            <p>${data.content}</p>
                        </li>
                    `;
                    commentList.insertAdjacentHTML('afterbegin', newReply);

                    // Reset the reply form
                    form.reset();

                    // Collapse the reply form after submitting
                    const collapseElement = form.closest('.collapse');
                    if (collapseElement) {
                        const collapseInstance = bootstrap.Collapse.getInstance(collapseElement);
                        collapseInstance.hide(); // Hide the collapse
                    }
                })
                .catch(error => console.error('Error:', error));
        });
    });
}

// Attach event listeners to existing reply forms on page load
attachReplyFormListener();

document.querySelectorAll('[data-created-at]').forEach(function (element) {
    const createdAtString = element.getAttribute('data-created-at');
    const createdAt = new Date(createdAtString);
    const now = new Date();

    const diffInMs = now - createdAt;
    const diffInMinutes = Math.floor(diffInMs / (1000 * 60));

    if (diffInMinutes < 1) {
        element.textContent = 'Vừa xong';
    } else if (diffInMinutes < 60) {
        element.textContent = `${diffInMinutes} phút trước`;
    } else {
        const options = { hour: '2-digit', minute: '2-digit', day: '2-digit', month: '2-digit', year: 'numeric' };
        element.textContent = createdAt.toLocaleString('vi-VN', options);
    }
});