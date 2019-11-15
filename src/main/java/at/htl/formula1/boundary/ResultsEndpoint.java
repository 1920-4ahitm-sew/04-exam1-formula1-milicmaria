package at.htl.formula1.boundary;

import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;
import at.htl.formula1.entity.Result;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("results")
public class ResultsEndpoint {

    @PersistenceContext
    EntityManager em;

    /**
     * @param name als QueryParam einzulesen
     * @return JsonObject
     */
    @GET
    @Path("{name}")
    public JsonObject getPointsSumOfDriver(@QueryParam("name") String name) {
        em.find(Result.class, name);

        return null;
    }

    /**
     * @param id des Rennens
     * @return
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findWinnerOfRace(@PathParam("id") long id) {

        return null;
    }

    @GET
    @Path("winner/{county}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWinner(@PathParam("county") String county){
       // Response response = em.createNamedQuery("Race.getWinner", Race.class).getSingleResult();
        return null;
    }

    // Ergänzen Sie Ihre eigenen Methoden ...

}
