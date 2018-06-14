package com.example.demo.datasource.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface IRuKuDataRepository extends CrudRepository<RuKuData, Long> {

	List<RuKuData> findByBookNameContainingAndShouShuDateBetween(String bookname, Date from, Date to);

}
