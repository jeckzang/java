package com.example.demo.datasource;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.datasource.repositories.BookAndTypeData;
import com.example.demo.datasource.repositories.IBookAndTypeDataRepository;
import com.example.demo.datasource.repositories.IRuKuDataRepository;
import com.example.demo.datasource.repositories.IRunTimeDataRepository;
import com.example.demo.datasource.repositories.RuKuData;
import com.example.demo.datasource.repositories.RunTimeData;
import com.example.demo.repositories.IBusinessTypeRepository;
import com.example.demo.repositories.TableBusinessType;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@Component
public class DataSourceImpl implements IDataSource {
	private static Logger logger = LoggerFactory.getLogger(DataSourceImpl.class);
	@Autowired
	IRunTimeDataRepository runTimeDataRepository;
	@Autowired
	IRuKuDataRepository ruKuDataRepository;
	@Autowired
	IBookAndTypeDataRepository bookAndTypeDataRepository;
	@Autowired
	IBusinessTypeRepository businessTypeRepository;

	@Override
	public void importBookAndTypeData(File file) {
		try {
			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = mapper.typedSchemaFor(BookAndTypeData.class).withHeader();
			MappingIterator<BookAndTypeData> readValues = mapper.readerFor(BookAndTypeData.class).with(schema)
					.readValues(file);
			List<BookAndTypeData> data = readValues.readAll();
			data.stream().parallel().forEach(m -> {
				// save bookandtype
				TableBusinessType t = new TableBusinessType();
				t.setName(m.getBookName());
				t.setBusinessType(m.getBusinessType());
				t.setType(m.getType());
				businessTypeRepository.save(t);
			});
		} catch (Exception e) {
			logger.error("Error occurred while loading object list from file", e);
		}
	}

	@Override
	public void importRuKuData(File file) {
		try {
			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = mapper.typedSchemaFor(RuKuData.class).withHeader();
			MappingIterator<RuKuData> readValues = mapper.readerFor(RuKuData.class).with(schema).readValues(file);
			ruKuDataRepository.saveAll(readValues.readAll());
		} catch (Exception e) {
			logger.error("Error occurred while loading object list from file", e);
		}

	}

	@Override
	public void importRunTimeData(File file) {
		try {
			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = mapper.typedSchemaFor(RunTimeData.class).withHeader();
			MappingIterator<RunTimeData> readValues = mapper.readerFor(RunTimeData.class).with(schema).readValues(file);
			runTimeDataRepository.saveAll(readValues.readAll());
		} catch (Exception e) {
			logger.error("Error occurred while loading object list from file", e);
		}
	}

	@Override
	public List<RunTimeData> getRunTimeDataByDate(String search, Date from, Date to) {
		return runTimeDataRepository.findByBookNameContainingAndTongZhiRiQiBetween(search, from, to);
	}

	@Override
	public List<RuKuData> getRuKuDataByDate(String search, Date from, Date to) {
		return ruKuDataRepository.findByBookNameContainingAndShouShuDateBetween(search, from, to);
	}
}
