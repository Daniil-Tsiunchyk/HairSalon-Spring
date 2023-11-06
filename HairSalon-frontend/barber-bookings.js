function updateBookingTable() {
    const BookingStatus = document.getElementById("BookingStatus").value;
    const time = document.getElementById("BookingTimeStatus").value;
    fetch("http://localhost:8080/api/bookings/user/15")
        .then((response) => response.json())
        .then(
            (data) => {
                const preFilteredData = filterBookingsByTime(data, time);
                const filteredData = filterBookingsByStatus(preFilteredData, BookingStatus);
                const tableBody = document.querySelector("#BookingTable tbody");
                tableBody.innerHTML = "";
                let i = 0;
                filteredData.forEach((booking) => {
                    const row = document.createElement("tr");
                    i++;

                    row.innerHTML = `
                    <td class="text-warning-emphasis">${i}</td>
                    <td>${booking.hairServiceName}</td>
                    <td>${booking.client}</td>
                    <td>${formatDateTime(booking.dateTime)}</td>
                    <td>
                      <button class="btn btn-outline-dark" type="button" data-bs-toggle="modal" data-bs-target="#editBookingModal" data-bs-whatever="@getbootstrap" onclick="showEditBooking(${booking.id})">Редактировать</button>
                      <button type="button" class="btn btn-outline-danger" data-bs-toggle="modal" data-bs-target="#declineBookingModal" onclick="showDeclineBookingModal(${booking.id})">Отменить заказ</button>
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

function filterBookingsByTime(data, time) {
    switch (time) {
        case "ALL": return data; break;
        case "DAY": now = new dateTime(); return data.filter((Booking) => getDate(Booking.dateTime) === getDate(now)); break;
    }
}


function filterBookingsByStatus(data, status) {
    if (status === "ALL") {
        return data;
    } else {
        return data.filter((Booking) => Booking.status === status);
    }
}



function loadBarbersIntoSelects() {
    fetch("http://localhost:8080/api/users")
        .then((response) => response.json())
        .then((data) => {
            const filteredData = filterUsersByRole(data, "USER");

            const barberSelect = document.getElementById("clientSelect");
            const editBarber = document.getElementById("editClient");

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
        const barberID = 6;
        const userId = parseInt(document.getElementById("clientSelect").value);
        const serviceId = parseInt(document.getElementById("serviceSelect").value);
        const dateTime = document.getElementById("dateTime").value;

        if (checkBookingAvailability(barberID, dateTime)) {

            fetch("http://localhost:8080/api/bookings", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ user: { id: userId }, hairService: { id: serviceId }, barber: { id: barberID }, dateTime: dateTime, status: "RESERVED" })

            }).then((response) => {
                if (response.status === 201) {
                    updateBookingTable();
                }
            });
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
            document.getElementById("editClient").value = BookingData.clientId;
            document.getElementById("editService").value = BookingData.serviceId;
            document.getElementById("dateTime").value = BookingData.dateTime;
        });

    submitEditButton.onclick = function () {
        const BookingId = parseInt(document.getElementById("editBookingId").value);
        const barberID = 6;
        const userId = parseInt(document.getElementById("editClient").value);
        const serviceId = parseInt(document.getElementById("editService").value);
        const editDateTime = document.getElementById("dateTime").value;

        editBooking(BookingId, userId, serviceId, barberID, editDateTime);
    }
    modal.style.display = "block";
}

function editBooking(BookingId, userId, serviceId, barberID, editDateTime) {
    fetch(`http://localhost:8080/api/bookings/${BookingId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ user: { id: userId }, hairService: { id: serviceId }, barber: { id: barberID }, dateTime: editDateTime, status: "RESERVED" }),
    }).then((response) => {
        if (response.status === 200) {
            const modal = document.getElementById("editBookingModal");
            modal.style.display = "none";
            updateBookingTable();
        }
    });
};

function showDeclineBookingModal(BookingId) {
    const modal = document.getElementById("declineBookingModal");
    const confirmButton = document.getElementById("confirmDeclineBooking");

    fetch(`http://localhost:8080/api/bookings/${BookingId}`)
        .then((response) => response.json())
        .then((BookingData) => {
            document.getElementById("declineBookingId").value = BookingData.id;
            document.getElementById("declineClient").value = BookingData.clientId;
            document.getElementById("declineService").value = BookingData.serviceId;
            document.getElementById("declineDateTime").value = BookingData.dateTime;
        });

    confirmButton.onclick = function () {
        const BookingId = parseInt(document.getElementById("declineBookingId").value);
        const barberID = 6;
        const userId = parseInt(document.getElementById("declineClient").value);
        const serviceId = parseInt(document.getElementById("declineService").value);
        const DateTime = document.getElementById("declineDateTime").value;
        declineBooking(BookingId, userId, serviceId, barberID, DateTime);
    };
}

function declineBooking(BookingId, userId, serviceId, barberID, DateTime) {
    fetch(`http://localhost:8080/api/bookings/${BookingId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ user: { id: userId }, hairService: { id: serviceId }, barber: { id: barberID }, dateTime: DateTime, status: "CANCELLED" }),
    }).then((response) => {
        if (response.status === 200) {
            updateBookingTable();
        }
    });
}

function formatDateTime(dateTimeString) {
    const date = new Date(dateTimeString);
    const formattedDate = `${date.toLocaleDateString()}, ${date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`;
    return formattedDate;
}

function checkBookingAvailability(barberId, dateTime) {
    return true;
}

loadBarbersIntoSelects();
loadServiceIntoSelects();
updateBookingTable();
