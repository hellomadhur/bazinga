package com.expedia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.apach

import com.expedia.utils.AutoUtils;


public class App 
{
	//final static Logger LOGGER = Logger.getLogger(App.class);
	
    public static void main( String[] args ) throws InterruptedException, IOException
    {
    	
    	
    	File hotels = new File("hotels.txt");
    	List<String> cityList = FileUtils.readLines(hotels);
    	
    	for(String city : cityList){
    		saveHotelsList(city);
    	}
    }
    
    private static void saveHotelsList(String location){
    	
    	PrintWriter pw=null;
    	File output;
    	try{
        	
    		/*--------- Initializing the WebDriver, loading website and waiting for an element to load---------*/
    		WebDriver driver = new FirefoxDriver();
	    	driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	    	driver.get("http://www.priceline.com");
	    	WebDriverWait wait = new WebDriverWait(driver, 30);
	    	wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search_htl_tab")));
	    	WebElement element = driver.findElement(By.id("search_htl_tab")) ;
	    	element.findElement(By.tagName("a")).click();
	    	
	    	/*  ***************** Filling the form *******************/
        	driver.findElement(By.id("cityName")).clear();
        	driver.findElement(By.id("cityName")).sendKeys(location);
        	int offsetCheckInDays = 7;
        	int offsetCheckOutDays = 10;
        	String checkInDate = AutoUtils.getDateAfter(offsetCheckInDays,"MM/dd/yyyy");
        	String checkOutDate = AutoUtils.getDateAfter(offsetCheckOutDays,"MM/dd/yyyy");
        	driver.findElement(By.id("checkInDate")).clear();
        	driver.findElement(By.id("checkInDate")).sendKeys(checkInDate);
        	driver.findElement(By.id("checkOutDate")).clear();
        	driver.findElement(By.id("checkOutDate")).sendKeys(checkOutDate);
        	Select selectBox = new Select(driver.findElement(By.id("numberOfRooms")));
        	selectBox.selectByValue("1");
        	driver.findElement(By.id("numberOfRooms")).submit();
        	
        	wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ht-listing")));
        	List<WebElement> retailList;
        	WebElement priceContainer,pagination;
        	
        	List<WebElement> pageList;
        	int pageCount;
        	String nextPageHref;
        	
        	/* *************Creating a output File ***********/
			try {
				File dir = new File(AutoUtils.getDateAfter(0,"MM-dd-yyyy"));
				if(!dir.exists()){
					boolean success = dir.mkdir();
					if (success == false) throw new Exception(dir + " could not be created");
				}
				
				String dirName = dir.getName();
				String fileName =  location.replace(',','-').replace(' ', '-') + "__" + 
						AutoUtils.getDateAfter(offsetCheckInDays,"MMM-dd-yy") + "__" + AutoUtils.getDateAfter(offsetCheckOutDays,"MMM-dd-yy") + ".csv";
				
				output=new File(dirName + "/" + fileName);
				pw = new PrintWriter(output);
				pw.write("\"Hotel Name\",\"Hotel Location\"\n");
			} catch (FileNotFoundException e) {
				throw e;
			} 
        	driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS); // it waits for 100ms for a findElement method to find a given element.
        																		   // If not found then NoSuchElementException is thrown.
        																		  // If not mentioned findElement is stuck for very long time if element is not found.
        	do{
        		
        		retailList = AutoUtils.findElementsByClassName(driver, "ht-content");
        		if(retailList != null && retailList.size() > 0){
        			for(WebElement retailElement : retailList){
                		
                		priceContainer = AutoUtils.findElementByClassName(retailElement, "price-container");
                		if(priceContainer != null){
                			List<WebElement> anchorList = AutoUtils.findElementsByTagName(priceContainer, "a");
                			if(anchorList != null && anchorList.size() > 1){
                				WebElement psAnchor = anchorList.get(1);
                				WebElement psIconImg = AutoUtils.findElementByClassName(psAnchor, "ps-icon");
                				if(psIconImg != null){
                					WebElement htDetails = AutoUtils.findElementByClassName(retailElement, "check-rates-ht-details");
                					if(htDetails != null){
                						WebElement hotName, hotLoc;
                						hotName = AutoUtils.findElementByTagName(htDetails, "h3");
                						hotLoc = AutoUtils.findElementByTagName(htDetails, "p");
                						pw.write("\"" + (hotName!=null? hotName.getText():"") + "\",\"" + (hotLoc!=null? hotLoc.getText():"")+ "\"\n");
                					}
                				}
                			}	
                		}
                		
                	}
        		}else{
        			System.out.println("*** No hotels found for "+ location + "*****\n");
        		}
            	
            	
            	pagination = AutoUtils.findElementByClassName(driver, "pagination"); 
            	if(pagination == null)
            		break;
            	pageList = pagination.findElements(By.tagName("a"));
            	
            	pageCount = pageList.size();
            	nextPageHref = pageList.get(pageCount-1).getAttribute("href"); // If href is not defined for 'Next' link then 'null' is returned by this
            	
            	if(nextPageHref == null || nextPageHref == "" || nextPageHref == "null"){
            		break;
            	}
            	
        		driver.get(nextPageHref);
        		Thread.sleep(15000); //This is put to wait for 15 seconds, so that page can load completely.
            	
        	}while(true);
	        	
	    	driver.quit();
    	
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}finally{
    		pw.close();
    	}
    }
    
    
    
}
    	
		

		
    
   
