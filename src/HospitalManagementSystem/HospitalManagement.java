package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagement {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital";
    private static final String USER = "root";
    private static final String PASSWORD = "root@123";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(System.in);
        try {
            Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected to database");
            Patient patient = new Patient(con, sc);
            Doctor doctor = new Doctor(con);
            System.out.println("---------------------Hospital Management System---------------------");
            while (true) {
                System.out.println("1.Patient Management");
                System.out.println("2.Doctor Management");
                System.out.println("3.Book Appointment");
                System.out.println("4.Exit");
                int choice = sc.nextInt();
                if(choice==4)
                    break;
                handleMenuChoice(choice,sc,patient,doctor,con);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleMenuChoice(int choice, Scanner sc, Patient patient, Doctor doctor,Connection con) throws Exception {
        switch (choice) {
            case 1:
                handlePatientChoice(patient, sc);
                break;

            case 2:
                handleDoctorChoice(doctor, sc);
                break;
            case 3:
                bookAppointment(sc,con,doctor,patient);
                break;
            default:
                System.out.println("Invalid choice!! Try again");
        }
    }

    private static void handleDoctorChoice(Doctor doctor, Scanner sc) throws Exception {
        while (true) {
            System.out.println("1. View Doctor Details");
            System.out.println("2. Check Doctor");
            System.out.println("3. Back to Main Menu");
            int dChoice = sc.nextInt();
            if (dChoice == 3) {
                break;
            }
            switch (dChoice) {
                case 1:
                    doctor.viewDoctor();
                    break;
                case 2:
                    System.out.println("Enter ID of Doctor:");
                    int id = sc.nextInt();
                    doctor.checkDoctor(id);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handlePatientChoice(Patient patient, Scanner sc) throws Exception {
        while (true){
            System.out.println("1. Add Patient");
            System.out.println("2. View Patient Details");
            System.out.println("3. Check Patient");
            System.out.println("4. Back to Main Menu");
            int pChoice = sc.nextInt();
            if (pChoice == 4) {
                break;
            }
            switch (pChoice) {
                case 1:
                    patient.addPatient();
                    break;
                case 2:
                    patient.viewPatient();
                    break;
                case 3:
                    System.out.println("Enter ID of Patient:");
                    int id = sc.nextInt();
                    patient.checkPatient(id);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    public static void bookAppointment(Scanner sc, Connection con, Doctor doctor, Patient patient){
        System.out.println("Book Appointment");
        System.out.println("******************************************************");
        System.out.println("Enter Patient Id:");
        int pid=sc.nextInt();
        System.out.println("Enter Doctor Id:");
        int did=sc.nextInt();
        System.out.println("Enter Date:");
        String date=sc.next();
        if (patient.checkPatient(pid) && doctor.checkDoctor(did)) {
            if (checkAvailability(did, date, con)) {
                try {
                    String query = "insert into appoinments(patient_id,doctor_id,appointment_date) values(?,?,?)";
                    PreparedStatement pstmt = con.prepareStatement(query);
                    pstmt.setInt(1, pid);
                    pstmt.setInt(2, did);
                    pstmt.setString(3, date);
                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Appointment booked successfully");
                    } else {
                        System.out.println("Some error occurred");
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                System.out.println("Doctor is not available on this date");
            }
        }
        else{
            System.out.println("No Patient or Doctor found with this id");
        }
    }

    private static boolean checkAvailability(int did, String date, Connection con) {
        try{
            String query="select count(*) from appoinments where doctor_id=? and appointment_date=?";
            PreparedStatement pstmt=con.prepareStatement(query);
            pstmt.setInt(1,did);
            pstmt.setString(2,date);
            ResultSet set=pstmt.executeQuery();
            if(set.next()){
                int count=set.getInt(1);
                if(count>=5){
                    System.out.println("Doctor is not available on this date");
                    return false;
                }
                else{
                    return true;
                }
            }
            else{
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
