# Deploy Connect 4 Server to Azure Container Apps

This server uses a raw Java TCP socket on port `5555`, so Azure Container Apps with TCP ingress is the Azure option that matches the current code.

## Test Docker locally

Run these commands from the `connect4` folder:

```bash
docker build -t connect4-server .
docker run --rm -p 5555:5555 connect4-server
```

In another terminal:

```bash
nc -vz localhost 5555
```

## Create Azure resources

In Azure Cloud Shell Bash:

```bash
RG=connect4-container-rg
LOCATION=centralus
ACR=connect4registry$RANDOM
APP=connect4-server
ENV=connect4-env

az group create --name $RG --location $LOCATION

az acr create \
  --resource-group $RG \
  --name $ACR \
  --sku Basic \
  --admin-enabled true

az containerapp env create \
  --name $ENV \
  --resource-group $RG \
  --location $LOCATION
```

## Build and push the image

From your Mac, inside the `connect4` folder:

```bash
az login
az acr login --name <YOUR_ACR_NAME>

docker build -t connect4-server .
docker tag connect4-server <YOUR_ACR_NAME>.azurecr.io/connect4-server:latest
docker push <YOUR_ACR_NAME>.azurecr.io/connect4-server:latest
```

## Deploy with TCP ingress

In Azure Cloud Shell:

```bash
az extension add --name containerapp --upgrade
az acr credential show --name $ACR
```

Use one password from that output:

```bash
az containerapp create \
  --name $APP \
  --resource-group $RG \
  --environment $ENV \
  --image $ACR.azurecr.io/connect4-server:latest \
  --registry-server $ACR.azurecr.io \
  --registry-username $ACR \
  --registry-password <PASSWORD_FROM_ACR> \
  --ingress external \
  --transport tcp \
  --target-port 5555 \
  --min-replicas 1 \
  --max-replicas 1 \
  --cpu 0.25 \
  --memory 0.5Gi
```

Get the public host:

```bash
az containerapp show \
  --name $APP \
  --resource-group $RG \
  --query "properties.configuration.ingress.fqdn" \
  -o tsv
```

Check whether Azure assigned a different exposed port:

```bash
az containerapp show \
  --name $APP \
  --resource-group $RG \
  --query "properties.configuration.ingress" \
  -o json
```

## Point the JavaFX client at Azure

Keep local defaults by doing nothing. To connect to Azure, set:

```bash
export CONNECT4_SERVER_HOST=<YOUR_CONTAINER_APP_FQDN>
export CONNECT4_SERVER_PORT=5555
```

Or pass JVM properties:

```bash
-Dconnect4.server.host=<YOUR_CONTAINER_APP_FQDN> -Dconnect4.server.port=5555
```

If Azure shows a different exposed TCP port, use that port instead of `5555`.

## Clean up Azure resources

When you are done testing:

```bash
az group delete --name connect4-container-rg --yes --no-wait
```
