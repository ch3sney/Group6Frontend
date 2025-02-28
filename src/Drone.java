public class Drone {
    private String serialNumber;
    private int yearManufactured;
    private double weightCapacity;
    private double distanceAutonomy;
    private double maxSpeed;
    private String name;
    private String model;
    private String status;
    private String warrantyExpiration;

    // No-argument constructor
    public Drone() {
    }

    // Parameterized constructor
    public Drone(String serialNumber, int yearManufactured, double weightCapacity, double distanceAutonomy,
                 double maxSpeed, String name, String model, String status, String warrantyExpiration) {
        this.serialNumber = serialNumber;
        this.yearManufactured = yearManufactured;
        this.weightCapacity = weightCapacity;
        this.distanceAutonomy = distanceAutonomy;
        this.maxSpeed = maxSpeed;
        this.name = name;
        this.model = model;
        this.status = status;
        this.warrantyExpiration = warrantyExpiration;
    }

    // Getters and Setters
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getYearManufactured() {
        return yearManufactured;
    }

    public void setYearManufactured(int yearManufactured) {
        this.yearManufactured = yearManufactured;
    }

    public double getWeightCapacity() {
        return weightCapacity;
    }

    public void setWeightCapacity(double weightCapacity) {
        this.weightCapacity = weightCapacity;
    }

    public double getDistanceAutonomy() {
        return distanceAutonomy;
    }

    public void setDistanceAutonomy(double distanceAutonomy) {
        this.distanceAutonomy = distanceAutonomy;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWarrantyExpiration() {
        return warrantyExpiration;
    }

    public void setWarrantyExpiration(String warrantyExpiration) {
        this.warrantyExpiration = warrantyExpiration;
    }

    // ToString Method
    @Override
    public String toString() {
        return "Drone{" +
                "serialNumber='" + serialNumber + '\'' +
                ", yearManufactured=" + yearManufactured +
                ", weightCapacity=" + weightCapacity +
                ", distanceAutonomy=" + distanceAutonomy +
                ", maxSpeed=" + maxSpeed +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", status='" + status + '\'' +
                ", warrantyExpiration='" + warrantyExpiration + '\'' +
                '}';
    }
}
