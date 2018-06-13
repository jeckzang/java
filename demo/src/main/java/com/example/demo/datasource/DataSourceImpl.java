package com.example.demo.datasource;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@Component
public class DataSourceImpl implements IDataSource {
	private static Logger logger = LoggerFactory.getLogger(DataSourceImpl.class);

	@Override
	public List<RunTimeData> getRunTimeDataByDate(Date from, Date to, File file) {
		try {
			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = mapper.typedSchemaFor(RunTimeData.class).withHeader();
			MappingIterator<RunTimeData> readValues = mapper.readerFor(RunTimeData.class).with(schema).readValues(file);
			return readValues.readAll().parallelStream()
					.filter(p -> from.compareTo(p.getTongZhiRiQi()) < 0 && p.getTongZhiRiQi().compareTo(to) < 0)
					.collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("Error occurred while loading object list from file", e);
			return Collections.emptyList();
		}
	}

	@Override
	public List<RuKuData> getRuKuDataByDate(Date from, Date to, File file) {
		try {
			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = mapper.typedSchemaFor(RuKuData.class).withHeader();
			MappingIterator<RuKuData> readValues = mapper.readerFor(RuKuData.class).with(schema).readValues(file);
			return readValues.readAll().parallelStream()
					.filter(p -> from.compareTo(p.getShouShuDate()) < 0 && p.getShouShuDate().compareTo(to) < 0)
					.collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("Error occurred while loading object list from file", e);
			return Collections.emptyList();
		}
	}

	@Override
	public List<BookAndTypeData> getBookAndTypeData(File file) {
		try {
			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = mapper.typedSchemaFor(BookAndTypeData.class).withHeader();
			MappingIterator<BookAndTypeData> readValues = mapper.readerFor(BookAndTypeData.class).with(schema)
					.readValues(file);
			return readValues.readAll();
		} catch (Exception e) {
			logger.error("Error occurred while loading object list from file", e);
			return Collections.emptyList();
		}
	}
}
