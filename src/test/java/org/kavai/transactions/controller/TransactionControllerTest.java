package org.kavai.transactions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kavai.transactions.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final Transaction[] testTransactions = new Transaction[]{
            new Transaction(null, "type1", 12.0),
            new Transaction(1L, "type1", 10.2),
            new Transaction(null, "type2", 14.1),
    };

    @Before
    public void setUp() throws Exception {
        for (int i = 0; i < testTransactions.length; i++) {
            mvc.perform(MockMvcRequestBuilders.put("/transactionservice/transaction/" + (i + 1))
                    .content(objectMapper.writeValueAsString(testTransactions[i]))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            testTransactions[i].setId(i + 1L);
        }
    }

    @Test
    public void testGetTransactionsById() throws Exception {
        //HAPPY Path
        for (Transaction transaction : testTransactions) {
            mvc.perform(MockMvcRequestBuilders.get("/transactionservice/transaction/" + transaction.getId())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(transaction)));
        }

        //WRONG Path
        mvc.perform(MockMvcRequestBuilders.get("/transactionservice/transaction/99")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetTransactionsByType() throws Exception {
        //HAPPY Path
        mvc.perform(MockMvcRequestBuilders.get("/transactionservice/types/type1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[1,2]"));
        mvc.perform(MockMvcRequestBuilders.get("/transactionservice/types/type2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[3]"));

        //WRONG Path
        mvc.perform(MockMvcRequestBuilders.get("/transactionservice/types/type3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testSumTransactionsById() throws Exception {
        //HAPPY Path
        mvc.perform(MockMvcRequestBuilders.get("/transactionservice/sum/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{sum:22.2}"));
        mvc.perform(MockMvcRequestBuilders.get("/transactionservice/sum/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{sum:10.2}"));
        mvc.perform(MockMvcRequestBuilders.get("/transactionservice/sum/3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{sum:14.1}"));

        //WRONG Path
        mvc.perform(MockMvcRequestBuilders.get("/transactionservice/sum/99")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }
}