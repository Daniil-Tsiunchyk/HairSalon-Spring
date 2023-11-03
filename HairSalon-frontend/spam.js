document
    .getElementById("createUserForm")
    .addEventListener("submit", function (event) {
        event.preventDefault();
        const subject = document.getElementById("setSubject").value;
        const text = document.getElementById("setText").value;
        const role = document.getElementById("SelectUserRole").value;

        fetch("http://localhost:8080//api/emails", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ subject, text, role }),
        }).then((response) => {
            if (response.status === 201) {
                updateTable();
                document.getElementById("setSubject").value = "";
                document.getElementById("setText").value = "";
                document.getElementById("SelectUserRole").value = "USER";
            }
        });
    });
