let toppings = [];
let crusts = [];
let sizes = [];

let userCrust;
let userToppings = [];
let userSize;
let userPizzas = [];
let quantity = 1;
let subtotal = 0;
let grandTotal = 0;

window.onload = function(){
    document.getElementById("placeOrderButton").addEventListener("click", confirmOrder);
    document.getElementById("changeOrderButton").addEventListener("click", editOrder);
    document.getElementById("addButton").addEventListener("click", addToCart);
    //Enable or disable time input based on asap checkbox
    document.getElementById("asap").addEventListener("change", function () {
        document.getElementById("timeInput").disabled = this.checked;
    });
    document.getElementById("delivery").addEventListener("change", estimateTime);
    document.getElementById("pickup").addEventListener("change", estimateTime);
    document.getElementById("confirmOrderButton").addEventListener("click", checkout);
    document.getElementById("goBackToCart").addEventListener("click", backToCart);
    document.getElementById("confirmButton").addEventListener("click", placeOrder);

    getToppings();
    getCrust();
    getSizes();
}

async function placeOrder(){
    let customer = {};
    if (document.getElementById("payNow").checked) {
        customer = {
            firstName: document.getElementById("firstName").value,
            lastName: document.getElementById("lastName").value,
            email: document.getElementById("email").value,
            phone: document.getElementById("phoneNumber").value,
            houseNumber: document.getElementById("houseNumber").value,
            street: document.getElementById("street").value,
            province: document.getElementById("province").value,
            postalCode: document.getElementById("postalCode").value,
        }
        try {
            let response = await fetch("CustomerService/customer", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(customer)
            })

            console.log(response);
            if (response.status === 201) {
                alert("Order Placed!");
                window.location.href = "index.html";
            }
            else
                console.log(response);
        }
        catch (error) {
            console.log(error)
            alert("Unable to connect to the server");
        }
    }
    else {
        window.location.href = "https://www.paypal.com/ca/home";
    }
}

function checkout() {
changePages("checkout");
}

function backToCart() {
    changePages("summary");
}

function estimateTime() {
    let pickup = document.getElementById("pickup").checked;
    document.getElementById("prepTime").value = (pickup) ? "20 Minutes" : "50 Minutes";
}

function removeFromCart(event){
    let index = event.target.getAttribute("data-index");

    //Delete pizza from cart repopulate pizza container
    userPizzas.splice(index, 1);
    confirmOrder();
}

function editOrder(){
    changePages("order")
}

function changePages(page){
    if (page === "order") {
        document.getElementById("orderContainer").classList.remove("hidden");
        document.getElementById("orderSummary").classList.add("hidden");
        document.getElementById("customerInfo").classList.add("hidden");
    }
    else if (page === "summary") {
        //Hide order form, show confirmation page
        document.getElementById("orderContainer").classList.add("hidden");
        document.getElementById("orderSummary").classList.remove("hidden");
        document.getElementById("customerInfo").classList.add("hidden");
    }
    else if( page === "checkout"){
        document.getElementById("orderSummary").classList.add("hidden");
        document.getElementById("orderContainer").classList.add("hidden");
        document.getElementById("customerInfo").classList.remove("hidden");
    }
    //Update Cart btn
    document.getElementById("placeOrderButton").innerText = "Go To Cart (" + userPizzas.length + ")";
}

