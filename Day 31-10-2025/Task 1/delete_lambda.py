import json, boto3
def lambda_handler(event, context):
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table('UserSubmissions')
    headers = {'Access-Control-Allow-Origin': '*', 'Access-Control-Allow-Methods': 'GET,POST,PUT,DELETE,OPTIONS', 'Access-Control-Allow-Headers': 'Content-Type'}
    submission_id = event['pathParameters']['id']
    try:
        response = table.delete_item(Key={'submissionId': submission_id}, ReturnValues='ALL_OLD')
        if 'Attributes' in response:
            return {'statusCode': 200, 'headers': headers, 'body': json.dumps({'message': f'Submission {submission_id} deleted'})}
        else:
            return {'statusCode': 404, 'headers': headers, 'body': json.dumps({'message': 'Not found'})}
    except Exception as e:
        return {'statusCode': 500, 'headers': headers, 'body': json.dumps({'message': str(e)})}