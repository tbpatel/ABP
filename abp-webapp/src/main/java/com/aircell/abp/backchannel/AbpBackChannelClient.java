package com.aircell.abp.backchannel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aircell.abp.model.AirPassenger;

/**.
 * Provides the back channel communication functionality from ABP to GBP. It
 * lives on the ABP application(deployed on the planes). It has 1 constructor
 * that needs 1 parameter that is required: 1- host -> the host to where the
 * back channel will be connecting to (Probably declared on
 * abp-webapp-controllers.xml) Establishes communication with remote host.
 * Either using plain http connection or SSL (https). HttpClient class used for
 * the remote connection provides full support for HTTP over Secure Sockets
 * Layer (SSL) or IETF Transport Layer Security (TLS) protocols by leveraging
 * the Java Secure Socket Extension (JSSE). JSSE has been integrated into the
 * Java 2 platform as of version 1.4 and works with HttpClient out of the box.
 * @author Oscar.Diaz
 */
public class AbpBackChannelClient {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private StringBuilder strHost;

    private String gbpSessionId;

    private String gbpCookieName = null;

    private String gbpCookieValue = null;

    private String httpDomain = null;

    private String httpPath = null;

    private int cookieMaxAge = -1;

    private boolean gbpCookieSecurity = false;

    /**.
     * Constructor that accepts 1 parameter that is required.
     *
     * @param host.
     *            The host that will be connecting to.
     * @throws Exception
     *             f no host is passed in
     */
    public AbpBackChannelClient(String host) throws Exception {

        // || host.indexOf("http") < 0
        if (host == null || host.length() < 5) {
            throw new Exception("Invalid host argument value");
        } else {
            this.strHost = new StringBuilder(host);
            this.httpPath = "/";
            this.cookieMaxAge = -1;
            gbpCookieSecurity = false;

        }
    }

    /**.**********************************************************************
     * Connects to the remote host and retrieves Username and Password. It
     * sends GBP sessionId and user IP over toGBP system on the formed URL.i.e.
     * https://gbp.portal/page.do;jsessionid=1222223435455?list.of.params... An
     * exception is thrown if anything goes wrong
     *
     * @param GBPSessionId
     * @param userIP
     * @return Map with user and pwd keys(declared on BackChannelUtils) if the
     *         user exists, NULL otherwise
     * @throws Exception
     */

    public Map getUserCredentials(
    String GBPSessionId, String userIP, String sslCertStorePath
    ) throws Exception {
        logger.debug(
        "AbpBackChannelClient.getUserCredentials with userIP={}" + "...Enters"
        + userIP
        );
        // || strHost.indexOf("http") < 0
        if (strHost == null || strHost.length() < 5 || GBPSessionId == null
        || GBPSessionId.length() == 0 || userIP == null
        || userIP.length() == 0) {
            throw new Exception("Invalid argument values");
        } else {
            // Everything is apparently correct, proceed to connect to
            // Ground Portal
            // and retrieve UserName and Password
            this.gbpSessionId = GBPSessionId;
            Map<String, String> params = new HashMap<String, String>();
            params.put(BackChannelUtils.ABP_USER_IP, userIP);
            String resp =
            this.getResults(params, "get", sslCertStorePath).trim();

            if (resp != null && resp.length() > 0
            && resp.indexOf(BackChannelUtils.RESULT_AUTH_SEPARATOR) > 0) {
                // something was retrieved on the specified format
                Map<String, String> map = new HashMap<String, String>();
                map.put(
                BackChannelUtils.GBP_USER_NAME, resp.substring(
                0, resp.indexOf(BackChannelUtils.RESULT_AUTH_SEPARATOR)
                )
                );
                map.put(
                BackChannelUtils.GBP_USER_PASSWORD, resp.substring(
                resp.indexOf(BackChannelUtils.RESULT_AUTH_SEPARATOR)
                + BackChannelUtils.RESULT_AUTH_SEPARATOR.length(), resp.length()
                )
                );
                logger
                .debug("AbpBackChannelClient.getUserCredentials" + "...Exits1");
                return map;
            } else {
                // the user hasnt been registered on GBP
                return null;
            }

        }

    }

