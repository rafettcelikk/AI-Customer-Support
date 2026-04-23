import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getAllTickets } from "../service/apiService.js";
import { formatTicketDate } from "../utils/formatTicketDate.js";

const Tickets = ({ searchRef }) => {
  const [tickets, setTickets] = useState([]);
  const [filteredTickets, setFilteredTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadTickets = async () => {
      try {
        const response = await getAllTickets();
        setTickets(response);
        setFilteredTickets(response);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    loadTickets();
  }, []);

  useEffect(() => {
    if (searchRef && searchRef.trim() !== "") {
      const filtered = tickets.filter(
        (ticket) =>
          ticket.referenceNumber &&
          ticket.referenceNumber.toLowerCase() === searchRef.toLowerCase(),
      );
      setFilteredTickets(filtered);
    } else {
      setFilteredTickets(tickets);
    }
  }, [searchRef, tickets]);
  return (
    <div>
      {filteredTickets.length === 0 ? (
        <div className="alert alert-found">
          Aradığınız kriterlere uygun bir bilet bulunamadı.
        </div>
      ) : (
        <table className="table table-hover table-bordered">
          <thead>
            <tr style={{ borderBottom: "1px solid #ddd" }}>
              <th>ID</th>
              <th>Müşteri Şikayeti</th>
              <th>Referans Numarası</th>
              <th>Durum</th>
              <th>Oluşturulma Tarihi</th>
              <th>Çözülme Tarihi</th>
              <th>Görüşme Numarası</th>
              <th>Detay</th>
            </tr>
          </thead>
          <tbody>
            {filteredTickets.map((ticket) => (
              <tr key={ticket.id}>
                <td>{ticket.id}</td>
                <td>{ticket.conversation?.conversationTitle}</td>
                <td>{ticket.referenceNumber}</td>
                <td>{ticket.status === "OPENED" ? "Açık" : "Çözüldü"}</td>
                <td>{formatTicketDate(ticket.createdAt)}</td>
                <td>{formatTicketDate(ticket.resolvedAt)}</td>
                <td>{ticket.conversation ? ticket.conversation.id : "-"}</td>
                <td>
                  <Link
                    to={`/tickets/${ticket.id}`}
                    className="btn btn-outline-primary"
                  >
                    Detay
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default Tickets;
