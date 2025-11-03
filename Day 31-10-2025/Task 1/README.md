# 🚀 Serverless CRUD Web App: EC2 ↔️ API Gateway ↔️ Lambda ↔️ DynamoDB

This project demonstrates a fully functional **C.R.U.D. (Create, Read, Update, Delete)** web application built on AWS serverless services.  
The frontend, hosted on an EC2 instance, interacts with a fully serverless backend using **API Gateway**, **Lambda**, and **DynamoDB**.

---

## ✨ Features

- **Full CRUD Operations:** Create, read, update, and delete submissions.
- **Decoupled Architecture:** EC2 frontend separated from backend logic (Lambda + DynamoDB).
- **Secure API Access:** Managed via AWS API Gateway and IAM roles.
- **Dynamic Data Retrieval:** Query all or by email address.
- **CORS Enabled:** Supports cross-origin requests between EC2 and API Gateway.

---

## 🏗️ Architecture Diagram

| Component | Role | Technology |
|------------|------|------------|
| Frontend | User Interface for data entry and display | HTML, Bootstrap, JavaScript (Fetch API) |
| Web Host | Serves static HTML/JS files | AWS EC2 (t3.micro) + Apache/Nginx |
| API Endpoint | Exposes RESTful interface | AWS API Gateway (REST API) |
| Logic Layer | Executes all CRUD logic | AWS Lambda (Python 3.x, Boto3) |
| Database | NoSQL storage for submissions | AWS DynamoDB |

---

## ⚙️ Technical Details & AWS Components

### 1. DynamoDB Table: `UserSubmissions`

| Attribute | Type | Key Role |
|------------|------|----------|
| submissionId | String | Partition Key (PK) |
| name | String |  |
| email | String |  |
| message | String |  |
| submissionDate | String (ISO 8601) |  |
| status | String |  |

---

### 2. Lambda Functions (CRUD Logic)

| Function Name | Operation | API Method / Resource | DynamoDB Action |
|----------------|------------|------------------------|-----------------|
| SubmissionLambda | Create | POST /submit | PutItem |
| QueryLambda | Read | GET /submissions | Scan / Query |
| UpdateLambda | Update | PUT /submissions/{id} | UpdateItem |
| DeleteLambda | Delete | DELETE /submissions/{id} | DeleteItem |

> **IAM Role:** `LambdaDynamoDBAccessRole` with `AWSLambdaBasicExecutionRole` and `AmazonDynamoDBFullAccess`.

---

### 3. API Gateway Endpoints

| Method | Resource Path | Integrated Lambda | Description |
|--------|----------------|------------------|-------------|
| POST | /submit | SubmissionLambda | Create a new record |
| GET | /submissions | QueryLambda | Retrieve data |
| PUT | /submissions/{id} | UpdateLambda | Update a record |
| DELETE | /submissions/{id} | DeleteLambda | Delete a record |

> Ensure **CORS** is enabled for all endpoints.

---

## 🚀 Deployment and Setup

### **Prerequisites**
- AWS Account and AWS CLI configured
- SSH access to EC2 instance
- Basic knowledge of AWS IAM, DynamoDB, and Lambda

### **Step-by-Step Setup**

1. **Create DynamoDB Table**
   - Table name: `UserSubmissions`
   - Partition Key: `submissionId` (String)

2. **Create IAM Role**
   - Role name: `LambdaDynamoDBAccessRole`
   - Attach policies:  
     - `AWSLambdaBasicExecutionRole`  
     - `AmazonDynamoDBFullAccess`

3. **Deploy Lambda Functions**
   - Deploy `SubmissionLambda`, `QueryLambda`, `UpdateLambda`, `DeleteLambda`
   - Each must return header:  
     ```json
     "Access-Control-Allow-Origin": "*"
     ```

4. **Set up API Gateway**
   - Create REST API
   - Configure resources and methods
   - Enable **CORS**
   - Deploy API to stage: `prod`
   - Copy the Invoke URL

5. **Launch EC2 Instance**
   - Instance type: `t2.micro`
   - Install Apache/Nginx:
     ```bash
     sudo yum install httpd -y
     sudo systemctl start httpd
     sudo systemctl enable httpd
     ```
   - Host static files in `/var/www/html/`
   - Update JavaScript file with your API Gateway Invoke URL

---


### 🧑‍💻 Author

**Komal Rethi**  
