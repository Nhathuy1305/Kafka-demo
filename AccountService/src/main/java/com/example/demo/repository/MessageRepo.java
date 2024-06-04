package com.example.demo.repository;

import com.example.demo.model.MessageDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<MessageDTO, Integer> {

    List<MessageDTO> findByStatus(boolean status);

}
