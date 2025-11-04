# 🛒 Real-Time E-commerce Order Pipeline

This project implements a **real-time order processing system** using **AWS DynamoDB**, **EventBridge Pipes**, and **AWS Lambda**.

## 🧩 Architecture Overview

1. **DynamoDB (Orders Table)**  
   - Stores customer order details such as order ID, status, and amount.
   - Streams are enabled to capture `INSERT` and `MODIFY` events.

2. **EventBridge Pipes**
   - Connects DynamoDB Streams to two separate Lambda functions.
   - Applies filtering patterns to ensure only relevant events trigger the right Lambda.

3. **Lambda Functions**
   - **NormalOrderLambda**: Handles orders with status `pending` or `shipped`.
   - **PremiumOrderLambda**: Handles high-value order updates (e.g., status changing to `shipped`).

---

## 🗂️ DynamoDB Example Items

You can insert these example items into your **Orders** table:

### ✅ Example 1 (High-value shipped order)
```json
{
  "orderId": { "S": "ORDER130" },
  "amount": { "N": "3500" },
  "createdAt": { "S": "2025-11-03T12:00:00Z" },
  "customerEmail": { "S": "buyer@example.com" },
  "status": { "S": "shipped" }
}
```

### ✅ Example 2 (Pending low-value order)
```json
{
  "orderId": { "S": "ORDER124" },
  "amount": { "N": "50" },
  "createdAt": { "S": "2025-11-03T12:00:00Z" },
  "customerEmail": { "S": "new@example.com" },
  "status": { "S": "pending" }
}
```

---

## 🎯 EventBridge Filtering Patterns

### 🔹 Normal Orders
```json
{
  "eventName": ["INSERT"],
  "dynamodb": {
    "NewImage": {
      "status": { "S": ["pending", "shipped"] },
      "customerEmail": {
        "S": [{ "anything-but": "test.com" }]
      }
    }
  }
}
```

### 🔹 Premium Orders
```json
{
  "eventName": ["MODIFY"],
  "dynamodb": {
    "NewImage": {
      "status": { "S": ["shipped"] },
      "customerEmail": {
        "S": [{ "anything-but": "*test.com*" }]
      }
    },
    "OldImage": {
      "status": { "S": ["pending"] }
    }
  }
}
```

---

## ⚙️ Lambda Functions

### **NormalOrderLambda**
- Triggered on new inserts (`INSERT` events).
- Processes all customer orders with `status` = `pending` or `shipped`.

### **PremiumOrderLambda**
- Triggered on order updates (`MODIFY` events).
- Activated when an order transitions from `pending` → `shipped`.

---

## 🚀 Deployment Steps

1. Deploy the CloudFormation template (`template.yaml`).
2. Upload Lambda:
   - NormalOrderLambda
   - PremiumOrderLambda
3. Insert the example items into the DynamoDB table to test.
4. View logs in **CloudWatch Logs** for both Lambda functions.

---

## 🧠 Key AWS Services Used

| Service | Purpose |
|----------|----------|
| **DynamoDB** | Stores order data and emits change streams. |
| **EventBridge Pipes** | Filters and routes events to Lambda. |
| **Lambda** | Processes order data in real-time. |
| **CloudWatch Logs** | Captures processing results and logs. |

---

## 🏁 Outcome
This pipeline ensures **efficient, event-driven order processing** with **fine-grained filtering**—keeping your architecture **scalable**, **cost-effective**, and **serverless**.


## 👩‍💻 Author

**Komal Rethi**  

### 🌟 Key Takeaway
> **Efficient filtering at the infrastructure level** = **lower cost**, **faster response**, and **cleaner architecture** for real-time e-commerce event processing.
