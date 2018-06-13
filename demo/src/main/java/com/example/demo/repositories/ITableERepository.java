package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ITableERepository extends PagingAndSortingRepository<TableE, Long> {
	Page<TableE> findByBookNameContaining(String bookname, Pageable pageable);

	List<TableE> findByBookNameContaining(String bookname);
}
