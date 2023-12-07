function updateBookingTable() {
    const BookingStatus = document.getElementById("BookingStatus").value;
    fetch("http://localhost:8080/api/bookings")
        .then((response) => response.json())
        .then(
            (data) => {
                const filteredData = filterBookingsByStatus(data, BookingStatus);
                const tableBody = document.querySelector("#BookingTable tbody");
                tableBody.innerHTML = "";
                let i = 0;
                filteredData.forEach((booking) => {
                    const row = document.createElement("tr");
                    let locationName;
                    switch (booking.location) {
                        case 1: locationName = "Ваупшасова 29"; break;
                        case 2: locationName = "Партизанский пр. 8"; break;
                        case 3: locationName = "Смоленская 15А"; break;
                        case 4: locationName = "Бехтерева 5"; break;
                    }
                    i++;

                    row.innerHTML = `
              <td>${i}</td>
              <td>${booking.barber}</td>
              <td>${booking.client}</td>
              <td>${booking.hairServiceName}</td>
              <td>${locationName}</td>
              <td>${formatDateTime(booking.dateTime)}</td>
              <td>
                <button class="btn btn-outline-secondary" type="button" data-bs-toggle="modal" data-bs-target="#editBookingModal" data-bs-whatever="@getbootstrap" onclick="showEditBooking(${booking.id})">Редактировать</button>
                <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#deleteBookingModal" onclick="showDeleteBookingModal(${booking.id})">Удалить</button>
              </td>
            `;
                    tableBody.appendChild(row);
                });
            });
}

const BookingStatusSelect = document.getElementById("BookingStatus");

BookingStatusSelect.addEventListener("change", () => {
    updateBookingTable();
});


function filterBookingsByStatus(data, status) {
    if (status === "ALL") {
        return data;
    } else {
        return data.filter((Booking) => Booking.status === status);
    }
}


function loadUsersIntoSelects() {
    fetch("http://localhost:8080/api/users")
        .then((response) => response.json())
        .then((data) => {
            const filteredData = filterUsersByRole(data, "USER");

            const userSelect = document.getElementById("userSelect");
            const editUser = document.getElementById("editUser");

            editUser.innerHTML = "";
            userSelect.innerHTML = "";

            filteredData.forEach((user) => {
                const option = document.createElement("option");
                option.value = user.id;
                option.text = user.firstName + " " + user.lastName;
                const editOption = document.createElement("option");
                editOption.value = user.id;
                editOption.text = user.firstName + " " + user.lastName;
                userSelect.appendChild(option);
                editUser.appendChild(editOption);
            });

        })
        .catch((error) => {
            console.error("Ошибка при загрузке данных о пользователях:", error);
        });
}

function loadBarbersIntoSelects() {
    fetch("http://localhost:8080/api/users")
        .then((response) => response.json())
        .then((data) => {
            const filteredData = filterUsersByRole(data, "BARBER");

            const barberSelect = document.getElementById("barberSelect");
            const editBarber = document.getElementById("editBarber");

            editBarber.innerHTML = "";
            barberSelect.innerHTML = "";

            filteredData.forEach((user) => {
                const option = document.createElement("option");
                option.value = user.id;
                option.text = user.firstName + " " + user.lastName;
                const editOption = document.createElement("option");
                editOption.value = user.id;
                editOption.text = user.firstName + " " + user.lastName;
                barberSelect.appendChild(option);
                editBarber.appendChild(editOption);
            });

        })
        .catch((error) => {
            console.error("Ошибка при загрузке данных о парикмахеров:", error);
        });
}

function loadServiceIntoSelects() {
    fetch("http://localhost:8080/api/hair-services")
        .then((response) => response.json())
        .then((data) => {

            const serviceSelect = document.getElementById("serviceSelect");
            const editService = document.getElementById("editService");

            editService.innerHTML = "";
            serviceSelect.innerHTML = "";

            data.forEach((service) => {
                const option = document.createElement("option");
                option.value = service.id;
                option.text = service.name;
                const editOption = document.createElement("option");
                editOption.value = service.id;
                editOption.text = service.name;
                serviceSelect.appendChild(option);
                editService.appendChild(editOption);
            });

        })
        .catch((error) => {
            console.error("Ошибка при загрузке данных об услугах:", error);
        });
}

