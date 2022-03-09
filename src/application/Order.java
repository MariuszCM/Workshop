package application;

import java.util.Random;

public class Order {
    int id;
    String name;
    String lastname;
    String city;
    String street;
    int buildingNumber;
    Random rand = new Random();

    String[] names = {"Zofia", "Marek", "Andrzej", "Igor", "Kasia", "Aneta", "Krzysztof", "Emilia", "Ilona", "Alicja", "Hubert"};
    String[] lastnames = {"Kowalski", "Nowak", "Lukasik", "Rajczak", "Cieslak", "Siwiec", "Lentas", "Kulpa", "Krzecio", "Skwara", "Dyszpit", "Mitura", "Rychta", "Sulej", "Dziekan"};
    String[] cities = {"Wrocalaw", "Warszawa", "Sopot", "Bialystok", "Zakopane"};
    String[] streets = {"Prosta", "Dluga", "Warszawska", "Krotka", "Polna", "Chodecka", "Gleboca", "Kolejowa"};

    Order(int id) {

        this.id = id;

        int a = rand.nextInt(names.length);
        this.name = names[a];

        a = rand.nextInt(lastnames.length);
        this.lastname = lastnames[a];

        a = rand.nextInt(cities.length);
        this.city = cities[a];

        a = rand.nextInt(streets.length);
        this.street = streets[a];

        a = rand.nextInt(300);
        this.buildingNumber = a;

    }

    Order() {
        this.name = "None";
        this.lastname = "None";
        this.city = "None";
        this.street = "None";
        this.buildingNumber = -1;
        this.id = -1;
    }

    public void displayData() {
        System.out.println("Order id : " + this.id);
        System.out.println("Name : " + this.name);
        System.out.println("lastame : " + this.lastname);
        System.out.println("City : " + this.city);
        System.out.println("Street : " + this.street);
        System.out.println("Building : " + this.buildingNumber);
    }

    public String returnNameData() {
        return name + " " + lastname;
    }

    public String returnAddressData() {
        return city + " " + street + " " + buildingNumber;
    }
}
