package com.klaudiusz_wachowiak.storingapp.repository;

import com.klaudiusz_wachowiak.storingapp.model.ResourceModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceRepository extends CrudRepository<ResourceModel, Long> {

    boolean existsByOriginUrlAddress(String url);

    boolean existsByName(String name);

    Optional<ResourceModel> findByName(String name);

    @Query(
            "select r from ResourceModel r " +
                    "where lower(r.name) like concat('%', :phrase, '%') " +
                    "or lower(r.fileName) like concat('%', :phrase, '%')" +
                    "or lower(r.originUrlAddress) like concat('%', :phrase, '%')"
    )
    Iterable<ResourceModel> findLike(@Param(value = "phrase") String phrase);

}
