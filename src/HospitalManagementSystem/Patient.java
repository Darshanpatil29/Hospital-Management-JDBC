package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection con;
    private Scanner sc;

    public Patient(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }
    public void addPatient(){
        System.out.println("Enter patient name:");
        String name=sc.next();
        System.out.println("Enter patient age:");
        int age=sc.nextInt();
        System.out.println("Enter your Gender:");
        String gender=sc.next();

        try{
            String query="insert into patients(name,age,gender) values(?,?,?)";
            PreparedStatement pstmt=con.prepareStatement(query);
            pstmt.setString(1,name);
            pstmt.setString(2, String.valueOf(age));
            pstmt.setString(3,gender);
            int affectedRows=pstmt.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient added successfully");
            }
            else{
                System.out.println("Some error occurred");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void viewPatient() throws Exception{
        try{
            String query="select * from patients";
            PreparedStatement pstmt=con.prepareStatement(query);
            ResultSet set=pstmt.executeQuery();
            System.out.println("Patients List");
            System.out.println("******************************************************");
            while(set.next()){
                System.out.println("Patient ID:"+set.getInt("pid"));
                System.out.println("Patient Name:"+set.getString("name"));
                System.out.println("Patient Age:"+set.getInt("age"));
                System.out.println("Patient Gender:"+set.getString("gender"));
                System.out.println("******************************************************");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean checkPatient(int id){
        try {
            String query="select * from patients where pid=?";
            PreparedStatement pstmt=con.prepareStatement(query);
            pstmt.setInt(1,id);
            ResultSet set=pstmt.executeQuery();
            //            if(set.next()){
            //                System.out.println("Patient ID:"+set.getInt("id"));
            //                System.out.println("Patient Name:"+set.getString("name"));
            //                System.out.println("Patient Age:"+set.getInt("age"));
            //                System.out.println("Patient Gender:"+set.getString("gender"));
            //                System.out.println("******************************************************");
            //            }

            //                System.out.println("No patient found with this id");
            return set.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void viewPatient(int id) {
        if (checkPatient(id)) {
            try {
                String query = "select * from patients where pid=?";
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setInt(1, id);
                ResultSet set = pstmt.executeQuery();
                System.out.println("Patient Details");
                System.out.println("******************************************************");
                while (set.next()) {
                    System.out.println("Patient ID:" + set.getInt("pid"));
                    System.out.println("Patient Name:" + set.getString("name"));
                    System.out.println("Patient Age:" + set.getInt("age"));
                    System.out.println("Patient Gender:" + set.getString("gender"));
                    System.out.println("******************************************************");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("No patient found with this id");
        }
    }
    // Method to update patient details
    public void updatePatient(int id) {
        if (checkPatient(id)) {
            System.out.println("Enter new patient name:");
            String name = sc.next();
            System.out.println("Enter new patient age:");
            int age = sc.nextInt();
            System.out.println("Enter new patient gender:");
            String gender = sc.next();

            try {
                String query = "UPDATE patients SET name=?, age=?, gender=? WHERE pid=?";
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setString(1, name);
                pstmt.setInt(2, age);
                pstmt.setString(3, gender);
                pstmt.setInt(4, id);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Patient updated successfully");
                } else {
                    System.out.println("Some error occurred");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("No patient found with this ID");
        }
    }

    // Method to delete a patient record
    public void deletePatient(int id) {
        if (checkPatient(id)) {
            try {
                String query = "DELETE FROM patients WHERE pid=?";
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setInt(1, id);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Patient deleted successfully");
                } else {
                    System.out.println("Some error occurred");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("No patient found with this ID");
        }
    }
}