    public String getAircellUserXML(
    String GBPSessionId, String userIP, String sslCertStorePath
    ) throws Exception {
        logger.debug(
        "AbpBackChannelClient.getAircellUserXML with userIP={}" + "...Enters"
        + userIP
        );
        // || strHost.indexOf("http") < 0
        if (strHost == null || strHost.length() < 5 || GBPSessionId == null
        || GBPSessionId.length() == 0 || userIP == null
        || userIP.length() == 0) {
            throw new Exception("Invalid argument values");
        } else {
            // Everything is apparently correct, proceed to connect to
            // Ground Portal
            // and retrieve AirPassenger as XML
            this.gbpSessionId = GBPSessionId;
            Map<String, String> params = new HashMap<String, String>();
            params.put(BackChannelUtils.ABP_USER_IP, userIP);
            String resp =
            this.getResults(params, "get", sslCertStorePath).trim();

            if (resp != null && resp.length() > 0) {
                // something was retrieved
                return resp;
            } else {
                // the user hasnt been registered on GBP
                return null;
            }

        }
    }

    public String getAircellUserString(
    String GBPSessionId, String userIP, String paramName, String methodName,
    String sslCertStorePath
    ) throws Exception {
        // || strHost.indexOf("http") < 0
        logger.debug("AbpBackChannelClient.getAircellUserString" + "...Enters");
        logger.debug("received session id: {}", GBPSessionId);
        logger.debug("received user ip: {}", userIP);
        if (strHost == null || strHost.length() < 5 || GBPSessionId == null
        || GBPSessionId.length() == 0 || userIP == null
        || userIP.length() == 0) {
            throw new Exception("Invalid argument values");
        } else {
            // Everything is apparently correct, proceed to connect to
            // Ground Portal
            // and retrieve AircellUser as XML
            this.gbpSessionId = GBPSessionId;
            Map<String, String> params = new HashMap<String, String>();
            params.put(paramName, methodName);
            params.put(BackChannelUtils.ABP_USER_IP, userIP);
            String resp =
            this.getResults(params, "get", sslCertStorePath).trim();

            if (resp != null && resp.length() > 0) {
                // something was retrieved
                logger.debug(
                "AbpBackChannelClient.getAircellUserString" + "...Exits1"
                );
                return resp;
            } else {
                // the user hasnt been registered on GBP
                logger.debug(
                "AbpBackChannelClient.getAircellUserString" + "...Exits2"
                );
                return null;
            }

        }

    }

    public String getAircellUserStringPostMethod(
    String userIP, String paramName, String methodName, String sslCertStorePath
    ) throws Exception {
        // || strHost.indexOf("http") < 0
        if (strHost == null || strHost.length() < 5 || userIP == null
        || userIP.length() == 0) {
            throw new Exception("Invalid argument values");
        } else {
            // Everything is apparently correct, proceed to connect to
            // Ground Portal
            // and retrieve AircellUser as XML
            Map<String, String> params = new HashMap<String, String>();
            params.put(paramName, methodName);
            params.put(BackChannelUtils.ABP_USER_IP, userIP);
            String resp =
            this.getResults(params, "post", sslCertStorePath).trim();

            if (resp != null && resp.length() > 0) {
                // something was retrieved
                return resp;
            } else {
                // the user hasnt been registered on GBP
                return null;
            }

        }
    }

