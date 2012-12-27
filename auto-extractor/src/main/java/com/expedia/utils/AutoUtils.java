package com.expedia.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public class AutoUtils {

	/**
	 * @param args
	 * @throws IOException 
	 */
//	public static Properties LoadProperties(String propFileName) throws IOException{
//	
//		InputStream is = AutoUtils.class.getResourceAsStream(propFileName);
//		
//		Properties prop = new Properties();
//		prop.load(is);
//		return prop;
//	}

	/**
	 * Get date after input 'days' parameter in MM/dd/yyyy format.
	 * @param days
	 * @param formatString - 'MM/dd/yyyy' or 'MMM-dd-yy'
	 * @return String
	 */
	public static String getDateAfter(int days,String formatString){
		
		DateFormat dateFormat = new SimpleDateFormat(formatString);
    	Date now,nextDate;
    	now = new Date();
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(now);
    	cal.add(Calendar.DAY_OF_YEAR,days);
    	nextDate = cal.getTime();
    	return dateFormat.format(nextDate);
    }
	public static WebElement findElementByClassName(SearchContext driver,String className){
    	WebElement element=null;
    	try{
    		element = driver.findElement(By.className(className));
    	}catch(NoSuchElementException ex){
    		//System.out.println(ex.getMessage());
    	}
    	return element;
    }
    
    public static List<WebElement> findElementsByClassName(SearchContext driver,String className){
    	List<WebElement> eList=null;
    	try{
    		eList = driver.findElements(By.className(className));
    	}catch(NoSuchElementException ex){
    		//System.out.println(ex.getMessage());
    	}
    	return eList;
    }
    
    public static WebElement findElementByTagName(SearchContext driver,String tagName){
    	WebElement element = null;
    	try{
    		element = driver.findElement(By.tagName(tagName));
    	}catch(NoSuchElementException ex){
    		//System.out.println(ex.getMessage());
    	}
    	return element;
    }
    public static List<WebElement> findElementsByTagName(SearchContext driver, String tagName){
    	List<WebElement> elmtList = null;// = new ArrayList<WebElement>();
    	try{
    		elmtList = driver.findElements(By.tagName(tagName));
    	}catch(NoSuchElementException ex){
    		//System.out.println(ex.getMessage());
    	}
    	return elmtList;
    }
}
