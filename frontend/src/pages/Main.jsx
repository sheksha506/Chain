import React, { useEffect, useState } from "react";
import { updateUserLocation, getNearbyUsers } from "../api/apiService";
import useChatSocket from "../hooks/useChatSocket";
import UserList from "../components/UserList";
import ChatWindow from "../components/ChatWindow";

const Main = () => {
  const token = localStorage.getItem("token");
  const [email, setEmail] = useState("");
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [messages, setMessages] = useState({});
  const [message, setMessage] = useState("");

  // Decode JWT token
  useEffect(() => {
    if (!token) return;
    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      setEmail(payload.sub);
    } catch (e) {
      console.error("Invalid JWT token", e);
    }
  }, [token]);

  // Handle incoming message
  const handleIncoming = (data) => {
    const other = data.sender === email ? data.receiver : data.sender;
    setMessages((prev) => {
      const list = prev[other] ? [...prev[other]] : [];
      list.push({
        from: data.sender === email ? "me" : data.sender,
        content: data.content,
        timestamp: data.timestamp || new Date().toISOString(),
      });
      return { ...prev, [other]: list };
    });
  };

  const { sendMessage, isConnected } = useChatSocket(token, email, handleIncoming);

  // Fetch nearby users and watch location
  useEffect(() => {
    if (!email || !navigator.geolocation) return;

    const fetchNearby = async (lat, lon) => {
      try {
        await updateUserLocation(email, lat, lon, token);
        const res = await getNearbyUsers(lat, lon, token);
        setUsers(res.data.filter((u) => u.email !== email));
      } catch (err) {
        console.warn(err);
      }
    };

    navigator.geolocation.getCurrentPosition(
      (pos) => {
        const { latitude, longitude } = pos.coords;
        fetchNearby(latitude, longitude);
      },
      (err) => console.error("Geolocation error:", err)
    );

    const watcher = navigator.geolocation.watchPosition(
      (pos) => {
        const { latitude, longitude } = pos.coords;
        fetchNearby(latitude, longitude);
      },
      (err) => console.error("Geolocation error:", err),
      { enableHighAccuracy: false, maximumAge: 10000 }
    );

    return () => navigator.geolocation.clearWatch(watcher);
  }, [token, email]);

  const handleSend = () => {
    if (!selectedUser || !message.trim()) return;
    sendMessage(selectedUser.email, message);

    setMessages((prev) => {
      const list = prev[selectedUser.email] ? [...prev[selectedUser.email]] : [];
      list.push({ from: "me", content: message, timestamp: new Date().toISOString() });
      return { ...prev, [selectedUser.email]: list };
    });

    setMessage("");
  };

  return (
    <div className="h-screen flex bg-gray-100">
      <UserList users={users} selectedUser={selectedUser} onSelect={setSelectedUser} />
      <ChatWindow
        user={selectedUser}
        messages={messages}
        message={message}
        setMessage={setMessage}
        onSend={handleSend}
        isConnected={isConnected}
      />
    </div>
  );
};

export default Main;
