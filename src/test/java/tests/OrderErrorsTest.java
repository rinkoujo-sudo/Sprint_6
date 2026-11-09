package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.MainPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderErrorsTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\koujo\\prodjects\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://qa-scooter.education-services.ru/");

        // Закрываем баннер с куки кнопкой "Да все привыкли"
        try {
            WebElement cookieButton = driver.findElement(By.xpath("//button[text()='Да все привыкли']"));
            cookieButton.click();
        } catch (Exception e) {
            // Баннера нет
        }
    }

    @Test
    public void testEmptyFieldsErrors() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickTopOrderButton();

        WebElement nextBtn = driver.findElement(By.xpath("//button[text()='Далее']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextBtn);

        // Проверяем, что появился блок с ошибками (ищем по тексту)
        assertTrue(driver.getPageSource().contains("Введите корректное имя"), "Нет ошибки для имени");
        assertTrue(driver.getPageSource().contains("Введите корректную фамилию"), "Нет ошибки для фамилии");
        assertTrue(driver.getPageSource().contains("Выберите станцию"), "Нет ошибки для станции метро");
        assertTrue(driver.getPageSource().contains("Введите корректный номер"), "Нет ошибки для телефона");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}