package HospitalManagementSystem;

import javax.print.Doc;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "harshsolanki.";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (Exception e){
            System.out.printf(e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url,username,password);
            Patients patients = new Patients(connection,scanner);
            Doctors doctors = new Doctors(connection);
            while (true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add Patients");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. View Appointment");
                System.out.println("6. Exit");
                System.out.println("Enter Your Choice: ");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        patients.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patients.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        doctors.viewDoctor();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(patients,doctors,connection,scanner);
                        System.out.println();
                        break;
                    case 5:
                         viewAppointment(connection);
                         System.out.println();
                         break;
                    case 6:
                        System.out.println("Thankyou For Using Hospital Management System");
                        return;
                    default:
                        System.out.println("Enter Valid Choice!!!");
                        break;
                }
            }
        }catch (Exception e){
            System.out.printf(e.getMessage());
        }
    }
    public static void bookAppointment(Patients patients,Doctors doctors, Connection connection, Scanner scanner){
        System.out.print("Enter Patients ID: ");
        int patientID = scanner.nextInt();
        System.out.print("Enter Doctors ID: ");
        int doctorID = scanner.nextInt();
        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();
        if (patients.getPatientById(patientID) && doctors.getDoctorById(doctorID)){
            if (checkDoctorAvailablity(doctorID,appointmentDate,connection)){
                String appointmentQuery = "insert into appointment(patient_id,doctor_id,appointment_date) values (?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientID);
                    preparedStatement.setInt(2,doctorID);
                    preparedStatement.setString(3,appointmentDate);
                    int affetedRows = preparedStatement.executeUpdate();
                    if (affetedRows > 0){
                        System.out.println("Appointment Booked");
                    }
                    else {
                        System.out.println("Failed to Book Appointment");
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }else{
                System.out.println("Doctor Not Available on this date!!!");
            }

        }else{
            System.out.println("Either Doctor Or Patient Doesnt Exist!!!");
        }
    }
    public static boolean checkDoctorAvailablity(int doctorId,String appointmentDate,Connection connection){
        String query = "SELECT COUNT(*) FROM appointment WHERE doctor_id = ? AND appointment_date = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                int count = resultSet.getInt(1);
                if (count == 0){
                    return true;
                }else{
                    return false;
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public static void viewAppointment(Connection connection){
        String query = "select * from appointment";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println("+------------+----------------+---------------+--------------------------+");
                System.out.println("| ID         | Patient_ID     | Doctors_ID    | Appointment_Date         |");
                System.out.println("+------------+----------------+---------------+--------------------------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                int patientId = resultSet.getInt("patient_ID");
                int doctorID = resultSet.getInt("doctor_id");
                String appointmentDate = resultSet.getString("appointment_date");
                System.out.printf("| %10s | %14s | %13s | %24s |\n",id,patientId,doctorID,appointmentDate);
                System.out.println("+------------+----------------+---------------+--------------------------+");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
