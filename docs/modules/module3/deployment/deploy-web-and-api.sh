#!/bin/bash

set -e

appSuffix=$1
petserviceTag=petstorepetservice
petserviceSubdomain="${petserviceTag}${appSuffix}"
productserviceTag=petstoreproductservice
productserviceSubdomain="${productserviceTag}${appSuffix}"
orderserviceTag=petstoreorderservice
orderserviceSubdomain="${orderserviceTag}${appSuffix}"
webAppTag=petstoreapp
webAppSubdomain="${webAppTag}${appSuffix}"

#creating RG and ACR first to let container ACR time to initialize, while dockers are being built
./deploy-rg-acr.sh $appSuffix

echo "Building PetService"
docker build -t $petserviceTag ../../../petstore/petstorepetservice

echo "Building ProductService"
docker build -t $productserviceTag ../../../petstore/petstoreproductservice

echo "Building OrderService"
docker build -t $orderserviceTag ../../../petstore/petstoreorderservice

echo "Building Web App"
docker build -t $webAppTag ../../../petstore/petstoreapp

# back to working dir
./deploy-app-service.sh $petserviceSubdomain $petserviceTag $appSuffix
./deploy-app-service.sh $productserviceSubdomain $productserviceTag $appSuffix
./deploy-app-service.sh $orderserviceSubdomain $orderserviceTag $appSuffix "PETSTOREPRODUCTSERVICE_URL=https://${productserviceSubdomain}.azurewebsites.net/"
./deploy-app-service.sh $webAppSubdomain $webAppTag $appSuffix "PETSTOREPRODUCTSERVICE_URL=https://${productserviceSubdomain}.azurewebsites.net PETSTOREPETSERVICE_URL=https://${petserviceSubdomain}.azurewebsites.net PETSTOREORDERSERVICE_URL=https://${orderserviceSubdomain}.azurewebsites.net"