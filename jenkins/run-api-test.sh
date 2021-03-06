#!/usr/bin/env bash

# This script will try to connect to your component during 3 minutes

# TO CHANGE: This line should point to your component in Staging
COMPONENT_URL="http://31.171.247.162:30003/"

# TO CHANGE: This line should point to the yaml file of your respository of your api definition
YAML_FILE="documentation/APISpecification_integrationTest.yaml"

attempts=0
until $(curl --output /dev/null --silent $COMPONENT_URL); do
    if [ ${attempts} -eq 20 ];then
      echo "Max attempts reached, cannot connect to component"
      exit 1
    fi

    printf '.'
    attempts=$(($attempts+1))
    sleep 9
done

# If you use Hooks, you have to modify this line. Read the Dredd manual.
#dredd $YAML_FILE $COMPONENT_URL
dredd $YAML_FILE $COMPONENT_URL --hookfiles=./jenkins/hooks.js