import React from "react";

const CustomerComplatint = ({ summary, ticket }) => {
  return (
    <section className="ticket-section">
      <p className="section-title">Müşteri Şikayeti</p>
      <p>{summary || "Görüşme Bilgisi Yok"}</p>
      {ticket?.status.toLowerCase() === "resolved" && (
        <>
          <p className="section-title">Çözüm</p>
          <p>{ticket?.resolutionDetails || "Çözüm Bilgisi Yok"}</p>
        </>
      )}
    </section>
  );
};

export default CustomerComplatint;
