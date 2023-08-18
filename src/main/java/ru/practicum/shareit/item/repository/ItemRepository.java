package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemIdValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllItemsByOwnerId(Long userId, Pageable pageable);

    @Query("select it from Item it " +
            "where it.available = true and " +
            "(lower(it.name) like lower(concat('%' , ?1, '%')) " +
            "or lower(it.description) like lower(concat('%' , ?1, '%') )) ")
    List<Item> findBySearch(String text, Pageable pageable);

     default Item getItemById(Long itemId){
        return findById(itemId).orElseThrow(() ->
                new ItemIdValidationException(String.format("Предмет с id: %s не найдена", itemId)));
    }
}
