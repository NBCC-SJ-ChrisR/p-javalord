let toppings = [];
let crusts = [];

let userCrust = "";
let userToppings = [];
let userSize = ""

window.onload = function(){
    document.getElementById("placeOrderButton").addEventListener("click", confirmOrder);
    document.getElementById("changeOrderButton").addEventListener("click", editOrder);
    getToppings();
    getCrust();
}

function editOrder(){
    //Hide order form, show confirmation page
    document.getElementById("orderContainer").classList.remove("hidden");
    document.getElementById("orderSummary").classList.add("hidden");
}

function confirmOrder() {
    //Put user selection in global variables
    userCrust = document.getElementById("crust").value;
    userSize = document.getElementById("size").value;
    //Gets the selected toppings and stores them all as objects with value, and display fields
    let selectToppings = document.getElementById("toppings");
    for (let option of selectToppings.options) {
        if (option.selected) {
            userToppings.push({
                value: option.value,
                text: option.innerText
            });
        }
    }
    console.log(userToppings);

    //Hide order form, show confirmation page
    document.getElementById("orderContainer").classList.add("hidden");
    document.getElementById("orderSummary").classList.remove("hidden");

    //Populate user selections
    document.getElementById("sizeConfirm").value = userSize;
    document.getElementById("crustConfirm").value = userCrust;
    let toppingsDisplay = document.getElementById("toppingsConfirm");


}

async function getCrust(){
    //Call for Crust data if not already populated
    if (crusts.length === 0) {
        try {
            let response = await fetch("PizzaCrustService/crusts", {
                method: "GET"
            })

            if (response.ok) {
                let data = await response.json();
                crusts = data;
                //console.log(data);
                if (data != null) {
                    // Do table population
                    let input = document.querySelector("#crust");
                    let html = "";
                    for (let i = 0; i < data.length; i++) {
                        html += "<option value='";
                        html += data[i].name + "'>" + data[i].name + " --- Extra +$" + data[i].price + "</option>";
                    }
                    input.innerHTML = html;
                }
            } else if (response.status === 500) {
                console.log(response.error);
                alert("Something went wrong with server.");
            }
        } catch (error) {
            console.log(error)
            alert("Unable to connect to the server");
        }
    }
}

async function getToppings() {
    //Call for Toppings data if not already populated
    if (toppings.length === 0) {
        try {
            let response = await fetch("ToppingsService/toppings", {
                method: "GET"
            })

            if (response.ok) {
                let data = await response.json();
                toppings = data;
                //console.log(data);
                if (data != null) {
                    // Do table population
                    let input = document.querySelector("#toppings");
                    let html = "";
                    for (let i = 0; i < data.length; i++) {
                        html += "<option value='";
                        html += data[i].name + "'>" + data[i].name + " --- Extra +$" + data[i].price + "</option>";
                    }
                    input.innerHTML = html;
                }
            } else if (response.status === 500) {
                console.log(response.error);
                alert("Something went wrong with server.");
            }
        } catch (error) {
            console.log(error)
            alert("Unable to connect to the server");
        }
    }
}