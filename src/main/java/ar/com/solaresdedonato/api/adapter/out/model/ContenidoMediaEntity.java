package ar.com.solaresdedonato.api.adapter.out.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "contenido_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContenidoMediaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desarrollo_id")
    private DesarrolloEntity desarrollo;

    @Column(name = "tipo", nullable = false, length = 10)
    private String tipo;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "categoria", nullable = false, length = 20)
    private String categoria;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @Column(name = "archivo_url", length = 500)
    private String archivoUrl;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "origen_drive_file_id", length = 120)
    private String origenDriveFileId;

    @Column(name = "es_portada", nullable = false)
    private boolean esPortada;

    @Column(name = "orden", nullable = false)
    private short orden;

    @Column(name = "enable", nullable = false)
    private boolean enable;

    @Column(name = "stamp_app", nullable = false, length = 60)
    private String stampApp;

    @Column(name = "stamp_user", nullable = false, length = 120)
    private String stampUser;

    @Column(name = "stamp_date", nullable = false)
    private LocalDateTime stampDate;
}
