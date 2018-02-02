# fabric-java-sdk-test
Testing Hyperledger java sdk 

Prerequisites

* MAVEN supported IDE
* Docker


Run docker compose
```bash
#start CA container
docker-compose -f docker-compose-ca.yaml up -d
#stop CA container
docker-compose -f docker-compose-ca.yaml down
```


Run wallet test code :
* Import Project 
 
  
* create user storage  
```java
   SampleUser user = sampleStore.getMember("user6", "org1");
```

* run fabric-java-sdk-test\hyperledger wallet\src\main\java\com\github\maven\example\test.java
to enroll admin

* uncomment following to register user
```java
    if (!user.isRegistered()) {  // users need to be registered AND enrolled
                //Register User with CA
                RegistrationRequest rr = new RegistrationRequest("user6", "org1.department1");
                String EnrollmentSecret=ca.register(rr, admin);
                System.out.println("Secret is "+EnrollmentSecret);

                user.setEnrollmentSecret(EnrollmentSecret);
                System.out.println("user.isRegistered()"+user.isRegistered());
            }
```
* uncomment following to enroll user
```java
    if (!user.isEnrolled()) {
                //Enroll user
                System.out.println("NOT enrolled");
                Enrollment enrollment = ca.enroll("user6", user.getEnrollmentSecret());
                System.out.println("enroll is "+enrollment.toString());
                user.setEnrollment(enrollment);
                user.setMspId("Org1MSP");
                System.out.println("user.isEnrolled()"+user.isEnrolled());
            }
```
  
* View Certificate
```java
System.out.println(("Certificate "+user.getEnrollment().getCert()));
```