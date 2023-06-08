package nl.hu.bep.setup;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import nl.hu.bep.shopping.model.Shopper;
import nl.hu.bep.shopping.webservices.AuthenticationResource;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String authHeader = containerRequestContext.getHeaderString("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtString = authHeader.split(" ")[1];

            try {
                JwtParser parser = Jwts.parser();
                Claims claims = parser.setSigningKey(AuthenticationResource.key).parseClaimsJws(jwtString).getBody();

                String shopperName = claims.getSubject();

                for (Shopper shopper : Shopper.getAllShoppers()) {
                    if (shopper.getName().equals(shopperName)) {
                        MySecurityContext msc = new MySecurityContext(shopper);
                        containerRequestContext.setSecurityContext(msc);
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid JWT, processing as guest!");
            }
        }
    }
}
