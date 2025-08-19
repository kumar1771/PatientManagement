package com.example.Patient_Service.Service;

import com.example.Patient_Service.Mapper.PatientMapper;
import com.example.Patient_Service.Model.Patient;
import com.example.Patient_Service.Repository.PatientRepository;
import com.example.Patient_Service.dto.PatientRequestDTO;
import com.example.Patient_Service.dto.PatientResponseDTO;
import com.example.Patient_Service.exception.EmailAlreadyExistException;
import com.example.Patient_Service.exception.PatientNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).toList();
    }
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistException("A Patient with this mail : "+patientRequestDTO.getEmail()+" already exist");
        }
        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDTO(newPatient);
    }
    public PatientResponseDTO updatePatient(UUID id,
                                            PatientRequestDTO patientRequestDTO) {

        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: " + id));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),
                id)) {
            throw new EmailAlreadyExistException(
                    "A patient with this email " + "already exists"
                            + patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
    }
    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
