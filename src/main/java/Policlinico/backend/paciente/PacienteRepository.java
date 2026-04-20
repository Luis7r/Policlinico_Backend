/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Policlinico.backend.paciente;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author LUIS
 */
public interface PacienteRepository extends JpaRepository<Paciente, String> {
    
}
