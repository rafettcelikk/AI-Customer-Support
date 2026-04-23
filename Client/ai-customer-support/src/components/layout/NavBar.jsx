import React, { useState } from "react";
import { useLocation } from "react-router-dom";
import { Link } from "react-router-dom";

const Navbar = () => {
  const location = useLocation();
  const [activeItem, setActiveItem] = useState(location.pathname);

  const handleItemClick = (item) => {
    setActiveItem(item);
  };

  const navItems = [
    { label: "Anasayfa", path: "/" },
    { label: "Sohbet", path: "/chat" },
    { label: "Destek Talepleri", path: "/tickets" },
  ];
  return (
    <nav className="nav-container">
      <ul className="nav-list">
        {navItems.map((item) => (
          <li key={item.path} className="nav-item">
            <Link
              to={item.path}
              className={activeItem === item.path ? "active" : ""}
              onClick={() => handleItemClick(item.path)}
            >
              {item.label}
            </Link>
          </li>
        ))}
      </ul>
    </nav>
  );
};

export default Navbar;
