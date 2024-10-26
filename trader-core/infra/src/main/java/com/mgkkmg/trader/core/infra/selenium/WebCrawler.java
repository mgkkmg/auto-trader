package com.mgkkmg.trader.core.infra.selenium;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
public class WebCrawler {

	private final WebDriverHandler webDriverHandler;

	private static final int TIMEOUT_SECONDS = 2;

	public String getUBCIFearAndGreedIndex(String url) {
		WebDriver webDriver = webDriverHandler.setupChromeDriver();

		try {
			webDriver.get(url);

			WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(TIMEOUT_SECONDS));

			WebDriverHelper.clickElement(wait, "/html/body/div[1]/div/div/div/div[1]/div/div/div[5]/div[1]");

			Thread.sleep(2000);

			WebElement score = webDriver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/div/div[1]/section/div/div/div[1]/div/div[2]"));
			WebElement itemName = webDriver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/div/div[1]/section/div/div/div[2]/div[3]/div[1]"));

			return "{score: '" + score.getText().replaceAll("\\n", " ") + "', item: '" + itemName.getText() + "'}";
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("Web Crawling was interrupted", e);
			throw new BusinessException("Web Crawling was interrupted", ErrorCode.CRAWLER_ERROR);
		} finally {
			webDriverHandler.closeWebDriver(webDriver);
		}
	}
}
