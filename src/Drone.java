public class Drone {
    private String serialNumber;
    private String model;
    private double maxPayload;
    private double batteryLife;
    private String status;
    private String location;

    // No-argument constructor
    public Drone() {
    }

    // Parameterized constructor
    public Drone(String serialNumber, String model, double maxPayload,
                 double batteryLife, String status, String location) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.maxPayload = maxPayload;
        this.batteryLife = batteryLife;
        this.status = status;
        this.location = location;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getMaxPayload() {
        return maxPayload;
    }

    public void setMaxPayload(double maxPayload) {
        this.maxPayload = maxPayload;
    }

    public double getBatteryLife() {
        return batteryLife;
    }

    public void setBatteryLife(double batteryLife) {
        this.batteryLife = batteryLife;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Drone{" +
                "serialNumber='" + serialNumber + '\'' +
                ", model='" + model + '\'' +
                ", maxPayload=" + maxPayload +
                ", batteryLife=" + batteryLife +
                ", status='" + status + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
