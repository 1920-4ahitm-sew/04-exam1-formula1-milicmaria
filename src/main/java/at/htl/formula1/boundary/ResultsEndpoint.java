package at.htl.formula1.boundary;

import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;
import at.htl.formula1.entity.Result;
import at.htl.formula1.entity.Team;

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
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getPointsSumOfDriver(@QueryParam("name") String name) {
        Driver d = em
                .createNamedQuery("Driver.findByName", Driver.class)
                .setParameter("NAME", name)
                .getSingleResult();
        Long dPoints = em
                .createNamedQuery("Result.getPointsSum", Long.class)
                .setParameter("DRIVER", d)
                .getSingleResult();

        JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        objBuilder.add("d", d.getName());
        objBuilder.add("dPoints", dPoints);

        return objBuilder.build();
    }

    @GET
    @Path("winner/{country}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findWinnerOfRace(@PathParam("country") String country) {
        Race race = em
                .createNamedQuery("Race.findByCountry", Race.class)
                .setParameter("COUNTRY", country)
                .getSingleResult();
        Driver driver = em
                .createNamedQuery("Driver.findWinnerOfRace", Driver.class)
                .setParameter("RACE", race)
                .getSingleResult();

        return Response.ok(driver).build();
    }

    @GET
    @Path("raceWon")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWinner(@QueryParam("team") String name){
        Team team = em
                .createNamedQuery("Team.findByName", Team.class)
                .setParameter("NAME", name)
                .getSingleResult();
        List<Race> raceWon = em
                .createNamedQuery("Result.findRacesByTeam", Race.class)
                .setParameter("TEAM", team)
                .getResultList();

        return Response.ok(raceWon).build();
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Object[]> getDriverWithPoints(String driver){
        List<Object[]> obj = em.createNamedQuery("Result.getSumPoints", Object[].class).getResultList();

        return obj;
    }
    // Ergänzen Sie Ihre eigenen Methoden ...

}
