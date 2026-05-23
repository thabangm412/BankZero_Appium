package utils;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

//public class ExtentReporterNG {
//
//    static ExtentReports extent;
//
//    public static ExtentReports getReportObject() {
//        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String path = System.getProperty("user.dir") + File.separator + "reports" + File.separator + "Report_" + timestamp + ".html";
//
//        ExtentSparkReporter reporter = new ExtentSparkReporter(path);
//        reporter.config().setReportName("BankZero Testing Report");
//        reporter.config().setDocumentTitle("Automation Testing Execution Report");
//        reporter.config().setTheme(Theme.STANDARD);
//        reporter.config().setEncoding("UTF-8");
//        reporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
//
//        extent = new ExtentReports();
//        extent.attachReporter(reporter);
//        extent.setSystemInfo("Tester", "Thabang Monoane");
//        return extent;
//    }


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import static utils.AppiumUtils.driver;

public class ExtentReporterNG {

    public static ExtentReports getReportObject(AndroidDriver driver) {

        System.out.println("REPORT DRIVER HASH: " + DriverManager.driver.hashCode());
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String reportDir = System.getProperty("user.dir") + File.separator + "reports";
        new File(reportDir).mkdirs();

        String reportPath = reportDir + File.separator +
                "BankZero_Automation_Report_" + timestamp + ".html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

        sparkReporter.config().setReportName("BankZero Mobile Automation Execution Report");
        sparkReporter.config().setDocumentTitle("BankZero Automation Test Results");
        sparkReporter.config().setTheme(Theme.DARK);

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setAnalysisStrategy(AnalysisStrategy.TEST);

        extent.setSystemInfo("Project", "BankZero Mobile Automation");
        extent.setSystemInfo("Tester", "Thabang Monoane");

        // ✅ FIXED DEVICE INFO (REAL DRIVER ONLY)
        if (DriverManager.driver != null) {
            extent.setSystemInfo("Device Name", DeviceInfo.getDeviceName(DriverManager.driver));
            extent.setSystemInfo("Platform Name", DeviceInfo.getPlatformName(DriverManager.driver));
            extent.setSystemInfo("Platform Version", DeviceInfo.getPlatformVersion(DriverManager.driver));
            extent.setSystemInfo("UDID", DeviceInfo.getUdid(DriverManager.driver));
            extent.setSystemInfo("App Version", DeviceInfo.getAppVersion(DriverManager.driver));
        }

        extent.setSystemInfo("Execution Start Time",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        return extent;
    }

}
