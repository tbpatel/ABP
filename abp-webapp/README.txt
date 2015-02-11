This module represents the ABP web applicaiton which will be deployed onto aircraft.

This module depends on a number of other ABP modules. These dependencies will be included as JAR files under this application's WEB-INF/lib diretory.

Configuration
-------------
this app expects the following files to be available on the Java classpath:
* abp-jms.properties
* abp-ws-endpoints.properties

Please note that these files are sufficient to be on any class-loader available to the app.
This means that when running the app on Tomcat, it is sufficient to drop these files
into TOMCAT_HOME/common/classes

For convenience, the app can be configured through environment variables.  Please see
this URL for further info on this:
http://static.springframework.org/spring/docs/2.0.x/api/org/springframework/beans/factory/config/PropertyPlaceholderConfigurer.html#SYSTEM_PROPERTIES_MODE_OVERRIDE

Please check each of the files above for full list of properties required by the app.

IMPORTANT!! - A note on cookies: The Context entry in server.xml must have a cookies="false" attribute!

SSL Additions
-------------
In order for the SSL portion to run correctly on localhost the following will need to be added to server.xml (or uncommented if it exists already) and
the redirectPort property in the standard connector (usually the one for port 8080) will need to be modified to match the port number the following
Connector entry. NOTE: $CATALINA_HOME is the location of your tomcat installation

<Connector port="8443" maxHttpHeaderSize="8192"
           maxThreads="150" minSpareThreads="25" maxSpareThreads="75"
           enableLookups="false" disableUploadTimeout="true"
           acceptCount="100" scheme="https" secure="true"
           clientAuth="false" sslProtocol="TLS"
           keystoreFile="$CATALINA_HOME/.keystore" />

In addition to the server.xml change the following command will need to be run in order to create a certificate for SSL to use.

$JAVA_HOME/bin/keytool -genkey -alias tomcat -keyalg RSA -keystore $CATALINA_HOME/.keystore

The result of this command will be a series of prompts, here are the answers to provide and the questsions. The default keystore password is changeit,
yours may be different.

Enter keystore password:  changeit
What is your first and last name?
  [Unknown]:  localhost
What is the name of your organizational unit?
  [Unknown]:  AKQA
What is the name of your organization?
  [Unknown]:  AKQA
What is the name of your City or Locality?
  [Unknown]:  New York
What is the name of your State or Province?
  [Unknown]:  New York
What is the two-letter country code for this unit?
  [Unknown]:  US
Is CN=localhost, OU=AKQA, O=AKQA, L=New York, ST=New York, C=US correct?
  [no]:  yes

Enter key password for <tomcat>
        (RETURN if same as keystore password):

Useful maven commands:
----------------------
* mvn test --does not run integration tests. has exclude on **/*IntegrationTest.java

* mvn -DrunIntegrationTests=true test --runs integrations tests as well as unit tests.
* to add captcha jar to your local maven repo: mvn install:install-file -DgroupId=nl.captcha -DartifactId=simplecaptcha -Dversion=0.3 -Dpackaging=jar -Dfile=simplecaptcha-20050925.jar
* to add captcha jar to remote maven repo albeit accessible via file-system: mvn deploy:deploy-file  -DgroupId=nl.captcha -DartifactId=simplecaptcha -Dversion=0.3 -Dpackaging=jar -Dfile=simplecaptcha-20050925.jar -Durl=file:///data/repo/maven/repository

After installing the keystore for the GBP webapp server we have to export the certificate from the site and install it on the trusted-certificates store on the machine that will have the ABP application deployed. This step if crucial and it is what makes possible the secure back channel communication between ABP and GBP applications.
These are the steps to do it on your local machine:
1- Start both instances of tomcat with the latest code deployed on them
2- Open Internet Explorer and go to the ABP website(usually http://airborne.gogo.com/abp/start.html and then start ABP)
3- Once the splash page is displayed, click on one of the purchase buttons and you will be landing in /purchase2.do page, this page is using SSL
4- Now you must save the certificate to your local machine, here are 2 different methods that I have found:
  a) Internet Explorer will balk saying that there is a Certificate Error from an un-trusted host (see details in the address bar, probably in red), click on that message and then in the popup window that appears, click on 'View Certificates' link. Go to details tab and Click on 'Copy to File...' button and save the certificate as DER(.cer) encode-binary file to your local hard drive(or to the hard drive where ABP will live). Save it as C:\certfile.cer 
  b) If Internet Explorer does not show the Certificate Error message, then you will get a lock icon on the status bar down the bottom of IE when you are into the secure site zone. Just double click on the lock icon and find 'Copy to File' button and proceed to save the certificate as DER (.cer) encode binary file to your local hard drive(or to the hard drive where ABP will live). Save it as C:\certfile.cer 

If you know how to save the certificate using Firefox or any other browser, please, let us know

5- Once you saved the certificate, you must find where your Java Runtime Environment lives, usually is in $JAVA_HOME\jre.
Then run this command (replace the PATH TO YOUR JRE with appropriate value):

$JAVA_HOME/bin/keytool -keystore "C:\PATH TO YOUR JRE\lib\security\cacerts" -import -alias mysecurestore -file C:\\certfile.cer –trustcacerts

(
One full possible example could be:
$JAVA_HOME/bin/keytool -keystore "C:\java\jdk1.5.0\jre\lib\security\cacerts" -import -alias mysecurestore -file C:\\certfile.cer -trustcacerts
)

Enter password: changeit


6- After that edit or add the following entry to your local filter-dev.properties located in /applications folder.
Windows users must be aware of the four back-slashes, while for Mac and linux will be a single slash


###PC verion (i.e. C:\\\\java\\\\jre1.5.0_06\\\\lib\\\\security\\\\cacerts)
ssl.cert.store=C:\\\\REPLACE\\\\WITH\\\\YOUR\\\\JAVAPATH\\\\jre\\\\lib\\\\security\\\\cacerts

###Linux and mac version (i.e. /opt/sun-jdk-1.5.0.12/jre/lib/security/cacerts)
ssl.cert.store=/REPLACE/WITH/YOUR/JAVAPATH/jre/lib/security/cacerts

7- After that, run mvn clean package cargo:undeploy and mvn clean package cargo:deploy from your /abp-webapp/ folder

With these steps there is no need to install a SSL certificate on the ABP's server 

The filter-qa-ny.properties file for the New York QA box already has the correct value