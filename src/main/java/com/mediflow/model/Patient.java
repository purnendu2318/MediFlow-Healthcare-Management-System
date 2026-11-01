package com.mediflow.model;

public record Patient(long id, String patientNumber, String fullName, String phone, String email, String status) {}
