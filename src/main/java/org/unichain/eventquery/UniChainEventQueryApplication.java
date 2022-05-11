package org.unichain.eventquery;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

import static org.unichain.common.utils.LogConfig.LOG;
import static org.unichain.eventquery.contractevents.ContractEventController.isRunRePushThread;


@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ServletComponentScan("org.unichain.eventquery.filter")
@PropertySource(value = {"file:./config.conf"}, ignoreResourceNotFound = true)
public class UniChainEventQueryApplication {
    public static void main(String[] args) {
        Optional<String> version = Optional.ofNullable(UniChainEventQueryApplication.class.getPackage().getImplementationVersion());
        String versionString = version.orElse("");
        LOG.info("UniChainEventQueryApplication {} starting...", versionString);
        SpringApplication.run(UniChainEventQueryApplication.class, args);
        LOG.info("UniChainEventQueryApplication {} started.", versionString);
        shutdown();
    }

    @Bean
    public MongoTemplate mongoTemplate(@Autowired MongoClient mongoClient, @Value("${mongo.dbname}") String mongodbDbName) {
        return new MongoTemplate(mongoClient, mongodbDbName);
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "|{}[]"));
        return factory;
    }

    @Bean
    @SuppressWarnings("all")
    public Map<String, Long> health(@Autowired MongoClient mongoClient, @Value("${mongo.dbname}") String mongodbDbName) {
        Map<String, Long> collectionMap = new HashMap();
        try {
            MongoDatabase db = mongoClient.getDatabase(mongodbDbName);
            MongoIterable<String> allCollections = db.listCollectionNames();
            for (String collection : allCollections) {
                MongoCollection mongoCollection = db.getCollection(collection);
                long count = mongoCollection.count();
                collectionMap.put(collection, count);
                LOG.info("loaded collection: {}, count document: {}", collection, count);
            }
        } catch (Exception e) {
            LOG.warn("{} -> {}", e.getMessage(), e.getStackTrace());
        }
        return collectionMap;
    }

    @Bean
    public MongoClient mongoClient(@Value("${mongo.host}") String host,
                                   @Value("${mongo.port}") int port,
                                   @Value("${mongo.dbname}") String dbName,
                                   @Value("${mongo.username}") String username,
                                   @Value("${mongo.password}") String password,
                                   @Value("${mongo.connectionsPerHost}") int connectionsPerHost,
                                   @Value("${mongo.threadsAllowedToBlockForConnectionMultiplier}") int threadsAllowedToBlockForConnectionMultiplier) {
        MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(connectionsPerHost)
                .threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier)
                .build();
        ServerAddress serverAddress = new ServerAddress(host, port);
        List<ServerAddress> addressList = new ArrayList<>();
        addressList.add(serverAddress);
        MongoCredential credential = MongoCredential.createScramSha1Credential(username, dbName, password.toCharArray());
        return new MongoClient(addressList, credential, options);
    }

  public static void shutdown() {
    Runnable stopThread = () -> isRunRePushThread.set(false);
    LOG.info("********register application shutdown hook********");
    Runtime.getRuntime().addShutdownHook(new Thread(stopThread));
  }
}
