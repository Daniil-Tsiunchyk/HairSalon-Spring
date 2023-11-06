function updateEditUser(userId) {

    fetch(`http://localhost:8080/api/users/${userId}`)
        .then((response) => response.json())
        .then((userData) => {
            document.getElementById("editUserId").value = userData.id;
            document.getElementById("editUserUsername").value = userData.username;
            document.getElementById("editUserPassword").value = "oleg";
            document.getElementById("editUserEmail").value = userData.email;
            document.getElementById("editUserFirstName").value = userData.firstName;
            document.getElementById("editUserLastName").value = userData.lastName;
            document.getElementById("editUserRole").value = userData.role;
        });

    submitEditButton.onclick = function () {
        const userId = document.getElementById("editUserId").value;
        const username = document.getElementById("editUserUsername").value;
        const password = document.getElementById("editUserPassword").value;
        const email = document.getElementById("editUserEmail").value;
        const firstName = document.getElementById("editUserFirstName").value;
        const lastName = document.getElementById("editUserLastName").value;
        const role = document.getElementById("editUserRole").value;

        editUser(userId, username, password, email, firstName, lastName, role);
    };

    function editUser(userId, username, password, email, firstName, lastName, role) {
        fetch(`http://localhost:8080/api/users/${userId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, email, password, firstName, lastName, role }),
        }).then((response) => {
            if (response.status === 200) {
                const modal = document.getElementById("editUserModal");
                modal.style.display = "none";
                updateEditUser(userId);
            }
        });
    }
}

updateEditUser(1);
