package com.example.demo.repository;

import com.example.demo.model.AccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<AccountDTO, Integer> {

}
