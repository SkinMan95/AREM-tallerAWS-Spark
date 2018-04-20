AREM - REPORTE DE PRUEBAS
=========================

# Función AWS Lambda
![Lambda](/images/function.png)

Se tiene la función lambda implementada en *Python 3.6* con el que se recibe un numero y se devuelve el cuadrado del mismo. (Se consideran dos casos de entrada: Entrada para casos de prueba dado como `event['value']` y caso general que se recibe como un JSON desde el entorno del Gateway).



# AWS Gateway
![Gateway](/images/gateway.png)

Se tiene el Gateway por donde se realizara la comunicación del exterior (la red) con la función lambda, estableciéndola por medio del método GET y donde se establece como obligatorio el parámetro `value` dentro de la petición que obedece el formato establecido en el modelo de JSON siguiente.

## Modelo de JSON
Se establece el modelo de JSON de tal forma que el parámetro de entrada `value` del método GET sea obligatoriamente un entero.

```json
{
	"title": "value",
	"type": "integer"
}
```

### Definición de *Body Mapping*
Adicionalmente se tiene el *Body Mapping* de donde se establecen los datos que se pasaran a la función de lambda, sin esta no es posible realizar la comunicación de la información de entrada, esta es una plantilla dada como *Method Request passthrough*, el cual deja pasar todos los datos de entrada a la función.

```json
##  See http://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-mapping-template-reference.html
##  This template will pass through all parameters including path, querystring, header, stage variables, and context through to the integration endpoint via the body/payload
#set($allParams = $input.params())
{
"body-json" : $input.json('$'),
"params" : {
#foreach($type in $allParams.keySet())
    #set($params = $allParams.get($type))
"$type" : {
    #foreach($paramName in $params.keySet())
    "$paramName" : "$util.escapeJavaScript($params.get($paramName))"
        #if($foreach.hasNext),#end
    #end
}
    #if($foreach.hasNext),#end
#end
},
"stage-variables" : {
#foreach($key in $stageVariables.keySet())
"$key" : "$util.escapeJavaScript($stageVariables.get($key))"
    #if($foreach.hasNext),#end
#end
},
"context" : {
    "account-id" : "$context.identity.accountId",
    "api-id" : "$context.apiId",
    "api-key" : "$context.identity.apiKey",
    "authorizer-principal-id" : "$context.authorizer.principalId",
    "caller" : "$context.identity.caller",
    "cognito-authentication-provider" : "$context.identity.cognitoAuthenticationProvider",
    "cognito-authentication-type" : "$context.identity.cognitoAuthenticationType",
    "cognito-identity-id" : "$context.identity.cognitoIdentityId",
    "cognito-identity-pool-id" : "$context.identity.cognitoIdentityPoolId",
    "http-method" : "$context.httpMethod",
    "stage" : "$context.stage",
    "source-ip" : "$context.identity.sourceIp",
    "user" : "$context.identity.user",
    "user-agent" : "$context.identity.userAgent",
    "user-arn" : "$context.identity.userArn",
    "request-id" : "$context.requestId",
    "resource-id" : "$context.resourceId",
    "resource-path" : "$context.resourcePath"
    }
}

```

# Pagina en el servidor AWS EC2
La pagina dada esta servida por el servidor en AWS ubicada en http://ec2-52-32-11-239.us-west-2.compute.amazonaws.com/, puesta en el puerto `80` por defecto desarrollada con *Spark*.

![Pagina](/images/page.png)
