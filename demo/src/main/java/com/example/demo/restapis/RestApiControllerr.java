package com.example.demo.restapis;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.input.ReaderInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.datasource.BookAndTypeData;
import com.example.demo.datasource.IDataSource;
import com.example.demo.datasource.RuKuData;
import com.example.demo.datasource.RunTimeData;
import com.example.demo.processes.IProcesses;
import com.example.demo.repositories.ITableARepository;
import com.example.demo.repositories.ITableBRepository;
import com.example.demo.repositories.ITableC1Repository;
import com.example.demo.repositories.ITableC2Repository;
import com.example.demo.repositories.ITableDRepository;
import com.example.demo.repositories.ITableERepository;
import com.example.demo.repositories.ITableF1Repository;
import com.example.demo.repositories.ITableF2Repository;
import com.example.demo.repositories.TableA;
import com.example.demo.repositories.TableB;
import com.example.demo.repositories.TableC1;
import com.example.demo.repositories.TableC2;
import com.example.demo.repositories.TableD;
import com.example.demo.repositories.TableE;
import com.example.demo.repositories.TableF1;
import com.example.demo.repositories.TableF2;
import com.example.demo.services.StorageFileNotFoundException;
import com.example.demo.services.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

@RestController
public class RestApiControllerr {
	@Autowired
	IDataSource dataSource;
	@Autowired
	IProcesses processes;
	@Autowired
	ITableARepository tableARepository;
	@Autowired
	ITableBRepository tableBRepository;
	@Autowired
	ITableC1Repository tableC1Repository;
	@Autowired
	ITableC2Repository tableC2Repository;
	@Autowired
	ITableDRepository tableDRepository;
	@Autowired
	ITableERepository tableERepository;
	@Autowired
	ITableF1Repository tableF1Repository;
	@Autowired
	ITableF2Repository tableF2Repository;
	@Autowired
	StorageService storageService;

	@RequestMapping(value = "/syncData", method = RequestMethod.PUT)
	public void syncData(@RequestParam("from") long fromDate, @RequestParam("to") long toDate) throws IOException {
		syncBookAndTypeDataWithFile("types.csv");
		syncRunTimeDataWithFile(fromDate, toDate, "shengchan.csv");
		syncRukuDataWithFile(fromDate, toDate, "ruku.csv");
	}

	@RequestMapping(value = "/syncData/shengchanData/{fileName:.+}", method = RequestMethod.PUT)
	public void syncRunTimeDataWithFile(@RequestParam("from") long fromDate, @RequestParam("to") long toDate,
			@PathVariable String fileName) throws IOException {
		Resource r = storageService.loadAsResource(fileName);
		if (!r.exists()) {
			throw new StorageFileNotFoundException(fileName);
		}
		Date from = fromDate != 0 ? new Date(fromDate) : new Date();
		Date to = toDate != 0 ? new Date(toDate) : new Date();
		List<RunTimeData> data = dataSource.getRunTimeDataByDate(from, to, r.getFile());

		if (data != null) {
			// table A
			processes.processForTableA(data);
			// table B and C
			processes.processForTableBAndTableC(from, to, data, data);
		}
	}

	@RequestMapping(value = "/syncData/bookAndTypeData/{fileName:.+}", method = RequestMethod.PUT)
	public void syncBookAndTypeDataWithFile(@PathVariable String fileName) throws IOException {
		Resource r = storageService.loadAsResource(fileName);
		if (!r.exists()) {
			throw new StorageFileNotFoundException(fileName);
		}
		List<BookAndTypeData> data = dataSource.getBookAndTypeData(r.getFile());

		if (data != null) {
			processes.processForBookAndType(data);
		}
	}

	@RequestMapping(value = "/syncData/rukuData/{fileName:.+}", method = RequestMethod.PUT)
	public void syncRukuDataWithFile(@RequestParam("from") long fromDate, @RequestParam("to") long toDate,
			@PathVariable String fileName) throws IOException {
		Resource r = storageService.loadAsResource(fileName);
		if (!r.exists()) {
			throw new StorageFileNotFoundException(fileName);
		}
		Date from = fromDate != 0 ? new Date(fromDate) : new Date();
		Date to = toDate != 0 ? new Date(toDate) : new Date();
		List<RuKuData> ruKu = dataSource.getRuKuDataByDate(from, to, r.getFile());
		if (ruKu != null) {
			processes.processForTableDTableEAndTableF(ruKu);
		}
	}

