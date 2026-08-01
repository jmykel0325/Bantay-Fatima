const csrf = () => document.querySelector('meta[name="csrf-token"]')?.content ?? '';

const fetchJson = async (url, payload) => {
    let response;
    try {
        response = await fetch(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/json', Accept: 'application/json', 'X-CSRF-TOKEN': csrf()},
            body: JSON.stringify(payload),
        });
    } catch {
        throw new Error('Network error. Check your connection and try again.');
    }

    const data = await response.json().catch(() => ({}));
    if (!response.ok) {
        const firstValidationMessage = Object.values(data.errors || {}).flat()[0];
        const error = new Error(firstValidationMessage || data.message || `Unable to process your request (${response.status}).`);
        error.validationErrors = data.errors || {};
        throw error;
    }
    return data;
};

const showAlert = (message, type = 'error') => {
    const alert = document.getElementById('auth-alert');
    if (!alert) return;
    alert.textContent = message;
    alert.className = `mt-6 rounded-xl border p-4 text-sm ${type === 'success' ? 'border-teal/20 bg-teal/10 text-navy' : 'border-red-urgent/20 bg-red-urgent/10 text-red-urgent'}`;
    alert.focus();
};

const clearErrors = () => document.querySelectorAll('.field-error').forEach((element) => element.classList.add('hidden'));
const showErrors = (errors = {}) => Object.entries(errors).forEach(([field, messages]) => {
    const element = document.querySelector(`[data-error-for="${field}"]`);
    if (element) { element.textContent = messages[0]; element.classList.remove('hidden'); }
});

const setLoading = (button, loading, label) => {
    if (!button) return;
    button.disabled = loading;
    button.querySelector('.loading-spinner')?.classList.toggle('hidden', !loading);
    if (label) button.querySelector('.button-label').textContent = loading ? 'Please wait…' : label;
};

const configureOtpInputs = (container, onChange) => {
    const inputs = [...container.querySelectorAll('.otp-input')];
    const value = () => inputs.map((input) => input.value).join('');
    const update = () => onChange(value());
    inputs.forEach((input, index) => {
        input.addEventListener('input', () => {
            input.value = input.value.replace(/\D/g, '').slice(-1);
            if (input.value && inputs[index + 1]) inputs[index + 1].focus();
            update();
        });
        input.addEventListener('keydown', (event) => {
            if (event.key === 'Backspace' && !input.value && inputs[index - 1]) inputs[index - 1].focus();
        });
        input.addEventListener('paste', (event) => {
            const digits = event.clipboardData.getData('text').replace(/\D/g, '').slice(0, 6);
            if (digits.length !== 6) return;
            event.preventDefault();
            digits.split('').forEach((digit, digitIndex) => { inputs[digitIndex].value = digit; });
            inputs[5].focus(); update();
        });
    });
    return {value, clear: () => { inputs.forEach((input) => { input.value = ''; }); update(); inputs[0]?.focus(); }};
};

const startCountdown = (button, label, seconds = 60) => {
    let remaining = seconds;
    button.disabled = true;
    label.textContent = `(${remaining}s)`;
    const timer = window.setInterval(() => {
        remaining -= 1;
        label.textContent = remaining > 0 ? `(${remaining}s)` : '';
        if (remaining <= 0) { window.clearInterval(timer); button.disabled = false; }
    }, 1000);
};

const initializePasswordControls = () => {
    document.querySelectorAll('.password-toggle').forEach((button) => button.addEventListener('click', () => {
        const input = document.getElementById(button.dataset.target);
        const hidden = input.type === 'password';
        input.type = hidden ? 'text' : 'password';
        button.textContent = hidden ? 'Hide' : 'Show';
    }));

    const password = document.getElementById('register_password');
    const meter = document.getElementById('password-strength');
    const label = document.getElementById('password-strength-label');
    password?.addEventListener('input', () => {
        const score = [password.value.length >= 8, /[A-Za-z]/.test(password.value), /\d/.test(password.value), password.value.length >= 12].filter(Boolean).length;
        const widths = ['w-0', 'w-1/4', 'w-1/2', 'w-3/4', 'w-full'];
        meter.className = `h-full transition-all ${widths[score]} ${score >= 3 ? 'bg-teal' : score >= 2 ? 'bg-amber' : 'bg-red-urgent'}`;
        label.textContent = ['Use at least 8 characters with letters and numbers.', 'Weak password', 'Fair password', 'Good password', 'Strong password'][score];
    });
};

