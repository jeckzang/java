package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ITableC2Repository extends PagingAndSortingRepository<TableC2, Long> {
	Page<TableC2> findByBusinessTypeContaining(String businessType, Pageable pageable);

	List<TableC2> findByBusinessTypeContaining(String businessType);
}
