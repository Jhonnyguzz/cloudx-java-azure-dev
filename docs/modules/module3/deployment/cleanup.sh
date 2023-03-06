#!/bin/sh
set -e

source $(dirname "$0")/parameters.sh

az group delete --name $rgName
