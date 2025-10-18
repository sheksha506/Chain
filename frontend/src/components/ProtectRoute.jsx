import React from "react";
import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem("token"); // or use context/state

  return token ? children : <Navigate to="/login" />;
};

export default ProtectedRoute;
