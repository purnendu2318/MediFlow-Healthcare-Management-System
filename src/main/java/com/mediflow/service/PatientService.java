package com.mediflow.service;

import com.mediflow.model.Patient;
import com.mediflow.repository.PatientRepository;
import java.util.List;

public class PatientService {
    private final PatientRepository repo=new PatientRepository();
    public void register(String name,String phone,String email,String dob,String gender,String address) throws Exception {
        if(name==null || name.isBlank()) throw new IllegalArgumentException("Patient name is required.");
        if(phone==null || !phone.matches("[0-9+() -]{7,20}")) throw new IllegalArgumentException("Enter a valid phone number.");
        if(dob!=null && !dob.isBlank()) java.time.LocalDate.parse(dob);
        repo.add(name.trim(),phone.trim(),email==null?"":email.trim(),dob==null?"":dob.trim(),gender,address==null?"":address.trim());
    }
    public List<Patient> search(String q) throws Exception { return repo.search(q==null?"":q.trim()); }
    public void deactivate(long id) throws Exception { repo.deactivate(id); }
}
