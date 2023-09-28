function showDeleteServiceModal(serviceId) {
  const modal = document.getElementById("deleteServiceModal");
  const confirmButton = document.getElementById("confirmDeleteService");
  const cancelButton = document.getElementById("cancelDeleteService");

  confirmButton.onclick = function () {
    modal.style.display = "none";
    deleteService(serviceId);
  };

  cancelButton.onclick = function () {
    modal.style.display = "none";
  };

  modal.style.display = "block";
}

function updateServiceTable() {
  fetch("http://localhost:8080/api/hair-services")
    .then((response) => response.json())
    .then((data) => {
      const tableBody = document.querySelector("#serviceTable tbody");
      tableBody.innerHTML = "";

      data.forEach((service) => {
        const row = document.createElement("tr");
        row.innerHTML = `
                  <td>${service.id}</td>
                  <td>${service.name}</td>
                  <td>${service.cost}</td>
                  <td>
                      <button onclick="editService(${service.id})">Редактировать</button>
                      <button onclick="showDeleteServiceModal(${service.id})">Удалить</button>
                  </td>
              `;

        tableBody.appendChild(row);
      });
    });
}

document
  .getElementById("createServiceForm")
  .addEventListener("submit", function (event) {
    event.preventDefault();
    const name = document.getElementById("createServiceName").value;
    const cost = document.getElementById("createServiceCost").value;

    fetch("http://localhost:8080/api/hair-services", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ name, cost }),
    }).then((response) => {
      if (response.status === 201) {
        updateServiceTable();
        document.getElementById("createServiceName").value = "";
        document.getElementById("createServiceCost").value = "";
      }
    });
  });

function editService(serviceId) {
  const modal = document.getElementById("editServiceModal");
  const editForm = document.getElementById("editServiceForm");
  const cancelEditButton = document.getElementById("cancelEditService");

  fetch(`http://localhost:8080/api/hair-services/${serviceId}`)
    .then((response) => response.json())
    .then((serviceData) => {
      document.getElementById("editServiceId").value = serviceData.id;
      document.getElementById("editServiceName").value = serviceData.name;
      document.getElementById("editServiceCost").value = serviceData.cost;
    });

  editForm.addEventListener("submit", function (event) {
    event.preventDefault();
    const serviceId = document.getElementById("editServiceId").value;
    const name = document.getElementById("editServiceName").value;
    const cost = document.getElementById("editServiceCost").value;

    fetch(`http://localhost:8080/api/hair-services/${serviceId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ name, cost }),
    }).then((response) => {
      if (response.status === 200) {
        modal.style.display = "none";
        updateServiceTable();
      }
    });
  });

  cancelEditButton.onclick = function () {
    modal.style.display = "none";
  };

  modal.style.display = "block";
}

function deleteService(serviceId) {
  fetch(`http://localhost:8080/api/hair-services/${serviceId}`, {
    method: "DELETE",
  }).then((response) => {
    if (response.status === 204) {
      updateServiceTable();
    }
  });
}

updateServiceTable();
