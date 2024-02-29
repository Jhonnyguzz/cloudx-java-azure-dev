## How to deploy PetStore

### Deployment code structure
1. `parameters.sh` contains RG name and container registry name. It is reused in multiple scripts
2. `deploy-rg-acr.sh` deploys Resource Group (container for all resources) and Container Registry.
3. `deploy-app-service.sh` deploys one AppService plan and AppService.
4. `deploy-web-and-api.sh`\
   - builds required docker images
   - deploys web app and public services to azure
   - apps are deployed with proper configuration
5. `cleanup.sh` cleans up all resources.

### Pre-requisites
1. Azure Subscription was created
2. Azure CLI is installed
3. Bash console is available

### Deployment steps
1. Open Bash in this folder
2. Login to Azure
```az login```
3. Run `./deploy-web-and-api.sh` with the suffix. Suffix is used to make app and service URIs unique\
Example: `./deploy-web-and-api.sh jdoesuf`\
App URI: [https://petstoreappjdoesuf.azurewebsites.net]()
4. Verify that all resources are in place using Azure Portal or CLI
5. Open app URI. Try multiple times as App and Services has a lengthy cold start.

### Cleanup
`./cleanup.sh`