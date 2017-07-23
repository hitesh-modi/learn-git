package com.moditraders.utility;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {

	public static String getInvoiceNumber(int sequenceNumber) {
		StringBuilder sb = new StringBuilder();
		DecimalFormat numberFormat = new DecimalFormat("00000");
		String newSeq = numberFormat.format(sequenceNumber+1);
		sb.append(getFinancialYearString()+"/"+getCurrentDateInString()+newSeq);
		return sb.toString();
	}
	
	private static String getCurrentDateInString() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(date);
	}
	
	private static String getFinancialYearString() {
		StringBuilder sb = new StringBuilder();
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int year1 = 0;
		if(month >= 3) {
			year1 = year +1;
			sb.append(year+"-"+year1);
		}
		else {
			year1 = year - 1;
			sb.append(year1+"-"+year);
		}
		
		return sb.toString();
	}
	
}
