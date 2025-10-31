import json, boto3
def lambda_handler(event, context):
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table('UserSubmissions')
    headers = {'Access-Control-Allow-Origin': '*', 'Access-Control-Allow-Methods': 'GET,POST,PUT,DELETE,OPTIONS', 'Access-Control-Allow-Headers': 'Content-Type'}
    submission_id = event['pathParameters']['id']
    try:
        data = json.loads(event['body'])
    except:
        return {'statusCode': 400, 'headers': headers, 'body': json.dumps({'message': 'Invalid JSON'})}
    update_exp, values = [], {}
    for k,v in data.items():
        if k in ['name','email','message','status']:
            update_exp.append(f"{k}=:{k}")
            values[f":{k}"]=v
    if not update_exp:
        return {'statusCode': 400, 'headers': headers, 'body': json.dumps({'message': 'No valid fields'})}
    try:
        table.update_item(Key={'submissionId': submission_id}, UpdateExpression='SET '+', '.join(update_exp), ExpressionAttributeValues=values)
        return {'statusCode': 200, 'headers': headers, 'body': json.dumps({'message': 'Updated successfully'})}
    except Exception as e:
        return {'statusCode': 500, 'headers': headers, 'body': json.dumps({'message': str(e)})}