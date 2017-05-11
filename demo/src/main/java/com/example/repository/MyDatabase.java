package com.example.repository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
    Acts as a database, but it is just an interface for working with certs.txt file.
    The first line in cert.txt file is current counter (the number of issued certificates so far)
    Each next line is of the following format:
            serial_number status is_certificate_authority unique_name
    status can be "valid" if the certficate is still active and valid or "revoked" if the certificate has been withdrawn
    is_certificate_authority can be either "true" or "false"
    unique_name is of the following format: organisation-organisation unit. It is unique for every subject,
                but we can have multiple certificates in our "database" with the same unique_name but different
                status (only one can be valid at a time)
 */
public class MyDatabase {

    //a file that we use as a "database"
    public static final String fileName = "certs.txt";


    //Reads the first line from certs.txt (counter) and returns it
    public int getCounter() {
        try {
            Scanner scanner = new Scanner(new FileReader(fileName));
            String counter = scanner.nextLine();
            return Integer.parseInt(counter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //Increments the first line from certs.txt (counter) and returns it
    private void incrementCounter(){
        try {
            List<String> lines = new ArrayList<>(Files.readAllLines(Paths.get("certs.txt"), StandardCharsets.UTF_8));
            String counterStr = lines.get(0);
            int counter = Integer.parseInt(counterStr);
            counter++;
            counterStr = String.valueOf(counter);
            lines.set(0, counterStr);
            Files.write(Paths.get("certs.txt"), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Writes a new line to the certs.txt file of the following format:
    serial_number status is_ca unique_name and uncrements the counter.
    Gets a serial number from certs.txt file aswell.
    for parameters (true, "UNS-FTN"), if the current counter is 6, will write:
        "6 valid true UNS-FTN"
     */
    public void writeNew(boolean isCa, String name) {
        try {
            int counter = getCounter();
            FileWriter fw = new FileWriter(fileName, true);
            fw.write(counter + " valid " + String.valueOf(isCa) + " " + name);
            fw.flush();
            fw.close();
            incrementCounter();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Returns a list of all (Organisation - Organisation unit)s pairs whose
    certificate is still valid in our "database".
    It is passed to the front end for the user to choose who he wants to sign
    his soon to be made certificate.
     */
    public List<String> getAllCAs(){
        List<String> lines = null;
        try {
            lines = new ArrayList<>(Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> caList = new ArrayList<>();
        for(int i = 1; i < lines.size();i++){
            String[] tokens = lines.get(i).split(" ");
            if(tokens[1].equals("valid") & tokens[2].equals("true")) // if it is valid and it is certificate authority,
                caList.add(tokens[3]);                               // add its name to the list
        }
        return caList;
    }

    /*
    Given a serial number of a certificate, returns it's status.
    It scans through the certs.txt to find a line that describes
    a certificate with the given serial number.
    Returns:
        - 1 if the certificate with the given serial number is valid
        - 2 if it is status is "revoked" (or status is corrupted)
        - 0 if the certificate doesn't exist in our "database"
     */
    public int getStatus(String serialNumber){
        List<String> lines = null;
        try {
            lines = new ArrayList<>(Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean found = false;
        String status = "";
        for(int i = 1; i < lines.size();i++){
            String[] tokens = lines.get(i).split(" ");
            if(tokens[0].equals(serialNumber)){
                found = true;
                status = tokens[1];
                break;
            }
        }
        if(found){
            if(status.equals("valid"))
                return 1;  // valid
            else
                return 2; // revoked
        }else{
            return 0; // not found (unknown)
        }

    }
    /*
    Given a serial number of a certificate, changes it's status to revoked.
    It scans through the certs.txt to find a line that describes
    a certificate with the given serial number. Once the line has
    been found, changes it's "status" field to "revoked".
    Returns whether or not the operation has been successful.
    */
    public boolean withdraw(String serialNumber){
        List<String> lines = null;
        try {
            lines = new ArrayList<>(Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean successfullyRevoked = false;

        for (int i = 1; i < lines.size(); i++) {
            String[] tokens = lines.get(i).split(" ");
            if(tokens[0].equals(serialNumber)){ // if the serial numbers match
                lines.set(i, tokens[0] +" revoked "+ tokens[2] +" " + tokens[3]);
                successfullyRevoked = true;
                break;
            }
        }
        try {
            Files.write(Paths.get(fileName), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return successfullyRevoked;
    }

    /*
    Given a unique name (of the format "organisation-organisation_unit"), gives
    a serial number of this subjects valid certificate (if it exists).
     */
    public String getSerialByName(String name){
        List<String> lines = null;
        String serial = "";
        try {
            lines = new ArrayList<>(Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 1; i < lines.size(); i++){
            String[] tokens = lines.get(i).split(" ");
            if(tokens[3].equals(name) && tokens[1].equals("valid") ){
                serial = tokens[0];
            }
        }
        return serial;
    }

    /*
    Checks if a valid certificate for a given subject ("organisation-organisation_unit")
    already exists in certs.txt. If it does not, we shall not be able to issue a new
    certificate to this subject.
     */
    public boolean containsValid(String name){
        List<String> lines = null;
        String serial = "";
        try {
            lines = new ArrayList<>(Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 1; i < lines.size(); i++){
            String[] tokens = lines.get(i).split(" ");
            if(tokens[3].equals(name) && tokens[1].equals("valid"))
                return true;
        }
        return false;
    }

}
