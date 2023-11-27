document
  .getElementById("loginForm")
  .addEventListener("submit", function (event) {
    event.preventDefault();
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("http://localhost:8080/api/users/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password }),
    }).then((response) => {
      if (response.status === 200) {
        response.json().then((data) => {

          document.cookie = `id=${data.id}; path=/`;
          document.cookie = `role=${data.role}; path=/`;

          if (data.role === "USER") {
            window.location.href = "client-bookings.html";
          } else if (data.role === "BARBER") {
            window.location.href = "barber-bookings.html";
          } else if (data.role === "MANAGER") {
            window.location.href = "bookings.html";
          }

          document.getElementById("loginMessage").textContent =
            "Авторизация успешна.";
        });
      } else {
        document.getElementById("loginMessage").textContent =
          "Ошибка авторизации.";
      }
    });
  });
