package com.bridgelabz.addressbook;

import java.util.*;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import java.io.FileWriter;
import java.io.IOException;


public class AddressBookMain {
    public static void main(String[] args) {
        // Create an AddressBookSystem
        AddressBookSystem system = new AddressBookSystem();

        // Create an AddressBook
        AddressBook book1 = new AddressBook("Friends");

        String fileName = "/Users/arnavsaharan/Desktop/JavaClassroom/src/com/bridgelabz/addressbook/addressbook.txt";

        // Add the AddressBook to the system
        system.addAddressBook(book1);

        // Create and add contacts
        Contact contact1 = new Contact("Arnav", "Saharan", "Street 1", "Delhi", "Delhi", "110001", "9876543210", "arnav@example.com");
        Contact contact2 = new Contact("Mayank", "Sharma", "Street 2", "Mumbai", "Maharashtra", "400001", "8765432109", "mayank@example.com");
        Contact contact3 = new Contact("Kanika", "Agarwal", "Street 3", "Nagpur", "Maharashtra", "400001", "8668321235", "mayank@example.com");
        Contact contact4 = new Contact("Shreyas", "Agarwal", "Street 3", "Nagpur", "Maharashtra", "400001", "8668321235", "mayank@example.com");



        book1.addContact(contact1);
        book1.addContact(contact2);
        book1.addContact(contact3);
        book1.addContact(contact4);


        book1.writeToFile(fileName);

        // Search by city and state
        system.searchPersonsByCity("Delhi");
        system.searchPersonsByState("Maharashtra");

        // Build city/state mappings and view
        system.buildCityAndStateMappings();
        System.out.println("People in Mumbai: " + system.viewPersonsByCity("Mumbai"));
        System.out.println("People in Delhi: " + system.viewPersonsByCity("Delhi"));

        // Sorting contacts in the AddressBook
        book1.sortContactsByName();
        book1.sortContactsByCity();
        book1.sortContactsByState();
        book1.sortContactsByZip();

        // Display contact counts by city and state
        System.out.println("Count by City: " + system.getCountByCity());
        System.out.println("Count by State: " + system.getCountByState());
    }
}

// Address Book System to manage multiple address books
class AddressBookSystem {
    private final HashMap<String, AddressBook> addressBooks = new HashMap<>();
    private final HashMap<String, List<Contact>> cityToPersons = new HashMap<>();
    private final HashMap<String, List<Contact>> stateToPersons = new HashMap<>();

    public void addAddressBook(AddressBook book) {
        if (addressBooks.containsKey(book.getName())) {
            System.out.println("Address Book with this name already exists!");
        } else {
            addressBooks.put(book.getName(), book);
            System.out.println("Address Book '" + book.getName() + "' added successfully.");
        }
    }

    // Search for persons in a specific city
    public void searchPersonsByCity(String city) {
        System.out.println("Searching for people in city: " + city);
        addressBooks.values().stream()
                .flatMap(book -> book.getContacts().stream())
                .filter(contact -> contact.getCity().equalsIgnoreCase(city))
                .forEach(contact -> {
                    System.out.println("Found in City -> " + city);
                    System.out.println(contact.getFirstName() + " " + contact.getLastName()
                            + ", Phone: " + contact.getPhoneNumber()
                            + ", Email: " + contact.getEmail());
                });
    }

    // Search for persons in a specific state
    public void searchPersonsByState(String state) {
        System.out.println("Searching for people in state: " + state);
        addressBooks.values().stream()
                .flatMap(book -> book.getContacts().stream())
                .filter(contact -> contact.getState().equalsIgnoreCase(state))
                .forEach(contact -> {
                    System.out.println("Found in State -> " + state);
                    System.out.println(contact.getFirstName() + " " + contact.getLastName()
                            + ", Phone: " + contact.getPhoneNumber()
                            + ", Email: " + contact.getEmail());
                });
    }

    /*
    public void buildCityMappings() {
    cityToPersons.clear();
    addressBooks.values().forEach(addressBook -> {
        addressBook.getContacts().forEach(contact -> {
            cityToPersons.computeIfAbsent(contact.getCity(), k -> new ArrayList<>()).add(contact);
        });
    });
}

public void buildStateMappings() {
    stateToPersons.clear();
    addressBooks.values().forEach(addressBook -> {
        addressBook.getContacts().forEach(contact -> {
            stateToPersons.computeIfAbsent(contact.getState(), k -> new ArrayList<>()).add(contact);
        });
    });
}

public void buildCityAndStateMappings() {
    buildCityMappings();
    buildStateMappings();
}

    */

    public void buildCityAndStateMappings() {
        cityToPersons.clear();
        stateToPersons.clear();

        addressBooks.values().forEach(addressBook -> {
            addressBook.getContacts().forEach(contact -> {
                cityToPersons.computeIfAbsent(contact.getCity(), k -> new ArrayList<Contact>()).add(contact);
                stateToPersons.computeIfAbsent(contact.getState(), k -> new ArrayList<Contact>()).add(contact);
            });
        });
    }

    public List<Contact> viewPersonsByCity(String city) {
        return cityToPersons.getOrDefault(city, new ArrayList<>());
    }

    public List<Contact> viewPersonsByState(String state) {
        return stateToPersons.getOrDefault(state, new ArrayList<>());
    }

