package src.com.qa.driver;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import src.com.qa.Exceptions.IllegalBrowserException;

public class BaseDriver {

	public static WebDriver driver;
	public static Properties properties;

	public BaseDriver() {

		properties = new Properties();
		try {
			properties.load(new FileInputStream("data//config.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void init() {
		try {

			String start = properties.getProperty("browser");
			System.out.println(start);

			if (start.equalsIgnoreCase("Chrome")) {

				System.setProperty("webdriver.chrome.driver", "Data//chromedriver.exe");
				driver = new ChromeDriver();

			} else {
				throw new IllegalBrowserException("Application is not implemented in the selected Browser");
			}

			driver.get(properties.getProperty("URL"));
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void ClearBrowserCache() throws InterruptedException {
		driver.manage().deleteAllCookies();// delete all cookies
		Thread.sleep(7000); // wait 7 seconds to clear cookies.

	}

	public void logIn() {
		driver.findElement(By.xpath("//a[contains(text(),'Log in')]")).click();

		boolean title = driver.findElement(By.xpath("//div[@class='page-title']/h1")).getText() != null;

		if (title) {

			System.out.println("Page title is validated");

		} else {
			System.out.println("Page title is not validated");
		}

		WebElement email = driver.findElement(By.id("Email"));
		WebElement password = driver.findElement(By.id("Password"));
		email.sendKeys(properties.getProperty("username"));
		waitFor();

		password.sendKeys(properties.getProperty("password"));

		driver.findElement(
				By.xpath("/html/body/div[4]/div[1]/div[4]/div[2]/div/div[2]/div[1]/div[2]/div[2]/form/div[5]/input"))
				.click();

	}

	public void validateAccId() {
		try {

			boolean AccId = driver.findElement(By.xpath("//a[contains(text(),'atest@gmail.com')]")).getText() != null;
			if (AccId) {
				System.out.println("User id is validated");
			} else {
				System.out.println("UserId is not validated");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearShippingCart() {
		try {
			WebElement cartSize = driver.findElement(By.xpath("//li[@id='topcartlink']/a/span[2]"));
			String msg = cartSize.getText();
			if (!msg.equalsIgnoreCase("(0)")) {
				driver.findElement(By.xpath("//li[@id='topcartlink']/a/span[1]")).click();
				boolean cart = driver.findElement(By.name("removefromcart")).isEnabled();
				if (cart) {
					driver.findElement(By.name("removefromcart")).click();
					driver.findElement(By.name("updatecart")).click();
					System.out.println("Shipping Cart is Cleared now");
				}
			}
		} catch (Exception e) {

		}
	}

	public void bookDetails() {
		driver.findElement(By.xpath("//a[contains(text(),'Books')]")).click();

		WebElement addToCart = driver.findElement(By.xpath("//div[@class='add-info']/div[2]/input"));

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView();", addToCart);
		addToCart.click();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e1) {

			e1.printStackTrace();
		}

		System.out.println("Successfully clicked on the Add to Cart");

		boolean msg = driver.findElement(By.xpath("//div[@id='bar-notification']/p")).getText() != null;
		if (msg) {
			System.out.println("Validate “The product has been added to shopping cart” message");

		} else {
			System.out.println("Product has been not added to the cart");
		}

		try {

			JavascriptExecutor js1 = (JavascriptExecutor) driver;
			js1.executeScript("javascript:window.scrollBy(0,-150)");
			System.out.println("Successfully scrolled up");

			WebElement cartview = driver.findElement(By.xpath("//li[@id='topcartlink']/a/span[1]"));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click()", cartview);
		} catch (Exception e) {

		}

		boolean price = driver.findElement(By.xpath("//table[@class='cart-total']/tbody/tr/td[2]/span/span"))
				.getText() != null;
		System.out.println(price);

		if (price) {
			System.out.println("Succssfully validate the “Sub-Total” Price for selected book.");
		} else {
			System.out.println("Not validated the sub Total for selected book");
		}
		driver.findElement(By.id("termsofservice")).click();

		driver.findElement(By.id("checkout")).click();
		try {

			Select dropdown = new Select(driver.findElement(By.id("billing-address-select")));
			int selectOptions = dropdown.getOptions().size();
			dropdown.selectByIndex(selectOptions - 1);
		} catch (Exception e) {

		}

		Select countryId = new Select(driver.findElement(By.id("BillingNewAddress_CountryId")));
		countryId.selectByValue("41");

		driver.findElement(By.id("BillingNewAddress_City")).sendKeys(properties.getProperty("city"));
		driver.findElement(By.id("BillingNewAddress_Address1")).sendKeys(properties.getProperty("address"));
		driver.findElement(By.id("BillingNewAddress_ZipPostalCode")).sendKeys(properties.getProperty("postalCode"));
		driver.findElement(By.id("BillingNewAddress_PhoneNumber")).sendKeys(properties.getProperty("phoneNumber"));

		driver.findElement(By.xpath("//div[@id='billing-buttons-container']/input")).click();
		driver.findElement(By.id("PickUpInStore")).click();

		driver.findElement(By.xpath("//div[@id='shipping-buttons-container']/input")).click();
		driver.findElement(By.name("paymentmethod")).click();

		WebElement continueCOD = driver.findElement(By.xpath("//div[@id='payment-method-buttons-container']/input"));
		JavascriptExecutor js1 = (JavascriptExecutor) driver;
		js1.executeScript("arguments[0].scrollIntoView();", continueCOD);
		System.out.println("Successfully scroll down");
		waitFor();
		continueCOD.click();

		boolean payCOD = driver.findElement(By.xpath("//p[contains(text(),'You will pay by COD')]")).getText() != null;
		if (payCOD) {
			System.out.println("Validation of COD is successfully");
		} else {
			System.out.println("Not validated COD Payment");
		}

		driver.findElement(By.xpath("//div[@id='payment-info-buttons-container']/input")).click();
		WebElement confirm = driver.findElement(By.xpath("//div[@id='confirm-order-buttons-container']/input"));
		JavascriptExecutor confimja = (JavascriptExecutor) driver;
		confimja.executeScript("arguments[0].scrollIntoView();", confirm);
		confirm.click();

		try {
			boolean orderValidation = driver
					.findElement(By.xpath("//strong[contains(text(),'Your order has been successfully processed!')]"))
					.getText() != null;
			if (orderValidation) {
				System.out.println("Order validated Successfully");
			} else {
				System.out.println("Order validation is not done");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String orderNumber = driver.findElement(By.xpath("//div[@class='section order-completed']/ul/li[1]")).getText();
		System.out.println("Order Number :: " + orderNumber);
		try {
			driver.quit();
		} catch (Exception e) {

		}

	}

	public void waitFor() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}