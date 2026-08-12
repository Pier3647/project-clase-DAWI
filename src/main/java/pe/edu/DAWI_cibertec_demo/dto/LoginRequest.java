package pe.edu.DAWI_cibertec_demo.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String correo;
    private String clave;
}
