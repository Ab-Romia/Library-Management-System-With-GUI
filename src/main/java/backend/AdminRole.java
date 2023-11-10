package backend;

import constants.FileNames;
import backend.EmployeeUserDatabase;
import backend.EmployeeUser;
import java.io.*;
import java.util.*;
public class AdminRole {
    private EmployeeUserDatabase database =new EmployeeUserDatabase(FileNames.EMPLOYEE_FILENAME);

    public AdminRole()
    {
        this.database = new EmployeeUserDatabase(FileNames.EMPLOYEE_FILENAME);
        this.database.readFromFile();
    }

    public boolean addEmployee(String employeeId, String name, String email, String address, String phoneNumber) {
       
        EmployeeUser emp = new EmployeeUser(employeeId, name, email, address, phoneNumber);
            if(database.contains(emp.getSearchKey()))
            {
                return false;
            }
        database.insertRecord(emp);
        return true;

    }
        public EmployeeUser[] getListOfEmployees()  {

        return database.returnAllRecords().toArray(new EmployeeUser[0]);
    }
    public boolean removeEmployee(String key)
    {
        if(database.contains(key))
        {
           
            database.deleteRecord(key);
            return true;
        }
        return false;
    }
    

    public void logout()  {
        database.saveToFile();
    }
}
