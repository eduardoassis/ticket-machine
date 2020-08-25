package com.ticketmachine.repositories;

import com.ticketmachine.models.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    //@Cacheable(value = "autocomplete")
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query(value = "select name from Station where lower(name) like lower(:word||'%')")
    List<String> autocomplete(@Param("word") final String word);
}