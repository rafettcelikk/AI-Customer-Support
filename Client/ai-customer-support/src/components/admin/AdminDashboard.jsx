import React, { useState } from "react";
import Tickets from "./Tickets";
import Sidebar from "./Sidebar";

const AdminDashboard = () => {
  const [activeMenu, setActiveMenu] = useState("tickets");
  const [searchRef, setSearchRef] = useState("");

  const handleSearch = (refNumber) => {
    setSearchRef(refNumber);
    setActiveMenu("tickets"); // Arama yapıldığında otomatik olarak Destek Talepleri sekmesine geçiş yap
  };
  return (
    <div className="admin-container">
      <Sidebar
        activeMenu={activeMenu}
        setActiveMenu={setActiveMenu}
        onSearch={handleSearch}
      />
      <main className="admin-main-content mt-4">
        {activeMenu === "tickets" && (
          <section>
            <h1>Destek Talepleri</h1>
            <Tickets searchRef={searchRef} />
          </section>
        )}
        {activeMenu === "conversations" && (
          <section>
            <h1>Görüşmeler</h1>
            <p>Görüşmeler bölümü henüz geliştirilme aşamasındadır.</p>
          </section>
        )}
      </main>
    </div>
  );
};

export default AdminDashboard;
