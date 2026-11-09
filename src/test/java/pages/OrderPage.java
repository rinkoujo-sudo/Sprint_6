package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class OrderPage extends BasePage {

    // Поле «Имя»
    private By nameField = By.xpath("//input[@placeholder='* Имя']");

    // Поле «Фамилия»
    private By surnameField = By.xpath("//input[@placeholder='* Фамилия']");

    // Поле «Адрес»
    private By addressField = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");

    // Поле «Станция метро»
    private By metroField = By.xpath("//input[@placeholder='* Станция метро']");

    // Поле «Телефон»
    private By phoneField = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");

    // Кнопка «Далее»
    private By nextButton = By.xpath("//button[text()='Далее']");

    // Поле «Когда привезти самокат»
    private By dateField = By.xpath("//input[@placeholder='* Когда привезти самокат']");

    // Выпадающий список «Срок аренды»
    private By rentalPeriodField = By.className("Dropdown-arrow");

    // Кнопка «Заказать»
    private By orderButton = By.xpath("//button[contains(@class, 'Button_Middle__1CSJM') and text()='Заказать']");

    // Кнопка «Да» в подтверждении
    private By confirmOrderButton = By.xpath("//button[text()='Да']");

    // Окно успешного заказа
    private By successMessage = By.className("Order_ModalHeader__3FDaJ");

    public OrderPage(WebDriver driver) {
        super(driver);
    }

    private void closeCookieBanner() {
        try {
            WebElement cookieBanner = driver.findElement(By.className("App_CookieConsent__1yUIN"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].remove();", cookieBanner);
        } catch (Exception e) {
            // Баннера нет
        }
    }

    public void fillFirstForm(String name, String surname, String address, String metroStation, String phone) {
        closeCookieBanner();

        driver.findElement(nameField).sendKeys(name);
        driver.findElement(surnameField).sendKeys(surname);
        driver.findElement(addressField).sendKeys(address);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement metroInput = wait.until(ExpectedConditions.elementToBeClickable(metroField));

        metroInput.click();
        metroInput.sendKeys(metroStation);

        WebElement metroOption = wait.until(ExpectedConditions.elementToBeClickable(By.className("select-search__select")));
        metroOption.click();

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        driver.findElement(phoneField).sendKeys(phone);

        WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
        nextBtn.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(dateField));
    }

    public void fillSecondForm(String date, String period, String color) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement dateInput = driver.findElement(dateField);
        dateInput.sendKeys(date);
        dateInput.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.elementToBeClickable(rentalPeriodField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(String.format("//div[text()='%s']", period)))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(String.format("//label[text()='%s']", color)))).click();
    }

    public void clickOrderButton() {
        WebElement orderBtn = driver.findElement(orderButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", orderBtn);
    }

    public void confirmOrder() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmOrderButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmBtn);
        } catch (Exception e) {
            System.out.println("БАГ: окно подтверждения заказа не появилось!");
        }
    }

    public String getSuccessMessageText() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
        } catch (Exception e) {
            return "Окно успеха не появилось (известный баг стенда)";
        }
    }
}