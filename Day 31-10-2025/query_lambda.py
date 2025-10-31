import json, boto3
from boto3.dynamodb.conditions import Attr
def lambda_handler(event, context):
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table('UserSubmissions')
    headers = {'Access-Control-Allow-Origin': '*', 'Access-Control-Allow-Methods': 'GET,POST,PUT,DELETE,OPTIONS', 'Access-Control-Allow-Headers': 'Content-Type'}
    query_params = event.get('queryStringParameters', {})
    try:
        if query_params and 'email' in query_params:
            response = table.scan(FilterExpression=Attr('email').eq(query_params['email']))
        else:
            response = table.scan()
        return {'statusCode': 200, 'headers': headers, 'body': json.dumps(response['Items'])}
    except Exception as e:
        return {'statusCode': 500, 'headers': headers, 'body': json.dumps({'message': str(e)})}