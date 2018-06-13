package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ITableBRepository extends PagingAndSortingRepository<TableB, Long> {
	Page<TableB> findByBookNameContaining(String bookname, Pageable pageable);

	List<TableB> findByBookNameContaining(String bookname);

	TableB findByBookName(String bookname);
}