    public String setGbpActiveServiceGetMethod(
    String GBPSessionId, String userIP, String paramName, String methodName
    ) throws Exception {
        // || strHost.indexOf("http") < 0
        if (strHost == null || strHost.length() < 5 || GBPSessionId == null
        || GBPSessionId.length() == 0 || userIP == null
        || userIP.length() == 0) {
            throw new Exception("Invalid argument values");
        } else {
            // Everything is apparently correct, proceed to connect to
            // Ground Portal
            // and retrieve AircellUser as XML
            this.gbpSessionId = GBPSessionId;
            Map<String, String> params = new HashMap<String, String>();
            params.put(paramName, methodName);
            params.put(BackChannelUtils.ABP_USER_IP, userIP);
            String resp = this.getResults(params, "get", null).trim();

            if (resp != null && resp.length() > 0) {
                // something was retrieved
                return resp;
            } else {
                // the user hasnt been registered on GBP
                return null;
            }

        }
    }

    public String setGbpActiveServicePostMethod(
    String userIP, String paramName, String methodName
    ) throws Exception {
        // || strHost.indexOf("http") < 0
        if (strHost == null || strHost.length() < 5 || userIP == null
        || userIP.length() == 0) {
            throw new Exception("Invalid argument values");
        } else {
            // Everything is apparently correct, proceed to connect to
            // Ground Portal
            // and retrieve AircellUser as XML
            Map<String, String> params = new HashMap<String, String>();
            params.put(paramName, methodName);
            params.put(BackChannelUtils.ABP_USER_IP, userIP);
            String resp = this.getResults(params, "post", null).trim();

            if (resp != null && resp.length() > 0) {
                // something was retrieved
                return resp;
            } else {
                // the user hasnt been registered on GBP
                return null;
            }

        }
    }
    /** This methods sets setGbpActivateErrorPostMethod.
     * @param userIP String
     * @param errorCode String
	 * @param errorText String
	 * @param errorCause String
     * @param paramName String
     * @param methodName String
     * @return String
     * @throws Exception Exception
     * */
    public String setGbpActivateErrorPostMethod(
    String userIP, String errorCode, String errorText, String errorCause, String paramName, String methodName
    ) throws Exception {

        if (strHost == null || strHost.length() < 5 || userIP == null
        || userIP.length() == 0) {
            throw new Exception("Invalid argument values");
        } else {
            // Everything is apparently correct, proceed to connect to
            // Ground Portal
            // and retrieve AircellUser as XML
            Map<String, String> params = new HashMap<String, String>();
            params.put(paramName, methodName);
            params.put(BackChannelUtils.ABP_USER_IP, userIP);
            params.put(BackChannelUtils.ACTIVATE_ERROR_CODE, errorCode);
            params.put(BackChannelUtils.ACTIVATE_ERROR_TEXT, errorText);
            params.put(BackChannelUtils.ACTIVATE_ERROR_CAUSE, errorCause);

            String resp = this.getResults(params, "post", null).trim();

            if (resp != null && resp.length() > 0) {
                // something was retrieved
                return resp;
            } else {
                // the user hasnt been registered on GBP
                return null;
            }

        }
    }

    /**.
     * This method is to retry back channel communication if it
     * failed to establish communication.
     *
     * @param parameters
     * @param methodType
     * @param sslCertStorePath
     * @return responseBody
     * @throws Exception
     */
    private String getResults(
    Map<String, String> parameters, String methodType, String sslCertStorePath
    ) throws Exception {
        boolean retry = false;
        int maxAttempts = 3;
        if (BackChannelUtils.getAbpProperties(this)
        .containsKey(BackChannelUtils.MAX_ABP_GBP_ATTEMPTS)) {
            try {
                maxAttempts = Integer.parseInt(
                (String) BackChannelUtils.getAbpProperties(this)
                .get(BackChannelUtils.MAX_ABP_GBP_ATTEMPTS)
                );
            } catch (Exception e) {
                maxAttempts = 3;
            }
        }
        String responseBody = null;
        for (int count = 1; count <= maxAttempts; count++) {
            try {
                logger.debug(
                "Establising communication with GBP : Attemp - " + count
                );
                responseBody = this
                .getResults(parameters, methodType, sslCertStorePath, retry);
                break;
            } catch (Exception e) {
                if (count == maxAttempts) {
                    throw e;
                }
                retry = true;
            }
        }
        return responseBody;
    }

