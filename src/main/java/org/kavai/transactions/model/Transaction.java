package org.kavai.transactions.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Created by Kávai on 2016.09.16..
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {
    @Id
    private Long id;
    @JsonProperty("parent_id")
    private Long parentId;
    @NotNull
    private String type;
    @NotNull
    private Double amount;

    protected Transaction() {
    }

    public Transaction(Long parentId, String type, Double amount) {
        this.parentId = parentId;
        this.type = type;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
