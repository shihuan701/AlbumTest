package com.myid.albumtest;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class BatchUpload {
    /**
     * 
     * 测试步骤：
        1 启动应用
		2 输入用户名和密码登录
		3 访问云端相册
		4 创建新相册
		5 将本地相册批量上传至刚创建的相册
		6 注销登录账户，退出应用
		7 重新打开应用，登录
		8 查看云端相册中刚刚上传的相片
		9.删除相片
		注意，请在用例加入等待的动作
     **/
	private AndroidDriver<AndroidElement> driver;
	private WebDriverWait wait;
	private int pics;
	
	@BeforeClass
	public void beforeClass() throws Exception{
		//apk文件地址
		File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "app");
        File app = new File(appDir, "Album_netease.apk");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "68a0f6863aed"); // "Android Emulator"
        capabilities.setCapability("platformVersion", "4.4");
        capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("appPackage", "com.netease.cloudalbum");
        capabilities.setCapability("unicodeKeyboard", "True");
        capabilities.setCapability("resetKeyboard", "True");
        driver = new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	}
	@AfterClass(alwaysRun=true)
	public void afterClass(){
		driver.quit();
	}
	
	@Test
	public void launchApp(){
		wait = new WebDriverWait(driver,2000);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("guide_bottom_id")));
	}
	
	@Test(dependsOnMethods = "launchApp")
	public void swipeGuidePage() throws Exception{
		double x = driver.manage().window().getSize().getWidth();
    	double y = driver.manage().window().getSize().getHeight();
    	driver.swipe((int)(x - 100), (int)(y / 2),100, (int)(y / 2), 2000);
    	Thread.sleep(3000);
    	driver.swipe((int)(x - 100), (int)(y / 2),100, (int)(y / 2), 2000);
    	driver.findElement(By.id("guide_btn")).click();
    	wait.until(ExpectedConditions.presenceOfElementLocated(By.id("skipSet")));
    	driver.findElement(By.id("skipSet")).click();
    	
	}
	
	@Test(dependsOnMethods = "swipeGuidePage")
	public void login(){
		driver.findElement(By.id("UserName")).sendKeys("1335077456@qq.com");
		driver.findElement(By.id("PassWord")).click();
		driver.findElement(By.id("PassWord")).sendKeys("helloseven123456");
		driver.hideKeyboard();
		driver.findElement(By.id("login")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("本地相册")));
        Assert.assertTrue(driver.findElementByName("本地相册").isDisplayed(),"登录失败");
	}
	
	
	@Test( dependsOnMethods= "login")
	  public void cloudalbum(){
	      driver.swipe(100, 200, 600, 200, 200);
	      driver.findElement(By.id("g_slidemenu_cloud_txt")).click();
	      wait.until(ExpectedConditions.presenceOfElementLocated(By.name("我的云相册")));
	  }
	   
	  @Test(dependsOnMethods = "cloudalbum",dataProvider = "data")
	  public void createCloudAlbum(String albumname){
		  driver.findElements(By.className("android.widget.ImageView")).get(2).click();
		  wait.until(ExpectedConditions.presenceOfElementLocated(By.name("创建相册")));
		  driver.findElement(By.id("createalbum_edit_name")).sendKeys(albumname);
		  driver.findElement(By.id("createalbum_confirm_btn")).click();
	      wait.until(ExpectedConditions.presenceOfElementLocated(By.name(albumname)));
	  }
	   
	  @Test(dependsOnMethods = "createCloudAlbum")
	  public void uploadToAlbum(){
		  driver.findElement(By.id("upload_into_thisalbum")).click();;
		  wait.until(ExpectedConditions.presenceOfElementLocated(By.name("选择备份相片")));
		  driver.findElement(By.name("Screensh...")).click();
		  pics = driver.findElements(By.className("android.widget.CheckBox")).size();
		  for(int i=0;i<pics;i++){
			  driver.findElements(By.className("android.widget.CheckBox")).get(i).click();
		  }
	      wait.until(ExpectedConditions.elementToBeClickable(By.id("photo_list_backup_btn")));
	      driver.findElement(By.id("photo_list_backup_btn")).click();
	      wait.until(ExpectedConditions.presenceOfElementLocated(By.name("备份报告")));
	      
	      driver.pressKeyCode(4);
	  }
	  
	  @Test(dependsOnMethods = "uploadToAlbum")
	  public void logout(){
		  wait.until(ExpectedConditions.presenceOfElementLocated(By.name("本地相册")));
		  driver.swipe(100, 200, 600, 200, 200);
		  driver.findElement(By.id("g_slidemenu_set_txt")).click();;
	      wait.until(ExpectedConditions.presenceOfElementLocated(By.name("设置")));
	      driver.findElement(By.name("注 销")).click();
	      driver.findElement(By.id("button1")).click();
	  }
	  
	  @Test(dependsOnMethods = "logout")
	  public void reLogin(){
		  driver.swipe(100, 200, 600, 200, 200);
	      driver.findElement(By.id("g_slidemenu_cloud_txt")).click();
	      login();
	  }
	  
	  @Test(dependsOnMethods = "reLogin",dataProvider = "data")
	  public void reView(String albumname){
		  driver.swipe(100, 200, 600, 200, 200);
	      driver.findElement(By.id("g_slidemenu_cloud_txt")).click();
	      wait.until(ExpectedConditions.presenceOfElementLocated(By.name("我的云相册")));
	      driver.findElement(By.name(albumname)).click();
	  }
	   
	  @Test(dependsOnMethods = "reView",dataProvider = "data")
	  public void deleteAlbum(String albumname){
	      wait.until(ExpectedConditions.presenceOfElementLocated(By.name(albumname)));
	      driver.findElement(By.name("操 作")).click();
	      driver.findElement(By.name("删除相册")).click();
	      wait.until(ExpectedConditions.presenceOfElementLocated(By.name("信息提示")));
	      driver.findElement(By.id("button1")).click();
	      //查找列表是否有相册
	      int albums = driver.findElements(By.id("cloud_album_name")).size();
	      int j=0;
	      for(int i=0;i<albums;i++){
	    	  if(driver.findElements(By.id("cloud_album_name")).get(i).getText().equals(albumname)){
	    		  j++;
	    	  }
	      }
	      Assert.assertTrue(j==0,"删除成功");
	  }
	  @DataProvider
	  public Object[][] data(){
	      return new Object[][]{
	          {"hp"}
	      };
	  }
}
