cd core-layer
mvn clean install

cd ../producer-service
mvn spring-boot:build-image

cd ../consumer-service
mvn spring-boot:build-image
