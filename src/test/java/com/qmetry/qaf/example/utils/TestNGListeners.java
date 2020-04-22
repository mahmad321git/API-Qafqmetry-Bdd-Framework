package com.qmetry.qaf.example.utils;

import org.springframework.util.FileSystemUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.qmetry.qaf.example.stepdefinitions.BaseApiSteps;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import java.io.File;
import java.util.Properties;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TestNGListeners implements ITestListener {

    File myFile = new File("./test-results");
    String reportFlag = FileManager.getPropertyValue
            ("src/main/resources/application.properties", "global.report.history");

    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("Start: "+ iTestResult.getName());
    }


    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("Success: "+ iTestResult.getName());
        BaseApiSteps.responseBodyJson.clear();
        BaseApiSteps.responseBodyList.clear();
        BaseApiSteps.responseCode.clear();
        BaseApiSteps.CreatedRecord = null;
    }


    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("Failed");
        BaseApiSteps.responseBodyJson.clear();
        BaseApiSteps.responseBodyList.clear();
        BaseApiSteps.responseCode.clear();
        BaseApiSteps.CreatedRecord = null;
    }


    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("Skipped");
    }


    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println(iTestResult.getName());
    }


    @Override
    public void onStart(ITestContext iTestContext) {
        if (reportFlag.equals("true")) {
            FileSystemUtils.deleteRecursively(myFile);
        }
        System.out.println("Start with Clear Report History: "+reportFlag);
    }


    @Override
    public void onFinish(ITestContext iTestContext) {

        //Zipping Files to Rar format
        TestNGListeners mfe = new TestNGListeners();
        List<String> files = new ArrayList<String>();
        files.add(System.getProperty("user.dir")+"\\dashboard.htm");
        files.add(System.getProperty("user.dir")+"\\abc.bat");
        files.add(System.getProperty("user.dir")+"\\test-results");
        mfe.zipFiles(files);

        //Sending Report via gmail
        sendReportByGMail("cdk.test.490@gmail.com"
                ,"cdk_test_321", "cdk.test.490@gmail.com", "QAF Report", "");

        System.out.println("Finished");
    }

    private static void sendReportByGMail(String from, String pass, String to, String subject, String body) {

        Properties props = System.getProperties();

        String host = "smtp.gmail.com";

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.", "true");

        Session session = Session.getDefaultInstance(props);

        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject(subject);
            message.setText(body);

            BodyPart objMessageBodyPart = new MimeBodyPart();

            objMessageBodyPart.setText("Please Find The Attached Report File!");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(objMessageBodyPart);

            objMessageBodyPart = new MimeBodyPart();

            String filename = System.getProperty("user.dir")+"\\testing.xyz";

            DataSource source = new FileDataSource(filename);

            objMessageBodyPart.setDataHandler(new DataHandler(source));
            objMessageBodyPart.setFileName(filename);

            multipart.addBodyPart(objMessageBodyPart);
            message.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }

    }

    private static void zipFiles(List<String> files) {

        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            File file = new File("C:/Users/ahmad.idrees/Desktop/testing.rar");
            if(file.exists()){
                System.out.println("File exists and it is read only, making it writable");
                file.setWritable(true);
            }
            fos = new FileOutputStream(file);
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for (String filePath : files) {
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                System.out.println("Zipping the file: " + input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4 * 1024];
                int size = 0;
                while ((size = fis.read(tmp)) != -1) {
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            System.out.println("Done... Zipped the files...");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (Exception ex) {

            }
        }
    }
}
