package at.htl.formula1.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Formula1 - Race
 *
 * The id's are not assigned by the database. The id's are given.
 */
@Entity
@Table(name = "F1_RACE")
@NamedQuery(name = "Race.getWinner", query = "select r from Race r,Result r2 where r.id=r2.race and (select max(r.points) from Result r)")
public class Race implements Serializable {

    @Id
    private Long id;
    private String country;
    private LocalDate date;

    //region Constructors
    public Race() {
    }

    public Race(Long id, String country, LocalDate date) {
        this.id = id;
        this.country = country;
        this.date = date;
        date.format(DateTimeFormatter.ofPattern("dd.mm.yyyy"));
    }
    //endregion

    //region Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    //endregion


    @Override
    public String toString() {
        return String.format("%d: %s (%te.%tm,%tY)",id,country,date,date,date);
    }
}
