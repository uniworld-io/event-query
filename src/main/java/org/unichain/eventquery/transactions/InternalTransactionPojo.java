package org.unichain.eventquery.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

public class InternalTransactionPojo {
  @Field(value = "hash")
  @JsonProperty(value = "hash")
  private String hash;
  @Field(value = "callValue")
  @JsonProperty(value = "callValue")
  private long callValue;
  private Map<String, Long> tokenInfo = new HashMap<>();

  @Field(value = "transferTo_address")
  @JsonProperty(value = "transferTo_address")
  private String transferTo_address;

  @Field(value = "data")
  @JsonProperty(value = "data")
  private String data;

  @Field(value = "caller_address")
  @JsonProperty(value = "caller_address")
  private String caller_address;

  @Field(value = "rejected")
  @JsonProperty(value = "rejected")
  private boolean rejected;

  @Field(value = "note")
  @JsonProperty(value = "note")
  private String note;

  public InternalTransactionPojo(String hash, long callValue, String transferTo_address,
                                 String data, String caller_address, boolean rejected,String note) {
    this.hash = hash;
    this.callValue = callValue;
    this.transferTo_address = transferTo_address;
    this.data = data;
    this.caller_address = caller_address;
    this.rejected = rejected;
    this.note = note;
  }
}
