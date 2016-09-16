package org.kavai.transactions.controller;

import org.kavai.transactions.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Kávai on 2016.09.16..
 */
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findByType(String type);

    List<Transaction> findByParentId(Long id);
}
