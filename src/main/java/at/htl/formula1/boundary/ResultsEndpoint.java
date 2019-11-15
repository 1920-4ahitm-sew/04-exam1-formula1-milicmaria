package at.htl.formula1.boundary;

import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;
import at.htl.formula1.entity.Result;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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
        JsonObject json = (JsonObject) em.createNamedQuery("Result.getPointsSum", Result.class).getResultList();

        return json;
    }

    /**
     * @param id des Rennens
     * @return
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findWinnerOfRace(@PathParam("id") long id) {
        Race r = em.createNamedQuery("Race.getWinner", Race.class).getSingleResult();
        id = r.getId();

        return Response.ok().entity(r).build();
    }

    @GET
    @Path("winner/{county}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWinner(@PathParam("county") String county){
       Result r = em.createNamedQuery("Result.getWinner", Result.class).getSingleResult();
        // Response response = em.createNamedQuery("Race.getWinner", Race.class).getSingleResult();
        return Response.ok(r).entity(r).build();
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Object[]> getDriverWithPoints(String driver){
        //Response response = em.createNamedQuery("");
       // List<Object[]> obj = em.createNamedQuery("Result.driverWithPoints", Result.class).setParameter("DRIVER", driver).getResultList();
        return null;
    }
    // Ergänzen Sie Ihre eigenen Methoden ...

}
