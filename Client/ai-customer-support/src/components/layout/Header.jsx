import React from "react";
import HeroLogo from "../hero/HeroLogo.jsx";
import HeroAnimation from "../hero/HeroAnimation.jsx";
import Navbar from "./NavBar.jsx";

const Header = () => {
  return (
    <section className="hero-section">
      <HeroLogo />
      <div
        style={{
          flex: "1 1 320px",
          display: "flex",
          justifyContent: "center",
        }}
      >
        <HeroAnimation />
      </div>
      <Navbar />
    </section>
  );
};

export default Header;
