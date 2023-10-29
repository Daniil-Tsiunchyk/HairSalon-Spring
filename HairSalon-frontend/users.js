function updateTable() {
  const UserRoleSelect = document.getElementById("UserRole");
  const UserRole = UserRoleSelect.value;
  fetch("http://localhost:8080/api/users")
    .then((response) => response.json())
    .then((data) => {
      const filteredData = filterUsersByRole(data, UserRole);
      const tableBody = document.querySelector("#userTable tbody");
      tableBody.innerHTML = "";
      let i = 0;
      filteredData.forEach((user) => {
        i++;
        const row = document.createElement("tr");
        row.innerHTML = `
                  <td>${i}</td>
                  <td>${user.username}</td>
                  <td>${user.firstName}</td>
                  <td>${user.lastName}</td>
                  <td>
                      <button class="btn btn-outline-secondary" type="button" data-bs-toggle="modal" data-bs-target="#editUserModal" data-bs-whatever="@getbootstrap" onclick="showEditUserModal(${user.id})">Редактировать</button>
                      <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#deleteUserModal" onclick="showDeleteUserModal(${user.id})">Удалить</button>
                  </td>
              `;
        tableBody.appendChild(row);
      });
    });
}

const UserRoleSelect = document.getElementById("UserRole");
UserRoleSelect.addEventListener("change", () => {
  updateTable();
});

document
  .getElementById("createUserForm")
  .addEventListener("submit", function (event) {
    event.preventDefault();
    const username = document.getElementById("createUsername").value;
    const password = document.getElementById("createPassword").value;
    const firstName = document.getElementById("createFirstName").value;
    const lastName = document.getElementById("createLastName").value;
    const role = document.getElementById("SelectUserRole").value;

    fetch("http://localhost:8080/api/users/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password, firstName, lastName, role }),
    }).then((response) => {
      if (response.status === 201) {
        updateTable();
        document.getElementById("createUsername").value = "";
        document.getElementById("createPassword").value = "";
        document.getElementById("createFirstName").value = "";
        document.getElementById("createLastName").value = "";
        document.getElementById("SelectUserRole").value = "USER";
      }
    });
  });

function showEditUserModal(userId) {
  const modal = document.getElementById("editUserModal");

  fetch(`http://localhost:8080/api/users/${userId}`)
    .then((response) => response.json())
    .then((userData) => {
      document.getElementById("editUserId").value = userData.id;
      document.getElementById("editUserUsername").value = userData.username;
      document.getElementById("editUserPassword").value = "oleg";
      document.getElementById("editUserFirstName").value = userData.firstName;
      document.getElementById("editUserLastName").value = userData.lastName;
      document.getElementById("editUserRole").value = userData.role;
    });

  submitEditButton.onclick = function () {
    const userId = document.getElementById("editUserId").value;
    const username = document.getElementById("editUserUsername").value;
    const password = document.getElementById("editUserPassword").value;
    const firstName = document.getElementById("editUserFirstName").value;
    const lastName = document.getElementById("editUserLastName").value;
    const role = document.getElementById("editUserRole").value;

    editUser(userId, username, password, firstName, lastName, role);
  };


  modal.style.display = "block";
}


function editUser(userId, username, password, firstName, lastName, role) {
  fetch(`http://localhost:8080/api/users/${userId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, password, firstName, lastName, role }),
  }).then((response) => {
    if (response.status === 200) {
      const modal = document.getElementById("editUserModal");
      modal.style.display = "none";
      updateTable();
    }
  });
}

function showDeleteUserModal(userId) {
  const confirmButton = document.getElementById("confirmDeleteUser");

  confirmButton.onclick = function () {
    deleteUser(userId);
  };
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
