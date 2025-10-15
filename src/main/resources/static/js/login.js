(() => {
  // ================= CONFIG =================
const BASE_URL = "https://finment-student-expense-tracker-production.up.railway.app";
 // same-origin (Spring Boot serves frontend)
  const ENDPOINTS = {
    login: '/api/auth/login',
    register: '/api/auth/register',
    forgotPassword: '/api/auth/forgot-password',
    health: '/api/health'
  };

  // ========== HELPERS ==========
  const $ = (sel) => document.querySelector(sel);
  const $$ = (sel) => Array.from(document.querySelectorAll(sel));

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  function showError(el, msg) {
    if (!el) return;
    el.textContent = msg || '';
    el.style.display = msg ? 'block' : 'none';
  }

  function toggleSpinner(btn, on) {
    const spinner = btn.querySelector('.spinner');
    const btnText = btn.querySelector('.btn-text');
    btn.disabled = on;
    if (spinner) spinner.style.display = on ? 'inline-block' : 'none';
    if (btnText) btnText.style.opacity = on ? '0.5' : '1';
  }

  function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    if (!input) return;
    input.type = input.type === 'password' ? 'text' : 'password';
  }
  window.togglePassword = togglePassword; // needed for onclick in HTML

  // ========== TOGGLE LOGIN/SIGNUP ==========
  function initToggleTabs() {
    const loginTab = $('#loginTab');
    const signupTab = $('#signupTab');
    const loginForm = $('#loginForm');
    const signupForm = $('#signupForm');

    if (!loginTab || !signupTab) return;

    loginTab.addEventListener('click', () => {
      loginTab.classList.add('active');
      signupTab.classList.remove('active');
      loginForm.classList.add('active');
      signupForm.classList.remove('active');
    });

    signupTab.addEventListener('click', () => {
      signupTab.classList.add('active');
      loginTab.classList.remove('active');
      signupForm.classList.add('active');
      loginForm.classList.remove('active');
    });
  }

  // ========== API CALLS ==========
  async function apiFetch(path, options = {}) {
    const url = BASE_URL + path;
    const opts = {
      headers: { 'Content-Type': 'application/json' },
      ...options
    };
    if (opts.body && typeof opts.body !== 'string') {
      opts.body = JSON.stringify(opts.body);
    }
    const res = await fetch(url, opts);
    const data = await res.json().catch(() => ({}));
    if (!res.ok) throw data;
    return data;
  }

  // ========== FORM VALIDATION ==========
  function validateLoginForm() {
    const email = $('#loginEmail').value.trim();
    const password = $('#loginPassword').value;
    let ok = true;
    if (!emailRegex.test(email)) {
      showError($('#loginEmailError'), 'Enter a valid email');
      ok = false;
    } else showError($('#loginEmailError'), '');
    if (!password) {
      showError($('#loginPasswordError'), 'Password required');
      ok = false;
    } else showError($('#loginPasswordError'), '');
    return ok;
  }

  function validateSignupForm() {
    const name = $('#signupName').value.trim();
    const email = $('#signupEmail').value.trim();
    const password = $('#signupPassword').value;
    const confirm = $('#confirmPassword').value;
    let ok = true;

    if (!name) {
      showError($('#signupNameError'), 'Full name required');
      ok = false;
    } else showError($('#signupNameError'), '');

    if (!emailRegex.test(email)) {
      showError($('#signupEmailError'), 'Enter valid email');
      ok = false;
    } else showError($('#signupEmailError'), '');

    if (password.length < 6) {
      showError($('#signupPasswordError'), 'Min 6 characters');
      ok = false;
    } else showError($('#signupPasswordError'), '');

    if (confirm !== password) {
      showError($('#confirmPasswordError'), 'Passwords don’t match');
      ok = false;
    } else showError($('#confirmPasswordError'), '');
    return ok;
  }

  // ========== FORM HANDLERS ==========
  async function handleLoginSubmit(e) {
    e.preventDefault();
    if (!validateLoginForm()) return;
    const btn = $('#loginBtn');
    toggleSpinner(btn, true);

    try {
      const data = await apiFetch(ENDPOINTS.login, {
        method: 'POST',
        body: {
          email: $('#loginEmail').value.trim(),
          password: $('#loginPassword').value
        }
      });
      alert('Login successful!');
      window.location.href = '/index.html';
    } catch (err) {
      showError($('#loginPasswordError'), err.message || 'Login failed');
    } finally {
      toggleSpinner(btn, false);
    }
  }

  async function handleSignupSubmit(e) {
    e.preventDefault();
    if (!validateSignupForm()) return;
    const btn = $('#signupBtn');
    toggleSpinner(btn, true);

    try {
      const data = await apiFetch(ENDPOINTS.register, {
        method: 'POST',
        body: {
          name: $('#signupName').value.trim(),
          email: $('#signupEmail').value.trim(),
          password: $('#signupPassword').value
        }
      });
      alert('Signup successful!');
      window.location.href = '/dashboard.html';
    } catch (err) {
      showError($('#signupEmailError'), err.message || 'Signup failed');
    } finally {
      toggleSpinner(btn, false);
    }
  }

  // ========== INIT ==========
  function startup() {
    initToggleTabs();
    $('#loginForm')?.addEventListener('submit', handleLoginSubmit);
    $('#signupForm')?.addEventListener('submit', handleSignupSubmit);
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', startup);
  } else startup();
})();
