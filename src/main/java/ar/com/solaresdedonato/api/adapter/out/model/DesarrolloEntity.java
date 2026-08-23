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
import java.util.Map;

@Entity
@Table(name = "desarrollo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesarrolloEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slug", nullable = false, length = 160)
    private String slug;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "zona", nullable = false, length = 150)
    private String zona;

    @Column(name = "direccion", nullable = false, length = 300)
    private String direccion;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "descripcion", nullable = false, columnDefinition = "text")
    private String descripcion;

    @Type(JsonType.class)
    @Column(name = "features", nullable = false, columnDefinition = "jsonb")
    private List<Map<String, String>> features;

    @Type(JsonType.class)
    @Column(name = "cercanias", nullable = false, columnDefinition = "jsonb")
    private Map<String, List<String>> cercanias;

    @Column(name = "instrumento_tokenizacion", nullable = false)
    private boolean instrumentoTokenizacion;

    @Column(name = "instrumento_renta_fija", nullable = false)
    private boolean instrumentoRentaFija;

    @Column(name = "publicado", nullable = false)
    private boolean publicado;

    @Column(name = "imagen_portada_url", length = 500)
    private String imagenPortadaUrl;

    @Column(name = "enable", nullable = false)
    private boolean enable;

    @Column(name = "stamp_app", nullable = false, length = 60)
    private String stampApp;

    @Column(name = "stamp_user", nullable = false, length = 120)
    private String stampUser;

    @Column(name = "stamp_date", nullable = false)
    private LocalDateTime stampDate;
}
