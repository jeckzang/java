package com.example.demo.processes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.datasource.BookAndTypeData;
import com.example.demo.datasource.RuKuData;
import com.example.demo.datasource.RunTimeData;
import com.example.demo.repositories.IBusinessTypeRepository;
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
import com.example.demo.repositories.TableBusinessType;
import com.example.demo.repositories.TableC1;
import com.example.demo.repositories.TableC2;
import com.example.demo.repositories.TableD;
import com.example.demo.repositories.TableE;
import com.example.demo.repositories.TableF1;
import com.example.demo.repositories.TableF2;

@Component
public class ProcessesImpl implements IProcesses {

	@Autowired
	ITableARepository tableARepository;
	@Autowired
	ITableBRepository tableBRepository;
	@Autowired
	ITableC1Repository tableC1Repository;
	@Autowired
	ITableC2Repository tableC2Repository;
	@Autowired
	IBusinessTypeRepository businessTypeRepository;
	@Autowired
	ITableDRepository tableDRepository;
	@Autowired
	ITableERepository tableERepository;
	@Autowired
	ITableF1Repository tableF1Repository;
	@Autowired
	ITableF2Repository tableF2Repository;

	@Override
	public void processForTableA(List<RunTimeData> data) {
		if (data != null) {
			// clean
			tableARepository.deleteAll();

			List<TableA> aList = data.stream().parallel().map(m -> {
				TableA a = new TableA();
				a.setBookName(m.getBookName());
				a.setMaYang(m.getZongMaYang());
				a.setPrice(m.getPrice());
				a.setDate(m.getTongZhiRiQi());
				// no count attribute
				return a;
			}).collect(Collectors.toList());
			tableARepository.saveAll(aList);
		}
	}

	@Override
	public void processForTableBAndTableC(Date from, Date to, List<RunTimeData> data, List<RunTimeData> oldData) {
		// new data
		if (data != null && data.size() != 0) {

			// clean
			tableBRepository.deleteAll();
			tableC1Repository.deleteAll();
			tableC2Repository.deleteAll();

			Map<String, TableBusinessType> allBusinessTypeMap = new HashMap<>();
			businessTypeRepository.findAll().forEach(b -> allBusinessTypeMap.put(b.getName(), b));

			Map<String, TableB> tableBOld = new TreeMap<>();
			Map<String, TableC1> tableC1Old = new TreeMap<>();
			Map<String, TableC2> tableC2Old = new TreeMap<>();
			oldData.stream().sorted((b1, b2) -> b1.getTongZhiRiQi().compareTo(b2.getTongZhiRiQi())).forEach(m -> {
				processTableB(to, tableBOld, m, allBusinessTypeMap);
				processTableC1(to, tableC1Old, m, allBusinessTypeMap);
				processTableC2(to, tableC2Old, m, allBusinessTypeMap);
			});
			// name, TableB
			Map<String, TableB> tableB = new TreeMap<>();
			Map<String, TableC1> tableC1 = new TreeMap<>();
			Map<String, TableC2> tableC2 = new TreeMap<>();
			data.stream().sorted((b1, b2) -> b1.getTongZhiRiQi().compareTo(b2.getTongZhiRiQi())).forEach(m -> {
				processTableB(to, tableB, m, allBusinessTypeMap);
				processTableC1(to, tableC1, m, allBusinessTypeMap);
				processTableC2(to, tableC2, m, allBusinessTypeMap);
			});

			// tableB increase
			tableB.values().stream().forEach(b -> {
				if (tableBOld.get(b.getBookName()) != null) {
					float currentCount = b.getCount();
					float increase = 0;
					if (tableBOld.get(b.getBookName()).getCount() == 0) {
						b.setIncrease("no old data");
					} else {
						increase = (currentCount / tableBOld.get(b.getBookName()).getCount() - 1) * 100;
					}
					b.setIncrease(increase + "");
				}
			});

			// tableC proportion
			tableC1.values().stream().forEach(c -> {
				float count = c.getCount();
				c.setProportion((count / data.size() * 100) + "");
			});
			tableC2.values().stream().forEach(c -> {
				float count = c.getCount();
				c.setProportion((count / data.size() * 100) + "");
			});

			// tableC increase
			tableC1.values().stream().forEach(c -> {
				if (tableC1Old.get(c.getType()) != null) {
					float count = c.getCount();
					float increase = 0;
					if (tableC1Old.get(c.getType()).getCount() == 0) {
						c.setIncrease("no old data");
					} else {
						increase = (count / tableC1Old.get(c.getType()).getCount() - 1) * 100;
					}
					c.setProportion(increase + "");
				}
			});
			tableC2.values().stream().forEach(c -> {
				if (tableC2Old.get(c.getBusinessType()) != null) {
					float count = c.getCount();
					float increase = 0;
					if (tableC2Old.get(c.getBusinessType()).getCount() == 0) {
						c.setIncrease("no old data");
					} else {
						increase = (count / tableC2Old.get(c.getBusinessType()).getCount() - 1) * 100;
					}
					c.setProportion(increase + "");
				}
			});

			tableBRepository.saveAll(tableB.values());
			tableC1Repository.saveAll(tableC1.values());
			tableC2Repository.saveAll(tableC2.values());
		}
	}

