package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.MainPage;
import pages.OrderPage;

import java.time.Duration;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\koujo\\prodjects\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://qa-scooter.education-services.ru/");
    }

    // ВНИМАНИЕ: Тест падает из-за известного бага на стенде.
    // При нажатии на кнопку "Да" окно подтверждения заказа не появляется (баг воспроизводится в Chrome).
    @ParameterizedTest
    @MethodSource("orderData")
    public void testOrderFlow(boolean isTopButton, String name, String surname, String address, String metroStation, String phone, String date, String period, String color) {
        MainPage mainPage = new MainPage(driver);

        // Точка входа
        if (isTopButton) {
            mainPage.clickTopOrderButton();
        } else {
            mainPage.clickBottomOrderButton();
        }

        OrderPage orderPage = new OrderPage(driver);

        // Заполняем форму
        orderPage.fillFirstForm(name, surname, address, metroStation, phone);
        orderPage.fillSecondForm(date, period, color);
        orderPage.clickOrderButton();

        // Подтверждаем и проверяем
        orderPage.confirmOrder();
        String successText = orderPage.getSuccessMessageText();

        assertTrue(successText.contains("Заказ оформлен"), "Сообщение об успешном заказе не появилось! (Известный баг стенда)");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    static Stream<Arguments> orderData() {
        return Stream.of(
                // Тест 1: верхняя кнопка, данные Романовой Екатерины
                Arguments.of(
                        true,
                        "Екатерина",
                        "Романова",
                        "улица Светлая, 2",
                        "Бульвар Рокоссовского",
                        "+70009998877",
                        "05.11.2026",
                        "сутки",
                        "чёрный жемчуг"
                ),
                // Тест 2: нижняя кнопка, данные Есенина Сергея
                Arguments.of(
                        false,
                        "Сергей",
                        "Есенин",
                        "улица Злачная, 23",
                        "Бульвар Рокоссовского",
                        "+72223335588",
                        "06.11.2026",
                        "двое суток",
                        "серая безысходность"
                )
        );
    }
}