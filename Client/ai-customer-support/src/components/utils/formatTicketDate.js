export const formatTicketDate = (dateString) => {
  if (!dateString) return "Tarih Yok";

  const date = new Date(dateString);

  if (isNaN(date.getTime())) return "Geçersiz Tarih";

  return new Intl.DateTimeFormat("tr-TR", {
    day: "2-digit",
    month: "long",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  }).format(date);
};
