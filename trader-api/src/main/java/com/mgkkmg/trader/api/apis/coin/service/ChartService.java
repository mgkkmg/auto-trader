package com.mgkkmg.trader.api.apis.coin.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mgkkmg.trader.core.infra.selenium.WebScreenshot;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChartService {

	private final WebScreenshot webScreenshot;

	@Value("${chart-image.path}")
	private String chartPath;

	@Value("${chart-image.upbit.file-name}")
	private String fileName;

	public void captureAndSaveScreenshot(String url, String waitForElementSelector) throws IOException {
		File screenshotFile = webScreenshot.captureScreenshot(url, waitForElementSelector);

		// Convert File to BufferedImage
		BufferedImage image = ImageIO.read(screenshotFile);

		// Get the path to the resources directory in the api module
		Path screenshotsPath = Paths.get(chartPath);
		if (!Files.exists(screenshotsPath)) {
			Files.createDirectories(screenshotsPath);
		}

		// Create the output file in the screenshots directory
		File outputFile = screenshotsPath.resolve(fileName).toFile();

		// Save the image
		ImageIO.write(image, "png", outputFile);

		webScreenshot.closeWebDriver();
	}
}
