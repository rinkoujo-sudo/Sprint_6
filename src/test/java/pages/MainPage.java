package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MainPage extends BasePage {

    // Кнопка «Заказать» (верхняя)
    private By topOrderButton = By.xpath("//button[text()='Заказать' and contains(@class, 'Button_Button__ra12g')]");

    // Кнопка «Заказать» (нижняя)
    private By bottomOrderButton = By.xpath("(//button[text()='Заказать'])[2]");

    // Вопросы о важном (поиск по тексту)
    private By faqQuestion(String questionText) {
        return By.xpath(String.format("//div[contains(text(), '%s')]", questionText));
    }

    // Ответ на вопрос (видимая панель)
    private By faqAnswerText = By.xpath("//div[contains(@class, 'accordion__panel') and not(@hidden)]/p");

    // Логотип Самоката
    private By scooterLogo = By.className("Header_LogoScooter__3lsAR");

    // Логотип Яндекса
    private By yandexLogo = By.className("Header_LogoYandex__3TSOI");

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public void clickTopOrderButton() {
        driver.findElement(topOrderButton).click();
    }

    public void clickBottomOrderButton() {
        WebElement element = driver.findElement(bottomOrderButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        element.click();
    }

    public void clickFaqQuestion(String questionText) {
        WebElement question = driver.findElement(faqQuestion(questionText));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", question);
        question.click();
    }

    public String getFaqAnswerText() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(faqAnswerText));
        return driver.findElement(faqAnswerText).getText();
    }

    public void clickScooterLogo() {
        driver.findElement(scooterLogo).click();
    }

    public void clickYandexLogo() {
        driver.findElement(yandexLogo).click();
    }
}