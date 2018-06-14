package com.example.demo.datasource;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.example.demo.datasource.repositories.RuKuData;
import com.example.demo.datasource.repositories.RunTimeData;

public interface IDataSource {

	void importRunTimeData(File file);

	void importRuKuData(File file);

	void importBookAndTypeData(File file);

	List<RunTimeData> getRunTimeDataByDate(String search, Date from, Date to);

	List<RuKuData> getRuKuDataByDate(String search, Date from, Date to);
}
