package house;

import java.io.Serializable;

public class House implements Serializable {
    private int id;
    private HouseType type;
    private int floors;
    private int rooms;
    private int year;
    private int cars;
    private int price;

    public House(int id) {
        this.id = id;
    }

    public House(int id, HouseType type, int floors, int rooms, int year, int cars, int price) {
        this.id = id;
        this.type = type;
        this.floors = floors;
        this.rooms = rooms;
        this.year = year;
        this.cars = cars;
        this.price = price;
    }

    public Object[] toArray() {
        Object[] o = {id, type, floors, rooms, year, cars, price};
        return o;
    }

    public int getId() {
        return id;
    }

    public HouseType getType() {
        return type;
    }

    public int getFloors() {
        return floors;
    }

    public int getRooms() {
        return rooms;
    }

    public int getYear() {
        return year;
    }

    public int getCars() {
        return cars;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "House{" +
                "id=" + id +
                ", type=" + type +
                ", floors=" + floors +
                ", rooms=" + rooms +
                ", year=" + year +
                ", cars=" + cars +
                ", price=" + price +
                '}';
    }
}
