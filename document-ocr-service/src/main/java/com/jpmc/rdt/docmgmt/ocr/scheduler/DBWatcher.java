package com.jpmc.rdt.docmgmt.ocr.scheduler;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.jpmc.rdt.docmgmt.ocr.exception.OcrServiceException;
import com.jpmc.rdt.docmgmt.ocr.service.ITAFaxAcknowledgementService;
import com.jpmc.rdt.docmgmt.ta.model.FaxData;


@Component("dbWatcher")
@ConditionalOnProperty(name="application.name",	havingValue="FAX-ACKNOWLEDGE")
public class DBWatcher {

	
	private static final Logger LOGGER = Logger.getLogger(DBWatcher.class);
	
	@Resource(name="faxAcknowledgeService")
	private ITAFaxAcknowledgementService faxAckService;
	
	@Value("${faxacknowledge.dbwatcher.enable}")
	boolean enabled;
	
	@Resource
	private ThreadPoolTaskExecutor taskExecutor;
	
	@Value("${faxacknowledge.dbwatcher.threadpoolsize}")
	int threadPoolSize;
	
	@Scheduled(cron = "${faxacknowledge.dbwatcher.cron}")
	public void pollDatabase() {
		LOGGER.info("pollDatabase method");
		if(enabled){
			LOGGER.info("pollDatabase method==true");
			try {
				List<FaxData> faxDataList = faxAckService.getFaxDataInOpenState();
				LOGGER.info("Data found for processing : " + faxDataList.size());
				for(FaxData faxData : faxDataList) {
					String batchId = faxData.getBatchId();
					faxAckService.updateStatusOftheBatch(batchId, "INPROGRESS");
					LOGGER.info("Sending the batch for processing :" + batchId);
					faxAckService.processEmails(batchId);
				}
			} catch (OcrServiceException e) {
				LOGGER.error(e);
			}
		}
	}
	
}
