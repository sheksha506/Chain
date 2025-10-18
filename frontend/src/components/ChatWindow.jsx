import React, { useEffect, useRef } from "react";

const ChatWindow = ({
  user,
  messages,
  message,
  setMessage,
  onSend,
  isConnected,
}) => {
  const endRef = useRef(null);

  useEffect(() => {
    endRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages, user]);

  if (!user) {
    return (
      <div className="flex-1 flex items-center justify-center text-gray-500 text-lg">
        Select a user to start chatting
      </div>
    );
  }

  return (
    <div className="w-[70%] flex flex-col">
      <div className="bg-black p-3 text-white font-bold">
        {user.username || user.email}
      </div>

      <div className="flex-1 overflow-y-auto space-y-2 p-3 custom-scroll">
        {(messages[user.email] || []).map((msg, id) => (
          <div
            key={id}
            className={`p-2 m-2 rounded-lg w-fit max-w-[70%] ${
              msg.from === "me" ? "ml-auto bg-blue-200" : "bg-white"
            }`}
          >
            {msg.content}
            <div className="text-xs text-gray-500">
              {new Date(msg.timestamp + "Z").toLocaleTimeString([], {
                hour: "2-digit",
                minute: "2-digit",
              })}
            </div>
          </div>
        ))}
        <div ref={endRef} />
      </div>

      <div className="flex items-center p-2 border-t">
        <input
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          placeholder={isConnected ? "Type a message..." : "Connecting..."}
          disabled={!isConnected}
          className="flex-1 border p-3 rounded-lg m-2"
        />
        <button
          onClick={onSend}
          disabled={!isConnected || !message.trim()}
          className={`border px-4 py-2 rounded-lg m-2 ${
            !isConnected ? "opacity-50 cursor-not-allowed" : "bg-blue-500 text-white"
          }`}
        >
          Send
        </button>
      </div>
    </div>
  );
};

export default ChatWindow;
