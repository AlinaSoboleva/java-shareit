package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllItemsByOwnerId(Long userId, Sort sort);

    @Query("select it from Item it " +
            "where it.available = true and " +
            "(lower(it.name) like lower(concat('%' , ?1, '%')) " +
            "or lower(it.description) like lower(concat('%' , ?1, '%') )) ")
    List<Item> findBySearch(String text);
}
