import React, { useEffect, useState } from "react";

function TransactionHistory({ username }) {
  const [transactions, setTransactions] = useState([]);
  const api = "http://localhost:8082";

  useEffect(() => {
    if (!username) return;

    fetch(api + "/transactions/" + username)
      .then((res) => res.json())
      .then((data) => setTransactions(data))
      .catch((err) => console.error(err));
  }, [username]);

  return (
    <div className="container">
      <h2>Transaction History</h2>

      {transactions.length === 0 ? (
        <p>No transactions yet.</p>
      ) : (
        <ul>
          {transactions.map((t) => (
            <li key={t.id} style={{ marginBottom: "10px" }}>
              {t.type} - ₹{t.amount} -{" "}
              {new Date(t.timestamp).toLocaleString()}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default TransactionHistory;
