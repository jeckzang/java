package com.example.demo.processes;

import java.util.Date;
import java.util.List;

import com.example.demo.datasource.repositories.RuKuData;
import com.example.demo.datasource.repositories.RunTimeData;

public interface IProcesses {
	void processForTableA(List<RunTimeData> data);

	void processForTableBAndTableC(Date from, Date to, List<RunTimeData> data, List<RunTimeData> oldData);

	void processForTableDTableEAndTableF(List<RuKuData> data);

}
