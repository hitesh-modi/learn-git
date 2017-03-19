package com.jpmc.rdt.docmgmt.ta.util;

import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.xml.bind.DatatypeConverter;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import com.jpmc.rdt.docmgmt.ocr.exception.OcrServiceException;
import com.jpmc.rdt.docmgmt.ta.model.FaxFileDetails;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.TIFFEncodeParam;
import com.sun.media.jai.codec.TIFFField;

@Component("faxInstructionScanOperation")
public class FaxInstructionScanOperation {

	private static final Logger LOGGER = Logger.getLogger(FaxInstructionScanOperation.class);
	// public static void main(String args[]) throws Exception
	// {
	// if(args.length<2)
	// {
	// System.out.println("The arguments passed were incorrect. Below is the
	// usage");
	//
	// try
	// {
	// System.in.read();
	// }
	// catch(Exception excp)
	// {
	// //do nothing
	// }
	// return;
	// }
	// try
	// {
	// //parse config JPMCLuxDealFIAutomatedScanConfig.xml file
	// JPMCLuxDealingFaxInstructionScanProcessConfigXmlParser.parserConfigXML(args[0]);
	//
	// String desktopId=args[1];
	//
	// LOGGER.info("@Java-Class:JPMCLuxDealingFaxInstructionScanOperation
	// ,Method:main, @Message : Application Started...");
	//
	// FaxInstructionScanOperation faxInstructionScanOperation = new
	// FaxInstructionScanOperation();
	//
	// String
	// faxInstructionSharedPath=JPMCLuxDealingFaxInstructionScanProcessConfigXmlParser.htDesktopConfigs.get(desktopId+JPMCLuxDealingFaxInstructionProcessConstants.DELIMITER+"faxInstructionFolderSharedPath");
	// if(faxInstructionSharedPath !=null)
	// {
	// File file = new File(faxInstructionSharedPath);
	//
	// if(file !=null && file.isDirectory()){
	//
	// if(file.list().length>0){
	//
	//
	// File[] faxInstructionList=file.listFiles();
	// if(faxInstructionList !=null)
	// {
	//
	// for(int i=0; i<faxInstructionList.length;i++)
	// {
	//
	//
	// if(faxInstructionList[i] !=null && !faxInstructionList[i].isDirectory())
	// {
	// String faxFileName=faxInstructionList[i].getName();
	// try
	// {
	// String fileExtension=faxFileName.substring(faxFileName.lastIndexOf(".") +
	// 1);
	//
	//
	// if(fileExtension !=null && (fileExtension.equalsIgnoreCase("tif") ||
	// fileExtension.equalsIgnoreCase("tiff")))
	// {
	// if(faxInstructionList[i].renameTo(faxInstructionList[i])){
	//
	// faxInstructionScanOperation.uploadDocument(desktopId,faxInstructionList[i],faxInstructionSharedPath);
	// break;
	// }
	// else
	// {
	// LOGGER.info("@Java-Class:JPMCLuxDealingFaxInstructionScanOperation
	// ,Method:main, @Message :Fax Instruction file :: "+faxFileName+" on shared
	// path :: "+faxInstructionSharedPath+" is locked/open So can not process
	// for batch creation please close/unlock the file to process further.");
	// break;
	// }
	//
	// }
	//
	// }
	// catch(Exception e)
	// {
	// LOGGER.error("@Java-Class:JPMCLuxDealingFaxInstructionScanOperation
	// ,Method:main, error to get Fax instruction file
	// extension--"+e.getMessage(), e);
	// }
	// }
	// }
	// }
	// else
	// {
	// LOGGER.info("@Java-Class:JPMCLuxDealingFaxInstructionScanOperation
	// ,Method:main, @Message : File does not exists in the fax instruction
	// shared drive for process.");
	//
	// }
	// }else{
	//
	// LOGGER.info("@Java-Class:JPMCLuxDealingFaxInstructionScanOperation
	// ,Method:main, @Message : File does not exists in the fax instruction
	// shared drive for process.");
	//
	// }
	//
	// }else{
	//
	// LOGGER.info("@Java-Class:JPMCLuxDealingFaxInstructionScanOperation
	// ,Method:main, @Message : Invalid Directory.");
	//
	// }
	//
	// }
	// else
	// {
	// throw new Exception("The K drive path is not found in the config xml
	// file..Please reconfigure the K drive shared path..");
	// }
	// }
	// catch(Exception e)
	// {
	//
	// LOGGER.error("@Java-Class:JPMCLuxDealingFaxInstructionScanOperation
	// ,Method:main,"+e.getMessage(), e);
	// }
	//
	// LOGGER.info("@Java-Class:JPMCLuxDealingFaxInstructionScanOperation
	// ,Method:main, @Message : Application Ended...");
	// }
	//

