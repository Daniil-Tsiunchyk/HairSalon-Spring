
function updateServiceTable() {
  fetch("http://localhost:8080/api/hair-services")
    .then((response) => response.json())
    .then((data) => {
      const tableBody = document.querySelector("#serviceTable tbody");
      tableBody.innerHTML = "";
        let i = 0;
      data.forEach((service) => {
        i++;
        const row = document.createElement("tr");
        row.innerHTML = `
                  <td>${i}</td>
                  <td>${service.name}</td>
                  <td>${service.cost}</td>
                  <td>
                      <button class="btn btn-outline-secondary" type="button" data-bs-toggle="modal" data-bs-target="#editServiceModal" data-bs-whatever="@getbootstrap" onclick="editService(${service.id})">Редактировать</button>
                      <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#deleteServiceModal" onclick="showDeleteServiceModal(${service.id})">Удалить</button>
                  </td>
              `;

        tableBody.appendChild(row);
      });
    });
}

updateServiceTable();
