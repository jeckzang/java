package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ITableF2Repository extends PagingAndSortingRepository<TableF2, Long> {
	Page<TableF2> findByBusinessTypeContaining(String businessType, Pageable pageable);

	List<TableF2> findByBusinessTypeContaining(String businessType);
}
