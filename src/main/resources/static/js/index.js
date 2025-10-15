const BASE_URL = "http://localhost:8080/api/expenses";
const BUDGET_URL = "http://localhost:8080/api/budget";
const userEmail = localStorage.getItem("userEmail");

if (!userEmail) {
    alert("Please login first!");
    window.location.href = "login.html";
}

// -------------------- DASHBOARD --------------------
let totalIncome = 0;
let totalExpenses = 0;
let cashInHand = 0;
let upiBalance = 0;
let transactions = [];

function updateDashboard() {
    const netBalance = totalIncome - totalExpenses;
    document.getElementById("totalIncome").innerText = `₹${totalIncome.toFixed(2)}`;
    document.getElementById("totalExpenses").innerText = `₹${totalExpenses.toFixed(2)}`;
    document.getElementById("cashInHand").innerText = `₹${cashInHand.toFixed(2)}`;
    document.getElementById("upiBalance").innerText = `₹${upiBalance.toFixed(2)}`;
    document.getElementById("currentBalance").innerText = `₹${netBalance.toFixed(2)}`;
}

// -------------------- NOTIFICATION --------------------
function showNotification(msg, type = "success") {
    const n = document.getElementById("notification");
    n.className = "notification " + (type === "error" ? "error" : "");
    n.innerText = msg;
    n.classList.add("show");
    setTimeout(() => n.classList.remove("show"), 3000);
}

// -------------------- LOAD EXPENSES --------------------
async function loadExpenses() {
    try {
        const res = await fetch(`${BASE_URL}/user/${encodeURIComponent(userEmail)}`);
        if (!res.ok) throw new Error("Failed to fetch expenses");

        const data = await res.json();
        transactions = data.map(item => ({
            type: item.category === "Income" ? "income" : "expense",
            amount: item.amount,
            wallet: item.wallet || "cash",
            date: item.date,
            category: item.category,
            description: item.description || ""
        }));

        totalIncome = 0;
        totalExpenses = 0;
        cashInHand = 0;
        upiBalance = 0;

        data.forEach(e => {
            if (e.category === "Income") totalIncome += e.amount;
            else totalExpenses += e.amount;

            if (e.wallet === "cash")
                cashInHand += e.amount * (e.category === "Income" ? 1 : -1);
            else
                upiBalance += e.amount * (e.category === "Income" ? 1 : -1);
        });

        renderTransactions();
        updateDashboard();
        updateBudgetDisplay();
        renderTodayExpenses(); // ✅ Update today's table
    } catch (err) {
        console.error(err);
        showNotification("⚠ Server not reachable!", "error");
    }
}

// -------------------- RENDER TRANSACTIONS --------------------
function renderTransactions() {
    const list = document.getElementById("transactionList");
    list.innerHTML = "";

    if (transactions.length === 0) {
        list.innerHTML = `
        <div style="text-align:center; padding:40px; opacity:0.7;">
            <h3>🎉 Ready to start tracking?</h3>
            <p>Add your first transaction above to see the magic happen!</p>
        </div>`;
        return;
    }

    transactions.forEach(t => {
        const div = document.createElement("div");
        div.classList.add("transaction-item", t.type);
        div.innerHTML = `
            <div class="transaction-info">
                <h4>${t.category}</h4>
                <div class="transaction-meta">${t.date.split("T")[0]} • ${t.description}</div>
            </div>
            <div class="transaction-amount ${t.type}">₹${t.amount.toFixed(2)}</div>
        `;
        list.appendChild(div);
    });
}

// -------------------- ADD INCOME --------------------
document.getElementById("incomeForm").addEventListener("submit", async e => {
    e.preventDefault();
    const amount = parseFloat(document.getElementById("incomeAmount").value);
    const wallet = document.getElementById("incomeWallet").value;
    const date = document.getElementById("incomeDate").value;
    const source = document.getElementById("incomeSource").value;

    if (!amount || !date) return showNotification("Enter all fields!", "error");

    try {
        const res = await fetch(`${BASE_URL}/add`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                email: userEmail,
                amount,
                category: "Income",
                wallet,
                description: source,
                date
            }),
        });

        if (res.ok) {
            showNotification("✅ Income added!");
            document.getElementById("incomeForm").reset();
            loadExpenses();
        } else showNotification("❌ Failed to add income!", "error");
    } catch (err) {
        showNotification("⚠ Server not reachable!", "error");
    }
});

// -------------------- ADD EXPENSE --------------------
document.getElementById("expenseForm").addEventListener("submit", async e => {
    e.preventDefault();
    const amount = parseFloat(document.getElementById("expenseAmount").value);
    const wallet = document.getElementById("expenseWallet").value;
    const date = document.getElementById("expenseDate").value;
    const category = document.getElementById("expenseCategory").value;
    const description = document.getElementById("expenseDescription").value;

    if (!amount || !date || !category) return showNotification("Enter all fields!", "error");

    try {
        const res = await fetch(`${BASE_URL}/add`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                email: userEmail,
                amount,
                category,
                wallet,
                description,
                date
            }),
        });

        if (res.ok) {
            showNotification("✅ Expense added!");
            document.getElementById("expenseForm").reset();
            loadExpenses();
        } else showNotification("❌ Failed to add expense!", "error");
    } catch (err) {
        showNotification("⚠ Server not reachable!", "error");
    }
});

