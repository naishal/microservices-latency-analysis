package com.dis.productservice.connectionpool;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class CustomMongoConnectionPool {

    private final int MAX_POOL_SIZE = 100;
    private final List<MongoClient> clients = new ArrayList<>();
    private int clientCount = 0;

    private MongoDatabase mongoDatabase;

    @PostConstruct
    public void init() {
        try {
            for (int i = 0; i < MAX_POOL_SIZE; i++) {
                clients.add(MongoClients.create("mongodb://localhost:27017"));
            }
            mongoDatabase = clients.get(0).getDatabase("product-service");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized MongoClient getClient() {
        MongoClient client = null;
        if (clientCount < MAX_POOL_SIZE) {
            client = clients.get(clientCount);
            clientCount++;
        }
        return client;
    }

    public synchronized void releaseClient(MongoClient client) {
        if (client != null) {
            clientCount--;
        }
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    @PreDestroy
    public void destroy() {
        clients.forEach(MongoClient::close);
    }
}


//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoDatabase;
//
//public class CustomMongoConnectionPool {
//    private static CustomMongoConnectionPool instance = null;
//    private final int MAX_POOL_SIZE = 10;
//    private final List<Connection> connections = new ArrayList<>();
//    private int connectionCount = 0;
//
//    private MongoClient mongoClient;
//    private MongoDatabase mongoDatabase;
//
//    private CustomMongoConnectionPool() {
//        try {
//            // create MongoDB connection
//            mongoClient = MongoClients.create("mongodb://localhost:27017");
//            mongoDatabase = mongoClient.getDatabase("product-service");
//            // create MySQL connection
//            Class.forName("com.mysql.jdbc.Driver");
//            for (int i = 0; i < MAX_POOL_SIZE; i++) {
//                connections.add(DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "user", "password"));
//            }
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static CustomMongoConnectionPool getInstance() {
//        if (instance == null) {
//            instance = new CustomMongoConnectionPool();
//        }
//        return instance;
//    }
//
//    public synchronized Connection getConnection() {
//        Connection connection = null;
//        if (connectionCount < MAX_POOL_SIZE) {
//            connection = connections.get(connectionCount);
//            connectionCount++;
//        }
//        return connection;
//    }
//
//    public synchronized void releaseConnection(Connection connection) {
//        try {
//            if (connection != null) {
//                connection.close();
//                connectionCount--;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public MongoDatabase getMongoDatabase() {
//        return mongoDatabase;
//    }
//}
