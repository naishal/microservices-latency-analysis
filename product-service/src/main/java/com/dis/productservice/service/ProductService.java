package com.dis.productservice.service;

import com.dis.productservice.connectionpool.CustomMongoConnectionPool;
import com.dis.productservice.dto.ProductRequest;
import com.dis.productservice.dto.ProductResponse;
import com.dis.productservice.model.Product;
import com.dis.productservice.repository.ProductRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
//public class ProductService {
//
//    private final ProductRepository productRepository;
//
//    public void createProduct(ProductRequest productRequest) {
//        Product product = Product.builder()
//                .name(productRequest.getName())
//                .description(productRequest.getDescription())
//                .price(productRequest.getPrice())
//                .build();
//
//        productRepository.save(product);
//        log.info("Product {} is saved", product.getId());
//    }
//
//    public List<ProductResponse> getAllProducts() {
//        List<Product> products = productRepository.findAll();
//
//        return products.stream().map(this::mapToProductResponse).toList();
//    }
//
//    private ProductResponse mapToProductResponse(Product product) {
//        return ProductResponse.builder()
//                .id(product.getId())
//                .name(product.getName())
//                .description(product.getDescription())
//                .price(product.getPrice())
//                .build();
//    }
//}


public class ProductService {

//    private final ProductRepository productRepository;

    // Create a MongoClient instance and connect to your MongoDB database
    private final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private final MongoDatabase mongoDatabase = mongoClient.getDatabase("product-service");

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        // Insert the new product into the "products" collection
        MongoCollection<Document> collection = mongoDatabase.getCollection("products");
        Document productDocument = new Document("name", product.getName())
                .append("description", product.getDescription())
                .append("price", product.getPrice());
        collection.insertOne(productDocument);

        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        MongoCollection<Document> collection = mongoDatabase.getCollection("products");

        List<Product> products = new ArrayList<>();
        for (Document productDocument : collection.find()) {
            Product product = Product.builder()
                    .id(productDocument.getObjectId("_id").toString())
                    .name(productDocument.getString("name"))
                    .description(productDocument.getString("description"))
                    .price(BigDecimal.valueOf(productDocument.getDouble("price")))
                    .build();
            products.add(product);
        }

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}


//public class ProductService {
//
//    @Autowired
//    private CustomMongoConnectionPool connectionPool;
//
//    public void createProduct(ProductRequest productRequest) {
//        MongoClient client = null;
//        try {
//            client = connectionPool.getClient();
//            MongoDatabase database = connectionPool.getMongoDatabase();
//            MongoCollection<Document> collection = database.getCollection("products");
//
//            Document document = new Document();
//            document.append("name", productRequest.getName());
//            document.append("description", productRequest.getDescription());
//            document.append("price", productRequest.getPrice());
//
//            collection.insertOne(document);
//
//            System.out.println("Product is created.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            connectionPool.releaseClient(client);
//        }
//    }
//
//    public List<ProductResponse> getAllProducts() {
//        MongoClient client = null;
//        try {
//            client = connectionPool.getClient();
//            MongoDatabase database = connectionPool.getMongoDatabase();
//            MongoCollection<Document> collection = database.getCollection("products");
//
//            List<ProductResponse> products = new ArrayList<>();
//            for (Document document : collection.find()) {
//                ProductResponse product = new ProductResponse();
//                product.setId(document.getObjectId("_id").toString());
//                product.setName(document.getString("name"));
//                product.setDescription(document.getString("description"));
//                product.setPrice(BigDecimal.valueOf(document.getDouble("price")));
//                products.add(product);
//            }
//
//            return products;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Collections.emptyList();
//        } finally {
//            connectionPool.releaseClient(client);
//        }
//    }
//}



//public class ProductService {
//
//    private final ProductRepository productRepository;
//    private final CustomMongoConnectionPool customMongoConnectionPool;
//
//    public void createProduct(ProductRequest productRequest) {
//        MongoClient mongoClient = (MongoClient) customMongoConnectionPool.getConnection();
//        MongoDatabase database = mongoClient.getDatabase("product-service");
//        MongoCollection<Document> collection = database.getCollection("products");
//
//        Document document = new Document("name", productRequest.getName())
//                .append("description", productRequest.getDescription())
//                .append("price", productRequest.getPrice());
//
//        collection.insertOne(document);
//
//        customMongoConnectionPool.releaseConnection((Connection) mongoClient);
//        log.info("Product {} is saved", document.get("_id"));
//    }
//
//    public List<ProductResponse> getAllProducts() {
//        MongoClient mongoClient = (MongoClient) customMongoConnectionPool.getConnection();
//        MongoDatabase database = mongoClient.getDatabase("product-service");
//        MongoCollection<Document> collection = database.getCollection("products");
//
//        List<Product> products = new ArrayList<>();
//        for (Document document : collection.find()) {
//            products.add(mapToProduct(document));
//        }
//
//        customMongoConnectionPool.releaseConnection((Connection) mongoClient);
//        return products.stream().map(this::mapToProductResponse).toList();
//    }
//
//    private Product mapToProduct(Document document) {
//        return Product.builder()
//                .id(document.getObjectId("_id").toString())
//                .name(document.getString("name"))
//                .description(document.getString("description"))
//                .price(document.get("price", BigDecimal.class))
//                .build();
//    }
//
//    private ProductResponse mapToProductResponse(Product product) {
//        return ProductResponse.builder()
//                .id(product.getId())
//                .name(product.getName())
//                .description(product.getDescription())
//                .price(product.getPrice())
//                .build();
//    }
//}