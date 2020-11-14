/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mjezerina.web.zrna;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.mjezerina.ws.serveri.AvionLeti;
import org.foi.nwtis.podaci.Aerodrom;


/**
 *
 * @author Matija
 */
@Named(value = "pregledAviona")
@SessionScoped
public class PregledAviona implements Serializable {

    @Inject
    PrijavaKorisnika prijavaKorisnika;
    String korisnik = "";
    String lozinka = "";

    @Getter
    @Setter
    String vrijemeOd;
    @Getter
    @Setter
    String vrijemeDo;
    @Getter
    @Setter
    String drzava;
    @Getter
    List<Aerodrom> aerodromi;
    @Getter
    List<Aerodrom> aerodromiKorisnika = new ArrayList<>();
    @Getter
    @Setter
    String icaoOdabrani;
    @Getter
    List<AvionLeti> listaAviona;

    /**
     * Creates a new instance of DodavanjeAerodroma
     */
    public PregledAviona() {
    }

    /**
     * Metoda koja prima kao parametar vrijeme u long formatu te 
     * pretvara to vrijeme u prikaz pogodan za formu PregledAviona.
     * @param vrijeme vrijeme u long formatu predstavlja UnixTimestamp kada je avion poletio i sletio
     * @return formatirani datum u string formatu
     */
    public String konvertirajEpochDatum(long vrijeme) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(vrijeme, 0, ZoneOffset.ofHours(2));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formattedDate = dateTime.format(formatter);
        return formattedDate;
    }

}
