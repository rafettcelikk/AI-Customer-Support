# 🤖 AI-Powered Customer Support System (Yapay Zeka Destekli Müşteri Destek Sistemi)

![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Spring AI](https://img.shields.io/badge/Spring_AI-6DB33F?style=for-the-badge&logo=spring)
![WebSocket](https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=socket.io&logoColor=white)

Bu proje, modern web teknolojilerini ve Üretken Yapay Zeka'yı (LLM) harmanlayarak müşteri hizmetleri süreçlerini otomatize eden, gerçek zamanlı bir Full-Stack bilet (ticket) yönetim sistemidir. 

Sistem, müşteri şikayetlerini analiz ederek temsilcilere anında uygulanabilir çözüm önerileri sunan yapay zeka entegrasyonu ve olay güdümlü (event-driven) e-posta bildirim sistemiyle profesyonel bir destek deneyimi sağlar.

---

## ✨ Öne Çıkan Özellikler (Key Features)

* **🧠 Akıllı Çözüm Asistanı (Spring AI):** Müşteri şikayet özetlerini analiz eder ve temsilcilere tek tıkla metne aktarılabilir 5 farklı pratik çözüm adımı üretir.
* **⚡ Gerçek Zamanlı Güncellemeler (WebSocket):** Bilet durumları ve mesajlaşmalar, sayfa yenilemeye gerek kalmadan anlık olarak tüm panellerde güncellenir.
* **📧 Otomatik E-Posta Bildirim Sistemi:** Yeni bilet oluşturulduğunda veya bir bilet çözüme kavuşturulduğunda müşteriye HTML formatında profesyonel bilgilendirme mailleri gönderilir.
* **🛡️ Gelişmiş Veri Mimarisi:** * Jackson JSR310 entegrasyonu ile zaman diliminden bağımsız (ISO-8601) tarih yönetimi.
    * Hatalı veri girişlerine (Regex-based parsing) karşı dayanıklı AI yanıt ayrıştırma sistemi.
    * Hassas bilgilerin (API keys, SMTP credentials) `application.properties` üzerinden güvenli yönetimi.
* **🎨 Modern UI/UX:** Bootstrap 5 ve React kullanılarak tasarlanmış, gelişmiş arama ve filtreleme özelliklerine sahip dinamik arayüz.

---

## 🛠️ Teknoloji Yığını (Tech Stack)

### Backend
- **Java 21** & **Spring Boot 3.x**
- **Spring AI:** LLM Prompt Engineering & Yanıt Ayrıştırma
- **Spring WebSockets:** Gerçek zamanlı veri akışı
- **Spring Data JPA:** Veritabanı yönetimi (MySQL/PostgreSQL)
- **Spring Mail:** SMTP tabanlı e-posta entegrasyonu
- **Lombok:** Boilerplate kod azaltımı

### Frontend
- **React 18** (Functional Components & Hooks)
- **React Router DOM:** Sayfa navigasyonu
- **Bootstrap 5:** Modern ve responsive tasarım
- **React Icons:** Görsel zenginlik

---
