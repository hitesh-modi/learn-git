package com.test.xmlparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XmlParser {
	public static void main(String[] args) throws FileNotFoundException, XMLStreamException, JAXBException {
		final QName qName = new QName("academic");
		File file = new File("D:\\sample.xml");
		InputStream inputStream = new FileInputStream(file);
		
		// Create xml reading stuff
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);
		
		// Initialize Jaxb
		JAXBContext jaxbContext = JAXBContext.newInstance(Academic.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		XMLEvent event = null;
		
		while((event = xmlEventReader.peek()) != null) {
			if(event.isStartElement() && ((StartElement)event).getName().equals(qName)) {
				Academic academic = unmarshaller.unmarshal(xmlEventReader, Academic.class).getValue();
				System.out.println("academic : " + academic);
			}
			else {
                xmlEventReader.next();
            }
		}
		
	}
}
