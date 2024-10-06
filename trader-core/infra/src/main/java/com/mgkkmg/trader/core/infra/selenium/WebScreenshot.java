package com.mgkkmg.trader.core.infra.selenium;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import com.mgkkmg.trader.common.code.ErrorCode;
import com.mgkkmg.trader.common.exception.BusinessException;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebScreenshot {

	private final WebDriver webDriver;

	private static final int TIMEOUT_SECONDS = 10;

	public WebScreenshot() {
		this.webDriver = setupWebDriver();
	}

	@PreDestroy
	public void closeWebDriver() {
		if (webDriver != null) {
			webDriver.quit();
		}
	}

	public File captureScreenshot(String url, String waitForElementSelector) {
		try {
			webDriver.get(url);

			WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(TIMEOUT_SECONDS));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(waitForElementSelector)));

			// 1시간 봉
			clickElement(wait, "/html/body/div[1]/div[2]/div[3]/span/div/div/div[1]/div/div/cq-menu[1]");
			clickElement(wait, "/html/body/div[1]/div[2]/div[3]/span/div/div/div[1]/div/div/cq-menu[1]/cq-menu-dropdown/cq-item[8]");

			// 보조 지표
			final String indicatorsXpath = "/html/body/div[1]/div[2]/div[3]/span/div/div/div[1]/div/div/cq-menu[3]";

			// Bollinger Bands
			clickElement(wait, indicatorsXpath);
			clickElementWithScroll(wait, "/html/body/div[1]/div[2]/div[3]/span/div/div/div[1]/div/div/cq-menu[3]/cq-menu-dropdown/cq-scroll/cq-studies/cq-studies-content/cq-item[15]");

			// RSI
			clickElement(wait, indicatorsXpath);
			clickElementWithScroll(wait, "/html/body/div[1]/div[2]/div[3]/span/div/div/div[1]/div/div/cq-menu[3]/cq-menu-dropdown/cq-scroll/cq-studies/cq-studies-content/cq-item[81]");

			// MACD
			clickElement(wait, indicatorsXpath);
			clickElementWithScroll(wait, "/html/body/div[1]/div[2]/div[3]/span/div/div/div[1]/div/div/cq-menu[3]/cq-menu-dropdown/cq-scroll/cq-studies/cq-studies-content/cq-item[53]");

			Thread.sleep(2000);

			return takeScreenshot();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("Screenshot capture was interrupted", e);
			throw new BusinessException("Screenshot capture was interrupted", ErrorCode.CAPTURE_SCREENSHOT_ERROR);
		}
	}

	private void clickElement(WebDriverWait wait, String xpath) {
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		element.click();
	}

	private void clickElementWithScroll(WebDriverWait wait, String xpath) {
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		((JavascriptExecutor)webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
	}

	private WebDriver setupWebDriver() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--window-size=1920,1080");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-gpu");
		options.addArguments("--ignore-ssl-errors=yes");
		options.addArguments("--ignore-certificate-errors");

		String remoteWebDriverUrl = System.getenv("SELENIUM_REMOTE_URL");
		if (remoteWebDriverUrl != null) {
			try {
				return new RemoteWebDriver(URI.create(remoteWebDriverUrl).toURL(), options);
			} catch (MalformedURLException e) {
				throw new BusinessException(e.getMessage(), ErrorCode.WEBDRIVER_SETUP_ERROR);
			}
		}

		return new ChromeDriver(options);
	}

	private File takeScreenshot() {
		return ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
	}
}