document
    .getElementById("createBookingForm")
    .addEventListener("submit", function (event) {
        event.preventDefault();
        const barberID = parseInt(document.getElementById("barberSelect").value);
        const userId = parseInt(document.getElementById("userSelect").value);
        const serviceId = parseInt(document.getElementById("serviceSelect").value);
        const location = parseInt(document.getElementById("locationSelect").value);
        const dateTime = document.getElementById("dateTime").value;

        if (checkBookingAvailability(barberID, dateTime)) {
            fetch("http://localhost:8080/api/bookings", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ user: { id: userId }, hairService: { id: serviceId }, barber: { id: barberID }, location: { id: location }, dateTime: dateTime, status: "RESERVED" })

            }).then((response) => {
                if (response.status === 201) {
                    updateBookingTable();
                }
            });
            document.getElementById("dateTime").value = "";
            alert("Успешное бронирование");
        } else {
            alert("Выбранное время занято. Пожалуйста, выберите другое время.");
        }
    });

function showEditBooking(BookingId) {
    const modal = document.getElementById("editBookingModal");
    fetch(`http://localhost:8080/api/bookings/${BookingId}`)
        .then((response) => response.json())
        .then((BookingData) => {
            document.getElementById("editBookingId").value = BookingData.id;
            document.getElementById("editBarber").value = BookingData.barberID;
            document.getElementById("editUser").value = BookingData.userId;
            document.getElementById("editService").value = BookingData.serviceId;
            document.getElementById("editLocation").value = BookingData.location;
        });

    submitEditButton.onclick = function () {
        const BookingId = parseInt(document.getElementById("editBookingId").value);
        const barberID = parseInt(document.getElementById("editBarber").value);
        const userId = parseInt(document.getElementById("editUser").value);
        const serviceId = parseInt(document.getElementById("editService").value);
        const location = parseInt(document.getElementById("editLocation").value);
        const editDateTime = document.getElementById("editDateTime").value;
        const editStatus = document.getElementById("editBookingStatus").value;

        editBooking(BookingId, userId, serviceId, barberID, location, editDateTime, editStatus)
    }
    modal.style.display = "block";
}

function editBooking(BookingId, userId, serviceId, barberID, location, editDateTime, editStatus) {
    fetch(`http://localhost:8080/api/bookings/${BookingId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ user: { id: userId }, hairService: { id: serviceId }, barber: { id: barberID }, location: { id: location }, dateTime: editDateTime, status: editStatus }),
    }).then((response) => {
        if (response.status === 200) {
            const modal = document.getElementById("editBookingModal");
            modal.style.display = "none";
            updateBookingTable();
        }
    });
}

function showDeleteBookingModal(BookingId) {
    const modal = document.getElementById("deleteBookingModal");
    const confirmButton = document.getElementById("confirmDeleteBooking");
    const cancelButton = document.getElementById("cancelDeleteBooking");

    confirmButton.onclick = function () {
        modal.style.display = "none";
        deleteBooking(BookingId);
    };

    cancelButton.onclick = function () {
        modal.style.display = "none";
    };

    modal.style.display = "block";
}

function deleteBooking(BookingId) {
    fetch(`http://localhost:8080/api/bookings/${BookingId}`, {
        method: "DELETE",
    }).then((response) => {
        if (response.status === 204) {
            updateBookingTable();
        }
    });
}

function formatDateTime(dateTimeString) {
    const date = new Date(dateTimeString);
    return `${date.toLocaleDateString()}, ${date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`;
}

function checkBookingAvailability(barberId, dateTime) {
    return true;
}

loadUsersIntoSelects();
loadBarbersIntoSelects();
loadServiceIntoSelects();
updateBookingTable();
