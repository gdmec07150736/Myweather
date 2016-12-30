package com.example.administrator.myweather;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class saxhandler extends DefaultHandler{
    private Map<String,List<String>> citymap=new HashMap<String,List<String>>();
    String cityname="";
    String provincename="";
    public Map<String,List<String>> getCitymap(){
        return citymap;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
       if("Province".equals(qName)){
           provincename=attributes.getValue("name");
           citymap.put(provincename,new ArrayList<String>());
       } else if("City".equals(qName)) {
           cityname = attributes.getValue("name");
       }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if("City".equals(qName)){
            citymap.get(provincename).add(cityname);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
