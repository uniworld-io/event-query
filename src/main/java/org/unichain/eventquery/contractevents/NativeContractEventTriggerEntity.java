package org.unichain.eventquery.contractevents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@AllArgsConstructor
@Document(collection = "nativeevent")
@Getter
@Setter
public class NativeContractEventTriggerEntity implements Serializable {

  private static final long serialVersionUID = -70777625567836430L;
  @Id
  private String id;

  @Field(value = "timeStamp")
  @JsonProperty(value = "timeStamp")
  private long timeStamp;

  @Field(value = "blockHash")
  @JsonProperty(value = "blockHash")
  private String blockHash;

  @Field(value = "blockNumber")
  @JsonProperty(value = "blockNumber")
  private long blockNumber;

  @Field(value = "latestSolidifiedBlockNumber")
  @JsonProperty(value = "latestSolidifiedBlockNumber")
  private long latestSolidifiedBlockNumber;

  @Field(value = "transactionId")
  @JsonProperty(value = "transactionId")
  private String transactionId;

  @Field(value = "index")
  @JsonProperty(value = "index")
  private long index;

  @Field(value = "contractType")
  @JsonProperty(value = "contractType")
  private String contractType;

  @Field(value = "topic")
  @JsonProperty(value = "topic")
  private String topic;

  @Field(value = "signature")
  @JsonProperty(value = "signature")
  private String signature;

  @Field(value = "rawData")
  @JsonProperty(value = "rawData")
  private String rawData;

  public NativeContractEventTriggerEntity(){

  }
}