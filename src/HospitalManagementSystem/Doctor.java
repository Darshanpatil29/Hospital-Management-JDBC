package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {
    private Connection con;

    public Doctor(Connection con) {
        this.con = con;
    }
    public void viewDoctor() throws Exception{
        try{
            String query="select * from doctors";
            PreparedStatement pstmt=con.prepareStatement(query);
            ResultSet set=pstmt.executeQuery();
            System.out.println("Doctors List");
            System.out.println("******************************************************");
            while(set.next()){
                System.out.println("Doctor ID:"+set.getInt("did"));
                System.out.println("Doctor Name:"+set.getString("name"));
                System.out.println("Specialization:"+set.getString("specialization"));
                System.out.println("******************************************************");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean checkDoctor(int id){
        try {
            String query="select * from doctors where did=?";
            PreparedStatement pstmt=con.prepareStatement(query);
            pstmt.setInt(1,id);
            ResultSet set=pstmt.executeQuery();
//            if(set.next()){
//                System.out.println("Doctor ID:"+set.getInt("did"));
//                System.out.println("Doctor Name:"+set.getString("name"));
//                System.out.println("Specialization:"+set.getString("specialization"));
//                System.out.println("******************************************************");
//            }
//            else{
//                System.out.println("No Doctor found with this id");
//            }
            return set.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
