package ar.com.solaresdedonato.api.adapter.out.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "consulta_contacto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaContactoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 120)
    private String apellido;

    @Column(name = "email", nullable = false, length = 180)
    private String email;

    @Column(name = "telefono", nullable = false, length = 40)
    private String telefono;

    @Column(name = "proyecto_interes", length = 200)
    private String proyectoInteres;

    @Column(name = "mensaje", nullable = false, columnDefinition = "text")
    private String mensaje;

    @Column(name = "enable", nullable = false)
    private boolean enable;

    @Column(name = "stamp_app", nullable = false, length = 60)
    private String stampApp;

    @Column(name = "stamp_user", nullable = false, length = 120)
    private String stampUser;

    @Column(name = "stamp_date", nullable = false)
    private LocalDateTime stampDate;
}
