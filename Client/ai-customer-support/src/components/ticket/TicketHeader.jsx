import React from "react";

const TicketHeader = ({ title }) => {
  return (
    <div>
      <header className="ticket-header">
        <h2 className="ticket-center">{title || "Başlık Yok"}</h2>
      </header>
    </div>
  );
};

export default TicketHeader;
