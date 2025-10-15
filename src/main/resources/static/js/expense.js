async function addExpense(event) {
  event.preventDefault();

  const email = localStorage.getItem("userEmail");
  if (!email) {
    alert("Please log in first!");
    return;
  }

  const expense = {
    email: email,
    amount: parseFloat(document.getElementById("amount").value),
    category: document.getElementById("category").value,
    note: document.getElementById("note").value,
    date: document.getElementById("date").value
  };

  const response = await fetch("https://finment-student-expense-tracker-production.up.railway.app/api/expenses/add", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(expense),
  });

  const data = await response.json();
  alert(data.message);
  loadExpenses();
}

async function loadExpenses() {
  const email = localStorage.getItem("userEmail");
  const list = document.getElementById("expenseList");
  list.innerHTML = "";

  const response = await fetch(`http://localhost:8080/api/expenses/user/${email}`);
  const data = await response.json();

  data.forEach(exp => {
    const li = document.createElement("li");
    li.textContent = `${exp.category}: ₹${exp.amount} (${exp.date})`;
    list.appendChild(li);
  });
}

document.addEventListener("DOMContentLoaded", loadExpenses);
