package ar.com.ultrafibra.administrator.responses;

import ar.com.ultrafibra.administrator.entities.SectionsWeb;
import java.util.List;
import lombok.Data;

@Data
public class SectionsWebResponse {
    
    private List<SectionsWeb> sectionsWeb;
}
