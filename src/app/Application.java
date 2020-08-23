package app;

import exception.WrongDataFormatException;
import house.House;
import house.HouseType;
import house.ResidentialComplexInfo;
import tool.BinaryWriter;
import tool.DomParser;
import org.xml.sax.SAXException;
import tool.HouseHandler;
import tool.XmlValidation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class Application extends JFrame {
    private final static Object[] COLUMNS = {0, HouseType.DETACHED, 1, 1, 1990, 0, 200000};
    private final static String[] COLUMNS_HEADER = {"ID", "Type", "Floors", "Rooms", "YearBuilt", "CarPlaces", "Price"};
    private DefaultTableModel tableModel;
    private ArrayList<House> houses = null;

    Application() {
        tableModel = new DefaultTableModel() {
            @Override
            public Class getColumnClass(int column) {
                return COLUMNS[column].getClass();
            }

            public boolean isCellEditable(int row, int column) {
                if (row == 0)
                    return false;
                else
                    return super.isCellEditable(row, column);
            }
        };

        JTable table = new JTable(tableModel);

        for (String element : COLUMNS_HEADER) {
            tableModel.addColumn(element);
        }

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openDomItem = new JMenuItem("Open using DOM");
        openDomItem.addActionListener(actionEvent -> {
            try {
                openDom();
            } catch (IOException | ParserConfigurationException | SAXException | WrongDataFormatException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Reading error using DOM!", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem saveDomItem = new JMenuItem("Save using DOM");
        saveDomItem.addActionListener(actionEvent -> {
            try {
                saveDom();
            } catch (WrongDataFormatException | ParserConfigurationException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Writing error using DOM!", JOptionPane.ERROR_MESSAGE);
            }
        });
        fileMenu.add(openDomItem);
        fileMenu.add(saveDomItem);

        JMenuItem openBinItem = new JMenuItem("Open binary file");
        openBinItem.addActionListener(actionEvent -> {
            try {
                openBinary();
            } catch (WrongDataFormatException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Restriction error!", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem saveBinItem = new JMenuItem("Save binary file");
        saveBinItem.addActionListener(actionEvent -> {
            try {
                saveBinary();
            } catch (WrongDataFormatException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Writing binary file error!", JOptionPane.ERROR_MESSAGE);
            }
        });

        fileMenu.add(openBinItem);
        fileMenu.add(saveBinItem);

        JMenuItem openSaxItem = new JMenuItem("Get info");
        openSaxItem.addActionListener(actionEvent -> {
            try {
                openSax();
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Reading error using SAX!", JOptionPane.ERROR_MESSAGE);
            }
        });

        fileMenu.add(openSaxItem);

        JMenuItem checkCorrectnessXmlItem = new JMenuItem("Check correctness XML");
        checkCorrectnessXmlItem.addActionListener(actionEvent -> {
            try {
                checkCorrectnessXml();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Wrong XML file!", JOptionPane.ERROR_MESSAGE);
            } catch (SAXException ex) {
                JOptionPane.showMessageDialog(null, ex, "Wrong XML file!", JOptionPane.ERROR_MESSAGE);
            }
        });
        fileMenu.add(checkCorrectnessXmlItem);

        JMenuItem saveHtml = new JMenuItem("Save in .html");
        saveHtml.addActionListener(actionEvent -> {
            try {
                saveHtml();
            } catch (TransformerException | IOException | WrongDataFormatException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Writing HTML file error!", JOptionPane.ERROR_MESSAGE);
            }
        });
        fileMenu.add(saveHtml);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem addItem = new JMenuItem("Add");
        addItem.addActionListener(actionEvent -> {
            try {
                add();
            } catch (WrongDataFormatException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Insertion error!", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(actionEvent -> {
            if (table.getSelectedRow() != -1) {
                tableModel.removeRow(table.getSelectedRow());
            }
        });

        editMenu.add(addItem);
        editMenu.add(deleteItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);

        JComboBox<String> types = new JComboBox<>(Arrays.toString(HouseType.values()).
                replaceAll("[\\[\\]]", "").split(", "));
        DefaultCellEditor typeEditor = new DefaultCellEditor(types);
        table.getColumnModel().getColumn(1).setCellEditor(typeEditor);

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);
        setPreferredSize(new Dimension(640, 480));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }

    private void setData(ArrayList<House> houses) {
        tableModel.getDataVector().clear();

        for (House house : houses) {
            Object[] o = new Object[COLUMNS_HEADER.length];
            o[0] = house.getId();
            o[1] = house.getType();
            o[2] = house.getFloors();
            o[3] = house.getRooms();
            o[4] = house.getYear();
            o[5] = house.getCars();
            o[6] = house.getPrice();
            tableModel.addRow(o);
        }
    }

    private void openDom() throws IOException, ParserConfigurationException, SAXException, WrongDataFormatException {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File openFile = fileChooser.getSelectedFile();
            //XmlValidation.validateXmlSchema(XSD_PATH, openFile.getPath());
            houses = DomParser.read(openFile);
            setData(houses);
        }
    }

    private ArrayList<House> getData() throws WrongDataFormatException {
        Vector<Vector> data = tableModel.getDataVector();
        if (data.size() == 0) {
            throw new WrongDataFormatException("There is no data to save!");
        }
        houses = new ArrayList<>();
        for (Vector datum : data) {
            int id = (int) datum.get(0);
            HouseType type;
            Object value = datum.get(1);
            if (value.getClass() == String.class) {
                type = HouseType.valueOf((String) value);
            } else {
                type = (HouseType) value;
            }
            int floors = (int) datum.get(2);
            int rooms = (int) datum.get(3);
            int year = (int) datum.get(4);
            int cars = (int) datum.get(5);
            int price = (int) datum.get(6);

            House house = new House(id, type, floors, rooms, year, cars, price);
            validateHouse(house);
            houses.add(house);
        }
        return houses;
    }

    private void saveDom() throws WrongDataFormatException, ParserConfigurationException {
        houses = getData();
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            if (saveFile != null) {
                DomParser.writeToXmlFile(saveFile, houses);
            }
        }
    }

    private void openBinary() throws WrongDataFormatException {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File openFile = fileChooser.getSelectedFile();
            houses = BinaryWriter.read(openFile.getPath());
            setData(houses);
        }
    }

    private void saveBinary() throws WrongDataFormatException {
        houses = getData();
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            if (saveFile != null) {
                BinaryWriter.write(houses, saveFile.getPath());
            }
        }
    }

    private void openSax() throws ParserConfigurationException, SAXException, IOException {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File openFile = fileChooser.getSelectedFile();
            //XmlValidation.validateXmlSchema(XSD_PATH, openFile.getPath());
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            HouseHandler houseHandler = new HouseHandler();
            saxParser.parse(openFile.getPath(), houseHandler);
            ResidentialComplexInfo info = houseHandler.getInfo();
            showInfo(info);
        }
    }

    private void showInfo(ResidentialComplexInfo info) {
        String information = "The sum price is " + info.getSumPrice() + "\n" +
                "The average price is " + info.getAveragePrice();
        JOptionPane.showMessageDialog(null, information, "Residential Complex Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void checkCorrectnessXml() throws IOException, SAXException {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File openFile = fileChooser.getSelectedFile();
            URL url = Thread.currentThread().getContextClassLoader().getResource("data/data.xsd");
            XmlValidation.validateXmlSchema(url, openFile.getPath());
            JOptionPane.showMessageDialog(null, "The XML file is correct!",
                    "Check correctness XML", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveHtml() throws WrongDataFormatException, TransformerException, IOException {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            if (saveFile != null) {
                TransformerFactory factory = TransformerFactory.newInstance();
                URL url = Thread.currentThread().getContextClassLoader().getResource("data/data.xsl");
                Transformer transformer = factory.newTransformer(new StreamSource(url.openStream()));
                transformer.transform(new StreamSource(new InputStreamReader(
                        new ByteArrayInputStream(this.getXMLStringFormat().getBytes(
                                StandardCharsets.UTF_8)))), new StreamResult(fileChooser.getSelectedFile()));
            }
        }
    }

    private void add() throws WrongDataFormatException {
        String o;

        o = JOptionPane.showInputDialog(null, "Enter ID");
        if (o == null) {
            return;
        }
        int id = Integer.parseInt(o);

        o = JOptionPane.showInputDialog(null,
                "Enter type of house (DETACHED, TERRACED, MANSION)");
        if (o == null) {
            return;
        }
        HouseType type = HouseType.valueOf(o);

        o = JOptionPane.showInputDialog(null, "Enter number of floors");
        if (o == null) {
            return;
        }
        int floors = Integer.parseInt(o);

        o = JOptionPane.showInputDialog(null, "Enter number of rooms");
        if (o == null) {
            return;
        }
        int rooms = Integer.parseInt(o);

        o = JOptionPane.showInputDialog(null,
                "Enter year when the house was built");
        if (o == null) {
            return;
        }
        int year = Integer.parseInt(o);

        o = JOptionPane.showInputDialog(null, "Enter number of car places");
        if (o == null) {
            return;
        }
        int cars = Integer.parseInt(o);

        o = JOptionPane.showInputDialog(null, "Enter price");
        if (o == null) {
            return;
        }
        int price = Integer.parseInt(o);

        House house = new House(id, type, floors, rooms, year, cars, price);
        validateHouse(house);
        tableModel.addRow(house.toArray());
    }

    private String getXMLStringFormat() throws WrongDataFormatException {
        houses = getData();
        StringBuilder string = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n");
        for (House house : houses)
            string.append(getXMLElement(house));
        string.append("</root>");
        return new String(string);
    }

    private StringBuilder getXMLElement(House house) {
        return new StringBuilder("\t<house id=\"" + house.getId() + "\">\n" +
                "\t\t<type>" + house.getType() + "</type>\n" +
                "\t\t<floors>" + house.getFloors() + "</floors>\n" +
                "\t\t<rooms>" + house.getRooms() + "</rooms>\n" +
                "\t\t<yearBuilt>" + house.getYear() + "</yearBuilt>\n" +
                "\t\t<garageCars>" + house.getCars() + "</garageCars>\n" +
                "\t\t<price>" + house.getPrice() + "</price>\n" +
                "\t</house>\n");
    }

    private void validateHouse(House house) throws WrongDataFormatException {
        if (house.getId() < 1) {
            throw new WrongDataFormatException("ID should be a positive number!");
        }
        if (house.getFloors() < 1 || house.getFloors() > 6) {
            throw new WrongDataFormatException("FLOORS should be between 1 and 6");
        }
        if (house.getRooms() < 1 || house.getRooms() > 30) {
            throw new WrongDataFormatException("ROOMS should be between 1 and 30");
        }
        if (house.getYear() < 1950 || house.getYear() > 2020) {
            throw new WrongDataFormatException("BUILT YEAR should be between 1950 and 2020");
        }
        if (house.getCars() < 0 || house.getCars() > 4) {
            throw new WrongDataFormatException("GARAGE CAR PLACES should be between 0 and 5");
        }
        if (house.getPrice() < 10000 || house.getPrice() > 5500000) {
            throw new WrongDataFormatException("PRICE should be between 10000 and 550000");
        }
    }

    public static void main(String[] args) {
        new Application();
    }
}
