package HospitalManagementSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Patients {
    private final Connection connection;
    private final Scanner scanner;
    public Patients(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }
    public void addPatient(){
        System.out.print("Enter Patient Name: ");
        String name = scanner.next();
        System.out.print("Enter Patient Age: ");
        int age = scanner.nextInt();
        System.out.print("Enter Patient Gender: ");
        String gender = scanner.next();

        try {
            String query = "INSERT INTO patients(name, age, gender) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0){
                System.out.println("Patient Added Successfully!!");
            }else{
                System.out.println("Failed to add patient!!");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void viewPatients(){
        String query = "select * from patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+-------------+--------------------+-----------+------------+");
            System.out.println("| Patients Id | Name               | Age       | Gender     |");
            System.out.println("+-------------+--------------------+-----------+------------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("| %-11s | %-18s | %-9s | %-10s |\n",id,name,age,gender);
                System.out.println("+-------------+--------------------+-----------+------------+");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public boolean getPatientById(int id){
        String query = "select * from patients where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() == true){
                return true;
            }
            else {
                return false;
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
