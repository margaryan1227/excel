package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;

public class XmlResolver {
    public static void main(String[] args) {
        try {
            // Specify the path to your XSD file
            File xsdFile = new File("C:\\Users\\marga\\IdeaProjects\\excel-script\\src\\main\\resources\\Bausteine\\ArtikelEndoprotheseBasis.xsd");

            // Create a DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xsdFile);

            // Create XPath
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();

            // Find complexType elements with choice
            XPathExpression expr = xPath.compile("//xs:complexType");
            NodeList complexTypesWithChoice = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            // Iterate over complexType elements with choice
            for (int i = 0; i < complexTypesWithChoice.getLength(); i++) {
                Element complexType = (Element) complexTypesWithChoice.item(i);
                String complexTypeName = complexType.getAttribute("name");

                System.out.println("ComplexType Name: " + complexTypeName);

                // Find elements within the choice
                NodeList choiceElements = (NodeList) xPath.evaluate(
                        "xs:sequence/xs:choice/xs:element", complexType, XPathConstants.NODESET);

                // Print elements within the choice
                for (int j = 0; j < choiceElements.getLength(); j++) {
                    Element choiceElement = (Element) choiceElements.item(j);
                    String elementName = choiceElement.getAttribute("name");

                    System.out.println("  Element Name: " + elementName);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}