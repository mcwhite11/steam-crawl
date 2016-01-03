package com.may1635.utilities;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class appIdHandler extends DefaultHandler
{
		private boolean gettingAppId = false;
		public ArrayList<String> appIds = new ArrayList<String>();
		
	   @Override
	   public void startElement(String uri, 
	   String localName, String qName, Attributes attributes)
	      throws SAXException {
	      if (qName.equalsIgnoreCase("appId")) {
	         gettingAppId = true;
	        
	      }
	      else gettingAppId = false;
	   }

	   @Override
	   public void endElement(String uri, 
	   String localName, String qName) throws SAXException {
		   gettingAppId= false;
	   }

	   @Override
	   public void characters(char ch[], 
	      int start, int length) throws SAXException {
	      if (gettingAppId) {
	    	  appIds.add(new String(ch, start, length));
	      }
	   }
	}
