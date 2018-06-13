package com.example.demo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ITableDRepository extends PagingAndSortingRepository<TableD, Long> {
	Page<TableD> findByBookNameContaining(String bookname, Pageable pageable);

	List<TableD> findByBookNameContaining(String bookname);

	List<TableD> findByBookNameContainingAndYcPublicDateBetween(String bookname, Date from, Date to);

	List<TableD> findByBookNameContainingAndShouShuDateBetween(String bookname, Date from, Date to);
}
