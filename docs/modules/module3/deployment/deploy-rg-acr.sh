#!/bin/sh
set -e

appSuffix=$1
source $(dirname "$0")/parameters.sh


az group create --name $rgName --location $location
az acr create --name "${containerRegistryName}${appSuffix}" --resource-group $rgName --sku Standard --admin-enabled true