    /**.
     * Establishes communication with remote host. Either using plain http
     * connection or SSL (https). HttpClient class used for the remote
     * connection provides full support for HTTP over Secure Sockets Layer (SSL)
     * or IETF Transport Layer Security (TLS) protocols by leveraging the Java
     * Secure Socket Extension (JSSE). JSSE has been integrated into the Java 2
     * platform as of version 1.4 and up and works with HttpClient out of the
     * box. For more info go to
     * http://commons.apache.org/httpclient/sslguide.html
     */
    private String getResults(
    Map<String, String> parameters, String methodType, String sslCertStorePath,
    boolean retry
    ) throws HttpException, IOException {

        logger.debug("in getResults; parameters: {}", parameters);
        if (methodType == null || !methodType.equalsIgnoreCase("post")) {
            methodType = "get";
        }

        logger.debug("methodType: {}", methodType);
        // create a singular HttpClient object
        HttpClient client = new HttpClient();

        // establish a connection within 1 second
        int connTimeout = 3000;
        if (BackChannelUtils.getAbpProperties(this)
        .containsKey(BackChannelUtils.ABP_GBP_TIMEOUT)) {
            try {
                connTimeout = Integer.parseInt(
                (String) BackChannelUtils.getAbpProperties(this)
                .get(BackChannelUtils.ABP_GBP_TIMEOUT)
                );
            } catch (Exception e) {
                connTimeout = 3000;
            }
        }
        client.getHttpConnectionManager().getParams()
        .setConnectionTimeout(connTimeout);

        // set the default GBP Admin credentials
        /*
           * if (this.creds != null) {
           * client.getState().setCredentials(AuthScope.ANY, creds); }
           */
        // create a method object
        HttpMethod method = null;

        if (methodType.toLowerCase().equals("get")) {
            StringBuffer params = new StringBuffer("");
            if (parameters != null && !parameters.isEmpty()) {
                params.append("?");
                for (Map.Entry<String, String> e : parameters.entrySet()) {
                    params.append(e.getKey()).append("=").append(e.getValue())
                    .append("&");
                }
                params.deleteCharAt(params.length() - 1);
            }
            String url =
            this.strHost.append(";jsessionid=").append(this.gbpSessionId)
            .append(params).toString();
            logger.debug("url: {}", url);
            //Using GET mechanism and passing the gbpSessionId on the URL
            method = new GetMethod(url);
            // method.setFollowRedirects(true);
        } else {
            //modified the strhost to take the jsessionid to make
            //  sure we get the session object in
            // gbp. this is the only way to figure out session id
            // in gbp or else we got to
            // use the deprecated method "http session context" to
            // check for the existence of session with
            // jsessionid. if jsession id exist then it uses or else
            // it wont append to the url -magesh
            String url;
            if (parameters != null
            && parameters.get(BackChannelUtils.COOKIE_SESSION_ID) != null) {
                url = this.strHost.append(";jsessionid=").append(
                parameters.get(BackChannelUtils.COOKIE_SESSION_ID).toString()
                ).toString();
            } else {
                url = this.strHost.toString();
            }

            // post mechanism
            // modified -magesh
            //PostMethod method1 = new PostMethod(this.strHost.toString());

            PostMethod method1 = new PostMethod(url);

            // Get initial state object

            HttpState initialState = null;

            //check for cookies values that must be set
            if (httpDomain != null && this.gbpCookieName != null
            && this.gbpCookieValue != null) {
                initialState = new HttpState();

                Cookie mycookie = new Cookie(
                this.httpDomain, this.gbpCookieName, this.gbpCookieValue,
                this.httpPath, this.cookieMaxAge, this.isCookieSecure()
                );
                // and then added to your HTTP state instance
                initialState.addCookie(mycookie);
                client.setState(initialState);

                // RFC 2109 cookie management spec is used per default
                // to parse, validate, format & match cookies
                client.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
                // if the above is the wrong format then try
            }
            // Adding parameters to the request via POST
            for (Map.Entry<String, String> e : parameters.entrySet()) {
				if(e != null && e.getKey() != null && e.getValue() != null){
                	method1.addParameter(e.getKey(), e.getValue());
                	logger.debug(e.getKey() + ": " + e.getValue());
				}
            }
            method = method1;
        }

        String responseBody = null;
        try {
            if (this.strHost.indexOf("https") > -1) {
                if (!StringUtils.isEmpty(sslCertStorePath)) {
                    System.setProperty(
                    "javax.net.ssl.trustStore", sslCertStorePath
                    );
                }
            }
            logger.debug("executing method");
            client.executeMethod(method);
            responseBody = method.getResponseBodyAsString();
            //Added by Kannan
            String tmpStr = responseBody;
            if (tmpStr.contains("username") && tmpStr.contains("password")) {
                try {
                    String one =
                    tmpStr.substring(0, tmpStr.indexOf("password") + 25);
                    String two = tmpStr
                    .substring(tmpStr.indexOf("username") - 44, tmpStr.length())
                    ;
                    logger.debug("responseBody::::> " + one + "xxxxxxxx" + two);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.debug("responseBody::in Catch:::==>" + tmpStr);
                }
            } else {
                logger.debug("responseBody in Else: {}", responseBody);
            }
            //End - Kannan
            //logger.debug("responseBody: {}",responseBody);
        } catch (HttpException he) {
            logger.error("Http error connecting to '" + this.strHost + "'");
            logger.error(he.getMessage());
            throw he;
        } catch (IOException ioe) {
            logger.error("Unable to connect to '" + this.strHost + "'");
            throw ioe;
        } finally {
            // clean up the connection resources
            logger.debug("releasing connection and client");
            method.releaseConnection();
            client = null;
        }

        return responseBody;
    }


