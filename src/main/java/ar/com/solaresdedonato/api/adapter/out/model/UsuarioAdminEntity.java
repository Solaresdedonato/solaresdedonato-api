package ar.com.solaresdedonato.api.adapter.out.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioAdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 180)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "nombre", nullable = false, length = 160)
    private String nombre;

    @Column(name = "rol", nullable = false, length = 20)
    private String rol;

    @Column(name = "ultimo_login_date")
    private LocalDateTime ultimoLoginDate;

    @Column(name = "enable", nullable = false)
    private boolean enable;

    @Column(name = "stamp_app", nullable = false, length = 60)
    private String stampApp;

    @Column(name = "stamp_user", nullable = false, length = 120)
    private String stampUser;

    @Column(name = "stamp_date", nullable = false)
    private LocalDateTime stampDate;
}
