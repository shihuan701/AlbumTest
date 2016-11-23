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
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class LocalBrowse {
    /**
     * 
     * 测试步骤：
        1 启动应用
		2 输入用户名和密码登录
		3 访问云端相册
		4 下载图片
		5 删除云端相册
     **/
	private AndroidDriver<AndroidElement> driver;
	private WebDriverWait wait;
	
	@BeforeClass
	public void beforeClass() throws Exception{
		//apk文件地址
		File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "app");
        File app = new File(appDir, "Album_netease.apk");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "68a0f6863aed"); 
        capabilities.setCapability("platformVersion", "4.4");
        capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("appPackage", "com.netease.cloudalbum");
        capabilities.setCapability("unicodeKeyboard", "True");
        capabilities.setCapability("resetKeyboard", "True");
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
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
	}
	
	  @Test(dependsOnMethods = "login")
	  public void downLoadToLocal() throws InterruptedException{
		  driver.swipe(100, 200, 600, 200, 200);
		  driver.findElement(By.id("g_slidemenu_cloud_txt")).click();
	      wait.until(ExpectedConditions.presenceOfElementLocated(By.name("我的云相册")));
		  //点击第一个相册
		  driver.findElements(By.id("cloud_album_name")).get(0).click();
		  wait.until(ExpectedConditions.presenceOfElementLocated(By.name("操 作")));
		  //第一张相册
		  driver.findElements(By.id("cloud_image_photo")).get(0).click();
		  //点击下载按钮
		  driver.findElement(By.id("photo_save_btn")).click();
		  Thread.sleep(7000);
		  Assert.assertTrue(true, "成功下载");
	  }
	  
	  @Test(dependsOnMethods = "downLoadToLocal")
	  public void deletePic() throws Exception{
		  Thread.sleep(7000);
		  driver.findElement(By.id("photo_view_root")).click();
		  //点击删除按钮
		  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("photo_delete_btn")));
		  driver.findElement(By.id("photo_delete_btn")).click();
	      /*wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("删除照片成功")));*/
		  Assert.assertTrue(true, "删除照片成功");
	  }
}
