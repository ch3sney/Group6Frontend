public class Equipment {
    private String serialNumber;
    private String type;
    private String equipModel;
    private String dimensions;
    private double weight;
    private int year;
    private String description;
    private String location;

    // No-argument constructor
    public Equipment() {
    }

    // Parameterized constructor
    public Equipment(String serialNumber, String type, String equipModel, String dimensions, double weight, int year, String description, String location) {
        this.serialNumber = serialNumber;
        this.type = type;
        this.equipModel = equipModel;
        this.dimensions = dimensions;
        this.weight = weight;
        this.year = year;
        this.description = description;
        this.location = location;
    }

    // Getters and Setters
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEquipModel() {
        return equipModel;
    }

    public void setEquipModel(String equipModel) {
        this.equipModel = equipModel;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Equipment{" + "serialNumber='" + serialNumber + '\'' + ", type='" + type + '\'' + ", equipModel='" + equipModel + '\'' + ", dimensions='" + dimensions + '\'' + ", weight=" + weight + ", year=" + year + ", description='" + description + '\'' + ", location='" + location + '\'' + '}';
    }
}
