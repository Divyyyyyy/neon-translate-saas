# 🌐 NeonTranslate: Neural AI SaaS Platform

**NeonTranslate** ek high-performance, cloud-native translation engine hai jo complex neural machine learning models ka use karke real-time translations provide karta hai. Is project ko enterprise-grade scalability aur security ko dhyan mein rakh kar design kiya gaya hai.

---

## 🚀 Key Features

* **Neural AI Engine:** Sub-100ms latency ke saath accurate language translation.
* **Secure Authentication:** SMTP-based OTP system (JavaMailSender) 16-digit secure App Password integration ke saath.
* **Robust Backend:** Spring Boot architecture jo heavy traffic aur secure data handling ke liye optimize hai.
* **Scalable Database:** MySQL integration for user management aur translation history logs.
* **Modern UI:** Dark-themed, responsive dashboard jo user experience (UX) ko priority deta hai.

---

## 🛠️ Technology Stack

| Component | Technology Used |
| :--- | :--- |
| **Backend** | Java, Spring Boot, Spring Data JPA |
| **Database** | MySQL (Relational Database Management) |
| **Frontend** | HTML5, CSS3, JavaScript |
| **Security/Mail** | JavaMailSender, SMTP (Gmail) |
| **Deployment** | Railway (Backend), Vercel (Frontend) |
| **Version Control** | Git & GitHub |

---

## 🏗️ Architecture Flow

1.  **Frontend (Vercel):** User interface se request trigger karta hai.
2.  **API Gateway:** Request Railway par deployed Spring Boot backend tak pahunchti hai.
3.  **Auth Service:** OTP verify karne ke liye Gmail SMTP servers ka use hota hai.
4.  **Neural Engine:** Translation logic process hota hai aur MySQL mein data sync hota hai.
5.  **Response:** Optimized JSON data wapas UI par display hota hai.

---

## ⚙️ Installation & Setup (Local)

1.  Repository clone karein:
    ```bash
    git clone [https://github.com/Divyyyyyy/neon-translate-saas.git](https://github.com/Divyyyyyy/neon-translate-saas.git)
    ```
2.  `application.properties` mein apni Database aur SMTP details update karein.
3.  Maven se dependencies install karein:
    ```bash
    mvn install
    ```
4.  Application run karein:
    ```bash
    mvn spring-boot:run
    ```

---

## 🌐 Live Demo & Deployment

* **Frontend Link:** [neon-translate-saas.vercel.app](https://neon-translate-saas.vercel.app)
* **Backend Hosting:** Deployed on Railway Cloud Infrastructure.

---

## 👤 Author
**[Divya Raj Singh]**
* Full Stack Developer
* AI & Cloud Enthusiast