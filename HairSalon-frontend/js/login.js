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
        document.getElementById("loginMessage").textContent =
          "Авторизация успешна.";
        window.location.href = "users.html";
      } else {
        document.getElementById("loginMessage").textContent =
          "Ошибка авторизации.";
      }
    });
  });
