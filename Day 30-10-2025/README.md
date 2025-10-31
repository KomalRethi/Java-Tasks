# 🖼️ AWS Serverless Image Management System

A fully serverless project built using **AWS Lambda**, **S3**, **DynamoDB**, and **API Gateway**.  
This system automatically stores, retrieves, and deletes image metadata from DynamoDB whenever images are uploaded or deleted in an S3 bucket.  
It also provides REST API endpoints to perform CRUD operations through API Gateway.

---

## 🚀 Features

- 📤 Upload image metadata to DynamoDB upon S3 upload
- 📜 Retrieve all or individual image metadata via API Gateway
- ❌ Delete images from S3 and corresponding records from DynamoDB
- 🔄 Event-driven architecture using AWS services
- 🔐 Secure IAM roles and policies for least-privilege access
- 🌍 Fully serverless – no need for manual infrastructure management

---

## 🏗️ Architecture Overview

```
Client → API Gateway → Lambda Functions → DynamoDB / S3
```

---

## 🧩 AWS Services Used

| Service | Purpose |
|----------|----------|
| **Amazon S3** | Stores uploaded image files |
| **AWS Lambda** | Processes upload, retrieval, and deletion logic |
| **Amazon DynamoDB** | Stores image metadata (filename, bucket, source, timestamp) |
| **Amazon API Gateway** | Provides REST endpoints for Lambda functions |
| **Amazon CloudWatch** | Monitors and logs Lambda executions |

---

## 🧱 Project Setup

### 1️⃣ Create S3 Bucket
- Name: `image-upload-demo-komal`
- Enable event notification for `PUT` (Object Created) events.
- Configure it to trigger the `S3UploadHandler` Lambda.

### 2️⃣ Create DynamoDB Table
- **Table name:** `ImageMetadata`
- **Primary key:** `FileName` (String)

### 3️⃣ Create Lambda Functions

#### 🔵 `S3UploadHandler`
Triggered automatically when an image is uploaded to S3.  
Adds image metadata into DynamoDB.

```python
import boto3
import json
from datetime import datetime

dynamodb = boto3.resource('dynamodb')
table = dynamodb.Table('ImageMetadata')

def lambda_handler(event, context):
    try:
        for record in event['Records']:
            bucket = record['s3']['bucket']['name']
            key = record['s3']['object']['key']
            timestamp = datetime.utcnow().isoformat()

            # Insert metadata into DynamoDB
            table.put_item(Item={
                'FileName': key,
                'Bucket': bucket,
                'Source': 'S3',
                'Timestamp': timestamp
            })

        return {'statusCode': 200, 'body': json.dumps('Metadata added successfully')}
    except Exception as e:
        return {'statusCode': 500, 'body': json.dumps({'error': str(e)})}
```

---

### 🔵 `S3GetAllImages`

Retrieves all image metadata from DynamoDB.

```python
import boto3
import json

dynamodb = boto3.resource('dynamodb')
table = dynamodb.Table('ImageMetadata')

def lambda_handler(event, context):
    try:
        response = table.scan()
        data = response.get('Items', [])
        return {'statusCode': 200, 'body': json.dumps({'images': data})}
    except Exception as e:
        return {'statusCode': 500, 'body': json.dumps({'error': str(e)})}
```

---

### 🟣 `S3GetImageByName`

Fetches details of a single image based on its file name.

```python
import boto3
import json

dynamodb = boto3.resource('dynamodb')
table = dynamodb.Table('ImageMetadata')

def lambda_handler(event, context):
    try:
        body = json.loads(event['body'])
        file_name = body.get('FileName')

        if not file_name:
            return {'statusCode': 400, 'body': json.dumps({'error': 'FileName missing'})}

        response = table.get_item(Key={'FileName': file_name})
        item = response.get('Item')

        if not item:
            return {'statusCode': 404, 'body': json.dumps({'error': 'Item not found'})}

        return {'statusCode': 200, 'body': json.dumps({'data': item})}
    except Exception as e:
        return {'statusCode': 500, 'body': json.dumps({'error': str(e)})}
```

---

### 🔴 `S3DeleteHandler`

