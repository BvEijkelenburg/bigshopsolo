package nl.hu.bep.setup;

import nl.hu.bep.shopping.model.Shopper;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class MySecurityContext implements SecurityContext {
    private Shopper theShopper;

    public MySecurityContext(Shopper shopper) {
        theShopper = shopper;
    }

    @Override
    public Principal getUserPrincipal() {
        return theShopper;
    }

    @Override
    public boolean isUserInRole(String s) {
        return theShopper.getRole().equals(s);
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}
