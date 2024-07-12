package com.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Test1 {
	public static WebDriver driver;
	
	public static String gettodaydateandadddoneday() {
		  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		   LocalDateTime now = LocalDateTime.now();  
		   System.out.println(dtf.format(now));  
		   String localdate = LocalDate
		      .parse(dtf.format(now))
		      .plusDays(1)
		      .toString();
		   return localdate.substring(8).trim();
	}
	
	public static String getBrowserName() throws IOException {
		Properties properties = new Properties();
		FileInputStream configfile = new FileInputStream("config.properties");
		properties.load(configfile);
		String browsername = properties.getProperty("browsername");
		return browsername;
		
	}

	public static void main(String[] args) throws Exception {
		String browser = getBrowserName();
		switch (browser) {
		case "chrome":
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
			
		case "edge":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;	
			
		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			break;	
    
		default:
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
		}
	
		driver.get("https://demoqa.com/date-picker");
		driver.manage().window().maximize();
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(30) );
		WebElement datepicker =wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#dateAndTimePickerInput")));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", datepicker);	
		datepicker.click();
		String datenextday = gettodaydateandadddoneday();
		WebElement day = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='react-datepicker__month']//div[text() = '"+gettodaydateandadddoneday()+"']")));
		day.click();
		WebElement datetextfield = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#dateAndTimePickerInput")));
		String value = datetextfield.getAttribute("value").substring(5,7);
		System.out.println(value);
		System.out.println(datenextday);
        if(value.contains(datenextday)) {
        	System.out.println("date changed");
        }
        else {
        	throw new Exception("Date did not changed!!");
        }
        driver.quit();
		
		
 
	}

}
