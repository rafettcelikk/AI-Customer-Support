import React from "react";

const CustomerInformation = ({ customer }) => {
  return (
    <section className="ticket-section">
      <p className="section-title">Müşteri Bilgileri</p>
      <dl className="details-list">
        <dt>Ad Soyad:</dt>
        <p>{customer?.fullName || "-"}</p>
        <dt>E-posta:</dt>
        <p>{customer?.emailAddress || "-"}</p>
        <dt>Telefon:</dt>
        <p>{customer?.phoneNumber || "-"}</p>
      </dl>
    </section>
  );
};

export default CustomerInformation;
