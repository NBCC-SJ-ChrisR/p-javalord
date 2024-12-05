window.onload = function() {
    //Prevents form submission and checks creds before going to next page
    document.querySelector("#loginForm").addEventListener("submit", async function(event) {
        event.preventDefault();

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        try {
            let response = await fetch("EmployeeService/employees/" + username, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    "username": username,
                    "password": password,
                })
            })

            if (response.ok) {
                let data = await response.json();
                console.log(data);
                if (data.valid) {
                    document.querySelector("#loginForm").submit();
                }
                else if(data.invalid)
                {
                    alert("Invalid username or password");
                    console.log(data);
                }
                else {
                    console.log(data);
                }
            }
        }
        catch (error) {
            console.log(error)
            alert("Unable to connect to the server");
        }
    })
}