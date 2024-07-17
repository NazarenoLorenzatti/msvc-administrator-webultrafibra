package ar.com.ultrafibra.administrator.responses;

import ar.com.ultrafibra.administrator.entities.Jwt;
import java.util.List;
import lombok.Data;

@Data
public class JWTResponse  {
    private List<Jwt> jwt;
}
