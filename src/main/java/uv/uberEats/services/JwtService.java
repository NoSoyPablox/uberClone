package uv.uberEats.services;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uv.uberEats.models.Usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    /*@Value("{security.jwt.secret-key}")
    private String secretKey;

    @Value("{security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extractUsername(String token) { return extractClaim(token, Claims::getSubject);}

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {return generateToken(new HashMap<>(), userDetails);}

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        Usuario usuario = (Usuario) userDetails;
        extraClaims.put("correoElectronico", usuario.getCorreo());
        extraClaims.put("nombre", usuario.getNombre()+" "+usuario.getApellidoPaterno()+" "+usuario.getApellidoMaterno());


        // Construir y devolver el token
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }*/
}
