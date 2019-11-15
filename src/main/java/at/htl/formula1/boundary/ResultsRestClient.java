package at.htl.formula1.boundary;

import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;
import at.htl.formula1.entity.Result;

import javax.json.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationPath("api")
public class ResultsRestClient {

    @PersistenceContext
    EntityManager em;

    public static final String RESULTS_ENDPOINT = "http://vm90.htl-leonding.ac.at/results";
    private Client client;
    private WebTarget target;

    /**
     * Vom RestEndpoint werden alle Result abgeholt und in ein JsonArray gespeichert.
     * Dieses JsonArray wird an die Methode persistResult(...) übergeben
     */
    public void readResultsFromEndpoint() {

        client = ClientBuilder.newClient();
        target = client.target(RESULTS_ENDPOINT);

        Response response = (Response) target.request(MediaType.APPLICATION_JSON);

        JsonArray payload = response.readEntity(JsonArray.class);

        persistResult(payload);
    }

    /**
     * Das JsonArray wird durchlaufen (iteriert). Man erhäjt dabei Objekte vom
     * Typ JsonValue. diese werden mit der Methode .asJsonObject() in ein
     * JsonObject umgewandelt.
     *
     * zB:
     * for (JsonValue jsonValue : resultsJson) {
     *             JsonObject resultJson = jsonValue.asJsonObject();
     *             ...
     *
     *  Mit den entsprechenden get-Methoden können nun die einzelnen Werte
     *  (raceNo, position und driverFullName) ausgelesen werden.
     *
     *  Mit dem driverFullName wird der entsprechende Driver aus der Datenbank ausgelesen.
     *
     *  Dieser Driver wird dann dem neu erstellten Result-Objekt übergeben
     *
     * @param resultsJson
     */
    @Transactional
    void persistResult(JsonArray resultsJson) {

        for (JsonValue jsonValue : resultsJson){
            JsonObject resultJson = jsonValue.asJsonObject();
            em.persist(resultJson);
        }

    }

}
