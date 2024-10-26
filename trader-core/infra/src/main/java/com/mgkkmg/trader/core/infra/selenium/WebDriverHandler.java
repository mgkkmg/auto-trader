package com.mgkkmg.trader.core.infra.selenium;

import java.net.MalformedURLException;
import java.net.URI;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

import com.mgkkmg.trader.common.code.ErrorCode;
import com.mgkkmg.trader.common.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebDriverHandler {

	public WebDriver setupChromeDriver() {
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

	public void closeWebDriver(WebDriver webDriver) {
		if (webDriver != null) {
			try {
				webDriver.quit();
			} catch (Exception e) {
				log.error("Error closing WebDriver", e);
			}
		}
	}
}
