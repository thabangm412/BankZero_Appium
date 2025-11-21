package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReporterNG {

    static ExtentReports extent;

    public static ExtentReports getReportObject() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String path = System.getProperty("user.dir") + File.separator + "reports" + File.separator + "Report_" + timestamp + ".html";

        ExtentSparkReporter reporter = new ExtentSparkReporter(path);
        reporter.config().setReportName("BankZero Testing Report");
        reporter.config().setDocumentTitle("Automation Testing Execution Report");
        reporter.config().setTheme(Theme.STANDARD);
        reporter.config().setEncoding("UTF-8");
        reporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Tester", "Thabang");
        return extent;
    }
}