Deletes an image from S3 and removes its metadata from DynamoDB.

```python
import boto3
import json

s3 = boto3.client('s3')
dynamodb = boto3.resource('dynamodb')
table = dynamodb.Table('ImageMetadata')

def lambda_handler(event, context):
    try:
        body = json.loads(event['body'])
        file_name = body.get('FileName')

        if not file_name:
            return {'statusCode': 400, 'body': json.dumps({'error': 'FileName missing'})}

        response = table.get_item(Key={'FileName': file_name})
        item = response.get('Item')

        if not item:
            return {'statusCode': 404, 'body': json.dumps({'error': 'Item not found'})}

        s3.delete_object(Bucket=item['Bucket'], Key=item['FileName'])
        table.delete_item(Key={'FileName': file_name})

        return {
            'statusCode': 200,
            'body': json.dumps({
                'message': 'Image and metadata deleted successfully',
                'deletedFile': file_name
            })
        }
    except Exception as e:
        return {'statusCode': 500, 'body': json.dumps({'error': str(e)})}
```

---

## 🌐 API Gateway Endpoints

| Method     | Endpoint   | Description                            | Lambda Function    |
| ---------- | ---------- | -------------------------------------- | ------------------ |
| **POST**   | `/upload`  | Upload image metadata                  | `S3UploadHandler`  |
| **GET**    | `/getall`  | Retrieve all image metadata            | `S3GetAllImages`   |
| **POST**   | `/getitem` | Retrieve metadata for a specific image | `S3GetImageByName` |
| **DELETE** | `/delete`  | Delete image and metadata              | `S3DeleteHandler`  |

---

## 🧪 Testing with Postman

### 🟢 Upload (POST `/upload`)

**Body (raw JSON):**

```json
{
  "FileName": "example.png",
  "Bucket": "image-upload-demo-komal",
  "Source": "S3"
}
```

---

### 🔵 Get All (GET `/getall`)

No body required.

---

### 🟣 Get by Name (POST `/getitem`)

**Body (raw JSON):**

```json
{
  "FileName": "example.png"
}
```

---

### 🔴 Delete (DELETE `/delete`)

**Body (raw JSON):**

```json
{
  "FileName": "example.png"
}
```

---

## 🔐 IAM Role Permissions

Attach this policy to your Lambda execution role to grant DynamoDB, S3, and CloudWatch permissions.

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["dynamodb:*"],
      "Resource": "arn:aws:dynamodb:ap-south-1:<ACCOUNT_ID>:table/ImageMetadata"
    },
    {
      "Effect": "Allow",
      "Action": ["s3:*"],
      "Resource": [
        "arn:aws:s3:::image-upload-demo-komal",
        "arn:aws:s3:::image-upload-demo-komal/*"
      ]
    },
    {
      "Effect": "Allow",
      "Action": ["logs:*"],
      "Resource": "*"
    }
  ]
}
```

---

## 🧾 Future Enhancements

* 🔄 Add **EventBridge** for automatic triggers on S3 events
* 📊 Enable **CloudWatch Metrics** for performance insights
* 🔐 Implement **API Gateway Authorizers** for authentication
* 💻 Create a **Frontend UI** to upload/view images directly

---

## 🧱 Project Summary

| Category      | Details                                                   |
| ------------- | --------------------------------------------------------- |
| **Framework** | Serverless (AWS Lambda)                                   |
| **Database**  | DynamoDB                                                  |
| **Storage**   | S3                                                        |
| **API Layer** | API Gateway                                               |
| **Language**  | Python 3.9+                                               |
| **Purpose**   | Manage image files and metadata using AWS-native services |

---

## 🖥️ System Architecture Diagram

```
+-----------+       +---------------+       +-----------------+
|  Client   | --->  | API Gateway   | --->  | AWS Lambda      |
+-----------+       +---------------+       +--------+--------+
                                              |       |
                                              v       v
                                       +------------+  +-----------+
                                       |  DynamoDB  |  |   S3      |
                                       +------------+  +-----------+
```

---

⭐ **Developed by Komal Rethi**  
💡 AWS | Python | Serverless | Cloud Projects
