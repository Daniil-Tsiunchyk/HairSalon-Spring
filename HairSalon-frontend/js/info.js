document.addEventListener("DOMContentLoaded", function () {
  function fetchAPI(endpoint, callback) {
    fetch(`http://localhost:8080/api/${endpoint}`)
      .then((response) => response.json())
      .then((data) => callback(data))
      .catch((error) => console.error(error));
  }

  function filterBookingsByStatus(data) {
    return data.filter((Booking) => Booking.status === "CONFIRMED");
  }

  function calculateStatistics() {
    let totalOrders = 0;
    let totalAmount = 0;
    const serviceStats = {};
    const barberStats = {};
    const salesData = {};

    fetchAPI("bookings", (bookings) => {
      const filteredBookings = filterBookingsByStatus(bookings);
      totalOrders = filteredBookings.length;

      for (const filteredBooking of filteredBookings) {
        totalAmount += filteredBooking.hairServiceCost;

        if (filteredBooking.hairServiceName in serviceStats) {
          serviceStats[filteredBooking.hairServiceName]++;
        } else {
          serviceStats[filteredBooking.hairServiceName] = 1;
        }

        if (filteredBooking.barber in barberStats) {
          barberStats[filteredBooking.barber]++;
        } else {
          barberStats[filteredBooking.barber] = 1;
        }

        const date = moment(filteredBooking.dateTime);
        const day = date.format("YYYY-MM-DD");
        if (salesData[day]) {
          salesData[day] += filteredBooking.hairServiceCost;
        } else {
          salesData[day] = filteredBooking.hairServiceCost;
        }
      }

      document.getElementById("totalOrders").textContent = totalOrders;
      document.getElementById("totalAmount").textContent = totalAmount;

      const serviceStatsList = document.getElementById("serviceStats");
      for (const service in serviceStats) {
        const listItem = document.createElement("li");
        listItem.textContent = `${service}: ${serviceStats[service]} раз`;
        serviceStatsList.appendChild(listItem);
      }

      const barberStatsList = document.getElementById("barberStats");
      for (const barber in barberStats) {
        const listItem = document.createElement("li");
        listItem.textContent = `${barber}: ${barberStats[barber]} заказов`;
        barberStatsList.appendChild(listItem);
      }

      const salesChart = document.getElementById("salesChart");
      const salesDates = Object.keys(salesData);
      salesDates.sort();
      const salesValues = Object.values(salesData);

      new Chart(salesChart, {
        type: "line",
        data: {
          labels: salesDates,
          datasets: [
            {
              label: "Продажи",
              data: salesValues,
              backgroundColor: "rgba(75, 192, 192, 0.2)",
              borderColor: "rgba(75, 192, 192, 1)",
              borderWidth: 1,
            },
          ],
        },
        options: {
          scales: {
            x: {
              type: "time",
              time: {
                parser: "YYYY-MM-DD",
                unit: "day",
                displayFormats: {
                  day: "MMM D, YYYY",
                },
              },
              title: {
                display: true,
                text: "Дата",
              },
            },
            y: {
              beginAtZero: true,
              suggestedMax: Math.max(...salesValues) + 10,
              title: {
                display: true,
                text: "Сумма",
              },
            },
          },
        },
      });
    });
  }
  calculateStatistics();
});
