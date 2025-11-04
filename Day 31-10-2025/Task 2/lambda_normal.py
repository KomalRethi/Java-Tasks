import json
import os
import boto3
from botocore.exceptions import ClientError

dynamodb = boto3.client('dynamodb')
TABLE = os.environ.get('TABLE_NAME')

def process_order_item(new_image):
    # robust extraction with defaults
    order_id = new_image.get('orderId', {}).get('S')
    amount = float(new_image.get('amount', {}).get('N', '0'))
    status = new_image.get('status', {}).get('S')
    email = new_image.get('customerEmail', {}).get('S', '').lower()

    if amount <= 100:
        print(f"Skipping {order_id}: amount {amount} <= 100")
        return False

    if 'test.com' in email:
        print(f"Skipping {order_id}: test email {email}")
        return False

    # Business logic placeholder - idempotent operation recommended
    print(f"Processing order {order_id}: status={status}, amount={amount}, email={email}")
    # Example: annotate the item with processedAt attribute (idempotent update)
    try:
        dynamodb.update_item(
            TableName=TABLE,
            Key={'orderId': {'S': order_id}},
            UpdateExpression='SET processedBy = :p, processedAt = :t',
            ExpressionAttributeValues={
                ':p': {'S': 'normal-processor'},
                ':t': {'S': __import__('datetime').datetime.utcnow().isoformat() + 'Z'}
            },
            ConditionExpression='attribute_not_exists(processedBy) OR processedBy = :p'
        )
    except ClientError as e:
        # If conditional update fails due to race condition, that's OK (already processed)
        print(f"Update failed for {order_id}: {e}")
    return True

def lambda_handler(event, context):
    processed = 0
    failed = []
    records = event if isinstance(event, list) else [event]
    for rec in records:
        new_image = rec.get('dynamodb', {}).get('NewImage', {})
        try:
            ok = process_order_item(new_image)
            if ok:
                processed += 1
        except Exception as e:
            print(f"Error processing record: {e}")
            failed.append(rec)
    # If some failed, raise an exception so EventBridge Pipes will consider retry or DLQ
    if failed:
        raise Exception(f"{len(failed)} records failed")
    return {"processed": processed}
