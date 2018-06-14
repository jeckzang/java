package com.example.demo.datasource.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface IRunTimeDataRepository extends CrudRepository<RunTimeData, Long> {

	List<RunTimeData> findByBookNameContainingAndTongZhiRiQiBetween(String bookname, Date from, Date to);

}
