package at.htl.formula1.control;

import at.htl.formula1.boundary.ResultsRestClient;
import at.htl.formula1.entity.Driver;
import at.htl.formula1.entity.Race;
import at.htl.formula1.entity.Team;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.stream.Stream;

@ApplicationScoped
@Transactional
public class InitBean {

    private static final String TEAM_FILE_NAME = "teams.csv";
    private static final String RACES_FILE_NAME = "races.csv";
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @PersistenceContext
    EntityManager em;

    @Inject
    ResultsRestClient client;


    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {

        readTeamsAndDriversFromFile(TEAM_FILE_NAME);
        readRacesFromFile(RACES_FILE_NAME);
        client.readResultsFromEndpoint();

    }

    /**
     * Einlesen der Datei "races.csv" und Speichern der Objekte in der Tabelle F1_RACE
     *
     * @param racesFileName
     */
    private void readRacesFromFile(String racesFileName) {
        URL url = Thread.currentThread().getContextClassLoader()
                .getResource(racesFileName);
        try (Stream<String> stream = Files.lines(Paths.get(url.getPath()), StandardCharsets.UTF_8)) {
            stream.skip(1)
                    .map(s -> s.split(";"))
                    .map(line -> new Race(Long.valueOf(line[0]), line[1], LocalDate.parse(line[2], dtf)))
                    .forEach(em::merge);

//            String[] rows = url.getFile().split(";");
//
//            Race r = new Race();
//            r.setId(Long.valueOf(rows[0]));
//            r.setCountry(rows[1]);
//            r.setDate(LocalDate.parse(rows[2], dtf));
//            em.persist(r);
//
//            stream.forEach(em::merge);

            //stream.forEach(this::);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Einlesen der Datei "teams.csv".
     * Das String-Array jeder einzelnen Zeile wird der Methode persistTeamAndDrivers(...)
     * übergeben
     *
     * @param teamFileName
     */
    private void readTeamsAndDriversFromFile(String teamFileName) {
        URL url = Thread.currentThread().getContextClassLoader()
                .getResource(teamFileName);

        try (Stream<String> stream = Files.lines(Paths.get(url.getPath()), StandardCharsets.UTF_8)){
            stream.skip(1)
                    .map(s -> s.split(";"))
                    .forEach(this::persistTeamAndDrivers);

//            String[] rows = url.getFile().split(";");
//
//            Team t = new Team();
//            t.setName(rows[0]);
//            //stream.forEach(em::merge);
//            em.persist(t);
//
//            Team team = em.find(Team.class, t.getId());
//            Driver d1 = new Driver(rows[1], team);
//            Driver d2 = new Driver(rows[2], team);
//            em.persist(d1);
//            em.persist(d2);
            //stream.forEach(em::merge);

            //persistTeamAndDrivers(rows);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Es wird überprüft ob es das übergebene Team schon in der Tabelle F1_TEAM gibt.
     * Falls nicht, wird das Team in der Tabelle gespeichert.
     * Wenn es das Team schon gibt, dann liest man das Team aus der Tabelle und
     * erstellt ein Objekt (der Klasse Team).
     * Dieses Objekt wird verwendet, um die Fahrer mit Ihrem jeweiligen Team
     * in der Tabelle F!_DRIVER zu speichern.
     *
     * @param line String-Array mit den einzelnen Werten der csv-Datei
     */

    private void persistTeamAndDrivers(String[] line) {
        Team team = null;

        try {
            team = em
                    .createQuery("select t from Team t where t.name = :NAME", Team.class)
                    .setParameter("NAME", line[0])
                    .getSingleResult();
        } catch (NoResultException e){
            team = new Team(line[0]);
            em.persist(team);
        }

        em.persist(new Driver(line[1], team));
        em.persist(new Driver(line[2], team));

    }


}
