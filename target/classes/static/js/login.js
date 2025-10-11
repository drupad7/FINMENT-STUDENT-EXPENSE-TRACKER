async function loginUser(event) {
  event.preventDefault();

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  const response = await fetch("http://localhost:8085/api/users/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });

  const data = await response.json();

  if (response.ok && data.status === "success") {
    alert(data.message);
    localStorage.setItem("userEmail", data.email);
    window.location.href = "index.html";
  } else {
    alert(data.message);
  }
}