	@Override
	public void processForTableDTableEAndTableF(List<RuKuData> data) {
		if (data != null) {

			// clean
			tableDRepository.deleteAll();
			tableERepository.deleteAll();
			tableF1Repository.deleteAll();
			tableF2Repository.deleteAll();

			Map<String, TableBusinessType> allBusinessTypeMap = new HashMap<>();
			businessTypeRepository.findAll().forEach(b -> allBusinessTypeMap.put(b.getName(), b));

			Map<String, TableF1> f1Map = new HashMap<>();
			Map<String, Set<String>> f1PinZhongMap = new HashMap<>();
			Map<String, TableF2> f2Map = new HashMap<>();
			Map<String, Set<String>> f2PinZhongMap = new HashMap<>();
			data.stream().forEach(m -> {
				// TableD
				TableD d = new TableD();
				d.setYcPublicDate(m.getYcPublicDate());
				d.setShouShuDate(m.getShouShuDate());
				if (d.getShouShuDate() != null && d.getYcPublicDate() != null) {
					LocalDateTime shouShu = d.getShouShuDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDateTime();
					LocalDateTime YcPublic = d.getYcPublicDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDateTime();
					d.setShengChanZhouQi(ChronoUnit.DAYS.between(shouShu, YcPublic));
				}

				d.setBookName(m.getBookName());
				d.setPrice(m.getPrice());

				d.setCount(m.getCount());
				d.setMaYang(m.getMaYang());

				// type and business type
				TableBusinessType types = allBusinessTypeMap.get(m.getBookName());
				if (types != null) {
					d.setBusinessType(types.getBusinessType());
					d.setType(types.getType());
				}

				tableDRepository.save(d);

				// TableE
				TableE e = new TableE();
				e.setBookName(m.getBookName());
				e.setPrice(m.getPrice());
				e.setCount(m.getCount());
				e.setMaYang(m.getMaYang());
				// type and business type
				if (types != null) {
					e.setBusinessType(types.getBusinessType());
					e.setType(types.getType());
				}
				tableERepository.save(e);

				// TableF
				String type = "";
				if (types != null) {
					type = types.getType();
				}
				TableF1 f1 = f1Map.get(type);
				Set<String> bNameSet1 = f1PinZhongMap.get(type);
				if (f1 == null) {
					f1 = new TableF1();
					// for test
					f1.setType(type);
					// for test
					f1Map.put(type, f1);

					bNameSet1 = new HashSet<>();
					f1PinZhongMap.put(type, bNameSet1);
				}
				bNameSet1.add(m.getBookName());
				f1.setPinZhongCount(bNameSet1.size());
				f1.setCount(f1.getCount() + m.getCount());
				f1.setMaYang(f1.getMaYang() + m.getMaYang());

				String businessType = "";
				if (types != null) {
					businessType = types.getBusinessType();
				}
				TableF2 f2 = f2Map.get(businessType);
				Set<String> bNameSet2 = f2PinZhongMap.get(businessType);
				if (f2 == null) {
					f2 = new TableF2();
					// for test
					f2.setBusinessType(businessType);
					// for test
					f2Map.put(businessType, f2);

					bNameSet2 = new HashSet<>();
					f2PinZhongMap.put(businessType, bNameSet2);
				}
				bNameSet2.add(m.getBookName());
				f2.setPinZhongCount(bNameSet2.size());
				f2.setCount(f2.getCount() + m.getCount());
				f2.setMaYang(f2.getMaYang() + m.getMaYang());

			});

			f1Map.values().forEach(f1 -> tableF1Repository.save(f1));
			f2Map.values().forEach(f2 -> tableF2Repository.save(f2));

		}

	}

