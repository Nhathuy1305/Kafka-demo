package com.main.statisticservice.service;

import com.main.statisticservice.entity.Statistic;
import com.main.statisticservice.repository.StatisticRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StatisticRepo statisticRepo;

    @RetryableTopic(attempts = "5", dltTopicSuffix = "-dlt",
                backoff = @Backoff(delay = 2_000, multiplier = 2))
    @KafkaListener(id = "statisticGroup", topics = "statistic")
    public void listen(Statistic statistic) {
        logger.info("Received: {}", statistic.getMessage());
//        statisticRepo.save(statistic);
        throw new RuntimeException();
    }

    @KafkaListener(id = "dltGroup", topics = "statistic.DLT")
    public void dltListen(Statistic statistic) {
        logger.info("Received statistic.DLT: {}", statistic.getMessage());
    }
}
