# CS553 - Design of Internet Services

## Project - Microservices Latency Analysis

### Developers: 
### Naishal Patel (np781)
### Himani Patel (hhp46)

### Overview: 
#### In this project we have developed 3 basic microservices for the ecommerce usecase. Our aim was to use state-of-the-art technology to improve latency. Also, we analysed these technologies with YCSB benchmark.

### Technologies used:
#### 1) Redis as cache to primary database
#### 2) Connection Pooling (We used inbuilt connection pool MongoClient for MongoDB; also we made our own custom connection pool)
#### 3) Monitoring microservices using Prometheus and Grafana
#### 4) YCSB used for benchmark testing

### How to run this application?
#### We have dockerized our microservices for the ease of running up the application. Also, we have attached our project report for deep analysis of our microservices. Here are the following steps to run the application:
#### Step 1: Clone this repository or download the zip code
#### Step 2: To run the application and build docker images locally run: ``mvn clean package -DskipTests``
#### Step 3: After docker images are pulled successfully, simply run: ``docker-compose up -d``
#### Step 4: After this all the necessary images will be downloaded and the containers will be up and running.

### Additional Dependencies
#### You must have docker installed in your system (keep in mind for windows you would need WSL). If you don't wish to run using docker, you can simply run individual spring applications.
#### You must have maven installed in your system.

### Benchmark Testing
#### This is the primary target for our project. We have done benchmark testing using YCSB and then analysed the results(refer to project report). Steps to setup YCSB locally are pretty simple and can be found here: https://github.com/brianfrankcooper/YCSB
#### For testing purpose we have targeted product service which interacts with MongoDB. 
#### General workflow for testing is to load a particular workload to our DB and then execute that workload. Differenet workloads available are:
Workload A: Update heavy workload: 50/50% Mix of Reads/Writes

Workload B: Read mostly workload: 95/5% Mix of Reads/Writes

Workload C: Read-only: 100% reads

Workload D: Read the latest workload: More traffic on recent inserts

Workload E: Short ranges: Short range based queries

Workload F: Read-modify-write: Read, modify and update existing records

#### Phase 1: Testing with the base code without Redis as cache and without Connection Pool:

Commands:  
To load the data (workload A) in our MongoDB: 
``
./bin/ycsb load mongodb-async -s -P workloads/workloada -p mongodb.url=mongodb://localhost:27017/product-service
``
You can change the workload you wish. For example to load workload B:
``
./bin/ycsb load mongodb-async -s -P workloads/workloadb -p mongodb.url=mongodb://localhost:27017/product-service
``

To execute the workload:
``
./bin/ycsb run mongodb-async -s -P workloads/workloada -p mongodb.url=mongodb://localhost:27017/product-service -threads 1 -p operationcount=10000
``
You can add parameters like `-thread 1` to specify the number of threads to exectue the workload. Also you can specify the number of total operations performed `-p operationcount=10000`

We primarily perform our tests with total 10000 operations per test and with 1 and 4 threads.

#### Phase 2: Testing with the base code with inbuilt MongoCLient Connection Pool for MongoDB but without Redis as cache:

The commands will be same as we used for Phase 1. 

#### Phase 3: Testing with the base code with our own custom Connection Pool for MongoDB but without Redis as cache:

The commands will be same as we used for Phase 1. 

#### Phase 4: Testing with the base code with inbuilt MongoCLient Connection Pool for MongoDB and with Redis as cache:

Commands:  
To load the data (workload A) in our MongoDB:
``
./bin/ycsb load mongodb-async -s -P workloads/workloada -p mongodb.url=mongodb://localhost:27017/product-service
``

To execute the workload:
``
./bin/ycsb run redis -s -P workloads/workloada -p "redis.host=127.0.0.1" -p "redis.port=6379" -p mongodb.url=mongodb://localhost:27017/product-service -threads 1 -p operationcount=10000
``

Here we need to execute the workload on redis since we want to test the working of cache. So we specify the redis host and port as well as our main DB url to establish connection. Also, we can configure threads and operation count as desired.

### Monitoring Microservices
#### To check and analyse for any potential bottleneck we are using prometheus and grafana.
#### To check the prometheus, simply go to `http://localhost:9090/` and you can view all properties, graphs and targets to check the status of microservices.
#### We use prometheus as datasource to create grafana dashboard.
First go to `http://localhost:3000/`. Then click on settings symbol and select the datasource to create a new datasource.

Select "Prometheus" and click on add datasource. In the URL prompt type `http://prometheus:9090` and then at the bottom click on "Save & Test".

After successfully creating datasource, now go to the "+" icon and select "import". In the json panel copy and paste the contents of "Grafana_Dashboard.json" in the root directory of our application. Then click on "Load".

The grafana dashboard will be up and running and we can monitor different aspects of each microservices to lookout for any potential bottleneck.

### We have also mentioned a video link in the report which walks through the entire process!!