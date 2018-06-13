package com.example.demo.datasource;

import java.io.File;
import java.util.Date;
import java.util.List;

public interface IDataSource {
	List<RunTimeData> getRunTimeDataByDate(Date from, Date to, File file);

	List<RuKuData> getRuKuDataByDate(Date from, Date to, File file);

	List<BookAndTypeData> getBookAndTypeData(File file);
}
