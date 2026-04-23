import React from "react";
import { Link } from "react-router-dom";

const HeroLogo = () => {
  return (
    <div
      style={{
        flex: "1 1 350px",
        maxWidth: 500,
      }}
    >
      <h1 className="hero-title">
        Yeni Nesil <span style={{ color: "#007bff" }}>Müşteri Deneyimine</span>{" "}
        Hoş Geldiniz!
      </h1>
      <p className="hero-description">
        Destek süreçlerinizi yapay zekanın gücüyle otomatikleştirin.
        Müşterilerinize 7/24 anında, akıllı ve kişiselleştirilmiş çözümler
        sunarak bekleme sürelerine son verin.
      </p>
      <Link to={"/chat"} className="start-chat-button">
        Sohbeti Başlat
      </Link>
    </div>
  );
};

export default HeroLogo;
