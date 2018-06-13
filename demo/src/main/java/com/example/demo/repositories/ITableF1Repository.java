package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ITableF1Repository extends PagingAndSortingRepository<TableF1, Long> {
	Page<TableF1> findByTypeContaining(String type, Pageable pageable);

	List<TableF1> findByTypeContaining(String type);
}
