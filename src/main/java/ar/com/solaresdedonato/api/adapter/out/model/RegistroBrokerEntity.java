package ar.com.solaresdedonato.api.adapter.out.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "registro_broker")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroBrokerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 160)
    private String nombre;

    @Column(name = "email", nullable = false, length = 180)
    private String email;

    @Column(name = "telefono", nullable = false, length = 40)
    private String telefono;

    @Column(name = "inmobiliaria", nullable = false, length = 200)
    private String inmobiliaria;

    @Column(name = "matricula", length = 80)
    private String matricula;

    @Column(name = "experiencia", length = 40)
    private String experiencia;

    @Column(name = "zona_operacion", nullable = false, length = 80)
    private String zonaOperacion;

    @Type(JsonType.class)
    @Column(name = "tipo_operaciones", nullable = false, columnDefinition = "jsonb")
    private List<String> tipoOperaciones;

    @Column(name = "operaciones_cerradas", length = 40)
    private String operacionesCerradas;

    @Column(name = "mensaje", columnDefinition = "text")
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
