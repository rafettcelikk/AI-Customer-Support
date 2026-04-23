import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import TicketHeader from "./TicketHeader";
import TicketDetails from "./TicketDetails";
import CustomerComplatint from "./CustomerComplatint";
import CustomerInformation from "./CustomerInformation";
import ResoulitonInput from "./ResolutionInput";
import TicketFooter from "./TicketFooter";
import {
  getTicketById,
  resolveTicket,
  fetchResolutionSuggestions,
} from "../service/apiService.js";

const Ticket = () => {
  const [ticket, setTicket] = useState(null);
  const { ticketId } = useParams();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState("");

  const [showResolutionInput, setShowResolutionInput] = useState(false);
  const [resolutionDetails, setResolutionDetails] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const [loadingSuggestions, setLoadingSuggestions] = useState(false);

  useEffect(() => {
    const fetchTicket = async () => {
      setLoading(true);
      try {
        const response = await getTicketById(ticketId);
        setTicket(response);
        setError(null);
      } catch (err) {
        setError(err.message || "Bilet bilgisi alınırken bir hata oluştu.");
        setTicket(null);
      } finally {
        setLoading(false);
      }
    };
    fetchTicket();
  }, [ticketId]);

  const handleFetchSuggestions = async () => {
    const complaintSummary = ticket?.conversation?.conversationSummary || "";
    if (showResolutionInput && complaintSummary) {
      setLoadingSuggestions(true);
      setError(null);
      try {
        const result = await fetchResolutionSuggestions(complaintSummary);
        setSuggestions(result);
      } catch (err) {
        setError(err.message || "Çözüm önerileri alınırken bir hata oluştu.");
        setSuggestions([]);
      } finally {
        setLoadingSuggestions(false);
        setTimeout(() => setError(null), 10000);
      }
    }
  };

  const handleMarkAsResolvedClick = () => {
    setShowResolutionInput(true);
  };

  useEffect(() => {
    handleFetchSuggestions();
  }, [showResolutionInput, ticket]);

  const handleResolveTicket = async () => {
    if (!resolutionDetails.trim()) {
      setError("Lütfen çözüm detaylarını girin.");
      return;
    }
    setLoading(true);
    try {
      const response = await resolveTicket(ticketId, resolutionDetails);
      setTicket(response);
      setShowResolutionInput(false);
      setResolutionDetails("");
      setSuccessMessage("Bilet başarıyla çözüldü.");
    } catch (err) {
      setError(err.message || "Destek talebi çözülürken bir hata oluştu.");
    } finally {
      setLoading(false);
      setTimeout(() => {
        setError(null);
        setSuccessMessage("");
      }, 5000);
    }
  };

  const handleCancel = () => {
    setShowResolutionInput(false);
    setResolutionDetails("");
    setError(null);
    setSuggestions([]);
  };

  const handleSuggestionClick = (suggestion) => {
    setResolutionDetails((prev) =>
      prev ? `${prev}\n${suggestion}` : suggestion,
    );
  };

  const handleClearTextArea = () => {
    setResolutionDetails("");
    setError(null);
  };

  if (loading)
    return <div className="loading">Destek talebi yükleniyor...</div>;

  return (
    <div className="ticket-container">
      <TicketHeader title={ticket?.conversation?.conversationTitle} />
      {successMessage && (
        <div className="alert alert-success">{successMessage}</div>
      )}
      <TicketDetails ticket={ticket} />
      <CustomerComplatint
        summary={ticket?.conversation?.conversationSummary}
        ticket={ticket}
      />
      <CustomerInformation customer={ticket?.conversation?.customer} />
      {showResolutionInput && (
        <ResoulitonInput
          resolutionDetails={resolutionDetails}
          setResolutionDetails={setResolutionDetails}
          suggestionsError={error}
          onSubmit={handleResolveTicket}
          onCancel={handleCancel}
          handleSuggestionClick={handleSuggestionClick}
          suggestions={suggestions}
          loadingSuggestions={loadingSuggestions}
          onReloadSuggestions={handleFetchSuggestions}
          onClearTextArea={handleClearTextArea}
          onSuggestionClick={handleSuggestionClick}
          loading={loading}
        />
      )}
      <TicketFooter
        ticketStatus={ticket?.status}
        onMarkedResolvedClick={handleMarkAsResolvedClick}
        showResolutionInput={showResolutionInput}
      />
    </div>
  );
};

export default Ticket;