	private void processTableB(Date baseTime, Map<String, TableB> tableB, RunTimeData m,
			Map<String, TableBusinessType> allBusinessTypeMap) {
		TableB b = tableB.get(m.getBookName());
		if (b == null) {
			b = new TableB();
			tableB.put(m.getBookName(), b);
		}
		b.setBookName(m.getBookName());
		long diff = baseTime.getTime() - m.getTongZhiRiQi().getTime();
		long diffInDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		// 90 days
		if (diffInDays <= 90) {
			b.setCountFor90(b.getCountFor90() + 1);
		}
		if (diffInDays <= 180) {
			b.setCountFor180(b.getCountFor180() + 1);
		}
		if (diffInDays <= 360) {
			b.setCountFor360(b.getCountFor360() + 1);
		}

		TableBusinessType type = allBusinessTypeMap.get(b.getBookName());
		if (type != null) {
			b.setBusinessType(type.getBusinessType());
			b.setType(type.getType());
		}
		// sum (ma yang)
		b.setMaYang(b.getMaYang() + m.getZongMaYang());
		// sum (count)
		b.setCount(b.getCount() + 1);
	}

	private void processTableC1(Date baseTime, Map<String, TableC1> tableC1, RunTimeData m,
			Map<String, TableBusinessType> allBusinessTypeMap) {
		String type = "";
		// search Type
		if (allBusinessTypeMap.get(m.getBookName()) != null) {
			type = allBusinessTypeMap.get(m.getBookName()).getType();
		}

		TableC1 c = tableC1.get(type);
		if (c == null) {
			c = new TableC1();
			c.setType(type);
			tableC1.put(type, c);
		}

		long diff = baseTime.getTime() - m.getTongZhiRiQi().getTime();
		long diffInDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		// 90 days
		if (diffInDays <= 180) {
			c.setCountFor180(c.getCountFor180() + 1);
		}
		if (diffInDays <= 360) {
			c.setCountFor360(c.getCountFor360() + 1);
		}

		// sum (ma yang)
		c.setMaYang(c.getMaYang() + m.getZongMaYang());
		// sum (count)
		c.setCount(c.getCount() + 1);
	}

	private void processTableC2(Date baseTime, Map<String, TableC2> tableC2, RunTimeData m,
			Map<String, TableBusinessType> allBusinessTypeMap) {
		String businessType = "";
		// search Type
		if (allBusinessTypeMap.get(m.getBookName()) != null) {
			businessType = allBusinessTypeMap.get(m.getBookName()).getBusinessType();
		}
		TableC2 c = tableC2.get(businessType);
		if (c == null) {
			c = new TableC2();

			c.setBusinessType(businessType);
			tableC2.put(businessType, c);
		}
		long diff = baseTime.getTime() - m.getTongZhiRiQi().getTime();
		long diffInDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		// 90 days
		if (diffInDays <= 180) {
			c.setCountFor180(c.getCountFor180() + 1);
		}
		if (diffInDays <= 360) {
			c.setCountFor360(c.getCountFor360() + 1);
		}

		// sum (ma yang)
		c.setMaYang(c.getMaYang() + m.getZongMaYang());
		// sum (count)
		c.setCount(c.getCount() + 1);
	}

	@Override
	public void processForBookAndType(List<BookAndTypeData> data) {
		if (data != null) {
			// clean
			businessTypeRepository.deleteAll();

			data.stream().parallel().forEach(m -> {
				// save bookandtype
				TableBusinessType t = new TableBusinessType();
				t.setName(m.getBookName());
				t.setBusinessType(m.getBusinessType());
				t.setType(m.getType());
				businessTypeRepository.save(t);
			});
		}
	}
}
