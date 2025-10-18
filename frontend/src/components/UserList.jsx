import React from "react";

const UserList = ({ users, selectedUser, onSelect }) => (
  <div className="w-[30%] border-r">
    <h1 className="font-bold text-3xl p-2 m-2">Friends Nearby</h1>
    <div className="space-y-2 overflow-y-auto h-[90vh] custom-scroll">
      {users.length === 0 && (
        <p className="text-gray-500 text-center">No users nearby</p>
      )}
      {users.map((u, idx) => (
        <div
          key={idx}
          onClick={() => onSelect(u)}
          className={`p-3 text-xl rounded cursor-pointer font-semibold transition ${
            selectedUser?.email === u.email
              ? "bg-blue-400 text-white"
              : "bg-slate-200 hover:bg-slate-300"
          }`}
        >
          {u.username || u.email}
        </div>
      ))}
    </div>
  </div>
);

export default UserList;
