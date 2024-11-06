# Identity service

This microservice is responsible for:

* Onboarding users
* Roles and permissions
* Authentication

## Tech stack

* Build tool: maven >= 3.9.5
* Java: 17
* Framework: Spring boot 3.2.x
* DBMS: MySQL

## Prerequisites

* Java SDK 17
* A MySQL server

## Start application

`mvn spring-boot:run`

## Build application

`mvn clean package`

## Docker guideline
### Build docker image
`docker build -t <account>/identity-service:v1 .`
### Push docker image to Docker Hub
`docker image push <account>/identity-service:v1`
### Create network:
`docker network create phamtanhoang-network`
### Start MySQL in phamtanhoang-network
`docker run --network phamtanhoang-network --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.36-debian`
### Run your application in phamtanhoang-network
`docker run --name identity-service --network phamtanhoang-network -p 8080:8080 -e DBMS_CONNECTION=jdbc:mysql://mysql:3306/identity_service -d identity-service:0.9.0`

## Install Docker on ubuntu

# Add Docker's official GPG key:
sudo apt-get update
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources:
echo \
"deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
$(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update

sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

sudo docker run hello-world