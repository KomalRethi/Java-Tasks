import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function Register() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const api = "http://localhost:8082";
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();

    const res = await fetch(api + "/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    const text = await res.text();
    setMessage(text);

    if (res.ok) {
      navigate("/");
    }
  };

  return (
    <div className="container">
      <form onSubmit={handleRegister}>
        <h2>Register</h2>

        <input
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <br />
        <br />

        <input
          placeholder="Password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <br />
        <br />

        <button type="submit" className="btn-primary">
          Register
        </button>

        <p className="message">{message}</p>
      </form>
    </div>
  );
}

export default Register;
