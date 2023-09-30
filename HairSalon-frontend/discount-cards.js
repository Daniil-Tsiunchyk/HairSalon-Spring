function showDeleteDiscountCardModal(discountCardId) {
  const modal = document.getElementById("deleteDiscountCardModal");
  const confirmButton = document.getElementById("confirmDeleteDiscountCard");
  const cancelButton = document.getElementById("cancelDeleteDiscountCard");

  confirmButton.onclick = function () {
    modal.style.display = "none";
    deleteDiscountCard(discountCardId);
  };

  cancelButton.onclick = function () {
    modal.style.display = "none";
  };

  modal.style.display = "block";
}

function updateDiscountCardTable() {
  fetch("http://localhost:8080/api/discount-cards")
    .then((response) => response.json())
    .then((data) => {
      const tableBody = document.querySelector("#discountCardTable tbody");
      tableBody.innerHTML = "";

      data.forEach((discountCard) => {
        const row = document.createElement("tr");
        row.innerHTML = `
                      <td>${discountCard.id}</td>
                      <td>${discountCard.discountPercentage}</td>
                      <td>${discountCard.user.username}</td>
                      <td>
                          <button onclick="editDiscountCard(${discountCard.id})">Редактировать</button>
                          <button onclick="showDeleteDiscountCardModal(${discountCard.id})">Удалить</button>
                      </td>
                  `;

        tableBody.appendChild(row);
      });
    });
}

function loadUsersIntoSelects() {
  console.log("Запрос на получение списка пользователей...");
  fetch("http://localhost:8080/api/users")
    .then((response) => response.json())
    .then((data) => {
      console.log("Данные о пользователях получены:", data);

      const userSelect = document.getElementById("userSelect");
      const editUserSelect = document.getElementById("editUserSelect");

      userSelect.innerHTML = "";
      editUserSelect.innerHTML = "";

      data.forEach((user) => {
        const option = document.createElement("option");
        option.value = user.id;
        option.text = user.username;
        userSelect.appendChild(option);

        const editOption = document.createElement("option");
        editOption.value = user.id;
        editOption.text = user.username;
        editUserSelect.appendChild(editOption);
      });
    })
    .catch((error) => {
      console.error("Ошибка при загрузке данных о пользователях:", error);
    });
}

document
  .getElementById("createDiscountCardForm")
  .addEventListener("submit", function (event) {
    event.preventDefault();
    const discountPercentage = parseFloat(
      document.getElementById("createDiscountPercentage").value
    );
    const userId = parseInt(document.getElementById("userSelect").value);

    fetch("http://localhost:8080/api/discount-cards", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ discountPercentage, userId }),
    }).then((response) => {
      if (response.status === 201) {
        updateDiscountCardTable();
        document.getElementById("createDiscountPercentage").value = "";
      }
    });
  });

function editDiscountCard(discountCardId) {
  const modal = document.getElementById("editDiscountCardModal");
  const editForm = document.getElementById("editDiscountCardForm");
  const cancelEditButton = document.getElementById("cancelEditDiscountCard");

  fetch(`http://localhost:8080/api/discount-cards/${discountCardId}`)
    .then((response) => response.json())
    .then((discountCardData) => {
      document.getElementById("editDiscountCardId").value = discountCardData.id;
      document.getElementById("editDiscountPercentage").value =
        discountCardData.discountPercentage;
      document.getElementById("editUserSelect").value =
        discountCardData.user.id;
    });

  editForm.addEventListener("submit", function (event) {
    event.preventDefault();
    const discountCardId = document.getElementById("editDiscountCardId").value;
    const discountPercentage = parseFloat(
      document.getElementById("editDiscountPercentage").value
    );
    const userId = parseInt(document.getElementById("editUserSelect").value);

    fetch(`http://localhost:8080/api/discount-cards/${discountCardId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ discountPercentage, userId }),
    }).then((response) => {
      if (response.status === 200) {
        modal.style.display = "none";
        updateDiscountCardTable();
      }
    });
  });

  cancelEditButton.onclick = function () {
    modal.style.display = "none";
  };

  modal.style.display = "block";
}

function deleteDiscountCard(discountCardId) {
  fetch(`http://localhost:8080/api/discount-cards/${discountCardId}`, {
    method: "DELETE",
  }).then((response) => {
    if (response.status === 204) {
      updateDiscountCardTable();
    }
  });
}

loadUsersIntoSelects();
updateDiscountCardTable();
