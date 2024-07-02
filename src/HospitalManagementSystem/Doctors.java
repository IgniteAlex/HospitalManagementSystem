package HospitalManagementSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Doctors {
    private Connection connection;
    public Doctors(Connection connection){
        this.connection = connection;
    }
    public void viewDoctor(){
        String query = "select * from doctors";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+------------+---------------------+------------------------------+");
            System.out.println("| Doctors ID | Doctor Name         | Doctor Department            |");
            System.out.println("+------------+---------------------+------------------------------+");
            while (resultSet.next()){
                int docsID = resultSet.getInt("id");
                String docsName = resultSet.getString("name");
                String docsDepartment = resultSet.getString("department");
                System.out.printf("| %-10s | %-19s | %-28s |\n",docsID,docsName,docsDepartment);
                System.out.println("+------------+---------------------+------------------------------+");
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public boolean getDoctorById(int id){
        String query = "select * from doctors where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
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
