let toppings;
let orders;

window.onload = function() {
    document.getElementById('logoutButton').addEventListener('click', function() {window.location.href="index.html";});
    document.getElementById("toppingsTable").addEventListener('click', tableSelect)
    document.getElementById("deleteButton").addEventListener('click', toppingDelete)
    document.getElementById("addButton").addEventListener("click", addTopping);
    document.getElementById("viewOrdersButton").addEventListener("click", showOrders)
    document.getElementById("ordersTable").addEventListener('click', orderTableSelect)
    document.getElementById("changeButton").addEventListener('click', changeStatus)
    loadTables();
}

async function changeStatus() {
    let requestedStatus = document.querySelector(".selectedOrder").getElementsByTagName("td")[3].innerText.toLowerCase();
    let id = document.querySelector(".selectedOrder").getElementsByTagName("td")[0].innerText;

    if (requestedStatus === "pending") {
        //Send ajax
        try {
            let response = await fetch("OrdersService/orders/" + id, {
                method: "PUT"
            })

            if (response.ok) {
                    showOrders(true);
            } else if (response.status === 500) {
                console.log(response.error);
                alert("Something went wrong with server.");
            }
        } catch (error) {
            console.log(error)
            alert("Unable to connect to the server");
        }
    }
    else {
        alert("Order Already Filled!");
    }
}

function orderTableSelect(event) {
    document.getElementById("changeButton").removeAttribute('disabled');

    let row = event.target.closest('tr');
    let rows = document.querySelector("#ordersTable").getElementsByTagName("tr");
    for (let i = 0; i < rows.length; i++) {
        rows[i].classList.remove('selectedOrder');
    }
    row.classList.add('selectedOrder');
}

async function showOrders(bool) {
    let requestedStatus = document.getElementById("orderStatus").value;

    //Call for Orders data if not already retrieved or if true sent from ajax call to recall
    if (orders == null || bool) {
        try {
            let response = await fetch("OrdersService/orders", {
                method: "GET"
            })

            if (response.ok) {
                let data = await response.json();
                orders = data;
                console.log(data);
                if (data != null) {
                    populateOrderTable(requestedStatus);
                }
            } else if (response.status === 500) {
                console.log(response.error);
                alert("Something went wrong with server.");
            }
        } catch (error) {
            console.log(error)
            alert("Unable to connect to the server");
        }
    } else if (orders != null) {
        populateOrderTable(requestedStatus);
    }
}

function populateOrderTable(requestedStatus) {
    let tempArray = [];
    for (let i = 0; i < orders.length; i++) {
        if (orders[i].orderStatus.toLowerCase() === requestedStatus.toLowerCase() ) {
            tempArray.push(orders[i]);
        }
    }

    // Do table population
    let table = document.querySelector("#ordersTable");
    let html = "<tr><th>Order ID</th><th>Customer ID</th><th>Total</th><th>Status</th></tr>";
    for (let i = 0; i < tempArray.length; i++) {
        html += "<tr>><td>" + tempArray[i].id + "</td>";
        html += "<td>" + tempArray[i].customerId + "</td>";
        html += "<td>" + tempArray[i].orderTotal + "</td>";
        html += "<td>" + tempArray[i].orderStatus + "</td></tr>";
    }
    table.innerHTML = html;

}

//Add Topping call
async function addTopping() {
let name = document.getElementById("topName").value;
let price = document.getElementById("topPrice").value;
if (name == null || name == "") {
    alert("Please enter valid name");
    return;
}
    try {
        let response = await fetch("ToppingsService/toppings", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                "id": 1,
                "name": name,
                "price": price,
                "createDate": new Date().toISOString(),
                "empAdd": 1,
                "active": 1
            })
        })
        if (response.ok) {
            let data = await response.json();
            console.log(data);
            if (data != null) {
                // Do table population
                loadTables();
            }
        }
        else if(response.status === 500){
            console.log(response.error);
            alert("Something went wrong with server.");
        }
    }
    catch (error) {
        console.log(error)
        alert("Unable to connect to the server");
    }
}

//Sends call to delete topping
function toppingDelete() {
    //Gets name of selected topping and id for that topping
    let name = document.querySelector(".selectedTopping").querySelector("td").innerHTML;
    let id;
    for (let i = 0; i < toppings.length; i++) {
        if (toppings[i].name === name)
            id = toppings[i].id;
    }

    let url = "ToppingsService/toppings/" + id;
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            let resp = xmlhttp.responseText.trim();
            if (resp.search("ERROR") >= 0 || resp !== "true") {
                alert("could not complete delete request");
            } else {
                alert("delete request completed successfully");
                loadTables();
            }
        }
    };
    xmlhttp.open("DELETE", url, true);
    xmlhttp.send();
}

//Deselects all rows then selects row clicked
function tableSelect(event) {
    //Enable btn
    document.getElementById("deleteButton").removeAttribute('disabled');
    let row = event.target.closest('tr');
    let rows = document.querySelector("#toppingsTable").getElementsByTagName("tr");
    for (let i = 0; i < rows.length; i++) {
        rows[i].classList.remove('selectedTopping');
    }
    row.classList.add('selectedTopping');
}

async function loadTables() {
    //Call for Toppings data
    try {
        let response = await fetch("ToppingsService/toppings", {
            method: "GET"
        })

        if (response.ok) {
            let data = await response.json();
            toppings = data;
            console.log(data);
            if (data != null) {
                // Do table population
                let table = document.querySelector("#toppingsTable");
                let html = "<tr><th>Name</th><th>Price</th><th>Available</th></tr>";
                for (let i = 0; i < data.length; i++) {
                    html += "<tr>><td>" + data[i].name + "</td>";
                    html += "<td>" + data[i].price + "</td>";
                    html += "<td>" + (data[i].active === 1 ? "Available" : "Unavailable") + "</td></tr>";
                }
                table.innerHTML = html;
            }
        }
        else if(response.status === 500){
            console.log(response.error);
            alert("Something went wrong with server.");
        }
    }
    catch (error) {
        console.log(error)
        alert("Unable to connect to the server");
    }
}