const initializeRegistration = () => {
    const form = document.getElementById('register-form');
    if (!form) return;
    const details = document.getElementById('register-details');
    const otpStep = document.getElementById('register-otp');
    const sendButton = document.getElementById('send-code-button');
    const verifyButton = document.getElementById('verify-code-button');
    const resendButton = document.getElementById('resend-code-button');
    const countdown = document.getElementById('resend-countdown');
    let registrationData = {};
    let email = '';
    const otp = configureOtpInputs(otpStep, (code) => { verifyButton.disabled = code.length !== 6; });

    const sendCode = async () => {
        registrationData = Object.fromEntries(new FormData(form).entries());
        const response = await fetchJson(form.dataset.sendUrl, registrationData);
        email = response.email;
        document.getElementById('masked-email').textContent = response.masked_email;
        startCountdown(resendButton, countdown);
        return response;
    };

    form.addEventListener('submit', async (event) => {
        event.preventDefault(); clearErrors(); setLoading(sendButton, true, 'Send Verification Code');
        try {
            const response = await sendCode();
            details.classList.add('hidden'); otpStep.classList.remove('hidden'); otp.clear();
            showAlert(response.message, 'success');
        } catch (error) { showErrors(error.validationErrors); showAlert(error.message); }
        finally { setLoading(sendButton, false, 'Send Verification Code'); }
    });

    verifyButton.addEventListener('click', async () => {
        setLoading(verifyButton, true, 'Verify Code');
        try {
            const response = await fetchJson(form.dataset.verifyUrl, {email, code: otp.value()});
            showAlert(response.message, 'success'); window.location.assign(response.redirect);
        } catch (error) { showErrors(error.validationErrors); showAlert(error.message); }
        finally { setLoading(verifyButton, false, 'Verify Code'); }
    });

    resendButton.addEventListener('click', async () => {
        try { const response = await sendCode(); otp.clear(); showAlert(response.message, 'success'); }
        catch (error) { showAlert(error.message); }
    });
    document.getElementById('change-email-button').addEventListener('click', () => {
        otpStep.classList.add('hidden'); details.classList.remove('hidden'); otp.clear();
    });
};

const initializePasswordReset = () => {
    const form = document.getElementById('password-reset-form');
    if (!form) return;
    const emailStep = document.getElementById('reset-email-step');
    const otpStep = document.getElementById('reset-otp-step');
    const passwordStep = document.getElementById('reset-password-step');
    const sendButton = document.getElementById('reset-send-code');
    const resendButton = document.getElementById('reset-resend-code');
    const resetButton = document.getElementById('reset-password-button');
    let email = '';
    const otp = configureOtpInputs(otpStep, (code) => { resetButton.disabled = code.length !== 6; });

    const sendCode = async () => {
        email = document.getElementById('reset_email').value;
        const response = await fetchJson(form.dataset.sendUrl, {email});
        email = response.email;
        document.getElementById('reset-masked-email').textContent = response.masked_email;
        startCountdown(resendButton, document.getElementById('reset-countdown'));
        return response;
    };

    form.addEventListener('submit', async (event) => {
        event.preventDefault(); clearErrors(); setLoading(sendButton, true, 'Send Verification Code');
        try {
            const response = await sendCode();
            emailStep.classList.add('hidden'); otpStep.classList.remove('hidden'); passwordStep.classList.remove('hidden'); otp.clear();
            showAlert(response.message, 'success');
        } catch (error) { showErrors(error.validationErrors); showAlert(error.message); }
        finally { setLoading(sendButton, false, 'Send Verification Code'); }
    });

    resendButton.addEventListener('click', async () => {
        try { const response = await sendCode(); otp.clear(); showAlert(response.message, 'success'); }
        catch (error) { showAlert(error.message); }
    });

    resetButton.addEventListener('click', async () => {
        setLoading(resetButton, true, 'Update Password');
        try {
            const response = await fetchJson(form.dataset.resetUrl, {
                email, code: otp.value(), password: document.getElementById('new_password').value,
                password_confirmation: document.getElementById('new_password_confirmation').value,
            });
            window.location.assign(response.redirect);
        } catch (error) { showErrors(error.validationErrors); showAlert(error.message); }
        finally { setLoading(resetButton, false, 'Update Password'); }
    });
};

document.addEventListener('DOMContentLoaded', () => {
    initializePasswordControls();
    initializeRegistration();
    initializePasswordReset();
});
