package com.example.demo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ITableARepository extends PagingAndSortingRepository<TableA, Long> {
	Page<TableA> findByBookNameContaining(String bookname, Pageable pageable);

	List<TableA> findByBookNameContaining(String bookname);

	List<TableA> findByBookNameContainingAndDateBetween(String bookname, Date from, Date to);
}