    /**.
     * Connects to the given remote host
     *
     * @param paramName, sessionId, methodName
     * @return String containing the gbp(Ground) device.xml data
     * @throws Exception
     *             if any methodName parameter is missing
     */

    public String getUpdatedDeviceData(
    String paramName, String sessionId, String methodName
    ) throws Exception {

        if (methodName == null || methodName.length() == 0) {
            throw new Exception("Invalid argument values");
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put(paramName, methodName);
            if (sessionId != null) {
                params.put(BackChannelUtils.COOKIE_SESSION_ID, sessionId);
            }
            return getResults(params, "post", null).trim();
        }

    }


    /**.
     * Connects to the given remote host and sends ABP user IP and ABP flight
     * info where the user is in. retrieves a fresh GBP Session ID. All two
     * parameters are required.
     *
     * @param IP
     * @param flightInfoXML
     * @return String containing the generated GBP Session ID
     * @throws Exception
     *             if any parameter is missing
     */
    public String getNewGBPSessionId(String IP, String flightInfoXML)
    throws Exception {

        if (IP == null || IP.length() == 0 || flightInfoXML == null
        || flightInfoXML.length() == 0) {
            throw new Exception("Invalid argument values");
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put(BackChannelUtils.ABP_USER_IP, IP);
            params.put(BackChannelUtils.ABP_FLIGHT_INFO, flightInfoXML);
            return getResults(params, "post", null).trim();
        }
    }

    //added sessionid param to this method - magesh
    /**.
     * Connects to the given remote host and sends ABP user IP and ABP flight
     * info where the user is in. retrieves a fresh GBP Session ID. All two
     * parameters are required.
     *
     * @param userMac String
     * @param ip String
     * @param flightInfoXML String
     * @param paramName String
     * @param sessionId String
     * @param methodName String
     * @return String containing the generated GBP Session ID
     * @throws Exception Exception
     *             if any parameter is missing
     */
    public String getNewGBPSessionId(String macAddress, String IP,
                String flightInfoXML, String paramName,
                String sessionId, String methodName
    ) throws Exception {

        if (IP == null || IP.length() == 0 || flightInfoXML == null
        || flightInfoXML.length() == 0) {
            throw new Exception("Invalid argument values");
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put(paramName, methodName);
            params.put(BackChannelUtils.ABP_USER_IP, IP);
            params.put(BackChannelUtils.ABP_USER_MAC_ADDR, macAddress);
            params.put(BackChannelUtils.ABP_FLIGHT_INFO, flightInfoXML);
            if (sessionId != null) {
                params.put(BackChannelUtils.COOKIE_SESSION_ID, sessionId);
            }
            return getResults(params, "post", null).trim();
        }
    }

