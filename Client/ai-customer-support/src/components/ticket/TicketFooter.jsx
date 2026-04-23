import React from "react";
import { FaArrowLeftLong } from "react-icons/fa6";
import { Link } from "react-router-dom";

const TicketFooter = ({
  ticketStatus,
  onMarkedResolvedClick,
  showResolutionInput,
}) => {
  return (
    <footer className="ticket-footer">
      <Link className="btn btn-outline-info" to="/tickets">
        <FaArrowLeftLong /> Geri Dön
      </Link>
      {ticketStatus !== "RESOLVED" && !showResolutionInput && (
        <button className="btn btn-warning" onClick={onMarkedResolvedClick}>
          Çözüldü Olarak İşaretle
        </button>
      )}
    </footer>
  );
};

export default TicketFooter;
