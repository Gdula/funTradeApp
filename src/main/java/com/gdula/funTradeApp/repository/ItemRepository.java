package com.gdula.funTradeApp.repository;

import com.gdula.funTradeApp.model.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<Item, String> {
    List<Item> findAllByIdIn(List<String> itemIds);
    List<Item> findAll();
}
