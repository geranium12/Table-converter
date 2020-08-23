package tool;

import house.House;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class BinaryWriter {
    public static void write(ArrayList<House> houses, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(houses);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Writing error!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static ArrayList<House> read(String filename) {
        ArrayList<House> houses = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            houses = ((ArrayList<House>) ois.readObject());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Reading! error!",
                    JOptionPane.ERROR_MESSAGE);
        }
        return houses;
    }
}