	// private IFileManager fileManager;

	@Value("${lux.spooler.docIdUrl.key}")
	private String docKeyGeneratorKey;

	@Value("${lux.spooler.docIdUrl.initVector}")
	private String docKeyGeneratorInitVector;

	@Resource(name = "faxAckPasswordDecryptor")
	private FaxAcknowledgeEncryption decryptor;

	public FaxFileDetails uploadDocumentLux(String url, String applicationName, File faxInstruction, String job,
			String faxTempPath, String docID, String clientType, String contentType, String processedFolderPath,
			long fileId, String timeZone) throws OcrServiceException, IOException {

		FaxFileDetails faxFileDetails = new FaxFileDetails();
		LOGGER.info(
				"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:uploadDocument, @Message : Start of method ,Implementation:: "
						+ applicationName);

		FileSeekableStream ss = null;
		
		try {

			// String url =
			// JPMCLuxDealingFaxInstructionScanProcessConfigXmlParser.htwTMConnection.get("wTMURL");
			// String application =
			// JPMCLuxDealingFaxInstructionScanProcessConfigXmlParser.htwTMConnection.get("applicationName");
			// String job =
			// JPMCLuxDealingFaxInstructionScanProcessConfigXmlParser.htwTMConnection.get("job");
			String response = null;
			String queueId = null;
			String batchId = null;
			File tempFolder = null;
			// File fileDTO = new
			// File(JPMCDealingFaxInstructionScanProcessConfigXmlParser.htDesktopConfigs.get(desktopId+JPMCDealingFaxInstructionProcessConstants.DELIMITER+"FaxInstructionFolderSharedPath"));
			// Multi tiff bursting

			ss = new FileSeekableStream(faxInstruction);
			ImageDecoder dec = ImageCodec.createImageDecoder("tiff", ss, null);
			int count = dec.getNumPages();
			// String strDate=null;
			//
			// // String reportFilePath=
			// JPMCLuxDealingFaxInstructionScanProcessConfigXmlParser.htDesktopConfigs.get(desktopId+JPMCLuxDealingFaxInstructionProcessConstants.DELIMITER+"reportFilePath");
			// // String csvFileFormat=
			// JPMCLuxDealingFaxInstructionScanProcessConfigXmlParser.htDesktopConfigs.get(desktopId+JPMCLuxDealingFaxInstructionProcessConstants.DELIMITER+"csvFileFormat");
			//
			// Date currentDate=new Date();
			// DateFormat dateFormat = new SimpleDateFormat(csvFileFormat);
			// strDate=dateFormat.format(currentDate);
			// reportFilePath=reportFilePath+"//"+strDate+"_Dealing.csv";
			// String clientType=
			// JPMCLuxDealingFaxInstructionScanProcessUtils.getClientTypeFromAuditReport(faxInstruction.getName(),
			// reportFilePath, desktopId);
			// String
			// docID=JPMCLuxDealingFaxInstructionDocIDGenerator.getUniqueDocID(desktopId);
			DateFormat dealDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			dealDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
			String fileName = faxInstruction.getName();
			String dealDate = null;
			if (fileName != null && fileName.contains("-")) {
				String[] splitFaxFileName = fileName.split("-");
				if (splitFaxFileName != null && splitFaxFileName.length == 4) {

					if (splitFaxFileName[3] != null && !splitFaxFileName[3].equals("")
							&& splitFaxFileName[3].contains(".")) {
						String[] splithrs = splitFaxFileName[3].split(Pattern.quote("."));
						if (splithrs != null && !splithrs.equals("") && splithrs.length == 2) {
							Date dateParsed = new SimpleDateFormat("yyMMddHHmmss")
									.parse(splitFaxFileName[2] + splithrs[0]);
							dealDate = dealDateFormat.format(dateParsed);
						} else {
							LOGGER.info(
									"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:uploadDocument, @Message : Invalid Fax file Name and Format :: "
											+ fileName
											+ " Datacap Batch creation will process without deal date...Please correct the file format...Implementation:: "
											+ applicationName);
						}

					} else {
						LOGGER.info(
								"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:uploadDocument, @Message : Invalid Fax file Name and Format :: "
										+ fileName
										+ " Datacap Batch creation will process without deal date...Please correct the file format...Implementation:: "
										+ applicationName);
					}

				} else {
					LOGGER.info(
							"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:uploadDocument, @Message : Invalid Fax file Name and Format :: "
									+ fileName
									+ " Datacap Batch creation will process without deal date...Please correct the file format...Implementation:: "
									+ applicationName);
				}
			} else {
				LOGGER.info(
						"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:uploadDocument, @Message : Invalid Fax file Name and Format :: "
								+ fileName
								+ " Datacap Batch creation will process without deal date...Please correct the file format...Implementation:: "
								+ applicationName);
			}
			//

			String pageFileStr = "<pageFile><B id=\"\"><V n=\"totalPageCount\">" + count + "</V><V n=\"ClientType\">"
					+ clientType + "</V><V n=\"ProcessingFaxFileName\">" + faxInstruction.getName()
					+ "</V><V n=\"UniqueDocID\">" + docID + "</V><V n=\"ProcessingDealDate\">" + dealDate
					+ "</V></B></pageFile>";
			response = createBatch(url, applicationName, job, pageFileStr);
			// response = createBatch(url, application, job,desktopId);
			if (response != null) {
				// batch created
				queueId = parseXMLBody(response, "queueId");
				batchId = parseXMLBody(response, "batchId");
				LOGGER.info(
						"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:uploadDocument, @Message : Batch created with Batch Id: "
								+ batchId + " ,Implementation:: " + applicationName);

				if (count > 1) {
					TIFFEncodeParam param = new TIFFEncodeParam();
					param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
					final ImageInfo imageInfo = Sanselan.getImageInfo(faxInstruction);

					int physicalWidthDpi = imageInfo.getPhysicalWidthDpi();
					int physicalHeightDpi = imageInfo.getPhysicalHeightDpi();

					// to get
					int imageHeight = imageInfo.getHeight();
					int imageWidth = imageInfo.getWidth();
					TIFFField[] extras = new TIFFField[2];
					extras[0] = new TIFFField(282, TIFFField.TIFF_RATIONAL, 1,
							(Object) new long[][] { { (long) physicalWidthDpi, (long) 1 }, { (long) 0, (long) 0 } });
					extras[1] = new TIFFField(283, TIFFField.TIFF_RATIONAL, 1,
							(Object) new long[][] { { (long) physicalHeightDpi, (long) 1 }, { (long) 0, (long) 0 } });

					param.setTileSize(imageWidth, imageHeight);

					param.setExtraFields(extras);
					//
					param.setLittleEndian(false); // Intel
					LOGGER.info(
							"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:uploadDocument, @Message : Fax Instruction has "
									+ count + " pages...Batch Id: " + batchId + " ,Implementation:: "
									+ applicationName);
					tempFolder = new File(faxTempPath);
					if (!tempFolder.exists()) {
						// tempFolder.mkdir();
						FileUtils.forceMkdir(tempFolder);
					} else {
						tempFolder.delete();
						tempFolder.mkdir();
					}
					for (int i = 0; i < count; i++) {
						RenderedImage page = dec.decodeAsRenderedImage(i);

						File file = new File(faxTempPath + "/" + batchId + "_" + i + ".tif");

						ParameterBlock pb = new ParameterBlock();
						pb.addSource(page);
						pb.add(file.toString());
						pb.add("tiff");
						pb.add(param);
						RenderedOp r = JAI.create("filestore", pb);
						ByteArrayInputStream byteStream = (new ByteArrayInputStream(
								FileUtils.readFileToByteArray(file)));
						byte[] bytes = IOUtils.toByteArray(byteStream);
						if (file != null) {

							response = uploadFile(url, applicationName, job, queueId, bytes, contentType);
						}
						r.dispose();

					}
					ss.close();

				} else {
					ss.close();
					tempFolder = new File(faxTempPath);
					if (tempFolder != null && tempFolder.exists()) {
						tempFolder.delete();
					} else {
						// do nothing
					}
					ByteArrayInputStream byteStream = (new ByteArrayInputStream(
							FileUtils.readFileToByteArray(faxInstruction)));
					byte[] bytes = IOUtils.toByteArray(byteStream);
					if (faxInstruction != null) {

						response = uploadFile(url, applicationName, job, queueId, bytes, contentType);
					}
				}

				if (releaseBatch(url, applicationName, queueId)) {
					LOGGER.info(
							"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:uploadDocument, @Message : DatacapUploadService batch release : success ,Implementation:: "
									+ applicationName);

					// Move fax to processed
					// String
					// processedFolderSharedPath=JPMCLuxDealingFaxInstructionScanProcessConfigXmlParser.htDesktopConfigs.get(desktopId+JPMCLuxDealingFaxInstructionProcessConstants.DELIMITER+"processedFaxSharedPath");

					File processedFolderSharedPathFile = new File(
							processedFolderPath + File.separator + batchId + "_" + faxInstruction.getName());
					// JPMCLuxDealingFaxInstructionScanProcessUtils.insertAuditEntryForFaxInstruction(reportFilePath,
					// desktopId, count, docID,faxInstruction.getName(),
					// batchId);
					// faxInstruction.renameTo(processedFolderSharedPathFile);

					File destinationDir = new File(processedFolderPath);
					if(!destinationDir.exists()) {
						FileUtils.forceMkdir(destinationDir);
					}
					
					FileUtils.copyFile(faxInstruction, processedFolderSharedPathFile);

					if (tempFolder != null && tempFolder.exists()) {

						for (File f1 : tempFolder.listFiles()) {

							f1.delete();
						}
						tempFolder.delete();
						tempFolder.deleteOnExit();
					} else {
						// do nothing
					}
				}
			}

			faxFileDetails.setApplicationName(applicationName);
			faxFileDetails.setBatchId(batchId);
			faxFileDetails.setDocumentId(docID);
			faxFileDetails.setFileName(fileName);
			faxFileDetails.setFileID(fileId);
			faxFileDetails.setPageCount(count);

		} catch (Exception e) {
			if(ss != null) {
				ss.close();
			}
			LOGGER.error(
					"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:uploadDocument, @ Error Message : "
							+ e.getMessage());
			throw new OcrServiceException(e.getMessage(), e);
		}
		LOGGER.info(
				"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:uploadDocument, @Message : End of method ,Implementation:: "
						+ applicationName);
		return faxFileDetails;
	}

