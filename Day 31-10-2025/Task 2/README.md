# 🚀 Real-Time E-commerce Order Processing Pipeline 🛒

This project implements a **highly available**, **cost-optimized**, and **real-time** data processing pipeline for an e-commerce platform using **AWS DynamoDB Streams** and **Amazon EventBridge Pipes**.  

The core functionality applies **complex filtering logic at the infrastructure layer**, ensuring that downstream compute resources (Lambda functions) are invoked **only for relevant and actionable events**, saving both cost and latency.

---

## 🛠️ Technology Stack

| **Service** | **Role** | **Key Feature Used** |
|--------------|-----------|----------------------|
| **DynamoDB** | Data Source | `StreamViewType: NEW_AND_OLD_IMAGES` |
| **EventBridge Pipes** | Event Router & Filter | Dynamic JSON Filtering (OldImage and NewImage analysis) |
| **AWS Lambda** | Target Processor | Executes business logic on filtered events |
| **Amazon SQS** | Error Handling | Dead-Letter Queues (DLQs) |
| **AWS CloudFormation** | Infrastructure as Code | Full stack definition and deployment |

---

## 📐 Architecture Overview

The pipeline utilizes **two independent EventBridge Pipes** consuming from the same **DynamoDB Stream** to perform **highly specific event routing and filtering**.

```
DynamoDB (Orders Table)
        │
        ▼
   DynamoDB Streams
        │
 ┌──────┴─────────┐
 │                │
 ▼                ▼
NormalOrdersPipe  PremiumOrdersPipe
 │                │
 ▼                ▼
NormalOrdersProcessor   PremiumOrdersProcessor
 │                │
 ▼                ▼
NormalPipeDLQ     PremiumPipeDLQ
```

---

## ✨ Filtering Logic Details

This project leverages **EventBridge Pipe filtering** to minimize Lambda invocations by pre-filtering at the infrastructure level.

### 1. 🧾 NormalOrdersPipe (Standard Processing)

**Purpose:** Routes standard order events to `NormalOrdersProcessor`, excluding test data.

| **Requirement** | **Stream Path** | **Filter Logic (CloudFormation YAML Pattern)** |
|------------------|------------------|-----------------------------------------------|
| **Status** | `dynamodb.NewImage.orderStatus.S` | `["pending", "shipped"]` |
| **Amount** | `dynamodb.NewImage.orderAmount.N` | Numeric Filter: `[">", 100]` |
| **Exclude Tests** | `dynamodb.NewImage.customerEmail.S` | `{"anything-but": {"prefix": "test.com"}}` |
| **Events** | `eventName` | `["INSERT", "MODIFY"]` |

---

### 2. 💎 PremiumOrdersPipe (Advanced Processing)

**Purpose:** Targets premium customers’ **status changes** to “shipped” for high-value orders.

| **Requirement** | **Stream Path** | **Filter Logic (CloudFormation YAML Pattern)** |
|------------------|------------------|-----------------------------------------------|
| **Event Type** | `eventName` | `["MODIFY"]` |
| **Value** | `dynamodb.NewImage.orderAmount.N` | Numeric Filter: `[">", 1000]` |
| **New Status** | `dynamodb.NewImage.orderStatus.S` | `["shipped"]` |
| **Status Change** | `dynamodb.OldImage.orderStatus.S` | `["pending"]` |

---

## 🛡️ Reliability and Error Handling

To ensure fault tolerance and resilience, each EventBridge Pipe is configured with robust error handling mechanisms:

- **Dead-Letter Queues (DLQs):**  
  Each pipe has a dedicated SQS DLQ (`NormalPipeDLQ` and `PremiumPipeDLQ`) to capture failed events.
  
- **Retry Logic:**  
  Configured with `MaximumRetryAttempts: 5`. Events are retried up to 5 times before being moved to the corresponding DLQ.

---

## 🚀 Deployment Guide

### 🧩 Prerequisites

- AWS CLI configured with appropriate credentials.
- IAM user/role with permissions for:
  - DynamoDB
  - Lambda
  - SQS
  - EventBridge
  - IAM
  - CloudFormation

---

### ⚙️ Deployment Steps

1. Clone or download this repository.
2. Save the `cloudformation.yaml` file locally.
3. Run the following command (replace *ecommerce-pipe-stack* with your desired stack name and region):

```bash
aws cloudformation deploy   --template-file cloudformation.yaml   --stack-name ecommerce-pipe-stack   --capabilities CAPABILITY_NAMED_IAM   --region your-aws-region
```

---

## 🧪 Testing

Once deployed, verify the setup using the **AWS Console** or **AWS CLI**:

1. Go to the **Orders** DynamoDB table.
2. Perform:
   - `PutItem` → to simulate a new order.
   - `UpdateItem` → to simulate order status changes.
3. Observe which **Lambda functions** are triggered:
   - `NormalOrdersProcessor` for normal events.
   - `PremiumOrdersProcessor` for premium shipped orders.
4. Check **CloudWatch Logs** and **SQS DLQs** for debugging and verification.

---

## 💰 Cost Optimization Highlights

- **Reduced Lambda Invocations:** Event filtering at EventBridge level avoids unnecessary compute.
- **Serverless Components:** Entire architecture scales automatically with zero idle cost.
- **DLQ Isolation:** Prevents data loss and unnecessary retries.

---

## 🧱 Infrastructure as Code (IaC)

The entire architecture is defined in the **`cloudformation.yaml`** file.  
This includes:
- DynamoDB table with streams enabled  
- EventBridge Pipes  
- Lambda functions and IAM roles  
- SQS DLQs for both pipes  

This ensures consistent, repeatable, and version-controlled deployment.

---

## 📊 Monitoring

Use **Amazon CloudWatch** to monitor:
- Lambda metrics (invocations, errors)
- SQS DLQ message counts
- EventBridge Pipe invocation success rates

---

## 👩‍💻 Author

**Komal Rethi**  

### 🌟 Key Takeaway
> **Efficient filtering at the infrastructure level** = **lower cost**, **faster response**, and **cleaner architecture** for real-time e-commerce event processing.
