# 📂 AWS S3 File Processing System Using Java Lambda

This project implements an **automated text file processing system** using **AWS S3**, **Lambda (Java 17)**, and **DynamoDB**.  
Whenever a `.txt` file is uploaded to an S3 bucket, a Java Lambda function is triggered to read, process, and store results in DynamoDB.

---

## 🚀 Architecture Overview

1. **Amazon S3 Bucket** — Stores uploaded `.txt` files.  
   - Bucket Name: `file-processing-bucket-vpg`
   - Event: Triggers Lambda when `.txt` file is uploaded.

2. **AWS Lambda Function** — Processes uploaded files automatically.  
   - Function Name: `TextFileProcessor`  
   - Runtime: `Java 17`  
   - Trigger: `S3:ObjectCreated:*`  
   - Responsibilities:
     - Reads file from S3.
     - Counts **lines**, **words**, and **characters**.
     - Extracts a **preview (first 100 characters)**.
     - Stores results in DynamoDB.

3. **Amazon DynamoDB Table** — Stores processing results.  
   - Table Name: `FileProcessingResults`  
   - Partition Key: `fileName (String)`  
   - Attributes: `lineCount`, `wordCount`, `charCount`, `preview`, `processedDate`

---

## 🧠 Expected JSON Output

```json
{
  "fileName": "sample.txt",
  "lineCount": 3,
  "wordCount": 12,
  "charCount": 58,
  "preview": "Sample text file content with multiple lines for testing...",
  "processedDate": "2025-11-03T10:30:00Z"
}
```

---

## 🧩 Java Lambda Source Code

The Lambda function consists of multiple classes for modularity:

| Class Name | Responsibility |
|-------------|----------------|
| **TextFileProcessor.java** | Main Lambda handler, reads S3 event, orchestrates processing and storage. |
| **S3FileReader.java** | Reads the uploaded `.txt` file from S3. |
| **FileProcessor.java** | Counts lines, words, characters, and extracts preview. |
| **DynamoDBWriter.java** | Writes processed results to DynamoDB. |
| **FileProcessingResult.java** | POJO model to represent and serialize results. |

---

## 🛠️ S3 Event Notification Configuration

**Event type:** `s3:ObjectCreated:*`  
**Suffix filter:** `.txt`  
**Destination:** `Lambda function (TextFileProcessor)`

---

## 🔐 IAM Role Policies

Lambda requires permissions to access both **S3** and **DynamoDB**.

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["s3:GetObject"],
      "Resource": "arn:aws:s3:::file-processing-bucket-vpg/*"
    },
    {
      "Effect": "Allow",
      "Action": ["dynamodb:PutItem"],
      "Resource": "arn:aws:dynamodb:*:*:table/FileProcessingResults"
    }
  ]
}
```

---

## 🧪 Sample Input File

**File name:** `task3file.txt`  
**Content:**
```
hi, hello world!
this is Komal
```

---

## ✅ Output in DynamoDB

| Attribute | Value |
|------------|--------|
| fileName | task3file.txt |
| lineCount | 2 |
| wordCount | 6 |
| charCount | 30 |
| preview | hi, hello world!\nthis is Komal |
| processedDate | 2025-11-03T04:37:49.294233882Z |

---

## 📊 CloudWatch Logs Snapshot

Logs confirm successful execution of the Lambda function:

```
INFO  TextFileProcessor - File processed: Lines: 2, Words: 6, Characters: 30
INFO  DynamoDBWriter - Successfully written to DynamoDB. Response status code: 200
INFO  TextFileProcessor - Result stored in DynamoDB for file: task3file.txt
```

---

## 🖼️ Screenshots

### 🪣 Uploaded File in S3
![S3 Screenshot]
<img width="1033" height="495" alt="Screenshot 2025-11-03 101517" src="https://github.com/user-attachments/assets/1476f7c8-81d9-4741-b85d-0caae38e8048" />


### 🧾 Lambda Execution Logs
![CloudWatch Logs Screenshot]
<img width="1033" height="495" alt="Screenshot 2025-11-03 101517" src="https://github.com/user-attachments/assets/4d4572c4-479f-469b-9667-553e08372811" />


---

## 🧰 Technologies Used

- **AWS Lambda (Java 17)**
- **Amazon S3**
- **Amazon DynamoDB**
- **CloudWatch Logs**

---

## 🧾 Success Criteria

✅ Upload `.txt` file to S3  
✅ Lambda automatically triggers  
✅ File statistics computed successfully  
✅ Results stored in DynamoDB  
✅ Verified in CloudWatch logs  

---

## 🧑‍💻 Author

**Komal Rethi** 

