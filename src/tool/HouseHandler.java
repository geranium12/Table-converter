package tool;

import house.ResidentialComplexInfo;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class HouseHandler extends DefaultHandler {
    private static final String HOUSE = "house";
    private static final String PRICE = "price";

    private ResidentialComplexInfo info;
    private String elementValue;
    private int houseNumber = 0;

    @Override
    public void characters(char[] ch, int start, int length) {
        elementValue = new String(ch, start, length);
    }

    @Override
    public void startDocument() {
        info = new ResidentialComplexInfo();
    }

    @Override
    public void endDocument() {
        info.setAveragePrice(info.getSumPrice() / houseNumber);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (HOUSE.equals(qName)) {
            houseNumber++;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (PRICE.equals(qName)) {
            int price = Integer.parseInt(elementValue);
            info.addPrice(price);
        }
    }

    public ResidentialComplexInfo getInfo() {
        return info;
    }
}
