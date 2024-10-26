package com.mgkkmg.trader.core.infra.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverHelper {

	private WebDriverHelper() {
		throw new IllegalStateException("Utility class");
	}

	public static void clickElement(WebDriverWait wait, String xpath) {
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		element.click();
	}

	public static void clickElementWithScroll(WebDriver webDriver, WebDriverWait wait, String xpath) {
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
	}
}