    // Get contact count by city and state using Java Streams
    public Map<String, Long> getCountByCity() {
        return addressBooks.values().stream()
                .flatMap(book -> book.getContacts().stream())
                .collect(Collectors.groupingBy(Contact::getCity, Collectors.counting()));
    }

    public Map<String, Long> getCountByState() {
        return addressBooks.values().stream()
                .flatMap(book -> book.getContacts().stream())
                .collect(Collectors.groupingBy(Contact::getState, Collectors.counting()));
    }
}

// Contact class with necessary fields and methods
class Contact {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String city;
    private final String state;
    private final String zip;
    private final String email;
    private String phoneNumber;

    public Contact(String firstName, String lastName, String address, String city, String state, String zip, String phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getZip() {
        return zip;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + city + ", " + state + ")";
    }

    // Implement equals() and hashCode() to check for duplicate contacts
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contact contact = (Contact) obj;
        return firstName.equals(contact.firstName) && lastName.equals(contact.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    public String getAddress() {
        return address;
    }
}

// Address Book class with contacts
class AddressBook {
    private final String name;
    private final List<Contact> contactList = new ArrayList<>();

    public AddressBook(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addContact(Contact contact) {
        if (contactList.contains(contact)) {
            System.out.println("Duplicate contact! A person with the same name already exists.");
            return;
        }
        contactList.add(contact);
        System.out.println("Contact added successfully.");
    }


    // Method to write to file
//    public void writeToFile(String fileName){
//        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
//            for(Contact contact : contactList){
//                writer.write(contact.getFirstName() + "," + contact.getLastName() + "," +
//                        contact.getAddress() + "," + contact.getCity() + "," + contact.getState() + "," +
//                        contact.getZip() + "," + contact.getPhoneNumber() + "," + contact.getEmail());
//
//                writer.newLine();
//            }
//            System.out.println("Contacts successfully written to file: " + fileName);
//        }catch (IOException e){
//            System.out.println("Error writing to file: " + e.getMessage());
//        }
//    }


    // Method to read from file
//    public void readFromFile(String fileName){
//        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
//            String line;
//            contactList.clear();
//            while((line = reader.readLine()) != null){
//                String[] parts = line.split(",");
//                if (parts.length == 8) {
//                    Contact contact = new Contact(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);
//                    contactList.add(contact);
//                }
//            }
//            System.out.println("Contacts successfully read from file: " + fileName);
//        }catch (IOException e){
//            System.out.println("Error reading from file: " + e.getMessage());
//        }
//    }

//    public void writeToFile(String fileName) {
//        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName))) {
//            // Add header row (if needed)
//            String[] header = { "First Name", "Last Name", "Address", "City", "State", "Zip", "Phone Number", "Email" };
//            csvWriter.writeNext(header);
//
//            // Write each contact to the CSV
//            for (Contact contact : contactList) {
//                String[] contactData = {
//                        contact.getFirstName(),
//                        contact.getLastName(),
//                        contact.getAddress(),
//                        contact.getCity(),
//                        contact.getState(),
//                        contact.getZip(),
//                        contact.getPhoneNumber(),
//                        contact.getEmail()
//                };
//                csvWriter.writeNext(contactData);
//            }
//            System.out.println("Contacts successfully written to file: " + fileName);
//        } catch (IOException e) {
//            System.out.println("Error writing to file: " + e.getMessage());
//        }
//    }

    public void writeToFile(String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // For a nicely formatted JSON
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(contactList, writer);
            System.out.println("Contacts successfully written to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

//    public void readFromFile(String fileName) {
//        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
//            String[] line;
//            contactList.clear(); // Clear the current list before reading
//            while ((line = csvReader.readNext()) != null) {
//                if (line.length == 8) { // Ensure the row has all required fields
//                    Contact contact = new Contact(line[0], line[1], line[2], line[3], line[4], line[5], line[6], line[7]);
//                    contactList.add(contact);
//                }
//            }
//            System.out.println("Contacts successfully read from file: " + fileName);
//        } catch (IOException | CsvValidationException e) {
//            System.out.println("Error reading from file: " + e.getMessage());
//        }
//    }

    public void readFromFile(String fileName) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(fileName)) {
            Type listType = new TypeToken<List<Contact>>() {}.getType();
            List<Contact> contacts = gson.fromJson(reader, listType);
            contactList.clear();
            contactList.addAll(contacts);
            System.out.println("Contacts successfully read from file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    public void sortContactsByName() {
        contactList.sort(Comparator.comparing(Contact::getFirstName)
                .thenComparing(Contact::getLastName));
        System.out.println("Contacts sorted by name successfully.");
    }

    // Sort contacts by city
    public void sortContactsByCity() {
        contactList.sort(Comparator.comparing(Contact::getCity));
        System.out.println("Contacts sorted by city successfully.");
    }

    // Sort contacts by state
    public void sortContactsByState() {
        contactList.sort(Comparator.comparing(Contact::getState));
        System.out.println("Contacts sorted by state successfully.");
    }

    // Sort contacts by ZIP code
    public void sortContactsByZip() {
        contactList.sort(Comparator.comparing(Contact::getZip));
        System.out.println("Contacts sorted by ZIP successfully.");
    }

    public List<Contact> getContacts() {
        return contactList;
    }
}
