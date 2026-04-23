import React from "react";

const TicketDetails = ({ ticket }) => {
  return (
    <section className="ticket-section">
      <p className="section-title">Destek Talebi Detayları</p>
      <d1 className="ticket-list">
        <dt>ID: </dt>
        <p>{ticket?.id || "-"}</p>
        <dt>Durum: </dt>
        <p className={`status-${ticket?.status?.toLowerCase() || ""}`}>
          {ticket?.status === "OPENED"
            ? "Açık"
            : ticket?.status === "RESOLVED"
              ? "Çözüldü"
              : "-"}
        </p>
        <dt>Referans Numarası:</dt>
        <h4 className="text-info">{ticket?.referenceNumber || "-"}</h4>
        <dt>Şikayet numarası:</dt>
        <p>{ticket?.conversation?.id || "-"}</p>
        <dt>Oluşma Tarihi: </dt>
        <p>
          {ticket?.createdAt
            ? new Date(ticket.createdAt).toLocaleString()
            : "-"}
        </p>
        <dt>
          Çözülme Tarihi:{" "}
          <p>
            {ticket?.resolvedAt
              ? new Date(ticket.resolvedAt).toLocaleString()
              : "-"}
          </p>
        </dt>
      </d1>
    </section>
  );
};

export default TicketDetails;
