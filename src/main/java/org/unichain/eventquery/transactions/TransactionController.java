package org.unichain.eventquery.transactions;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.unichain.eventquery.query.QueryFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Component
public class TransactionController {
  @Autowired(required = false)
  MongoTemplate mongoTemplate;

  @RequestMapping(method = RequestMethod.GET, value = "/transactions/total")
  public Long totalTransaction() {
    QueryFactory query = new QueryFactory();
    long number = mongoTemplate.count(query.getQuery(), TransactionTriggerEntity.class);
    return number;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/transactions")
  public JSONObject getTransactions(
      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
      @RequestParam(value = "sort", required = false, defaultValue = "-timeStamp") String sort,
      @RequestParam(value = "start", required = false, defaultValue = "0") int start,
      @RequestParam(value = "contractType", required = false, defaultValue = "") String contractType,
      @RequestParam(value = "block", required = false, defaultValue = "-1") long block
      ) {
    QueryFactory query = new QueryFactory();
    if (block > 0) {
      query.setBlockNumGte(block);
    }
    if (contractType.length() != 0) {
      query.setContractTypeEqual(contractType);
    }
    query.setPage(QueryFactory.setPagniateVariable(start, limit, sort));
    List<TransactionTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(),
        TransactionTriggerEntity.class);
    Map map = new HashMap();
    map.put("total", queryResult.size());
    map.put("data", queryResult);
    return new JSONObject(map);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/transactions/{hash}")
  public JSONObject getTransactionByHash(
      @PathVariable String hash
  ) {

    QueryFactory query = new QueryFactory();
    query.setTransactionIdEqual(hash);
    List<TransactionTriggerEntity> queryResult = mongoTemplate.find(query.getQuery(), TransactionTriggerEntity.class);
    if (queryResult.size() == 0) {
      return null;
    }
    Map map = new HashMap();
    map.put("transaction", queryResult.get(0));
    return new JSONObject(map);
  }
}
