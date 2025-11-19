import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function Login({ setUsername, setLoggedIn }) {
  const [usernameInput, setUsernameInput] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const api = "http://localhost:8082";
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    const res = await fetch(api + "/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        username: usernameInput,
        password,
      }),
    });

    const result = await res.text();

    if (res.ok) {
      setUsername(usernameInput);
      setLoggedIn(true);
      navigate("/dashboard");
    } else {
      setMessage(result);
    }
  };

  return (
    <div className="container">
      <form onSubmit={handleLogin}>
        <h2>Login</h2>

        <input
          placeholder="Username"
          value={usernameInput}
          onChange={(e) => setUsernameInput(e.target.value)}
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
          Login
        </button>

        <button
          type="button"
          className="btn-secondary"
          onClick={() => navigate("/register")}
          style={{ marginLeft: "10px" }}
        >
          Register
        </button>

        <p className="message">{message}</p>
      </form>
    </div>
  );
}

export default Login;
