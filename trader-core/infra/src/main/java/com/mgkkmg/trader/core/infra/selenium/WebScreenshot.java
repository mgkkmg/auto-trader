package com.mgkkmg.trader.core.infra.selenium;

import java.io.File;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import com.mgkkmg.trader.common.code.ErrorCode;
import com.mgkkmg.trader.common.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebScreenshot {

	private final WebDriverHandler webDriverHandler;

	private static final int TIMEOUT_SECONDS = 10;

	public File captureScreenshotOfBitcoinChart(String url, String waitForElementSelector) {
		WebDriver webDriver = webDriverHandler.setupChromeDriver();

		try {
			webDriver.get(url);

			WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(TIMEOUT_SECONDS));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(waitForElementSelector)));

			// 1시간 봉
			WebDriverHelper.clickElement(wait, "/html/body/div[1]/div[2]/div[3]/span/div/div/div[1]/div/div/cq-menu[1]");
			WebDriverHelper.clickElement(wait, "/html/body/div[1]/div[2]/div[3]/span/div/div/div[1]/div/div/cq-menu[1]/cq-menu-dropdown/cq-item[8]");

			// 보조 지표
			final String indicatorsXpath = "/html/body/div[1]/div[2]/div[3]/span/div/div/div[1]/div/div/cq-menu[3]";

			// Bollinger Bands
			WebDriverHelper.clickElement(wait, indicatorsXpath);
			WebDriverHelper.clickElementWithScroll(webDriver, wait, "/html/body/div[1]/div[2]/div[3]/span/div/div/div[1]/div/div/cq-menu[3]/cq-menu-dropdown/cq-scroll/cq-studies/cq-studies-content/cq-item[15]");

			// RSI
			WebDriverHelper.clickElement(wait, indicatorsXpath);
			WebDriverHelper.clickElementWithScroll(webDriver, wait, "/html/body/div[1]/div[2]/div[3]/span/div/div/div[1]/div/div/cq-menu[3]/cq-menu-dropdown/cq-scroll/cq-studies/cq-studies-content/cq-item[81]");

			// MACD
			WebDriverHelper.clickElement(wait, indicatorsXpath);
			WebDriverHelper.clickElementWithScroll(webDriver, wait, "/html/body/div[1]/div[2]/div[3]/span/div/div/div[1]/div/div/cq-menu[3]/cq-menu-dropdown/cq-scroll/cq-studies/cq-studies-content/cq-item[53]");

			Thread.sleep(2000);

			return takeScreenshot(webDriver);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("Screenshot capture was interrupted", e);
			throw new BusinessException("Screenshot capture was interrupted", ErrorCode.CAPTURE_SCREENSHOT_ERROR);
		} finally {
			webDriverHandler.closeWebDriver(webDriver);
		}
	}

	private File takeScreenshot(WebDriver webDriver) {
		return ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
	}
}