function addToCart(){
    //Clears previous selections
    userCrust = [];
    userToppings = [];
    userSize = [];
    //Gets the selected toppings and stores them all as objects with value, and display fields
    let selectToppings = document.getElementById("toppings");
    for (let option of selectToppings.options) {
        if (option.selected) {
            let toppingObj = matchTopping(option.value);
            if (toppingObj) {
                userToppings.push({
                    obj: toppingObj,
                    text: option.innerText
                });
            }
            else
                console.log("No topping found");
        }
    }

    //Put user selection in variables
    let userCrustName = document.getElementById("crust").value;
    let userSizeName = document.getElementById("size").value;
    quantity = document.getElementById("quantity").value;

    //Populate user selection objects for crust and size
    userSize = sizes.find(sizes => sizes.name === userSizeName);
    userCrust = crusts.find(crust => crust.name === userCrustName);

    //Create A Pizza object
    userPizzas.push({
        crust: userCrust,
        size: userSize,
        toppings: userToppings,
        quantity: Number(quantity)
    });
    console.log(userPizzas);

    //Set selections to default
    document.getElementById("size").selectedIndex = 0;
    document.getElementById("toppings").selectedIndex = -1;
    document.getElementById("crust").selectedIndex = 0;
    document.getElementById("quantity").value = 1;

    //Show cart amount in button
    document.getElementById("placeOrderButton").innerText = "Go To Cart (" + userPizzas.length + ")";
}

//Goes to summary page and displays cart
function confirmOrder() {
    grandTotal = 0;
    subtotal = 0;
    changePages("summary");

    //Populate pizza container
    let container = document.getElementById("pizzaContainer");
    container.innerHTML = '';

    //For each pizza in array add to div for display
    userPizzas.forEach((pizza, index) => {
        //Send pizza to get subtotalled
        let sub = calculateSubtotal(pizza);
        subtotal += Number(sub); //Adds pizza total to subtotal
        let div = document.createElement("div");
        div.classList.add("pizzaDiv");

        div.innerHTML = `
        <p><strong>Size:</strong> ${pizza.size.name + " Price: " + pizza.size.price}</p>
        <p><strong>Crust:</strong> ${pizza.crust.name + " Price: " + pizza.crust.price}</p>
        <p><strong>Toppings:</strong> ${pizza.toppings.map(t => t.text).join(', ')}</p>
        <p><strong>Quantity:</strong> ${pizza.quantity}</p>
        <p><strong>Total:</strong> ${"$" + sub.toFixed(2)}</p>
        <button class="removeButton" data-index="${index}">Remove</button>`; //Adds index button corresponding with pizza array for delete

        container.append(div);
    });

    calculateTotals();

    //Add event listner for dynamically created elements
    let removeButtons = document.querySelectorAll(".removeButton");
    //loop through add event listner and then remove via index
    removeButtons.forEach(removeButton => {
        removeButton.addEventListener("click", removeFromCart);
    })
}

//Receives a pizza and calculates its subtotal
function calculateSubtotal(pizza){
    let crust = pizza.crust.price;
    let size = pizza.size.price;
    let toppings = 0;
    if (pizza.toppings.length > 0) {
        pizza.toppings.forEach(topping => {
            toppings += topping.obj.price;
        })
    }
    let quantity = pizza.quantity;

    return (crust + size + toppings) * quantity;

}

//Calculates and populates totals
function calculateTotals() {
    grandTotal = subtotal * 1.15;

    document.getElementById("subTotal").value = "$" + subtotal.toFixed(2);
    document.getElementById("grandTotal").value = "$" + grandTotal.toFixed(2);
    document.getElementById("subTotalInfo").value = "$" + subtotal.toFixed(2);
    document.getElementById("grandTotalInfo").value = "$" + grandTotal.toFixed(2);
}

//Returns matching topping type by name sent in
function matchTopping(name) {
    return toppings.find(topping => topping.name === name);
}

async function getSizes() {
    //Call for Toppings data if not already populated
    if (sizes.length === 0) {
        try {
            let response = await fetch("PizzaSizeService/size", {
                method: "GET"
            })

            if (response.ok) {
                let data = await response.json();
                sizes = data;
                //console.log(data);
                if (data != null) {
                    // Do table population
                    let input = document.querySelector("#size");
                    let html = "";
                    for (let i = 0; i < data.length; i++) {
                        if (data[i].name.toLowerCase() === "X-Large".toLowerCase()) {
                            html += "<option value='";
                            html += data[i].name + "' disabled>" + data[i].name + " --- $" + data[i].price + "</option>";
                        } else {
                            html += "<option value='";
                            html += data[i].name + "'>" + data[i].name + " --- $" + data[i].price + "</option>";
                        }
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