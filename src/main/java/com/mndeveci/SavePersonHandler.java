package com.mndeveci;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3Client;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class SavePersonHandler implements RequestHandler<PersonRequest, PersonResponse> {

  private static String DYNAMODB_TABLE_NAME = "Person";
  private static String S3_BUCKET_NAME = "person-events";
  private static Regions REGION = Regions.US_WEST_2;

  private DynamoDB dynamoDb;
  private AmazonS3Client s3Client;

  @Override
  public PersonResponse handleRequest(PersonRequest input, Context context) {
    initClients();
    persistData(input);

    PersonResponse personResponse = new PersonResponse();
    personResponse.setMessage("Saved successfully!");

    PersonRequestResponseWrapper requestResponseWrapper = new PersonRequestResponseWrapper(input, personResponse);
    persistEventToS3(requestResponseWrapper);

    return personResponse;
  }

  private void persistEventToS3(PersonRequestResponseWrapper requestResponseWrapper) {
    try {
      File s3File = Files.createFile(Paths.get("/tmp/tmp." + UUID.randomUUID().toString() + ".json")).toFile();
      s3File.deleteOnExit();
      new ObjectMapper().writeValue(s3File, requestResponseWrapper);
      s3Client.putObject(S3_BUCKET_NAME, UUID.randomUUID().toString() + ".json", s3File);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private PutItemOutcome persistData(PersonRequest personRequest) {
    return this.dynamoDb
        .getTable(DYNAMODB_TABLE_NAME)
        .putItem(new PutItemSpec()
            .withItem(new Item()
                .withLong("id", personRequest.getId())
                .withString("firstName", personRequest.getFirstName())
                .withString("lastName", personRequest.getLastName())));
  }

  private void initClients() {
    AmazonDynamoDBClient client = new AmazonDynamoDBClient();
    client.setRegion(Region.getRegion(REGION));
    this.dynamoDb = new DynamoDB(client);

    this.s3Client = new AmazonS3Client();
  }


}
