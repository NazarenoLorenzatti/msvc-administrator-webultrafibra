package ar.com.ultrafibra.administrator.entities;

import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "sections-web")
public class SectionsWeb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_key", unique = true, nullable = false)
    private String accessKey;

    @OneToMany(mappedBy = "sectionWeb", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TextSection> texts;

    @OneToMany(mappedBy = "sectionWeb", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImgSection> imgs;

}