// -------------------- TRANSFER WALLET --------------------
function openTransferModal(type) {
    const title = document.getElementById("transferTitle");
    if (type === "cash-to-upi") title.innerText = "Cash → UPI";
    else if (type === "upi-to-cash") title.innerText = "UPI → Cash";
    else return;

    document.getElementById("transferModal").style.display = "block";
    document.getElementById("transferForm").onsubmit = async (e) => {
        e.preventDefault();
        const amount = parseFloat(document.getElementById("transferAmount").value);
        const description = document.getElementById("transferDesc").value;
        const date = new Date().toISOString().split("T")[0];

        let fromWallet = type === "cash-to-upi" ? "cash" : "upi";
        let toWallet = type === "cash-to-upi" ? "upi" : "cash";

        const res = await fetch(`${BASE_URL}/transfer`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email: userEmail, fromWallet, toWallet, amount, description, date })
        });

        if (res.ok) {
            showNotification("✅ Transfer successful!");
            closeTransferModal();
            loadExpenses();
        } else showNotification("❌ Transfer failed!", "error");
    };
}

function closeTransferModal() {
    document.getElementById("transferModal").style.display = "none";
    document.getElementById("transferForm").reset();
}

// -------------------- BUDGET --------------------
async function loadBudget() {
    const month = new Date().getMonth() + 1;
    try {
        const res = await fetch(`${BUDGET_URL}/${encodeURIComponent(userEmail)}/${month}`);
        if (!res.ok) throw new Error("Failed to fetch budget");

        const data = await res.json();
        document.getElementById("budgetTotal").innerText = data.limitAmount.toFixed(2);
        updateBudgetDisplay();
        document.getElementById("budgetDisplay").style.display = "block";
    } catch {
        document.getElementById("budgetDisplay").style.display = "none";
    }
}

async function setBudget() {
    const month = new Date().getMonth() + 1;
    const year = new Date().getFullYear();
    const amount = parseFloat(document.getElementById("budgetAmount").value);

    if (!amount) return showNotification("Enter a budget amount!", "error");

    try {
        const res = await fetch(`${BUDGET_URL}/set`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email: userEmail, month, year, limitAmount: amount })
        });

        if (res.ok) {
            showNotification("🎯 Budget set successfully!");
            document.getElementById("budgetAmount").value = "";
            loadBudget();
        } else showNotification("❌ Failed to set budget!", "error");
    } catch {
        showNotification("⚠ Server not reachable!", "error");
    }
}

function updateBudgetDisplay() {
    const spent = totalExpenses;
    const total = parseFloat(document.getElementById("budgetTotal").innerText) || 0;
    document.getElementById("budgetSpent").innerText = spent.toFixed(2);
    document.getElementById("budgetRemaining").innerText = (total - spent).toFixed(2);
    const fill = document.getElementById("budgetFill");
    const percentage = total > 0 ? Math.min((spent / total) * 100, 100) : 0;
    fill.style.width = percentage + "%";
}

// -------------------- TODAY'S EXPENSES --------------------
function renderTodayExpenses() {
    const today = new Date().toISOString().split("T")[0];
    const dailyExpensesDiv = document.getElementById("dailyExpenses");
    dailyExpensesDiv.innerHTML = "";

    // ✅ Filter only today's actual expenses (exclude transfers)
    const todayExpenses = transactions.filter(
        t =>
            t.date.split("T")[0] === today &&
            t.type === "expense" &&
            t.category.toLowerCase() !== "transfer"
    );

    if (todayExpenses.length === 0) {
        dailyExpensesDiv.innerHTML = `
            <div style="text-align: center; padding: 20px; opacity: 0.7;">
                <p>No expenses today yet!</p>
            </div>`;
        document.getElementById("todayTotal").innerText = "0.00";
        return;
    }

    // ✅ Create table for today's expenses
    const table = document.createElement("table");
    table.classList.add("today-expense-table");
    table.innerHTML = `
        <thead>
            <tr>
                <th>Category</th>
                <th>Wallet</th>
                <th>Description</th>
                <th>Amount (₹)</th>
            </tr>
        </thead>
        <tbody></tbody>
    `;

    const tbody = table.querySelector("tbody");
    let todayTotal = 0;

    todayExpenses.forEach(exp => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${exp.category}</td>
            <td>${exp.wallet}</td>
            <td>${exp.description}</td>
            <td>₹${exp.amount.toFixed(2)}</td>
        `;
        tbody.appendChild(tr);
        todayTotal += exp.amount;
    });

    dailyExpensesDiv.appendChild(table);
    document.getElementById("todayTotal").innerText = todayTotal.toFixed(2);
}


// -------------------- INITIAL LOAD --------------------
window.onload = () => {
    loadExpenses();
    loadBudget();
};
