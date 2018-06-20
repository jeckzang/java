package com.example.demo.restapis;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.input.ReaderInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.datasource.IDataSource;
import com.example.demo.datasource.repositories.RuKuData;
import com.example.demo.datasource.repositories.RunTimeData;
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
	public void syncData() throws IOException {
		syncBookAndTypeDataWithFile("types.csv");
		syncRunTimeDataWithFile("shengchan.csv");
		syncRukuDataWithFile("ruku.csv");
	}

	@RequestMapping(value = "/syncData/shengchanData/{fileName:.+}", method = RequestMethod.PUT)
	public void syncRunTimeDataWithFile(@PathVariable String fileName) throws IOException {
		Resource r = storageService.loadAsResource(fileName);
		if (!r.exists()) {
			throw new StorageFileNotFoundException(fileName);
		}

		dataSource.importRunTimeData(r.getFile());
	}

	@RequestMapping(value = "/syncData/bookAndTypeData/{fileName:.+}", method = RequestMethod.PUT)
	public void syncBookAndTypeDataWithFile(@PathVariable String fileName) throws IOException {
		Resource r = storageService.loadAsResource(fileName);
		if (!r.exists()) {
			throw new StorageFileNotFoundException(fileName);
		}

		dataSource.importBookAndTypeData(r.getFile());
	}

	@RequestMapping(value = "/syncData/rukuData/{fileName:.+}", method = RequestMethod.PUT)
	public void syncRukuDataWithFile(@PathVariable String fileName) throws IOException {
		Resource r = storageService.loadAsResource(fileName);
		if (!r.exists()) {
			throw new StorageFileNotFoundException(fileName);
		}

		dataSource.importRuKuData(r.getFile());
	}

	@RequestMapping(value = "/tableas", params = { "search", "from", "to" }, method = RequestMethod.GET)
	@ResponseBody
	public List<TableA> tableas(@RequestParam("search") String search, @RequestParam("from") long fromDate,
			@RequestParam("to") long toDate) {
		Date from = fromDate != 0 ? new Date(fromDate) : null;
		Date to = toDate != 0 ? new Date(toDate) : new Date();
		List<RunTimeData> data = dataSource.getRunTimeDataByDate(search, from, to);

		LocalDateTime oldFrom = from != null
				? from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusYears(-1)
				: null;
		LocalDateTime oldTo = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusYears(-1);

		List<RunTimeData> oldData = dataSource.getRunTimeDataByDate(search,
				oldFrom == null ? null : Date.from(oldFrom.atZone(ZoneId.systemDefault()).toInstant()),
				Date.from(oldTo.atZone(ZoneId.systemDefault()).toInstant()));
		if (data != null) {
			// table A
			processes.processForTableA(data);
			// table B and C
			processes.processForTableBAndTableC(from, to, data, oldData);
		}
		return (List<TableA>) tableARepository.findAll();
	}

	@RequestMapping(value = "/tablebs")
	public @ResponseBody List<TableB> tablebs() {
		return (List<TableB>) tableBRepository.findAll();
	}

	@RequestMapping("/tablec1s")
	public @ResponseBody List<TableC1> tablec1s() {
		List<TableC1> responseData = new ArrayList<>();
		tableC1Repository.findAll().forEach(responseData::add);
		return responseData;
	}

	@RequestMapping("/tablec2s")
	public @ResponseBody List<TableC2> tablec2s() {

		List<TableC2> responseData = new ArrayList<>();
		tableC2Repository.findAll().forEach(responseData::add);
		return responseData;
	}

	@RequestMapping(value = "/tableds", params = { "search", "from", "to" }, method = RequestMethod.GET)
	public @ResponseBody List<TableD> tableds(@RequestParam("search") String search,
			@RequestParam("from") long fromDate, @RequestParam("to") long toDate) {

		Date from = fromDate != 0 ? new Date(fromDate) : null;
		Date to = toDate != 0 ? new Date(toDate) : new Date();
		List<RuKuData> ruKu = dataSource.getRuKuDataByDate(search, from, to);
		if (ruKu != null) {
			processes.processForTableDTableEAndTableF(ruKu);
		}
		return (List<TableD>) tableDRepository.findAll();
	}

	@RequestMapping(value = "/tableds")
	public @ResponseBody List<TableD> tableds() {
		return (List<TableD>) tableDRepository.findAll();
	}

	@RequestMapping(value = "/tablees")
	public @ResponseBody List<TableE> tablees() {
		return (List<TableE>) tableERepository.findAll();
	}

	@RequestMapping(value = "/tablef1s")
	public @ResponseBody List<TableF1> tablef1s() {
		return (List<TableF1>) tableF1Repository.findAll();
	}

	@RequestMapping(value = "/tablef2s")
	public @ResponseBody List<TableF2> tablef2s(@RequestParam("search") String search) {
		List<TableF2> responseData = new ArrayList<>();
		tableF2Repository.findAll().forEach(responseData::add);
		return responseData;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exports", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Resource> exprots() throws JsonProcessingException {
		Map<String, CrudRepository<?, ?>> exports = new HashMap<>();
		exports.put("生产数据原始数据明细表.csv", tableARepository);
		exports.put("按书名执行分类汇总的明细表.csv", tableBRepository);
		exports.put("按图书类型执行分类汇总的明细表.csv", tableC1Repository);
		exports.put("按图书分类执行分类汇总的明细表.csv", tableC2Repository);
		exports.put("入库数据原始数据明细表.csv", tableDRepository);
		exports.put("按书名执行分类汇总的明细表.csv", tableERepository);
		exports.put("按图书类型执行分类汇总的明细表.csv", tableF1Repository);
		exports.put("按图书分类执行分类汇总的明细表.csv", tableF2Repository);
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
