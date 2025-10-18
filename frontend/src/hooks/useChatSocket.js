import { useEffect, useRef, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

export default function useChatSocket(token, email, onMessageReceived) {
  const stompClientRef = useRef(null);
  const [isConnected, setIsConnected] = useState(false);

  useEffect(() => {
    if (!token || !email) return;

    const socket = new SockJS(`http://localhost:8081/ws?token=${token}`);
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: () => {
        console.log("✅ Connected to WebSocket");
        setIsConnected(true);

        client.subscribe("/user/queue/chat", (msg) => {
          const data = JSON.parse(msg.body);
          onMessageReceived(data);
        });
      },
      onWebSocketClose: () => {
        console.log("❌ Disconnected from WebSocket");
        setIsConnected(false);
      },
      onStompError: (frame) => {
        console.error("STOMP error:", frame.headers["message"]);
      },
    });

    client.activate();
    stompClientRef.current = client;

    return () => {
      if (client.active) client.deactivate();
      setIsConnected(false);
    };
  }, [token, email]);

  const sendMessage = (receiver, content) => {
    if (!isConnected || !stompClientRef.current) return;
    const payload = {
      receiver,
      content,
      timestamp: new Date().toISOString(),
    };
    stompClientRef.current.publish({
      destination: "/app/send",
      body: JSON.stringify(payload),
    });
  };

  return { sendMessage, isConnected };
}
