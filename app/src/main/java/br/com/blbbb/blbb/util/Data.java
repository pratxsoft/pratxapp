package com.br.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class Data {
	
	private SimpleDateFormat sdf;
		
	public String brToEn(String pDataStr){
		
		String splitData[] = new String[8];
		splitData = pDataStr.split("/");
		return splitData[2] + "-" +  splitData[1] + "-" + splitData[0];
		
	}
	
	public String enToBr(String pDataStr){
		String splitData[] = new String[8];
		splitData = pDataStr.split("-");
		return splitData[2] + "-" +  splitData[1] + "-" + splitData[0];
	}

	public Date stringBr2Date(String pDataStr) {
		Date data = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		try {  
		    data = format.parse(this.brToEn(pDataStr));  
		    System.out.println(data);  
		} catch (ParseException e) {  
		    // TODO Auto-generated catch block  
		    e.printStackTrace();  
		}
		return data;
	}
	
	public Date string2Date(String pDataStr) {
		Date data = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		try {  
		    data = format.parse(pDataStr);  
		    System.out.println(data);  
		} catch (ParseException e) {  
		    // TODO Auto-generated catch block  
		    e.printStackTrace();  
		}
		return data;
	}
	
	public String dateToBr(Date pData){
		
		this.sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		String dataStr = new String();
		
		dataStr = this.sdf.format(pData);
		
		return dataStr;
	}
	
	public String dateToEn(Date pData){
		
		this.sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String dataStr = new String();
		
		dataStr = this.sdf.format(pData);
		
		return dataStr;
	}
	

}
