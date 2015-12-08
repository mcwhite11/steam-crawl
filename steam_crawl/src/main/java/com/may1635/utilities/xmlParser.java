package com.may1635.utilities;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class xmlParser {
	
	public static ArrayList<String> parseXMLFile()
	{
		try {	
	         File inputFile = new File("GameData_9_28.xml");
	         SAXParserFactory factory = SAXParserFactory.newInstance();
	         SAXParser saxParser = factory.newSAXParser();
	         appIdHandler idhandler = new appIdHandler();
	         saxParser.parse(inputFile, idhandler);    
	         
	         return idhandler.appIds;
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		return null;
	}
	
	
		
}

	
	
	