    /**.
     * This method getNewGBPSessionId
     * @param userMac String
     * @param passenger String
     * @param flightInfoXML String
     * @param paramName String
     * @param sessionId String
     * @param methodName String
     * @return String containing the generated GBP Session ID
     * @throws Exception Exception
     *             if any parameter is missing
     */
    public String getNewGBPSessionId(String macAddress, AirPassenger passenger,
                    String flightInfoXML, String paramName,
                    String sessionId, String methodName
                ) throws Exception {

        String IP = passenger.getLoginDetails().getIpAddress();
        if (IP == null || IP.length() == 0 || flightInfoXML == null
        || flightInfoXML.length() == 0) {
            throw new Exception("Invalid argument values");
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put(paramName, methodName);
            params.put(BackChannelUtils.ABP_USER_IP, IP);
            params.put(BackChannelUtils.ABP_USER_MAC_ADDR, macAddress);
            params.put(
            BackChannelUtils.GBP_USER_NAME,
            passenger.getLoginDetails().getUsername()
            );
            params.put(
            BackChannelUtils.GBP_CAPTCH_PASSED,
            Boolean.toString(passenger.isCaptchaPassed())
            );
            params.put(BackChannelUtils.ABP_FLIGHT_INFO, flightInfoXML);

            if (sessionId != null) {
                params.put(BackChannelUtils.COOKIE_SESSION_ID, sessionId);
            }
            return getResults(params, "post", null).trim();
        }
    }

    public void setGbpPrepareToLandAlertFlag(
    String methodName, String paramName, String flightInfoXML
    ) throws Exception {
        if (flightInfoXML == null || flightInfoXML.length() == 0) {
            throw new Exception("FlightInformation is null");
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put(paramName, methodName);
            params.put(BackChannelUtils.ABP_FLIGHT_INFO, flightInfoXML);
            getResults(params, "post", null);
        }
    }
    
    public String updateGBPVideoServiceFlag(
    String methodName,String paramName,String flightInfoXML,String cookieId
    )throws Exception {

        if (flightInfoXML == null || flightInfoXML.length() == 0) {
        logger.info("flight Info XML is null");
            throw new Exception("FlightInformation is null");
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.clear();
            params.put(paramName, methodName);
            params.put(BackChannelUtils.ABP_FLIGHT_INFO, flightInfoXML);
            if(cookieId != null) {
            	params.put(BackChannelUtils.COOKIE_SESSION_ID, cookieId);
            }
            
            return  getResults(params, "post", null);
        }
    
    }

    public String getCookieName() {
        return gbpCookieName;
    }

    public void setCookieName(final String st) {
        this.gbpCookieName = st;
    }

    public String getCookieValue() {
        return gbpCookieValue;
    }

    public void setCookieValue(final String st) {
        this.gbpCookieValue = st;
    }

    public String getCookieHttpDomain() {
        return httpDomain;
    }

    public void setCookieHttpDomain(final String st) {
        this.httpDomain = st;
    }

    public String getCookieHttpPath() {
        return httpPath;
    }

    public void setCookieHttpPath(final String st) {
        this.httpPath = st;
    }

    public int getCookieMaxAge() {
        return this.cookieMaxAge;
    }

    public void setCookieMaxAge(final int age) {
        this.cookieMaxAge = age;
    }

    public boolean isCookieSecure() {
        return this.gbpCookieSecurity;
    }

    public void setCookieSecure(final boolean cookieSecurity) {
        this.gbpCookieSecurity = cookieSecurity;
    }

}
