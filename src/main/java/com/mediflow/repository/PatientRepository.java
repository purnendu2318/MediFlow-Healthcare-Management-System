package com.mediflow.repository;

import com.mediflow.config.Database;
import com.mediflow.model.Patient;
import java.sql.*;
import java.util.*;

public class PatientRepository {
    public String nextPatientNumber() throws Exception {
        try(Connection c=Database.getConnection(); Statement s=c.createStatement();
            ResultSet r=s.executeQuery("SELECT COALESCE(MAX(id),0)+1 AS n FROM patients")) {
            r.next(); return String.format("MED-%06d",r.getLong("n"));
        }
    }
    public void add(String name,String phone,String email,String dob,String gender,String address) throws Exception {
        String sql="INSERT INTO patients(patient_number,full_name,phone,email,date_of_birth,gender,address) VALUES(?,?,?,?,?,?,?)";
        try(Connection c=Database.getConnection(); PreparedStatement p=c.prepareStatement(sql)) {
            p.setString(1,nextPatientNumber()); p.setString(2,name); p.setString(3,phone);
            p.setString(4,email.isBlank()?null:email);
            if(dob.isBlank()) p.setNull(5,Types.DATE); else p.setDate(5,java.sql.Date.valueOf(dob));
            p.setString(6,gender); p.setString(7,address); p.executeUpdate();
        }
    }
    public List<Patient> search(String term) throws Exception {
        List<Patient> out=new ArrayList<>();
        String sql="SELECT id,patient_number,full_name,phone,email,status FROM patients WHERE status='ACTIVE' AND (patient_number LIKE ? OR full_name LIKE ? OR phone LIKE ?) ORDER BY id DESC LIMIT 300";
        try(Connection c=Database.getConnection(); PreparedStatement p=c.prepareStatement(sql)) {
            String q="%"+term+"%"; p.setString(1,q); p.setString(2,q); p.setString(3,q);
            try(ResultSet r=p.executeQuery()) { while(r.next()) out.add(new Patient(r.getLong("id"),r.getString("patient_number"),r.getString("full_name"),r.getString("phone"),r.getString("email"),r.getString("status"))); }
        }
        return out;
    }
    public void deactivate(long id) throws Exception {
        try(Connection c=Database.getConnection(); PreparedStatement p=c.prepareStatement("UPDATE patients SET status='INACTIVE' WHERE id=?")) {
            p.setLong(1,id); p.executeUpdate();
        }
    }
}
