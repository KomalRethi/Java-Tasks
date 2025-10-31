import json, uuid, datetime, boto3
def lambda_handler(event, context):
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table('UserSubmissions')
    headers = {'Access-Control-Allow-Origin': '*', 'Access-Control-Allow-Methods': 'GET,POST,PUT,DELETE,OPTIONS', 'Access-Control-Allow-Headers': 'Content-Type'}
    try:
        body = json.loads(event['body'])
    except:
        return {'statusCode': 400, 'headers': headers, 'body': json.dumps({'message': 'Invalid JSON body'})}
    if not all(k in body for k in ('name', 'email', 'message')):
        return {'statusCode': 400, 'headers': headers, 'body': json.dumps({'message': 'Missing required fields'})}
    item = {'submissionId': str(uuid.uuid4()), 'name': body['name'], 'email': body['email'], 'message': body['message'], 'submissionDate': datetime.datetime.now(datetime.timezone.utc).isoformat(), 'status': 'New'}
    try:
        table.put_item(Item=item)
        return {'statusCode': 201, 'headers': headers, 'body': json.dumps({'message': 'Submission successful', 'id': item['submissionId']})}
    except Exception as e:
        return {'statusCode': 500, 'headers': headers, 'body': json.dumps({'message': str(e)})}