
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
                  <td>${service.cost} руб.</td>
                  <td>
                      <button class="btn btn-outline-primary" type="button" data-bs-toggle="modal" data-bs-target="#bookingModal" data-bs-whatever="@getbootstrap" onclick="createBooking(${service.id})">Сделать заказ</button>
                  </td>
              `;

        tableBody.appendChild(row);
      });
    });
}

function createBooking(serviceId) {
  submitBooking.onclick = function () {
    const barberID = parseInt(document.getElementById("editBarber").value);
    const userId = 15;
    const dateTime = document.getElementById("dateTime").value;

    console.log({ user: { id: userId }, hairService: { id: serviceId }, barber: { id: barberID }, dateTime: dateTime, status: "RESERVED" })


    fetch("http://localhost:8080/api/bookings", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ user: { id: userId }, hairService: { id: serviceId }, barber: { id: barberID }, dateTime: dateTime, status: "RESERVED" })
    })
  }
}

function loadBarbersIntoSelects() {
  console.log("Запрос на получение списка парикмахеров...");
  fetch("http://localhost:8080/api/users")
    .then((response) => response.json())
    .then((data) => {
      const filteredData = filterUsersByRole(data, "BARBER");
      console.log("Данные о парикмахерах получены:", filteredData);

      const editBarber = document.getElementById("editBarber");

      editBarber.innerHTML = "";

      const editOption = document.createElement("option");
      editOption.value = 0;
      editOption.text = "Любой";
      editBarber.appendChild(editOption);

      filteredData.forEach((user) => {
        const editOption = document.createElement("option");
        editOption.value = user.id;
        editOption.text = user.firstName + " " + user.lastName;
        editBarber.appendChild(editOption);
      });

    })
    .catch((error) => {
      console.error("Ошибка при загрузке данных о парикмахеров:", error);
    });
}


updateServiceTable();
loadBarbersIntoSelects();