	public FaxFileDetails uploadDocumentHK(String application, String job, String url, File faxInstruction,
			String documentId, String faxInstructionTempPath, String contentType, String processedFolderSharedPath,
			long fileId, String timezone) throws OcrServiceException, IOException {
		LOGGER.debug(
				"@Java-Class:JPMCDealingFutureDateProcessOperation ,Method:uploadDocument, @Message : Start of method ,Implementation:: ");
		FaxFileDetails faxFileDetail = new FaxFileDetails();
		FileSeekableStream ss = null;
		try {

			// String url =
			// JPMCDealingFaxInstructionScanProcessConfigXmlParser.htwTMConnection.get("wTMURL");
			// String application =
			// JPMCDealingFaxInstructionScanProcessConfigXmlParser.htwTMConnection.get("applicationName");
			// String job =
			// JPMCDealingFaxInstructionScanProcessConfigXmlParser.htwTMConnection.get("job");
			String response = null;
			String queueId = null;
			String batchId = null;
			File tempFolder = null;
			// File fileDTO = new
			// File(JPMCDealingFaxInstructionScanProcessConfigXmlParser.htDesktopConfigs.get(desktopId+JPMCDealingFaxInstructionProcessConstants.DELIMITER+"FaxInstructionFolderSharedPath"));
			// Multi tiff bursting
			String fileName = faxInstruction.getName();
			ss = new FileSeekableStream(faxInstruction);
			ImageDecoder dec = ImageCodec.createImageDecoder("tiff", ss, null);
			int count = dec.getNumPages();
			String strDate = null;
			try {

				long longDealDate = 0;
				longDealDate = faxInstruction.lastModified();
				if (longDealDate > 0) {
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
					Date dealDate = new Date(longDealDate);
					strDate = dateFormat.format(dealDate);

					LOGGER.debug(
							"@Java-Class:JPMCDealingFutureDateProcessOperation ,Method:uploadDocument, @Message : Date Modified form Fax instruction after local time change :: "
									+ strDate);

				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error(
						"@Java-Class:JPMCDealingFutureDateProcessOperation ,Method:uploadDocument, @ Parsing fax instruction last modified capture Error Message : "
								+ e.getMessage(),
						e);
			}

			String pageFileStr = "<pageFile><B id=\"\"><V n=\"totalPageCount\">" + count
					+ "</V><V n=\"DateFaxModified\">" + strDate + "</V><V n=\"ProcessingFaxFileName\">"
					+ faxInstruction.getName() + "</V><V n=\"UniqueDocId\">"
					+ documentId + "</V></B></pageFile>";
			response = createBatch(url, application, job, pageFileStr);
			// response = createBatch(url, application, job,desktopId);
			if (response != null) {
				// batch created
				queueId = parseXMLBody(response, "queueId");
				batchId = parseXMLBody(response, "batchId");
				LOGGER.info(
						"@Java-Class:JPMCDealingFutureDateProcessOperation ,Method:uploadDocument, @Message : Batch created with Batch Id: "
								+ batchId + " ,Implementation:: " + application);

				if (count > 1) {
					TIFFEncodeParam param = new TIFFEncodeParam();
					param.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
					final ImageInfo imageInfo = Sanselan.getImageInfo(faxInstruction);

					int physicalWidthDpi = imageInfo.getPhysicalWidthDpi();
					int physicalHeightDpi = imageInfo.getPhysicalHeightDpi();

					// to get
					int imageHeight = imageInfo.getHeight();
					int imageWidth = imageInfo.getWidth();
					TIFFField[] extras = new TIFFField[2];
					extras[0] = new TIFFField(282, TIFFField.TIFF_RATIONAL, 1,
							(Object) new long[][] { { (long) physicalWidthDpi, (long) 1 }, { (long) 0, (long) 0 } });
					extras[1] = new TIFFField(283, TIFFField.TIFF_RATIONAL, 1,
							(Object) new long[][] { { (long) physicalHeightDpi, (long) 1 }, { (long) 0, (long) 0 } });

					param.setTileSize(imageWidth, imageHeight);

					param.setExtraFields(extras);
					//
					param.setLittleEndian(false); // Intel
					LOGGER.info(
							"@Java-Class:JPMCDealingFutureDateProcessOperation ,Method:uploadDocument, @Message : Fax Instruction has "
									+ count + " pages...Batch Id: " + batchId + " ,Implementation:: ");
					tempFolder = new File(faxInstructionTempPath);
					if (!tempFolder.exists()) {
						//tempFolder.mkdir();
						FileUtils.forceMkdir(tempFolder);
					} else {
						tempFolder.delete();
						tempFolder.mkdir();
					}
					for (int i = 0; i < count; i++) {
						RenderedImage page = dec.decodeAsRenderedImage(i);

						File file = new File(faxInstructionTempPath + batchId + "_" + i + ".tif");

						ParameterBlock pb = new ParameterBlock();
						pb.addSource(page);
						pb.add(file.toString());
						pb.add("tiff");
						pb.add(param);
						RenderedOp r = JAI.create("filestore", pb);
						ByteArrayInputStream byteStream = (new ByteArrayInputStream(
								FileUtils.readFileToByteArray(file)));
						byte[] bytes = IOUtils.toByteArray(byteStream);
						if (file != null) {

							// String contentType
							// =JPMCDealingFaxInstructionScanProcessConfigXmlParser.htDesktopConfigs.get(desktopId+JPMCDealingFaxInstructionProcessConstants.DELIMITER+"fileContentType");
							response = uploadFile(url, application, job, queueId, bytes, contentType);
						}
						r.dispose();

					}
					ss.close();

				} else {
					ss.close();
					tempFolder = new File(faxInstructionTempPath);
					if (tempFolder != null && tempFolder.exists()) {
						tempFolder.delete();
					} else {
						// do nothing
					}
					ByteArrayInputStream byteStream = (new ByteArrayInputStream(
							FileUtils.readFileToByteArray(faxInstruction)));
					byte[] bytes = IOUtils.toByteArray(byteStream);
					if (faxInstruction != null) {

						// String contentType
						// =JPMCDealingFaxInstructionScanProcessConfigXmlParser.htDesktopConfigs.get(desktopId+JPMCDealingFaxInstructionProcessConstants.DELIMITER+"fileContentType");
						response = uploadFile(url, application, job, queueId, bytes, contentType);
					}
				}
			}
			if (releaseBatch(url, application, queueId)) {
				LOGGER.debug(
						"@Java-Class:JPMCDealingFutureDateProcessOperation ,Method:uploadDocument, @Message : DatacapUploadService batch release : success ,Implementation:: "
								+ application);

				// Move fax to processed
				// String
				// processedFolderSharedPath=JPMCDealingFaxInstructionScanProcessConfigXmlParser.htDesktopConfigs.get(desktopId+JPMCDealingFaxInstructionProcessConstants.DELIMITER+"processedFaxSharedPath");

				File processedFolderSharedPathFile = new File(
						processedFolderSharedPath + "/" + batchId + "_" + faxInstruction.getName());
				
				File destinationDir = new File(processedFolderSharedPath);
				if(!destinationDir.exists()) {
					FileUtils.forceMkdir(destinationDir);
				}

				FileUtils.moveFile(faxInstruction, processedFolderSharedPathFile);

				if (tempFolder != null && tempFolder.exists()) {

					for (File f1 : tempFolder.listFiles()) {

						f1.delete();
					}
					tempFolder.delete();
					tempFolder.deleteOnExit();
				} else {
					// do nothing
				}

			}

			faxFileDetail.setApplicationName(application);
			faxFileDetail.setBatchId(batchId);
			faxFileDetail.setDocumentId(documentId);
			faxFileDetail.setFileName(fileName);
			faxFileDetail.setFileID(fileId);
			faxFileDetail.setPageCount(count);
		} catch (Exception e) {
			if (ss != null) {
				ss.close();
			}
			LOGGER.error("@Java-Class:JPMCDealingFutureDateProcessOperation ,Method:uploadDocument, @ Error Message : "
					+ e.getMessage());
			throw new OcrServiceException(e.getMessage(), e);
		}
		LOGGER.debug(
				"@Java-Class:JPMCDealingFutureDateProcessOperation ,Method:uploadDocument, @Message : End of method ,Implementation:: "
						+ application);
		return faxFileDetail;
	}

	private String createBatch(String url, String application, String job, String pageFileStr) throws Exception{
		LOGGER.info(
				"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:createBatch, @Message : Start of method ,Implementation:: "
						+ application);
		HttpURLConnection conn = null;
		url = url + "CreateBatch";
		String body = "<createBatchAttributes><application>" + application + "</application><job>" + job + "</job>"
				+ pageFileStr + "</createBatchAttributes>";
		// String body = "<createBatchAttributes><application>" + application +
		// "</application><job>" + job + "</job></createBatchAttributes>";
		conn = InitializeRequest(url, "POST", "text/xml");

		LOGGER.info(
				"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:createBatch, @Message : Create batch : url : "
						+ url + "\n body : " + body + " ,Implementation:: " + application);

		try {
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(body);
			dos.flush();
			dos.close();

			int status = conn.getResponseCode();
			LOGGER.info(
					"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:createBatch, @Message : Status :: "
							+ status + " ,Implementation:: " + application);

			if (status != 201) // 201 = Created
			{
				errorParse(conn, status);

				return null;
			}

		} catch (Exception e) {
			LOGGER.error("Exception on createBatch : " + e.getMessage(), e);
		}
		String response = getResponseBody(conn);
		LOGGER.info(
				"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:createBatch, @Message : \nCreateBatch response --\n\n"
						+ response + "  ,Implementation:: " + application);

		LOGGER.info(
				"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:createBatch, @Message : End of method ,Implementation:: "
						+ application);

		return response;

	}

	private boolean releaseBatch(String url, String application, String queueId) throws Exception{
		LOGGER.info(
				"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:releaseBatch, @Message : Start of method ,Implementation:: "
						+ application);

		HttpURLConnection conn = null;
		url = url + "ReleaseBatch/" + application + "/" + queueId + "/finished";
		conn = InitializeRequest(url, "PUT", "text/xml");
		LOGGER.info("@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:releaseBatch, @Message : wTm URL :: "
				+ url + " ,queueId :: " + queueId + " ,ApplicationName :: " + application + ",Implementation:: "
				+ application);
		// conn.setRequestProperty("ScanPath", "Test");
		try {
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.flush();
			dos.close();

			int status = conn.getResponseCode();
			LOGGER.info(
					"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:releaseBatch, @Message : Status :: "
							+ status + " ,Implementation:: " + application);

			if (status != 200) // 200 = OK
			{
				errorParse(conn, status);
				LOGGER.info(
						"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:releaseBatch, @Message : DatacapUploadService batch release : failure ,Implementation:: "
								+ application);

				return false;
			}

		} catch (Exception e) {
			LOGGER.error(
					"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:releaseBatch, @Message : Exception  on releaseBatch : "
							+ e.getMessage() + " Implementation:: " + application,
					e);

		}
		LOGGER.info(
				"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:releaseBatch, @Message : End of method ,Implementation:: "
						+ application);

		return true;

	}

	private String uploadFile(String url, String application, String job, String queueId, byte[] byteArray,
			String contentType) throws Exception{

		LOGGER.info(
				"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:uploadFile, @Message : Start of method contentType : "
						+ contentType + " ,Implementation:: " + application);

		HttpURLConnection conn = null;
		url = url + "UploadFile/" + application + "/" + queueId;
		byte[] body = byteArray;
		conn = InitializeRequest(url, "POST", contentType);
		try {
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.write(body);
			dos.flush();
			dos.close();

			int status = conn.getResponseCode();
			if (status != 201) // 201 = Created
			{
				errorParse(conn, status);
				return null;
			}

		} catch (Exception e) {
			LOGGER.error(
					"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:uploadFile, @Message : Exception  on createBatch : "
							+ e.getMessage(),
					e);
		}
		String response = getResponseBody(conn);
		LOGGER.info(
				"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,Method:uploadFile, @Message : End of method Response "
						+ response + " ,Implementation:: " + application);

		return response;

	}

	private static HttpURLConnection InitializeRequest(String url, String method, String contentType) {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod(method);
			conn.setRequestProperty("Content-Type", contentType);
			conn.setDoOutput(true);
			return conn;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String getResponseBody(HttpURLConnection conn) {
		BufferedReader br = null;
		StringBuilder body = null;
		String line = "";
		try {

			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			body = new StringBuilder();
			while ((line = br.readLine()) != null)
				body.append(line);
			return body.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String parseXMLBody(String body, String searchToken) {
		String xPathExpression;
		try {
			xPathExpression = String.format("//*[1]/*[local-name()='%s']", searchToken);
			XPath xPath = XPathFactory.newInstance().newXPath();
			return (xPath.evaluate(xPathExpression, new InputSource(new StringReader(body))));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getUniqueDocID(String docIdUrl, String userName, String password) throws Exception {

		LOGGER.debug(
				"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,getUniqueDocID, @Message : Start of Method ,Implementation :: ");

		String unifiedDococId = null;
		OutputStreamWriter writer = null;
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		String userNameDec = decryptor.decrypt(docKeyGeneratorKey, docKeyGeneratorInitVector, userName);
		String passwordDec = decryptor.decrypt(docKeyGeneratorKey, docKeyGeneratorInitVector, password);
		try {
			// String
			// strURL=JPMCLuxDealingFaxInstructionScanProcessConfigXmlParser.htDocIdGenerator.get("docIdURL");

			final URL url = new URL(docIdUrl);
			// String userName = docIdUserName;
			String encodedAuthString = DatatypeConverter
					.printBase64Binary((userNameDec + ":" + passwordDec).getBytes());
			LOGGER.debug(
					"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,getUniqueDocID, @Message : Unique Doc ID service URL : "
							+ docIdUrl + " User Name : " + userNameDec + " ,Implementation :: ");

			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", encodedAuthString);
			connection.setRequestProperty("Accept", "text/plain");

			String urlParameters = "docsystem=TAFE";

			writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			int responseCode = connection.getResponseCode();
			LOGGER.debug(
					"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,getUniqueDocID, @Message : Unique Doc ID service Response Code :: "
							+ responseCode + " ,Implementation :: ");

			if (responseCode == HttpURLConnection.HTTP_OK) {
				String str;
				StringBuilder outputBuilder = new StringBuilder();
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while (null != (str = reader.readLine())) {
					outputBuilder.append(str);
				}

				// Logger.info("Generated Doc id :" + outputBuilder.toString());
				unifiedDococId = outputBuilder.toString();
				LOGGER.debug(
						"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,getUniqueDocID, @Message : Generated Unique Doc ID :: "
								+ unifiedDococId + " ,Implementation :: ");

			} else {
				throw new Exception("Not able to connect to Unified Id generator service.");
			}

		} catch (Exception e) {
			e.printStackTrace();

			LOGGER.error(
					"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,getUniqueDocID, @Message :Implementation ::  Exception :: "
							+ e.getMessage(),
					e);
			throw new Exception(e.getMessage());
		} finally {
			try {
				writer.flush();
				writer.close();
				if (reader != null)
					reader.close();
				if (connection != null) {
					connection.getInputStream().close();
					connection.disconnect();
				}
			} catch (IOException e) {
				LOGGER.error(
						"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,getUniqueDocID, @Message :Implementation :: Exception :: "
								+ e.getMessage(),
						e);

			}
		}
		LOGGER.debug(
				"@Java-Class:JPMCLuxDealingFaxInstructionScanOperation ,getUniqueDocID, @Message : End of Method ,Implementation :: ");

		return unifiedDococId;

	}

	private static void errorParse(HttpURLConnection conn, int status) {
		BufferedReader br;
		String line;
		StringBuilder responseError;
		try {
			LOGGER.error("API call failed, status returned was: " + status);
			InputStreamReader isr = new InputStreamReader(conn.getErrorStream());
			br = new BufferedReader(isr);
			responseError = new StringBuilder();
			line = null;
			while ((line = br.readLine()) != null)
				responseError.append(line);
			LOGGER.error("\nError description:  \n" + responseError.toString());
			return;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
