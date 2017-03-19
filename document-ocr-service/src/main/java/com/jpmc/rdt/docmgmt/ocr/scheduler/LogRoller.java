package com.jpmc.rdt.docmgmt.ocr.scheduler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("logRoller")
public class LogRoller {
	
	private static int logCounter = 1;
	
	@Value("${service.log.path}")
	private String logPath;
	
	@Value("${service.log.name}")
	private String logName;
	
	@Value("${service.log.size.offset}")
	private long logSize;
	
	@Value("${service.log.toKeep}")
	private int logsToKeep;
	
	@Scheduled(cron = "${service.log.rollChecker.cron}")
	public void rollTheLogs() {
		File logFile = new File(logPath+File.separator+logName);
		long logFileSize = FileUtils.sizeOf(logFile);
		if(logFileSize >= logSize) {
			try {
				FileUtils.copyFile(logFile, new File(logPath + File.separator + logName + "_" + logCounter++));
				FileWriter fileWriter = new FileWriter(logFile);
				fileWriter.write("");
				fileWriter.close();
				if(logCounter > logsToKeep) {
					// Delete the older logs
					int numberOfFilesToDelete = logCounter - logsToKeep;
					for(int i = 1 ; i <= numberOfFilesToDelete ; i++) {
						FileUtils.forceDelete(new File(logPath + File.separator + logName + "_"+i));
					}
				}
				
				Collection<File> files = FileUtils.listFiles(new File(logPath), new IOFileFilter() {
					
					@Override
					public boolean accept(File dir, String name) {
						// TODO Auto-generated method stub
						return false;
					}
					
					@Override
					public boolean accept(File file) {
						return file.getName().contains(logPath+ File.separator + logName + "_");
					}
				}, null);
				
				int renameCounter = 1;
				for(File oneLogFile : files) {
					oneLogFile.renameTo(new File(logPath + File.separator + logName + "_"+renameCounter));
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
