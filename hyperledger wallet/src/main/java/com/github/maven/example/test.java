package com.github.maven.example;


import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;

import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Properties;

/**
 * Created by siddesh_sangodkar on 31-Jan-18.
 */
public class test {

    private static final String TEST_ADMIN_NAME = "admin";
    private static final String TESTUSER_1_NAME = "user1";

    public static void main( String[] args )
    {
        System.out.println("**************************STARTED THE TEST*****************************");

        //Create instance of client.
        HFClient client = HFClient.createNewInstance();
        HFCAClient ca;



        try {

            // Setup client

            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            File sampleStoreFile = new File(System.getProperty("java.io.tmpdir")+ "/HFCSampletest.properties");
            System.out.println("File storage location"+System.getProperty("java.io.tmpdir")+"/HFCSampletest.properties");

            if (sampleStoreFile.exists()){
                sampleStoreFile.delete();
            }

           final SampleStore sampleStore = new SampleStore(sampleStoreFile);


            String cert = "src/test/fixture/crypto-config/ordererOrganizations/psl.com/ca/ca.psl.com-cert.pem";
            File cf = new File(cert);

            if (!cf.exists() || !cf.isFile()) {
                throw new RuntimeException("TEST is missing cert file " + cf.getAbsolutePath());
            }

            Properties caProperties = new Properties();
            caProperties.setProperty("pemFile", cf.getAbsolutePath());
            caProperties.setProperty("allowAllHostNames", "true"); //testing environment only NOT FOR PRODUCTION!
            ca = HFCAClient.createNewInstance("http://10.244.48.69:7054", caProperties);
            ca.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

            System.out.println("**************************ADMIN TEST*****************************");
            //Get Admin from file storage
            SampleUser admin = sampleStore.getMember("admin", "Org1MSP");

            System.out.println("ADMIN ENROLLED?? = "+admin.isEnrolled());

            //Check If Admin Enrolled
            if (!admin.isEnrolled()) {  //Preregistered admin only needs to be enrolled with Fabric caClient.
                //Enroll Admin
                admin.setEnrollment(ca.enroll(admin.getName(), "adminpw"));
                admin.setMspId("Org1MSP");
                System.out.println("ADMIN ENROLLED??="+admin.isEnrolled());
            }
            System.out.println("**************************ADMIN TEST DONE*****************************");

            System.out.println("**************************USER TEST *****************************");
            //Get User from file storage
            SampleUser user = sampleStore.getMember("user6", "org1");

            System.out.println("user.isRegistered()"+user.isRegistered());

            //  Check If User already Registered
           /* if (!user.isRegistered()) {  // users need to be registered AND enrolled
                //Register User with CA
                RegistrationRequest rr = new RegistrationRequest("user6", "org1.department1");
                String EnrollmentSecret=ca.register(rr, admin);
                System.out.println("Secret is "+EnrollmentSecret);

                user.setEnrollmentSecret(EnrollmentSecret);
                System.out.println("user.isRegistered()"+user.isRegistered());
            }*/

            System.out.println("user.isEnrolled()"+user.isEnrolled());
            //  Check If User already Enrolled
           /* if (!user.isEnrolled()) {
                //Enroll user
                System.out.println("NOT enrolled");
                Enrollment enrollment = ca.enroll("user6", user.getEnrollmentSecret());
                System.out.println("enroll is "+enrollment.toString());
                user.setEnrollment(enrollment);
                user.setMspId("Org1MSP");
                System.out.println("user.isEnrolled()"+user.isEnrolled());
            }*/

            System.out.println(("Certificate "+user.getEnrollment().getCert()));
//            System.out.println(("Private Key ALGORITHM: "+user.getEnrollment().getKey().getAlgorithm()));
//            System.out.println(("Private Key FORMAT:"+user.getEnrollment().getKey().getFormat()));
//            System.out.println(("Private Key "+user.getEnrollment().getKey().getEncoded().toString()));
//            System.out.println("**************************USER TEST DONE*****************************");

        }  catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
