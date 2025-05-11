document.addEventListener('DOMContentLoaded', () => {
    const userTable = document.getElementById("userTable");

    fetchAllUsers();

    function fetchAllUsers() {
        userTable.innerHTML = '<tr><td colspan="5">Loading...</td></tr>';

        fetch('/api/users')
            .then(res => res.json())
            .then(users => renderUserTable(users))
            .catch(() => {
                userTable.innerHTML = '<tr><td colspan="5">Failed to load users.</td></tr>';
            });
    }

    function renderUserTable(users) {
        userTable.innerHTML = '';
        const fragment = document.createDocumentFragment();

        users.forEach(user => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${user.id}</td>
                <td>${user.email}</td>
                <td>${user.firstName} ${user.lastName}</td>
                <td class="min-role-width">${formatRoles(user.roles)}</td>
                <td class="action-buttons"></td>
            `;

            const actionsTd = tr.querySelector('.action-buttons');
            const isCurrentUser = user.email === CURRENT_USERNAME;

            const editBtn =
                createButton('Edit role', 'btn-info mr-2', () => editUser(tr, user.id));

            editBtn.disabled = isCurrentUser;

            const deleteBtn =
                createButton('Delete', 'btn-danger mr-2', () => deleteUser(user.id));

            deleteBtn.disabled = isCurrentUser;

            actionsTd.append(editBtn, deleteBtn);
            fragment.appendChild(tr);
        });

        userTable.appendChild(fragment);
    }

    function formatRoles(roles) {
        if (roles.includes('ADMIN')) return 'Admin';
        if (roles.includes('USER')) return 'User';
        return 'Unknown';
    }

    function createButton(text, classes, handler) {
        const btn = document.createElement('button');
        btn.textContent = text;
        btn.className = `btn ${classes}`;
        btn.addEventListener('click', handler);
        return btn;
    }

    function editUser(row, userId) {
        const cells = row.children;
        const actionsTd = cells[4];
        const roleTd = cells[3];
        const originalRoleText = roleTd.textContent.trim();

        const roleSelect = document.createElement('select');
        ['ADMIN', 'USER'].forEach(role => {
            const opt = document.createElement('option');
            opt.value = role;
            opt.textContent = role === 'ADMIN' ? 'Admin' : 'User';
            if (originalRoleText.toUpperCase() === role) opt.selected = true;
            roleSelect.appendChild(opt);
        });

        roleTd.innerHTML = '';
        roleTd.appendChild(roleSelect);

        actionsTd.innerHTML = '';

        const saveBtn = createButton('Save', 'btn-success mr-2', () => {
            const selected = roleSelect.value.toUpperCase();
            const original = originalRoleText.toUpperCase();

            // Essentially omits saving the user and sending a query if the same role is selected
            if (selected === original) {
                enableAllRows();
                fetchAllUsers();
                return;
            }

            saveUser(userId, selected);
        });

        const cancelBtn =
            createButton('Cancel', 'btn-secondary mr-2', () => cancelEdit(row, originalRoleText));

        actionsTd.append(saveBtn, cancelBtn);

        disableOtherRows(row);
    }

    function disableOtherRows(activeRow) {
        const rows = userTable.querySelectorAll('tr');
        rows.forEach(row => {
            if (row !== activeRow) {
                row.querySelectorAll('button').forEach(btn => btn.disabled = true);
            }
        });
    }

    function enableAllRows() {
        const rows = userTable.querySelectorAll('tr');
        rows.forEach(row => {
            const email = row.children[1]?.textContent.trim();
            row.querySelectorAll('button').forEach(btn => {
                btn.disabled = email === CURRENT_USERNAME.trim();
            });
        });
    }

    function cancelEdit(row, originalRole) {
        row.children[3].textContent = originalRole;
        enableAllRows();
        fetchAllUsers();
    }

    function saveUser(userId, selectedRole) {
        if (!window.confirm("Are you sure you want to change the role of this user?")) return;

        const payload = {
            id: userId,
            roles: selectedRole === 'ADMIN' ? ['ADMIN', 'USER'] : ['USER']
        };

        fetch('/api/users', {
            method: 'PATCH',
            headers: {
                [CSRF_HEADER_NAME]: CSRF_TOKEN,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        })
            .then(() => {
                enableAllRows();
                fetchAllUsers();
            })
            .catch(() => window.location.href = '/error');
    }

    function deleteUser(userId) {
        if (!window.confirm("Are you sure you want to delete this user?")) return;

        fetch(`/api/users/${userId}`, {
            method: 'DELETE',
            headers: {
                [CSRF_HEADER_NAME]: CSRF_TOKEN
            }
        })
            .then(() => fetchAllUsers())
            .catch(() => window.location.href = '/error');
    }
});
