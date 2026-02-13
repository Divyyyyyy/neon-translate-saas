// ==========================================
// 1. NEURAL OTP AUTH LOGIC (UPGRADED V8.0)
// ==========================================

// Send OTP Button Click
document.getElementById('sendOTPBtn').addEventListener('click', async () => {
  const email = document.getElementById('authEmail').value;
  const name = document.getElementById('fullName')?.value || "User";
  const phone = document.getElementById('phoneInput')?.value || "N/A";
  const profession = document.getElementById('professionInput')?.value || "Neural Explorer";
  
  if (!email || !email.includes('@')) {
      alert("Bhai, valid email toh daal!");
      return;
  }

  const btn = document.getElementById('sendOTPBtn');
  const step1 = document.getElementById('authStep1');
  const step2 = document.getElementById('authStep2');

  btn.innerText = "Syncing Neural Data...";
  btn.disabled = true;

  try {
      // Updated URL with Profile Parameters
      const params = `email=${encodeURIComponent(email)}&name=${encodeURIComponent(name)}&phone=${encodeURIComponent(phone)}&profession=${encodeURIComponent(profession)}`;
      
      const response = await fetch(`http://localhost:8080/api/auth/send-otp?${params}`, {
          method: 'POST'
      });
      
      const result = await response.text();

      if (result === "SUCCESS") {
          step1.style.display = 'none'; 
          step2.style.display = 'block';
          alert("OTP tere email par bhej diya hai! Profile details sync ho gayi hain.");
      } else {
          alert("Lafda ho gaya: " + result);
          btn.innerText = "Get OTP Code";
          btn.disabled = false;
      }
  } catch (error) {
      console.error("Error:", error);
      alert("Backend band hai! Terminal mein Spring Boot start karo.");
      btn.innerText = "Get OTP Code";
      btn.disabled = false;
  }
});

// Verify OTP Button Click
document.getElementById('verifyOTPBtn').addEventListener('click', async () => {
  const email = document.getElementById('authEmail').value;
  const otp = document.getElementById('otpInput').value;

  if (!otp) {
      alert("OTP daalna bhool gaya bhai!");
      return;
  }

  try {
      const response = await fetch(`http://localhost:8080/api/auth/verify-otp?email=${encodeURIComponent(email)}&otp=${otp}`, {
          method: 'POST'
      });
      
      const data = await response.json(); // Ab backend JSON object bhej raha hai

      if (data.status === "VERIFIED") {
          // Save Profile Data to Browser Memory
          localStorage.setItem('userName', data.name);
          localStorage.setItem('userEmail', data.email);
          localStorage.setItem('userPhone', data.phone);
          localStorage.setItem('userProf', data.profession);

          alert(`Mubarak ho ${data.name}! Login Success.`);
          
          // Redirect to Dashboard (Is file ko hum next step mein banayenge)
          window.location.href = "dashboard.html"; 
      } else {
          alert("Galat OTP hai! Phir se try kar.");
      }
  } catch (error) {
      console.error("Verification error:", error);
      alert("Error: Shayad backend JSON nahi bhej raha. AuthController check karo!");
  }
});

// ==========================================
// 2. TRANSLATION, MIC & UI LOGIC
// ==========================================

window.openAuth = function() {
  document.getElementById('authOverlay').style.display = 'flex';
}

window.closeAuth = function() {
  document.getElementById('authOverlay').style.display = 'none';
}

document.getElementById('translateBtn').addEventListener('click', () => {
  const text = document.getElementById('inputText').value;
  const output = document.getElementById('outputText');

  if (!text) {
      alert("Pehle kuch likh toh sahi!");
      return;
  }

  output.value = "Neural Engine processing...";
  setTimeout(() => {
      output.value = "Translation coming soon! Abhi profile check karo.";
  }, 800);
});

const micBtn = document.getElementById('micBtn');
micBtn.addEventListener('click', () => {
  const recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)();
  recognition.lang = 'en-US';
  
  recognition.onstart = () => {
      micBtn.innerText = "🛑 Listening...";
      micBtn.style.background = "#ff4444";
  };

  recognition.onresult = (event) => {
      document.getElementById('inputText').value = event.results[0][0].transcript;
  };

  recognition.onend = () => {
      micBtn.innerText = "🎤 Start Mic";
      micBtn.style.background = "rgba(112, 0, 255, 0.2)";
  };

  recognition.start();
});

// Tilt Effect
VanillaTilt.init(document.querySelectorAll(".io-card, .pricing-card"), {
  max: 10,
  speed: 400,
  glare: true,
  "max-glare": 0.2
});