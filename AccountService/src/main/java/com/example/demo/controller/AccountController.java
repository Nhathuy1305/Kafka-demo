package com.example.demo.controller;

import com.example.demo.model.AccountDTO;
import com.example.demo.model.MessageDTO;
import com.example.demo.model.StatisticDTO;
import com.example.demo.repository.AccountRepo;
import com.example.demo.repository.MessageRepo;
import com.example.demo.repository.StatisticRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    MessageRepo messageRepo;

    @Autowired
    StatisticRepo statisticRepo;

    @PostMapping("/new")
    public AccountDTO create(@RequestBody AccountDTO account) {
        StatisticDTO stat = new StatisticDTO("Account " + account.getEmail() + " is created", new Date());
        stat.setStatus(false);

        // send notification
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTo(account.getEmail());
        messageDTO.setToName(account.getName());
        messageDTO.setSubject("Welcome to Kafka");
        messageDTO.setContent("Kafka Is a Distributed Data Streaming Technology Leveraged by Over 70% of Fortune 500.");
        messageDTO.setStatus(false);

        accountRepo.save(account);
        messageRepo.save(messageDTO);
        statisticRepo.save(stat);

//        for (int i = 0; i < 100; i++) {
//            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("notification", messageDTO);
//            future.thenAccept(result -> System.out.println(result.getRecordMetadata().partition()))
//                    .exceptionally(ex -> { ex.printStackTrace(); return null; });
//        }
//
//        kafkaTemplate.send("statistic", stat);

        return account;
    }
}
