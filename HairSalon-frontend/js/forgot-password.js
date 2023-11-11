document.addEventListener("DOMContentLoaded", function () {
    const resetForm = document.getElementById("resetForm");
    const resetMessage = document.getElementById("resetMessage");
    const resetCodeModal = new bootstrap.Modal(document.getElementById("resetCodeModal"));
    const resetCodeForm = document.getElementById("resetCodeForm");

    const resetPasswordButton = document.getElementById("resetPasswordButton");

    resetPasswordButton.addEventListener("click", function () {
        const email = document.getElementById("email").value;
        if (!email) {
            resetMessage.innerHTML = "Пожалуйста, введите вашу электронную почту.";
            return;
        }

        const response = fetch("http://localhost:8080/api/users/reset-code?email=" + email, {
            method: "POST",
        })
            .then((response) => {
                if (response.status === 200) {
                    resetCodeModal.show();
                } else {
                    resetMessage.innerHTML = "Не удалось отправить запрос. Пожалуйста, попробуйте позже.";
                }
            });
    });

    resetCodeForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const resetCode = document.getElementById("resetCode").value;
        const email = document.getElementById("email").value;

        if (!resetCode) {
            resetMessage.innerHTML = "Пожалуйста, введите код сброса пароля.";
            return;
        }

        fetch("http://localhost:8080/api/users/reset-code/check?email=" + email + "&resetCode=" + resetCode, {
            method: "POST",
        })
            .then((response) => {
                if (response.status === 200) {
                    resetCodeModal.hide();
                } else {
                    resetMessage.innerHTML = "Неверный код сброса пароля. Пожалуйста, попробуйте еще раз.";
                }
            });
    });
});