package com.example.demo.service;

import com.example.demo.model.MessageDTO;
import com.example.demo.model.StatisticDTO;
import com.example.demo.repository.MessageRepo;
import com.example.demo.repository.StatisticRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class PollingService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    MessageRepo messageRepo;

    @Autowired
    StatisticRepo statisticRepo;

    @Scheduled(fixedDelay = 1000)
    public void producer() {
        List<MessageDTO> messageDTOS = messageRepo.findByStatus(false);

        for (MessageDTO messageDTO : messageDTOS) {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("notification", messageDTO);
            future.thenAccept(result -> {
                logger.error("SUCCESS");

                messageDTO.setStatus(true); // success
                messageRepo.save(messageDTO);
                    })
                    .exceptionally(ex -> {
                        logger.error("FAIL ", ex);
                        return null;
                    });
        }

        List<StatisticDTO> statisticDTOS = statisticRepo.findByStatus(false);

        for (StatisticDTO statisticDTO : statisticDTOS) {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("statistic", statisticDTO);
            future.thenAccept(result -> {
                logger.error("SUCCESS");

                statisticDTO.setStatus(true); // success
                statisticRepo.save(statisticDTO);
                    })
                    .exceptionally(ex -> {
                        logger.error("FAIL ", ex);
                        return null;
                    });
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void delete() {
        List<MessageDTO> messageDTOS = messageRepo.findByStatus(true);
        messageRepo.deleteAllInBatch(messageDTOS);

        List<StatisticDTO> statisticDTOS = statisticRepo.findByStatus(true);
        statisticRepo.deleteAllInBatch(statisticDTOS);
    }
}
