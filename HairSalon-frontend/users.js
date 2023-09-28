function showDeleteUserModal(userId) {
  const modal = document.getElementById("deleteUserModal");
  const confirmButton = document.getElementById("confirmDeleteUser");
  const cancelButton = document.getElementById("cancelDeleteUser");

  confirmButton.onclick = function () {
    modal.style.display = "none";
    deleteUser(userId);
  };

  cancelButton.onclick = function () {
    modal.style.display = "none";
  };

  modal.style.display = "block";
}

function updateTable() {
  fetch("http://localhost:8080/api/users")
    .then((response) => response.json())
    .then((data) => {
      const tableBody = document.querySelector("#userTable tbody");
      tableBody.innerHTML = "";

      data.forEach((user) => {
        const row = document.createElement("tr");
        row.innerHTML = `
                  <td>${user.id}</td>
                  <td>${user.username}</td>
                  <td>${user.firstName}</td>
                  <td>${user.lastName}</td>
                  <td>
                      <button onclick="editUser(${user.id})">Редактировать</button>
                      <button onclick="showDeleteUserModal(${user.id})">Удалить</button>
                  </td>
              `;

        tableBody.appendChild(row);
      });
    });
}

document
  .getElementById("createUserForm")
  .addEventListener("submit", function (event) {
    event.preventDefault();
    const username = document.getElementById("createUsername").value;
    const password = document.getElementById("createPassword").value;
    const firstName = document.getElementById("createFirstName").value;
    const lastName = document.getElementById("createLastName").value;

    fetch("http://localhost:8080/api/users", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password, firstName, lastName }),
    }).then((response) => {
      if (response.status === 201) {
        updateTable();
        document.getElementById("createUsername").value = "";
        document.getElementById("createPassword").value = "";
        document.getElementById("createFirstName").value = "";
        document.getElementById("createLastName").value = "";
      }
    });
  });

function editUser(userId) {
  const modal = document.getElementById("editUserModal");
  const editForm = document.getElementById("editUserForm");
  const cancelEditButton = document.getElementById("cancelEditUser");

  fetch(`http://localhost:8080/api/users/${userId}`)
    .then((response) => response.json())
    .then((userData) => {
      document.getElementById("editUserId").value = userData.id;
      document.getElementById("editUserUsername").value = userData.username;
      document.getElementById("editUserPassword").value = userData.password;
      document.getElementById("editUserFirstName").value = userData.firstName;
      document.getElementById("editUserLastName").value = userData.lastName;
    });

  editForm.addEventListener("submit", function (event) {
    event.preventDefault();
    const userId = document.getElementById("editUserId").value;
    const username = document.getElementById("editUserUsername").value;
    const password = document.getElementById("editUserPassword").value;
    const firstName = document.getElementById("editUserFirstName").value;
    const lastName = document.getElementById("editUserLastName").value;

    fetch(`http://localhost:8080/api/users/${userId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password, firstName, lastName }),
    }).then((response) => {
      if (response.status === 200) {
        modal.style.display = "none";
        updateTable();
      }
    });
  });

  cancelEditButton.onclick = function () {
    modal.style.display = "none";
  };

  modal.style.display = "block";
}

function deleteUser(userId) {
  fetch(`http://localhost:8080/api/users/${userId}`, {
    method: "DELETE",
  }).then((response) => {
    if (response.status === 204) {
      updateTable();
    }
  });
}

updateTable();
