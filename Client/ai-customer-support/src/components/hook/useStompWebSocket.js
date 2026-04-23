import { useEffect, useState, useRef } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
const useStompWebSocket = (sessionId) => {
  const [messages, setMessages] = useState([]);
  const clientRef = useRef(null);
  useEffect(() => {
    if (!sessionId) return;

    const socketUrl = "http://localhost:9069/ws-chat";
    const client = new Client({
      webSocketFactory: () => new SockJS(socketUrl),
      reconnectDelay: 5000,
      debug: (str) => {
        console.log("STOMP: " + str);
      },
    });
    client.onConnect = () => {
      console.log("WebSocket connected");
      client.subscribe(`topic/message/${sessionId}`, (message) => {
        if (message.body) {
          setMessages((prev) => [
            ...prev,
            {
              role: "assistant",
              content: message.body,
            },
          ]);
        }
      });
    };
    client.onStompError = (frame) => {
      console.error("STOMP error: ", frame.headers["message"]);
      console.error("Details: ", frame.body);
    };
    client.activate();
    clientRef.current = client;
    return () => {
      if (clientRef.current) {
        clientRef.current.deactivate();
        console.log("WebSocket disconnected");
      }
    };
  }, [sessionId]);
  return { messages };
};

export default useStompWebSocket;
