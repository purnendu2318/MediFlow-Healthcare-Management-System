CREATE DATABASE IF NOT EXISTS mediflow_hms CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mediflow_hms;

CREATE TABLE IF NOT EXISTS users (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 full_name VARCHAR(120) NOT NULL,
 email VARCHAR(160) NOT NULL UNIQUE,
 password_hash VARCHAR(100) NOT NULL,
 role ENUM('PATIENT','DOCTOR','RECEPTIONIST','HOSPITAL_ADMIN','SUPER_ADMIN') NOT NULL,
 status ENUM('ACTIVE','DISABLED') NOT NULL DEFAULT 'ACTIVE',
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS departments (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(120) NOT NULL UNIQUE,
 description VARCHAR(500),
 status ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS patients (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 patient_number VARCHAR(30) NOT NULL UNIQUE,
 user_id BIGINT NULL,
 full_name VARCHAR(120) NOT NULL,
 date_of_birth DATE NULL,
 gender VARCHAR(30),
 phone VARCHAR(20),
 email VARCHAR(160),
 address VARCHAR(500),
 emergency_contact_name VARCHAR(120),
 emergency_contact_phone VARCHAR(20),
 allergies TEXT,
 medical_history TEXT,
 status ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_patient_user FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS doctors (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 user_id BIGINT NULL,
 department_id BIGINT NOT NULL,
 full_name VARCHAR(120) NOT NULL,
 license_number VARCHAR(80),
 phone VARCHAR(20),
 consultation_fee DECIMAL(10,2) NOT NULL DEFAULT 0,
 status ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
 CONSTRAINT fk_doctor_user FOREIGN KEY(user_id) REFERENCES users(id),
 CONSTRAINT fk_doctor_department FOREIGN KEY(department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS doctor_availability (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 doctor_id BIGINT NOT NULL,
 start_time DATETIME NOT NULL,
 end_time DATETIME NOT NULL,
 availability_status ENUM('AVAILABLE','UNAVAILABLE') NOT NULL DEFAULT 'AVAILABLE',
 CONSTRAINT fk_availability_doctor FOREIGN KEY(doctor_id) REFERENCES doctors(id)
);

CREATE TABLE IF NOT EXISTS appointments (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 reference VARCHAR(40) NOT NULL UNIQUE,
 patient_id BIGINT NOT NULL,
 doctor_id BIGINT NOT NULL,
 start_time DATETIME NOT NULL,
 end_time DATETIME NOT NULL,
 status ENUM('BOOKED','CONFIRMED','CHECKED_IN','IN_CONSULTATION','COMPLETED','CANCELLED','NO_SHOW') NOT NULL DEFAULT 'BOOKED',
 notes VARCHAR(500),
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_appointment_patient FOREIGN KEY(patient_id) REFERENCES patients(id),
 CONSTRAINT fk_appointment_doctor FOREIGN KEY(doctor_id) REFERENCES doctors(id),
 INDEX idx_appointment_doctor_time(doctor_id,start_time,end_time)
);

CREATE TABLE IF NOT EXISTS queue_tokens (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 appointment_id BIGINT NULL,
 patient_id BIGINT NOT NULL,
 doctor_id BIGINT NOT NULL,
 token_number INT NOT NULL,
 queue_date DATE NOT NULL,
 status ENUM('WAITING','CALLED','IN_CONSULTATION','COMPLETED','SKIPPED','CANCELLED') NOT NULL DEFAULT 'WAITING',
 CONSTRAINT fk_queue_appointment FOREIGN KEY(appointment_id) REFERENCES appointments(id),
 CONSTRAINT fk_queue_patient FOREIGN KEY(patient_id) REFERENCES patients(id),
 CONSTRAINT fk_queue_doctor FOREIGN KEY(doctor_id) REFERENCES doctors(id),
 UNIQUE KEY uq_queue_token(doctor_id,queue_date,token_number)
);

CREATE TABLE IF NOT EXISTS medical_records (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 patient_id BIGINT NOT NULL,
 doctor_id BIGINT NOT NULL,
 appointment_id BIGINT NULL,
 symptoms TEXT,
 observations TEXT,
 diagnosis TEXT,
 treatment_plan TEXT,
 vitals TEXT,
 status ENUM('DRAFT','FINALIZED') NOT NULL DEFAULT 'DRAFT',
 author_user_id BIGINT NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 CONSTRAINT fk_record_patient FOREIGN KEY(patient_id) REFERENCES patients(id),
 CONSTRAINT fk_record_doctor FOREIGN KEY(doctor_id) REFERENCES doctors(id),
 CONSTRAINT fk_record_appointment FOREIGN KEY(appointment_id) REFERENCES appointments(id),
 CONSTRAINT fk_record_author FOREIGN KEY(author_user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS prescriptions (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 medical_record_id BIGINT NOT NULL,
 prescriber_id BIGINT NOT NULL,
 status ENUM('DRAFT','FINALIZED','CANCELLED') NOT NULL DEFAULT 'DRAFT',
 finalized_at DATETIME NULL,
 CONSTRAINT fk_prescription_record FOREIGN KEY(medical_record_id) REFERENCES medical_records(id),
 CONSTRAINT fk_prescription_prescriber FOREIGN KEY(prescriber_id) REFERENCES doctors(id)
);

CREATE TABLE IF NOT EXISTS prescription_items (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 prescription_id BIGINT NOT NULL,
 medicine_name VARCHAR(160) NOT NULL,
 dosage VARCHAR(100),
 route VARCHAR(80),
 frequency VARCHAR(100),
 duration VARCHAR(100),
 instructions VARCHAR(500),
 CONSTRAINT fk_prescription_item FOREIGN KEY(prescription_id) REFERENCES prescriptions(id)
);

CREATE TABLE IF NOT EXISTS invoices (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 invoice_number VARCHAR(40) NOT NULL UNIQUE,
 patient_id BIGINT NOT NULL,
 appointment_id BIGINT NULL,
 subtotal DECIMAL(12,2) NOT NULL DEFAULT 0,
 tax_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
 discount_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
 total_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
 status ENUM('DRAFT','ISSUED','PARTIALLY_PAID','PAID','CANCELLED','OVERDUE') NOT NULL DEFAULT 'DRAFT',
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_invoice_patient FOREIGN KEY(patient_id) REFERENCES patients(id),
 CONSTRAINT fk_invoice_appointment FOREIGN KEY(appointment_id) REFERENCES appointments(id)
);

CREATE TABLE IF NOT EXISTS invoice_items (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 invoice_id BIGINT NOT NULL,
 description VARCHAR(200) NOT NULL,
 quantity INT NOT NULL,
 unit_price DECIMAL(12,2) NOT NULL,
 amount DECIMAL(12,2) NOT NULL,
 CONSTRAINT fk_invoice_item FOREIGN KEY(invoice_id) REFERENCES invoices(id)
);

CREATE TABLE IF NOT EXISTS payments (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 invoice_id BIGINT NOT NULL,
 amount DECIMAL(12,2) NOT NULL,
 payment_method VARCHAR(40) NOT NULL,
 reference VARCHAR(100) UNIQUE,
 status ENUM('SUCCESS','FAILED','REFUNDED') NOT NULL DEFAULT 'SUCCESS',
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_payment_invoice FOREIGN KEY(invoice_id) REFERENCES invoices(id)
);

CREATE TABLE IF NOT EXISTS notifications (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 user_id BIGINT NOT NULL,
 type VARCHAR(60) NOT NULL,
 message VARCHAR(500) NOT NULL,
 read_at DATETIME NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_notification_user FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS audit_logs (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 actor_id BIGINT NULL,
 action VARCHAR(100) NOT NULL,
 resource_type VARCHAR(80) NOT NULL,
 resource_id VARCHAR(80),
 details VARCHAR(500),
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_audit_actor FOREIGN KEY(actor_id) REFERENCES users(id)
);
