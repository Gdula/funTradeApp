package com.gdula.funTradeApp.repository;

import com.gdula.funTradeApp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    List<User> findAll();
    Boolean existsByLogin(String string);

    User findFirstByLogin(String username);
    User findFirstById(String id);
}
