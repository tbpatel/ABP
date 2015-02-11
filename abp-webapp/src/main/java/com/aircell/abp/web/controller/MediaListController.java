/*
 * MediaListController.java 22 Aug 2007
 *
 * This source file is Proprietary and Confidential.
 *
 * Redistribution and use in source and binary forms, without consent
 * are strictly forbidden.
 *
 * Copyright (c) 2007 Aricell LLC. All rights reserved.
 */

package com.aircell.abp.web.controller;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.aircell.abp.utils.AircellServletUtils;

/**.
 * Controller to return the status tray data
 * @author AKQA - bryan.swift
 * @version $Revision: 3362 $
 */
public class MediaListController extends AircellCommandController {

   
    private String httpClientGbpPage = null;
    private String methodName = null;
    private String paramName = null;
    
    private static  boolean mediaListConfigured = false;
    
    private static String MediaListView = "success";
    
    private String mediaListConfigPath;
    
    private static Map<String, Object> values = new HashMap<String, Object>();
    
    /**.
     * @see com.aircell.abp.web.controller.AircellCommandController#
     * handler(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object,
     *      org.springframework.validation.BindException)
     */
    @Override
    public ModelAndView handler(
    HttpServletRequest request, HttpServletResponse response, Object command,
    BindException errors
    ) {
        logger.debug("MediaListController.handler" + "...Enters");
        logger.info("Inside media List Handle");
        
        mediaListCheck();
        if(MediaListView.equalsIgnoreCase("success")) {
        	if(AircellServletUtils.isXMLAjax(request)) {
        	response.setContentType("text/xml");
        	}
        	 logger.info("Inside media List Handle :::: inside if Success");
        	return createSuccessMediaModelAndView(request, values);
        } else {
        	if(AircellServletUtils.isXMLAjax(request)) {
            	response.setContentType("text/xml");
            	}
        	return createFailureMediaModelAndView(request, values);
        }
        
    }
    
    private void parseXMLFile() {
    	
    	String tomcat_path = System.getProperty("catalina.base");
    	//method should execute only once.. Setting mediaListConfigured to true 
    	mediaListConfigured = true;
    	try {
        File summaryFile = new File(tomcat_path +getMediaListConfigPath());
        logger.info(tomcat_path +getMediaListConfigPath() + "  Exists : "
        				+ summaryFile.exists());
        if (summaryFile.exists() && summaryFile.isFile()
        		&& summaryFile.length() > 0) {
        
        		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
            	DocumentBuilder db = dbf.newDocumentBuilder();
        	    Document doc = db.parse(summaryFile);
        	    String xmlinString = convertXMLFileToString(summaryFile);
        	    values.put("xml", xmlinString);
        	    //This Following logic has been added to parse and create a json object - Summary XML
        	    //Add the tag if added additionally in the summary XML
                String[] tagList = { "acpuBatchVersion","journalCreationDate","media"};
                
                for(String tag: tagList ){
                	NodeList nodeList = doc.getElementsByTagName(tag);
                	StringBuffer result = new StringBuffer();
                	if(nodeList.getLength()>0) {
                		
                		for(int i=0;i<nodeList.getLength();i++) {
                			
                			Element eNode =(Element) nodeList.item(i);
                			if(eNode.hasAttributes()) {
                				if(eNode.hasAttribute("id")) {
                				result.append(eNode.getAttribute("id"));
                				result.append(",");
                				}
                			} else {
                				result.append(eNode.getTextContent());
                				result.append(",");
                			}
                		}
                		
                		if(result != null && result.charAt(result.length()-1) == ',')
                		result.setLength(result.length() -1);
                	}
                	
                	logger.debug("The Tag in the XML File ::::" + tag);
                	logger.debug("The Tag Value ::::" + result);
				  	if(!(result.toString().length() == 0))
                	values.put(tag, result);
                }
        } else {
        	MediaListView = "Failure";
        }        
        } catch(Exception e) {
        		MediaListView = "Failure";
        		
        }
        
    }
    
    public static String convertXMLFileToString(File filepath) 
	{ 
	    /*
	     * This method to convert the xml to string
	     */
		try { 
	        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();      
	        Document doc = docBuilder.parse(filepath);
	        
	        StringWriter stringWritter = new StringWriter();
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer(); 
	        transformer.transform(new DOMSource(doc), new StreamResult(stringWritter)); 
	        return stringWritter.toString(); 
	    } catch (Exception e) { 
	    	System.out.println("CountXMLContentFromResponse::Exception " + e.getMessage()); 
	    } 
	    return null; 
	}
    private void mediaListCheck() {
    	  if(!mediaListConfigured) {
          	parseXMLFile();
          }
    }

    public String getHttpClientGbpPage() {
        return this.httpClientGbpPage;
    }

    public void setHttpClientGbpPage(String st) {
        this.httpClientGbpPage = st;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String st) {
        this.methodName = st;
    }

    public String getParamName() {
        return this.paramName;
    }

    public void setParamName(String st) {
        this.paramName = st;
    }

	
	public String getMediaListConfigPath() {
		return mediaListConfigPath;
	}

	public void setMediaListConfigPath(String mediaListConfigPath) {
		this.mediaListConfigPath = mediaListConfigPath;
	}

   
}
