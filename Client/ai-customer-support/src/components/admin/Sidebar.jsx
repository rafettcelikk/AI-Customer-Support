import React from "react";
import Searchbar from "./Searchbar";

const Sidebar = ({ activeMenu, setActiveMenu, onSearch }) => {
  return (
    <aside className="admin-sidebar">
      <h2 className="admin-title">Yönetici Paneli</h2>
      <nav className="mb-5">
        <ul className="admin-menu">
          <li
            className={`admin-menu-item ${activeMenu === "tickets" ? "active" : ""}`}
            onClick={() => setActiveMenu("tickets")}
          >
            Destek Talepleri
          </li>
          <li
            className={`admin-menu-item ${activeMenu === "conversations" ? "active" : ""}`}
            onClick={() => setActiveMenu("conversations")}
          >
            Görüşmeler
          </li>
        </ul>
      </nav>
      <Searchbar onSearch={onSearch} />
    </aside>
  );
};

export default Sidebar;
