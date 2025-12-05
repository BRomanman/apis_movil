package com.clinica.api.personal_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class UsuarioCreateRequestDto {

    private static final String NAME_REGEX = "^[A-Za-zÁÉÍÓÚÑáéíóúñ ]+$";

    @NotBlank
    @Size(max = 60)
    @Pattern(regexp = NAME_REGEX, message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @NotBlank
    @Size(max = 100)
    @Pattern(regexp = NAME_REGEX, message = "El apellido solo puede contener letras y espacios")
    private String apellido;

    @NotNull
    @Past
    private LocalDate fechaNacimiento;

    @NotBlank
    @Email
    private String correo;

    @Pattern(regexp = "^\\+569\\d{8}$", message = "El teléfono debe tener formato +569XXXXXXXX")
    private String telefono;

    @NotBlank
    private String contrasena;

    @Valid
    @NotNull
    private RoleReference rol;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public RoleReference getRol() {
        return rol;
    }

    public void setRol(RoleReference rol) {
        this.rol = rol;
    }

    public static class RoleReference {
        @NotNull
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}
