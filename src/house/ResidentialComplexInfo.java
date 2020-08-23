package house;

public class ResidentialComplexInfo {
    private int sumPrice;
    private int averagePrice;

    public ResidentialComplexInfo() {
        sumPrice = 0;
        averagePrice = 0;
    }

    public void addPrice(int price) {
        sumPrice += price;
    }

    public int getSumPrice() {
        return sumPrice;
    }

    public int getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(int averagePrice) {
        this.averagePrice = averagePrice;
    }
}
