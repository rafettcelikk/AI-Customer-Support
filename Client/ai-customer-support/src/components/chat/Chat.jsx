import React, { useEffect, useRef, useState } from "react";
import useStompWebSocket from "../hook/useStompWebSocket.js";
import { FaPaperPlane } from "react-icons/fa";
import { sendMessage } from "../service/apiService.js";

const Chat = () => {
  const [sessionId, setSessionId] = useState(null);
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState("");
  const [waitingForAI, setWaitingForAI] = useState(false);
  const messageEndRef = useRef(null);

  const { messages: wsMessages } = useStompWebSocket(sessionId);

  useEffect(() => {
    let chatId = localStorage.getItem("sessionId");
    if (!chatId) {
      chatId = crypto.randomUUID();
      localStorage.setItem("sessionId", chatId);
    }
    setSessionId(chatId);
  }, []);

  useEffect(() => {
    if (!wsMessages.length) return;
    setMessages((prev) => {
      const newWs = wsMessages.filter(
        (wsMsg) =>
          !prev.some(
            (msg) => msg.content === wsMsg.content && msg.role === wsMsg.role,
          ),
      );
      if (newWs.length > 0) setWaitingForAI(false);
      return [...prev, ...newWs];
    });
  }, [wsMessages]);

  const chatWithAI = async () => {
    if (!message.trim()) return;
    setMessages((prev) => [...prev, { role: "user", content: message }]);
    setWaitingForAI(true);
    try {
      const response = await sendMessage(sessionId, message);
      setMessages((prev) => [
        ...prev,
        { role: "assistant", content: response },
      ]);
      setWaitingForAI(false);
    } catch (error) {
      console.error(error);
      setMessages((prev) => [
        ...prev,
        {
          role: "assistant",
          content:
            "Mesaj gönderilirken bir hata oluştu. Lütfen tekrar deneyin.",
        },
      ]);
      setWaitingForAI(false);
    }
    setMessage("");
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      chatWithAI();
    }
  };

  useEffect(() => {
    messageEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages, waitingForAI]);

  const lastMessage = messages[messages.length - 1];
  const showTyping = waitingForAI && lastMessage && lastMessage.role === "user";
  return (
    <main className="support-page mt-5">
      <main className="main-content">
        <section className="chat-section">
          <div className="chat-container">
            <div className="chat-messages">
              {messages.map((msg, index) => (
                <div
                  key={index}
                  className={`message ${msg.role === "user" ? "user" : "assistant"}`}
                >
                  {msg.content}
                </div>
              ))}
              {showTyping && (
                <div className="ai-typing-indicator">
                  <strong>YZ yazıyor...</strong>
                  <em>Lütfen bekleyin, YZ yanıtınızı oluşturuyor.</em>
                </div>
              )}
              <div ref={messageEndRef} />
            </div>
            <div className="chat-input-container">
              <div className="input-with-button">
                <textarea
                  rows={2}
                  className="chat-textarea"
                  placeholder="Mesajınızı yazın..."
                  value={message}
                  onChange={(e) => setMessage(e.target.value)}
                  onKeyDown={handleKeyPress}
                />
                <button
                  className="send-button"
                  onClick={chatWithAI}
                  disabled={!message.trim()}
                  aria-label="Gönder"
                >
                  <FaPaperPlane size={20} />
                </button>
              </div>
            </div>
          </div>
        </section>
      </main>
    </main>
  );
};

export default Chat;
