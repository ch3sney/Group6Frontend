import java.time.LocalDate;

public class Drone {

    private String serial;
    private int manufacturer;
    private double weightCapacity;
    private int yearManufactured;
    private double distanceAutonomy;
    private String status;
    private double maxSpeed;
    private String name;
    private LocalDate warrantyExpiration;
    private String model;
    private String currentLocation;
    private String warehouseAddr;

    // No-argument constructor
    public Drone() {
    }

    // Parameterized constructor
    public Drone(String serial, int manufacturer, double weightCapacity, int yearManufactured, double distanceAutonomy, String status, double maxSpeed, String name, LocalDate warrantyExpiration, String model, String currentLocation, String warehouseAddr) {
        this.serial = serial;
        this.manufacturer = manufacturer;
        this.weightCapacity = weightCapacity;
        this.yearManufactured = yearManufactured;
        this.distanceAutonomy = distanceAutonomy;
        this.status = status;
        this.maxSpeed = maxSpeed;
        this.name = name;
        this.warrantyExpiration = warrantyExpiration;
        this.model = model;
        this.currentLocation = currentLocation;
        this.warehouseAddr = warehouseAddr;
    }

    // Getters and Setters

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public int getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(int manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getWeightCapacity() {
        return weightCapacity;
    }

    public void setWeightCapacity(double weightCapacity) {
        this.weightCapacity = weightCapacity;
    }

    public int getYearManufactured() {
        return yearManufactured;
    }

    public void setYearManufactured(int yearManufactured) {
        this.yearManufactured = yearManufactured;
    }

    public double getDistanceAutonomy() {
        return distanceAutonomy;
    }

    public void setDistanceAutonomy(double distanceAutonomy) {
        this.distanceAutonomy = distanceAutonomy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public LocalDate getWarrantyExpiration() {
        return warrantyExpiration;
    }

    public void setWarrantyExpiration(LocalDate warrantyExpiration) {
        this.warrantyExpiration = warrantyExpiration;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getWarehouseAddr() {
        return warehouseAddr;
    }

    public void setWarehouseAddr(String warehouseAddr) {
        this.warehouseAddr = warehouseAddr;
    }

    @Override
    public String toString() {
        return "Drone{" + "serial='" + serial + '\'' + ", manufacturer=" + manufacturer + ", weightCapacity=" + weightCapacity + ", yearManufactured=" + yearManufactured + ", distanceAutonomy=" + distanceAutonomy + ", status='" + status + '\'' + ", maxSpeed=" + maxSpeed + ", name='" + name + '\'' + ", warrantyExpiration=" + warrantyExpiration + ", model='" + model + '\'' + ", currentLocation='" + currentLocation + '\'' + ", warehouseAddr='" + warehouseAddr + '\'' + '}';
    }
}
