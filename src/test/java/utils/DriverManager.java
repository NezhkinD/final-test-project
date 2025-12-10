package utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverManager {

    private static final ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    private static final ThreadLocal<AndroidDriver> mobileDriver = new ThreadLocal<>();

    public static WebDriver getWebDriver() {
        if (webDriver.get() == null) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");

            WebDriver driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(
                    Duration.ofSeconds(ConfigReader.getInt("web.implicit.wait")));
            webDriver.set(driver);
        }
        return webDriver.get();
    }

    public static AndroidDriver getMobileDriver() {
        if (mobileDriver.get() == null) {
            UiAutomator2Options options = new UiAutomator2Options()
                    .setPlatformName(ConfigReader.get("android.platform.name"))
                    .setPlatformVersion(ConfigReader.get("android.platform.version"))
                    .setDeviceName(ConfigReader.get("android.device.name"))
                    .setAppPackage(ConfigReader.get("android.app.package"))
                    .setAppActivity(ConfigReader.get("android.app.activity"))
                    .setAutomationName(ConfigReader.get("android.automation.name"))
                    .setNoReset(false);

            try {
                AndroidDriver driver = new AndroidDriver(
                        new URL(ConfigReader.get("appium.server.url")), options);
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                mobileDriver.set(driver);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid Appium server URL", e);
            }
        }
        return mobileDriver.get();
    }

    public static void quitWebDriver() {
        if (webDriver.get() != null) {
            webDriver.get().quit();
            webDriver.remove();
        }
    }

    public static void quitMobileDriver() {
        if (mobileDriver.get() != null) {
            try {
                mobileDriver.get().quit();
            } catch (Exception e) {
                // Driver may already be crashed, ignore
            } finally {
                mobileDriver.remove();
            }
        }
    }
}
