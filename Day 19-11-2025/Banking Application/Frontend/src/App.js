import React, { useState } from "react";
import { Routes, Route, Link, useNavigate } from "react-router-dom";

import Login from "./components/Login";
import Register from "./components/Register";
import Dashboard from "./components/Dashboard";
import TransactionHistory from "./components/TransactionHistory";
import ProtectedRoute from "./components/ProtectedRoute";

import "./App.css";

function App() {
  const [username, setUsername] = useState("");
  const [loggedIn, setLoggedIn] = useState(false);

  const navigate = useNavigate();

  const handleLogout = () => {
    setLoggedIn(false);
    setUsername("");
    navigate("/");
  };

  return (
    <div>
      <nav
        style={{
          padding: "10px",
          background: "#4a90e2",
          color: "white",
          display: "flex",
          justifyContent: "space-between",
        }}
      >
        <h3>Banking App</h3>

        <div>
          {!loggedIn ? (
            <>
              <Link to="/" style={{ color: "white", marginRight: 20 }}>
                Login
              </Link>
              <Link to="/register" style={{ color: "white" }}>
                Register
              </Link>
            </>
          ) : (
            <>
              <Link
                to="/dashboard"
                style={{ color: "white", marginRight: 20 }}
              >
                Dashboard
              </Link>
              <Link to="/history" style={{ color: "white", marginRight: 20 }}>
                History
              </Link>

              <button
                onClick={handleLogout}
                style={{
                  background: "transparent",
                  color: "white",
                  border: "1px solid white",
                  padding: "5px 10px",
                  borderRadius: "6px",
                  cursor: "pointer",
                }}
              >
                Logout
              </button>
            </>
          )}
        </div>
      </nav>

      <Routes>
        <Route
          path="/"
          element={
            <Login setUsername={setUsername} setLoggedIn={setLoggedIn} />
          }
        />

        <Route path="/register" element={<Register />} />

        <Route
          path="/dashboard"
          element={
            <ProtectedRoute loggedIn={loggedIn}>
              <Dashboard username={username} />
            </ProtectedRoute>
          }
        />

        <Route
          path="/history"
          element={
            <ProtectedRoute loggedIn={loggedIn}>
              <TransactionHistory username={username} />
            </ProtectedRoute>
          }
        />
      </Routes>
    </div>
  );
}

export default App;
