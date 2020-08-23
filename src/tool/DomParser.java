package tool;

import app.Application;
import exception.WrongDataFormatException;
import house.House;
import house.HouseType;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DomParser {
    public static ArrayList<House> read(File file) throws ParserConfigurationException, SAXException,
            IOException, IllegalArgumentException {
        ArrayList<House> houseArrayList = new ArrayList<>();
        int id, floors = 0, rooms = 0, year = 0, cars = 0, price = 0;
        HouseType type = null;

        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(file);

        Node root = document.getDocumentElement();
        NodeList houses = root.getChildNodes();

        for (int i = 0; i < houses.getLength(); i++) {
            Node house = houses.item(i);
            if (house.getNodeType() != Node.TEXT_NODE) {
                id = Integer.parseInt(house.getAttributes().getNamedItem("id").getNodeValue());
                NodeList houseProps = house.getChildNodes();
                for (int j = 0; j < houseProps.getLength(); j++) {
                    Node houseProp = houseProps.item(j);
                    if (houseProp.getNodeType() != Node.TEXT_NODE) {
                        switch (houseProp.getNodeName()) {
                            case ("type"): {
                                type = HouseType.valueOf(houseProp.getChildNodes().item(0).getTextContent());
                                break;
                            }
                            case ("floors"): {
                                floors = Integer.parseInt(houseProp.getChildNodes().item(0).getNodeValue());
                                break;
                            }
                            case ("rooms"): {
                                rooms = Integer.parseInt(houseProp.getChildNodes().item(0).getNodeValue());
                                break;
                            }
                            case ("yearBuilt"): {
                                year = Integer.parseInt(houseProp.getChildNodes().item(0).getNodeValue());
                                break;
                            }
                            case ("garageCars"): {
                                cars = Integer.parseInt(houseProp.getChildNodes().item(0).getNodeValue());
                                break;
                            }
                            case ("price"): {
                                price = Integer.parseInt(houseProp.getChildNodes().item(0).getNodeValue());
                            }
                        }
                    }
                }
                houseArrayList.add(new House(id, type, floors, rooms, year, cars, price));
            }
        }
        return houseArrayList;
    }

    public static void writeToXmlFile(File file, ArrayList<House> houses) throws
            TransformerFactoryConfigurationError, DOMException, ParserConfigurationException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = document.createElement("root");

        for (House house : houses) {
            Element element = getHouse(document, house);
            root.appendChild(element);
        }

        document.appendChild(root);
        writeDocument(document, file);
    }

    private static Element getHouse(Document document, House house) {
        Element element = document.createElement("house");
        element.setAttribute("id", Integer.toString(house.getId()));

        getHouseElements(document, element, "type", house.getType().toString());
        getHouseElements(document, element, "floors", Integer.toString(house.getFloors()));
        getHouseElements(document, element, "rooms", Integer.toString(house.getRooms()));
        getHouseElements(document, element, "yearBuilt", Integer.toString(house.getYear()));
        getHouseElements(document, element, "garageCars", Integer.toString(house.getCars()));
        getHouseElements(document, element, "price", Integer.toString(house.getPrice()));

        return element;
    }


    private static void getHouseElements(Document document, Element element, String name, String value) {
        Element elem = document.createElement(name);
        elem.appendChild(document.createTextNode(value));
        element.appendChild(elem);
    }

    private static void writeDocument(Document document, File file) throws TransformerFactoryConfigurationError {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            FileOutputStream fos = new FileOutputStream(file);
            StreamResult result = new StreamResult(fos);
            tr.transform(source, result);

        } catch (TransformerException | IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "XML error", JOptionPane.ERROR_MESSAGE);
        }
    }
}