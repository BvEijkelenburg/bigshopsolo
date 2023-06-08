package nl.hu.bep.shopping.webservices;

import nl.hu.bep.shopping.model.Item;
import nl.hu.bep.shopping.model.Shop;
import nl.hu.bep.shopping.model.Shopper;
import nl.hu.bep.shopping.model.ShoppingList;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import static nl.hu.bep.shopping.model.Shop.getShop;

class CreateListRequest {
    public String shoppinglistname;
    public String shoppername;
}

@Path("list")
public class ListResource {

    @POST
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createList(CreateListRequest req) {
        List<Shopper> shoppers = Shop.getShop().getAllPersons();

        for (Shopper aShopper : shoppers) {
            if (aShopper.getName().equals(req.shoppername)) {
                if (Shop.getShop().getShoppingListByName(req.shoppinglistname) == null) {
                    ShoppingList newList = new ShoppingList(req.shoppinglistname, aShopper);
                    ShoppingList.addList(newList);
                    aShopper.addList(newList);

                    return Response.ok(Map.of("message", "Success!")).build();
                } else {
                    return Response.status(409).entity(Map.of("message", "List already exists!")).build();
                }
            }
        }

        return Response.status(404).entity(Map.of("message", "Shopper not found!")).build();
    }

    @PUT
    @Path("{name}")
    public Response resetShoppingList(@PathParam("name") String name) {
        ShoppingList list = getShop().getShoppingListByName(name);
        if (list != null) {
            list.reset();
            return Response.ok().build();
        } else {
            return Response.status(404).build();
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShoppingLists() {
        List<ShoppingList> shoppingLists = getShop().getAllShoppingLists();
        if (shoppingLists.isEmpty()) {
            var message = new AbstractMap.SimpleEntry<>("error", "no lists present");
            return Response.status(404).entity(message).build();
        }

        return Response.ok(shoppingLists).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{name}")
    public Response getShoppingListByName(@PathParam("name") String name) {
        Shop shop = getShop();
        ShoppingList list = shop.getShoppingListByName(name);
        return Response.ok(list).build();
    }
}
