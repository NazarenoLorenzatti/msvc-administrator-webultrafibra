package ar.com.ultrafibra.administrator.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "imgs-section")
public class ImgSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url-obs")
    private String urlObs;

    @Column(name = "description")
    private String description;

    @Column(name = "access_key", unique = true, nullable = false)
    private String accessKey;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "sections_web_id")
    private SectionsWeb sectionWeb;
}
