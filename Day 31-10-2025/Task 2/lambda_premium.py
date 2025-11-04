import json
import os
import boto3
from botocore.exceptions import ClientError

dynamodb = boto3.client('dynamodb')
TABLE = os.environ.get('TABLE_NAME')

def is_premium(new_image, old_image):
    try:
        new_status = new_image.get('status', {}).get('S')
        old_status = old_image.get('status', {}).get('S')
        amount = float(new_image.get('amount', {}).get('N', '0'))
        email = new_image.get('customerEmail', {}).get('S', '').lower()
    except Exception:
        return False

    if old_status == 'pending' and new_status == 'shipped' and amount > 1000 and 'test.com' not in email:
        return True
    return False

def handle_premium_order(new_image):
    order_id = new_image.get('orderId', {}).get('S')
    amount = new_image.get('amount', {}).get('N')
    print(f"Handling premium order {order_id} amount={amount}")
    # Example: write a flag to DynamoDB or call external CRM
    try:
        dynamodb.update_item(
            TableName=TABLE,
            Key={'orderId': {'S': order_id}},
            UpdateExpression='SET premiumNotified = :t',
            ExpressionAttributeValues={':t': {'S': __import__('datetime').datetime.utcnow().isoformat() + 'Z'}}
        )
    except ClientError as e:
        print(f"Error updating premium flag for {order_id}: {e}")
        raise

def lambda_handler(event, context):
    processed = []
    records = event if isinstance(event, list) else [event]
    for rec in records:
        new = rec.get('dynamodb', {}).get('NewImage', {})
        old = rec.get('dynamodb', {}).get('OldImage', {})
        try:
            if is_premium(new, old):
                handle_premium_order(new)
                processed.append(new.get('orderId', {}).get('S'))
            else:
                print("Record skipped for premium pipeline")
        except Exception as e:
            print(f"Error handling premium record: {e}")
            raise
    return {"premiumProcessed": processed}
