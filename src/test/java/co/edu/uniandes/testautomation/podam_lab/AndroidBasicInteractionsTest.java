package co.edu.uniandes.testautomation.podam_lab;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import co.edu.uniandes.testautomation.podam_lab.model.CheckList;
import co.edu.uniandes.testautomation.podam_lab.model.TextNote;
import co.edu.uniandes.testautomation.podam_lab.test.TestConfiguration;
import io.appium.java_client.android.AndroidDriver;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class AndroidBasicInteractionsTest extends TestConfiguration {
	private AndroidDriver<WebElement> driver;
	private WebDriverWait wait;
	private final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();
	
	@BeforeClass
	public void setUp() throws IOException {
		File classpathRoot = new File(System.getProperty("user.dir"));
		File appDir = new File(classpathRoot, "src/main/resources");
		File app = new File(appDir, "omninotes.apk");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "Android Emulator");
		capabilities.setCapability("app", app.getAbsolutePath());
		capabilities.setCapability("automationName", "UiAutomator2");
		capabilities.setCapability("adbExecTimeout", "60000");
		capabilities.setCapability("uiautomator2ServerLaunchTimeout", "120000");
		capabilities.setCapability("appWaitActivity", "it.feio.android.omninotes.intro.IntroActivity");
		driver = new AndroidDriver<WebElement>(getServiceUrl(), capabilities);
		wait = new WebDriverWait(driver, 1);
	}
	
	@AfterClass
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void welcomeWizardTest() {
		
		String[] expectedTitle = { "Welcome to Omni Notes!", "Navigate", "Categories", "Improve", "Links", "Enjoy"};

		for (int i = 0; i < 6; i++) {
			
			wait.until(ExpectedConditions.textToBe(By.id("it.feio.android.omninotes:id/intro_title"), expectedTitle[i]));

			if (i == 5) {
				driver.findElementById("it.feio.android.omninotes:id/done").click();
			} else {
				driver.findElementById("it.feio.android.omninotes:id/next").click();
			}
			
		}

	}
	
	@Test(dependsOnMethods = {"welcomeWizardTest"})
	public void createTextNoteTest() throws InterruptedException {
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("it.feio.android.omninotes:id/fab_expand_menu_button")));
		driver.findElementById("it.feio.android.omninotes:id/fab_expand_menu_button").click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("it.feio.android.omninotes:id/fab_note")));
		driver.findElementById("it.feio.android.omninotes:id/fab_note").click();
		
		TextNote textNote = PODAM_FACTORY.manufacturePojo(TextNote.class);
		driver.findElementById("it.feio.android.omninotes:id/detail_title").sendKeys(textNote.getTitle());
		driver.findElementById("it.feio.android.omninotes:id/detail_content").sendKeys(textNote.getContent());
		
		driver.navigate().back();
		
		Assert.assertEquals(driver.findElementById("it.feio.android.omninotes:id/note_title").getText(), textNote.getTitle());
		Assert.assertEquals(driver.findElementById("it.feio.android.omninotes:id/note_content").getText(), textNote.getContent());
		
	}
	
	@Test(dependsOnMethods = {"createTextNoteTest"})
	public void editTextNoteTest() throws InterruptedException {
		
		driver.findElementById("it.feio.android.omninotes:id/note_content").click();
		
		TextNote textNote = PODAM_FACTORY.manufacturePojo(TextNote.class);
		driver.findElementById("it.feio.android.omninotes:id/detail_title").sendKeys(textNote.getTitle());
		driver.findElementById("it.feio.android.omninotes:id/detail_content").sendKeys(textNote.getContent());
		
		driver.navigate().back();
		
		Assert.assertEquals(driver.findElementById("it.feio.android.omninotes:id/note_title").getText(), textNote.getTitle());
		Assert.assertEquals(driver.findElementById("it.feio.android.omninotes:id/note_content").getText(), textNote.getContent());
		
	}
	
	@Test(dependsOnMethods = {"editTextNoteTest"})
	public void deleteTextNoteTest() throws InterruptedException {
		
		driver.findElementById("it.feio.android.omninotes:id/note_content").click();
		
		driver.findElementByXPath("//android.widget.ImageView[@content-desc=\"More options\"]").click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[4]/android.widget.LinearLayout")));
		driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[4]/android.widget.LinearLayout").click();
		
	}
	
	@Test(dependsOnMethods = {"deleteTextNoteTest"})
	public void createCheckList() {

		wait.until(ExpectedConditions.elementToBeClickable(By.id("it.feio.android.omninotes:id/fab_expand_menu_button")));
		driver.findElementById("it.feio.android.omninotes:id/fab_expand_menu_button").click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("it.feio.android.omninotes:id/fab_note")));
		driver.findElementById("it.feio.android.omninotes:id/fab_checklist").click();
		
		CheckList checkList = PODAM_FACTORY.manufacturePojo(CheckList.class);
		driver.findElementById("it.feio.android.omninotes:id/detail_title").sendKeys(checkList.getTitle());
		
		List<String> tasks = checkList.getTasks();
		for (int i = 0; i < tasks.size(); i++) {
			driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.RelativeLayout/android.widget.FrameLayout[2]/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout[" + (i + 1) + "]/android.widget.LinearLayout/android.widget.EditText").sendKeys(tasks.get(i));
		}
				
		driver.navigate().back();
		
		Assert.assertEquals(driver.findElementById("it.feio.android.omninotes:id/note_title").getText(), checkList.getTitle());
		
	}
	
	@Test(dependsOnMethods = {"createCheckList"})
	public void editCheckList() {

		driver.findElementById("it.feio.android.omninotes:id/note_content").click();
		
		CheckList checkList = PODAM_FACTORY.manufacturePojo(CheckList.class);
		driver.findElementById("it.feio.android.omninotes:id/detail_title").sendKeys(checkList.getTitle());
		
		List<String> tasks = checkList.getTasks();
		for (int i = 0; i < tasks.size(); i++) {
			driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.RelativeLayout/android.widget.FrameLayout[2]/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout[" + (i + 1) + "]/android.widget.LinearLayout/android.widget.EditText").sendKeys(tasks.get(i));
		}
				
		driver.navigate().back();
		
		Assert.assertEquals(driver.findElementById("it.feio.android.omninotes:id/note_title").getText(), checkList.getTitle());
		
	}
	
	@Test(dependsOnMethods = {"editCheckList"})
	public void deleteCheckListTest() throws InterruptedException {
		
		driver.findElementById("it.feio.android.omninotes:id/note_content").click();
		
		driver.findElementByXPath("//android.widget.ImageView[@content-desc=\"More options\"]").click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[4]/android.widget.LinearLayout")));
		driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[4]/android.widget.LinearLayout").click();
		
	}

}