import React, { useState } from "react";

const Searchbar = ({ onSearch }) => {
  const [refNumber, setRefNumber] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!refNumber.trim()) {
      alert("Lütfen geçerli bir referans numarası giriniz.");
      return;
    }
    // Arama işlemi burada gerçekleştirilecek
    console.log("Arama yapılıyor: ", refNumber);
    onSearch(refNumber.trim());
  };

  const handleClear = (e) => {
    e.preventDefault();
    setRefNumber("");
    onSearch("");
  };
  return (
    <form onSubmit={handleSubmit}>
      <div className="input-group mb-3">
        <input
          type="text"
          className="form-control ticket-search-input"
          placeholder="Referans Numarası"
          value={refNumber}
          onChange={(e) => setRefNumber(e.target.value)}
        />
        <button className="btn btn-info ticket-search-button" type="submit">
          Ara
        </button>
      </div>
      <div className="mt-2">
        <button
          className="ticket-clear-search-button"
          type="button"
          onClick={handleClear}
        >
          Temizle
        </button>
      </div>
    </form>
  );
};

export default Searchbar;
