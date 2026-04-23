import React from "react";
import { Spinner } from "react-bootstrap";
import { FaCheck } from "react-icons/fa6";
import { FcCancel } from "react-icons/fc";
import { IoReloadSharp } from "react-icons/io5";
import { LiaTimesSolid } from "react-icons/lia";

const ResoulitonInput = ({
  resolutionDetails,
  setResolutionDetails,
  suggestions,
  loadingSuggestions,
  suggestionsError,
  onSubmit,
  onCancel,
  loading,
  onSuggestionClick,
  onClearTextArea,
  onReloadSuggestions,
}) => {
  return (
    <div className="resolution-input-container mt-3 mb-5">
      <div className="ai-suggestions-section mb-3">
        {loadingSuggestions && (
          <div className="d-flex align-items-center text-primary mb-2">
            <Spinner animation="border" size="sm" className="me-2" />
            <span>
              ✨ Yapay Zeka çözüm önerileri üretiyor, lütfen bekleyin...
            </span>
          </div>
        )}

        {suggestionsError && (
          <div className="alert alert-warning py-2 shadow-sm">
            Öneriler yüklenirken bir hata oluştu: {suggestionsError}
          </div>
        )}
        {!loadingSuggestions && suggestions && suggestions.length > 0 && (
          <>
            <p className="text-success fw-medium mb-2">
              ✨ Şikayete özel yapay zeka çözüm önerileri (Metne aktarmak için
              tıklayın):
            </p>
            <div className="d-flex flex-wrap gap-2">
              {suggestions.map((suggestion, index) => (
                <div
                  key={index}
                  className="suggestion-item p-2 bg-light border border-info rounded shadow-sm transition"
                  style={{ cursor: "pointer", fontSize: "0.95rem" }}
                  onClick={() => onSuggestionClick(suggestion)}
                >
                  <span className="text-dark">{suggestion}</span>
                </div>
              ))}
            </div>
          </>
        )}
      </div>
      <div className="textarea-section shadow-sm rounded">
        <textarea
          className="form-control border-primary"
          rows="5"
          placeholder="Çözüm detaylarını buraya yazın veya yukarıdaki önerilerden birini seçin..."
          value={resolutionDetails}
          onChange={(e) => setResolutionDetails(e.target.value)}
          disabled={loading}
        />

        {/* BUTONLAR TEXTAREA'NIN DIŞINDA! */}
        <div className="mt-3 d-flex justify-content-start gap-2">
          <button
            className="btn btn-success d-flex align-items-center"
            onClick={onSubmit}
            disabled={loading}
            title="Çözümü Kaydet"
          >
            {loading ? (
              <Spinner size="sm" className="me-2" />
            ) : (
              <FaCheck size={20} className="me-2" />
            )}
            Kaydet
          </button>

          <button
            className="btn btn-outline-secondary"
            onClick={onCancel}
            disabled={loading}
            title="İptal"
          >
            <LiaTimesSolid size={24} />
          </button>

          <button
            className="btn btn-outline-danger"
            onClick={onClearTextArea}
            disabled={loading || !resolutionDetails}
            title="Metni Temizle"
          >
            <FcCancel size={24} />
          </button>

          <button
            className="btn btn-outline-primary"
            onClick={onReloadSuggestions}
            disabled={loading || loadingSuggestions}
            title="Yapay Zeka Önerilerini Yenile"
          >
            <IoReloadSharp size={24} />
          </button>
        </div>
      </div>
    </div>
  );
};

export default ResoulitonInput;
