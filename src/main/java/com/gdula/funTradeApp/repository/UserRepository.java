package com.gdula.funTradeApp.repository;

import com.gdula.funTradeApp.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    List<User> findAll();
    Boolean existsByLogin(String string);

    User findFirstByLogin(String username);
    User findFirstById(String id);

    @Query("SELECT u FROM User u WHERE CONCAT(u.login, u.name, u.surname, u.address, u.city, u.zip) LIKE %?1%")
    List<User> findAllWithKeyword(String keyword);
}
