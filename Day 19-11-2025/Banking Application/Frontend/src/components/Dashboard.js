import React, { useState } from "react";

function Dashboard({ username }) {
  const [amount, setAmount] = useState("");
  const [message, setMessage] = useState("");

  const api = "http://localhost:8082";

  const validateAmount = () => {
    if (!amount || isNaN(amount) || Number(amount) <= 0) {
      setMessage("Please enter a valid positive amount.");
      return false;
    }
    return true;
  };

  const checkBalance = async () => {
    const res = await fetch(api + "/bank/balance/" + username);
    const data = await res.text();
    setMessage("Balance: ₹" + data);
  };

  const deposit = async () => {
    if (!validateAmount()) return;

    const res = await fetch(
      api + `/bank/deposit?username=${username}&amount=${amount}`,
      { method: "POST" }
    );

    const text = await res.text();
    setMessage(text);
    setAmount("");
  };

  const withdraw = async () => {
    if (!validateAmount()) return;

    const res = await fetch(
      api + `/bank/withdraw?username=${username}&amount=${amount}`,
      { method: "POST" }
    );

    const text = await res.text();
    setMessage(text);
    setAmount("");
  };

  return (
    <div className="container">
      <h2>Welcome, {username}</h2>

      <input
        placeholder="Amount"
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
      />
      <br />
      <br />

      <button className="action-btn" onClick={deposit}>
        Deposit
      </button>
      <button className="action-btn" onClick={withdraw}>
        Withdraw
      </button>
      <button className="action-btn" onClick={checkBalance}>
        Check Balance
      </button>

      <p className="message">{message}</p>
    </div>
  );
}

export default Dashboard;