	@RequestMapping(value = "/tableas", params = { "search", "page", "size", "sort",
			"direction" }, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<TableA> tableas(@RequestParam("search") String search, @RequestParam("page") int page,
			@RequestParam("size") int size, @RequestParam(name = "sort", defaultValue = "date") String sort,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {
		Page<TableA> resultPage = tableARepository.findByBookNameContaining(search,
				PageRequest.of(page, size, Direction.valueOf(direction), sort));
		Pagination<TableA> result = new Pagination<>();
		result.setDataList(resultPage.getContent());
		result.setTotalCounts(resultPage.getTotalElements());
		result.setTotalPages(resultPage.getTotalPages());
		return result;
	}

	@RequestMapping(value = "/tableas", params = { "search", "from", "to" }, method = RequestMethod.GET)
	@ResponseBody
	public List<TableA> tableas(@RequestParam("search") String search, @RequestParam("from") long fromDate,
			@RequestParam("to") long toDate) {
		Date from = new Date(fromDate);
		Date to = new Date(toDate);
		List<TableA> responseData = tableARepository.findByBookNameContainingAndDateBetween(search, from, to);
		return responseData;
	}

	@RequestMapping(value = "/tableas", params = { "search" }, method = RequestMethod.GET)
	@ResponseBody
	public List<TableA> tableas(@RequestParam("search") String search) {
		return tableARepository.findByBookNameContaining(search);
	}

	@RequestMapping("/tableas")
	public @ResponseBody List<TableA> tableas() {

		List<TableA> responseData = new ArrayList<>();
		tableARepository.findAll().forEach(responseData::add);
		return responseData;
	}

	@RequestMapping(value = "/tablebs", params = { "search", "page", "size", "sort",
			"direction" }, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<TableB> tablebs(@RequestParam("search") String search, @RequestParam("page") int page,
			@RequestParam("size") int size, @RequestParam(name = "sort", defaultValue = "date") String sort,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {
		Page<TableB> resultPage = tableBRepository.findByBookNameContaining(search,
				PageRequest.of(page, size, Direction.valueOf(direction), sort));
		Pagination<TableB> result = new Pagination<>();
		result.setDataList(resultPage.getContent());
		result.setTotalCounts(resultPage.getTotalElements());
		result.setTotalPages(resultPage.getTotalPages());
		return result;
	}

	@RequestMapping(value = "/tablebs", params = { "search" }, method = RequestMethod.GET)
	@ResponseBody
	public List<TableB> tablebs(@RequestParam("search") String search) {
		return tableBRepository.findByBookNameContaining(search);
	}

	@RequestMapping("/tablebs")
	public @ResponseBody List<TableB> tablebs() {

		List<TableB> responseData = new ArrayList<>();
		tableBRepository.findAll().forEach(responseData::add);
		return responseData;
	}

	@RequestMapping(value = "/tablec1s", params = { "search", "page", "size", "sort",
			"direction" }, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<TableC1> tablec1s(@RequestParam("search") String search, @RequestParam("page") int page,
			@RequestParam("size") int size, @RequestParam(name = "sort", defaultValue = "date") String sort,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {
		Page<TableC1> resultPage = tableC1Repository.findByTypeContaining(search,
				PageRequest.of(page, size, Direction.valueOf(direction), sort));
		Pagination<TableC1> result = new Pagination<>();
		result.setDataList(resultPage.getContent());
		result.setTotalCounts(resultPage.getTotalElements());
		result.setTotalPages(resultPage.getTotalPages());
		return result;
	}

	@RequestMapping("/tablec1s")
	public @ResponseBody List<TableC1> tablec1s() {

		List<TableC1> responseData = new ArrayList<>();
		tableC1Repository.findAll().forEach(responseData::add);
		return responseData;
	}

	@RequestMapping(value = "/tablec1s", params = { "search" }, method = RequestMethod.GET)
	public @ResponseBody List<TableC1> tablec1s(@RequestParam("search") String search) {
		return tableC1Repository.findByTypeContaining(search);
	}

	@RequestMapping(value = "/tablec2s", params = { "search", "page", "size", "sort",
			"direction" }, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<TableC2> tablec2s(@RequestParam("search") String search, @RequestParam("page") int page,
			@RequestParam("size") int size, @RequestParam(name = "sort", defaultValue = "date") String sort,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {
		Page<TableC2> resultPage = tableC2Repository.findByBusinessTypeContaining(search,
				PageRequest.of(page, size, Direction.valueOf(direction), sort));
		Pagination<TableC2> result = new Pagination<>();
		result.setDataList(resultPage.getContent());
		result.setTotalCounts(resultPage.getTotalElements());
		result.setTotalPages(resultPage.getTotalPages());
		return result;
	}

	@RequestMapping(value = "/tablec2s", params = { "search" }, method = RequestMethod.GET)
	public @ResponseBody List<TableC2> tablec2s(@RequestParam("search") String search) {
		return tableC2Repository.findByBusinessTypeContaining(search);
	}

	@RequestMapping("/tablec2s")
	public @ResponseBody List<TableC2> tablec2s() {

		List<TableC2> responseData = new ArrayList<>();
		tableC2Repository.findAll().forEach(responseData::add);
		return responseData;
	}

	@RequestMapping(value = "/tableds", params = { "search" }, method = RequestMethod.GET)
	public @ResponseBody List<TableD> tableds(@RequestParam("search") String search) {
		if (StringUtils.isEmpty(search)) {
			List<TableD> responseData = new ArrayList<>();
			tableDRepository.findAll().forEach(responseData::add);
			return responseData;
		} else {
			return tableDRepository.findByBookNameContaining(search);
		}
	}

	@RequestMapping(value = "/tablees", params = { "search" }, method = RequestMethod.GET)
	public @ResponseBody List<TableE> tablees(@RequestParam("search") String search) {
		if (StringUtils.isEmpty(search)) {
			List<TableE> responseData = new ArrayList<>();
			tableERepository.findAll().forEach(responseData::add);
			return responseData;
		} else {
			return tableERepository.findByBookNameContaining(search);
		}
	}

	@RequestMapping(value = "/tablef1s", params = { "search" }, method = RequestMethod.GET)
	public @ResponseBody List<TableF1> tablef1s(@RequestParam("search") String search) {
		if (StringUtils.isEmpty(search)) {
			List<TableF1> responseData = new ArrayList<>();
			tableF1Repository.findAll().forEach(responseData::add);
			return responseData;
		} else {
			return tableF1Repository.findByTypeContaining(search);
		}
	}

	@RequestMapping(value = "/tablef2s", params = { "search" }, method = RequestMethod.GET)
	public @ResponseBody List<TableF2> tablef2s(@RequestParam("search") String search) {
		if (StringUtils.isEmpty(search)) {
			List<TableF2> responseData = new ArrayList<>();
			tableF2Repository.findAll().forEach(responseData::add);
			return responseData;
		} else {
			return tableF2Repository.findByBusinessTypeContaining(search);
		}
	}

	@RequestMapping(value = "/exports", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Resource> exprots() throws JsonProcessingException {
		Map<String, CrudRepository<?, ?>> exports = new HashMap<>();
		exports.put("tablea.csv", tableARepository);
		exports.put("tableb.csv", tableBRepository);
		exports.put("tablec1.csv", tableC1Repository);
		exports.put("tablec2.csv", tableC2Repository);
		exports.put("tabled.csv", tableDRepository);
		exports.put("tablee.csv", tableERepository);
		exports.put("tablef1.csv", tableF1Repository);
		exports.put("tablef2.csv", tableF2Repository);
		CsvMapper mapper = new CsvMapper();

		storageService.createExportDir();

		exports.entrySet().forEach(e -> {
			StringReader reader;
			try {
				List<Object> list = (List<Object>) e.getValue().findAll();
				if (list.size() != 0) {
					Object d = list.get(0);
					CsvSchema schema = mapper.schemaFor(d.getClass());
					ObjectWriter writer = mapper.writer(schema);
					reader = new StringReader(writer.writeValueAsString(list));
					storageService.storeFileIntoExprotDir(e.getKey(),
							new ReaderInputStream(reader, Charset.forName("UTF-8")));
				}
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}
		});
		String fileName = storageService.zipExportDir();
		Resource file = storageService.loadAsResource(fileName);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
