package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ITableC1Repository extends PagingAndSortingRepository<TableC1, Long> {
	Page<TableC1> findByTypeContaining(String type, Pageable pageable);

	List<TableC1> findByTypeContaining(String type);
}
