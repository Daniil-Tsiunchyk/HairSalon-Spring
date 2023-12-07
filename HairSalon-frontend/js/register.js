document
  .getElementById("registrationForm")
  .addEventListener("submit", function (event) {
    event.preventDefault();
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const repeatPassword = document.getElementById("repeatPassword").value;

    if (username.length < 5 || username.length > 12) {
      document.getElementById("registrationMessage").textContent =
        "Логин должен содержать от 5 до 12 символов";
      return;
    }

    if (password.length < 8 || password.length > 15) {
      document.getElementById("registrationMessage").textContent =
        "Пароль должен содержать от 8 до 15 символов";
      return;
    }

    if (password !== repeatPassword) {
      document.getElementById("registrationMessage").textContent =
        "Пароли не совпадают";
      return;
    }

    fetch("http://localhost:8080/api/users/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password }),
    }).then((response) => {
      if (response.status === 201) {
        response.json().then((data) => {
          document.cookie = `id=${data.id}; path=/`;
          document.cookie = `role=${data.role}; path=/`;
          window.location.href = "client-bookings.html";
        });
      } else {
        document.getElementById("registrationMessage").textContent =
          "Ошибка регистрации.";
      }
    });
  });
