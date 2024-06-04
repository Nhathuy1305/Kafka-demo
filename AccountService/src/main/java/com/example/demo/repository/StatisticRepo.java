package com.example.demo.repository;

import com.example.demo.model.StatisticDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatisticRepo extends JpaRepository<StatisticDTO, Integer> {

    List<StatisticDTO> findByStatus(boolean status);

}
