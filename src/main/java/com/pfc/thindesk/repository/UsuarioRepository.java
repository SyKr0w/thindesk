/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pfc.thindesk.repository;

import com.pfc.thindesk.entity.Usuario;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author alunocmc
 */
public interface UsuarioRepository extends MongoRepository<Usuario, String>{
    Optional<Usuario>findByEmail(String email);
    
    boolean existsByEmail(String email);
}
