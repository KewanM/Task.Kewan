import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AllTests {

    WebDriverWait wait;
    WebDriver driver ;

    List<String> titles = new ArrayList<>();



    @Before
    public void navigatePage(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();


        driver.get("https://www.webstaurantstore.com");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait (10, TimeUnit.SECONDS);


    }


    @Test
    public void test1() {

    //This test is to Search for 'stainless work table', add all items title to a list

        wait = new WebDriverWait(driver,10);

        driver.findElement(By.id("searchval")).sendKeys("stainless work table",Keys.ENTER);
        //driver.findElement(By.id("searchval")).sendKeys();

        List<WebElement> items = driver.findElements(By.xpath("//div[@id='product_listing']/div/div/a[@class='block']"));

        int counter=0;

        for(int i = 0; i<items.size();i++){
            wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath("//div[@id='product_listing']/div/div/a[@class='block']"))));
            items.get(i).click();
            titles.add(driver.getTitle().toString());
            driver.navigate().back();
            counter++;


        }

        String lastPage = driver.findElement(By.xpath("//a[contains(@aria-label,'last page')]")).getText();
        int pages= Integer.valueOf(lastPage);



        while(pages>1) {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.cssSelector("#paging > nav > ul > li.inline-block.leading-4.align-top.rounded-r-md"))));

            driver.findElement(By.cssSelector("#paging > nav > ul > li.inline-block.leading-4.align-top.rounded-r-md")).click();
            for (int i = 0; i < items.size(); i++) {
                wait = new WebDriverWait(driver, 10);
                wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath("//div[@id='product_listing']/div/div/a[@class='block']")).get(i)));
                driver.findElements(By.xpath("//div[@id='product_listing']/div/div/a[@class='block']")).get(i).click();
                titles.add(driver.getTitle().toString());
                driver.navigate().back();
                pages--;
                counter++; }
        }

    }
    @Test
    public void test2_assertions(){

    for(String title:titles){

        Assert.assertTrue(title.contains("Table"));}
}



    @Test
    public void test3_addAndEmptyCard() throws InterruptedException {

        wait = new WebDriverWait(driver, 10);

        driver.findElement(By.id("searchval")).sendKeys("stainless work table",Keys.ENTER);

        // to go to page 9
        driver.findElement(By.xpath("//a[contains(@aria-label,'last page')]")).click();
        // add page 9 items to the list
        List<WebElement> items= driver.findElements(By.xpath("//div[@id='product_listing']/div/div/a[@class='block']"));

        items.get(items.size()-1).click();


        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("buyButton")));

        //add to cart btn
        driver.findElement(By.id("buyButton")).click();

        // go to the cart btn

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@data-testid='cart-nav-link']")));

        driver.findElement(By.xpath("//a[@data-testid='cart-nav-link']")).click();

    Thread.sleep(3000);
        //empty cart btn

        driver.findElement(By.xpath("//button[@class='emptyCartButton btn btn-mini btn-ui pull-right']")).click();


        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='flex-1 m-5 ']/../footer/button[1]")));


        driver.findElement(By.xpath("//*[@class='flex-1 m-5 ']/../footer/button[1]")).click();


        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='header-1']")));

        String textcart = driver.findElement(By.xpath("//*[@class='header-1']")).getText();
        Assert.assertEquals("Your cart is empty.",textcart);
        driver.quit();


    }







    @After
    public void driverQuit() {
        //driver.quit();
    }


}
