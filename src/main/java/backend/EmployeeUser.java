package backend;


import backend.TheInterface;

public class EmployeeUser implements TheInterface {
    private String employeeId;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    public EmployeeUser(String employeeId, String name, String email, String address, String phoneNumber) {
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    public String lineRepresentation()
    {
        return employeeId + "," + name + "," + email + "," + address + "," + phoneNumber;
    }
    public String getSearchKey()
    {
        return employeeId;
    }
    public String getEmployeeId() {
        return employeeId;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getAddress() {
        return address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email= email;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}