package Model;

public class User {

    private int id;
    private String login;
    private String password;
    private String name;
    private String surname;
    private String fatherName;
    private String city;
    private String street;
    private String house;
    private int flat;

    public User(){

    }

    public User(int id, String login, String password, String name, String surname,
                String fatherName, String city, String street, String house, int flat) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.fatherName = fatherName;
        this.city = city;
        this.street = street;
        this.flat = flat;
        this.house = house;
    }

    public String getFIO() {
        return surname + " " + name.substring(0, 1).toUpperCase() + ". " + fatherName.substring(0, 1).toUpperCase() + ".";
    }

    public String getFullName() {
        return surname + " " + name + " " + fatherName;
    }

    public String getAddress() {
        return "г. " + city + ", ул. " + street + ", д. " + house + ", кв. " + flat;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getFlat() {
        return flat;
    }

    public void setFlat(int flat) {
        this.flat = flat;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }
}
