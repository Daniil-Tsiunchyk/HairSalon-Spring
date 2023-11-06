document
    .getElementById("createUserForm")
    .addEventListener("submit", function (event) {
        event.preventDefault();
        const subject = document.getElementById("setSubject").value;
        const text = document.getElementById("setText").value;
        const role = document.getElementById("SelectUserRole").value;

        const url = `http://localhost:8080/api/emails/spam?subject=${encodeURIComponent(subject)}&text=${encodeURIComponent(text)}&role=${role}`;

        fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        }).then((response) => {
            if (response.status === 204) {
                alert("Сообщение успешно отправлено!");

                document.getElementById("setSubject").value = "";
                document.getElementById("setText").value = "";
                document.getElementById("SelectUserRole").value = "USER";
            } else {
                alert("Ошибка при отправке сообщения.");
            }
        });
    });
