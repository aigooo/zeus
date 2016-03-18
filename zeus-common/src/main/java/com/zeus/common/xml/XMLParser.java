package com.zeus.common.xml;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
	
    public static Map<String,Object> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is =  Util.getStringStream(xmlString);
        Document document = builder.parse(is);

        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, Object> map = new HashMap<String, Object>();
        int i=0;
        while (i < allNodes.getLength()) {
            node = allNodes.item(i);
            if(node instanceof Element){
                map.put(node.getNodeName(),node.getTextContent());
            }
            i++;
        }
        return map;
    }
    
    public static String  parseObject(Object obj) throws ParserConfigurationException, IllegalArgumentException, IllegalAccessException{
    	StringBuilder xml = new StringBuilder("<xml>\n");
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field:fields){
        	field.setAccessible(true);
        	Object value = field.get(obj);
        	if(value!=null&&StringUtils.isNotBlank(String.valueOf(value))){
        		xml.append("<").append(field.getName()).append(">");
        		xml.append("<![CDATA[").append(String.valueOf(value)).append("]]>");
        		xml.append("</").append(field.getName()).append(">\n");
        	}
        }
        xml.append("</xml>");
        return xml.toString();
    }
}
