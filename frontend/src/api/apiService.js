import axios from "axios";

const BASE_URL = "http://localhost:8080/api";

export const api = axios.create({
  baseURL: BASE_URL,
});

export const updateUserLocation = async (email, latitude, longitude, token) => {
  return api.patch(
    "/update",
    { email, latitude, longitude },
    { headers: { Authorization: `Bearer ${token}` } }
  );
};

export const getNearbyUsers = async (latitude, longitude, token, range = 200) => {
  return api.get("/nearby", {
    params: { latitude, longitude, range },
    headers: { Authorization: `Bearer ${token}` },
  });
};
