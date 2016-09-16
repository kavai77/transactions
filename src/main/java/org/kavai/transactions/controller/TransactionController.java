package org.kavai.transactions.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.kavai.transactions.exception.IllegalTransactionIdException;
import org.kavai.transactions.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/transactionservice")
public class TransactionController {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private ObjectMapper mapper;
    
    @GetMapping(path = "/transaction/{id}", produces = APPLICATION_JSON_VALUE)
    public Transaction getTransactionById(@PathVariable long id) {
        Transaction transaction = repository.findOne(id);
        if (transaction == null) {
            throw new IllegalTransactionIdException();
        }
        return transaction;
    }

    @PutMapping(path = "/transaction/{id}", consumes = APPLICATION_JSON_VALUE)
    public void storeTransactionById(@PathVariable long id, @RequestBody @Valid Transaction transaction) {
        transaction.setId(id);
        repository.save(transaction);
    }

    @GetMapping(path = "/types/{type}", produces = APPLICATION_JSON_VALUE)
    public List<Long> getTransactionsByTyoe(@PathVariable String type) {
        return repository.findByType(type).stream().map(Transaction::getId).collect(toList());
    }

    @GetMapping(path = "/sum/{id}", produces = APPLICATION_JSON_VALUE)
    public JsonNode getSumById(@PathVariable long id) {
        if (!repository.exists(id)) {
            throw new IllegalTransactionIdException();
        }
        double sum = 0.0;
        Queue<Long> childrenIds = new LinkedList<>();
        Set<Long> processedIds = new HashSet<>();
        childrenIds.add(id);


        while (!childrenIds.isEmpty()) {
            long childId = childrenIds.poll();
            if (processedIds.contains(childId)) { //simple cyclic dependency handling to avoid infinite loop
                continue;
            }
            repository.findByParentId(childId).stream().map(Transaction::getId).forEach(childrenIds::add);
            processedIds.add(childId);
            sum += repository.findOne(childId).getAmount();
        }

        ObjectNode node = mapper.createObjectNode();
        node.put("sum", sum);
        return node;
    }
}
