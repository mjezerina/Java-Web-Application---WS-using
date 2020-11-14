/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mjezerina.web.zrna;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.mjezerina.rest.klijenti.Zadaca2_2RS;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Icao;

import org.foi.nwtis.podaci.OdgovorAerodrom;

/**
 *
 * @author Matija
 */
@Named(value = "dodavanjeAerodroma")
@SessionScoped
public class DodavanjeAerodroma implements Serializable {

    @Inject
    PrijavaKorisnika prijavaKorisnika;
    String korisnik = "";
    String lozinka = "";

    @Getter
    @Setter
    String naziv;
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

    /**
     * Creates a new instance of DodavanjeAerodroma
     */
    public DodavanjeAerodroma() {
    }

    /**
     * Metoda koja preuzima podatke korisnika iz prijave korisnika 
     * i sprema u lokalne varijable korisnik i lozinka.
     */
    public void preuzmiPodatkeKorisnika() {
        korisnik = prijavaKorisnika.getKorisnik();
        lozinka = prijavaKorisnika.getLozinka();
    }

    /**
     * Metoda koja preuzima podatke korisnika te poziva Web servis druge aplikacije 
     * koristeći ws klijent te dohvaća aerodrome prema nazivu ili drzavi koja je unesena.
     * @return Lista tipa aerodrom, lista aerodroma koji su pronađeni koristeći web servis.
     */
    public List<Aerodrom> dajAerodromaNaziv() {
        preuzmiPodatkeKorisnika();
        aerodromi = new ArrayList<>();
        Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka);
        OdgovorAerodrom odgovor = zadaca2_2RS.dajAerodromeNaziv(OdgovorAerodrom.class, drzava, naziv);
        aerodromi = Arrays.asList(odgovor.getOdgovor());

        return aerodromi;
    }

    /**
     * Metoda koja  dohvaća sve aerodrome korisnika koji je trenutno prijavljen
     * @return Lista tipa aerodrom, vraća se lista aerodroma koje korisnik prati (myairports)
     */
    public List<Aerodrom> dohvatiAerodromeKorisnika() {
        preuzmiPodatkeKorisnika();
        Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka);
        OdgovorAerodrom odgovor = zadaca2_2RS.dajAerodomeKorisnika(OdgovorAerodrom.class);
        aerodromiKorisnika = Arrays.asList(odgovor.getOdgovor());
        return aerodromiKorisnika;
    }

    /**
     * Dodaje izabran aerodrom u tablicu myairports za korisnika koji je ulogiran
     * @param icao parametar icao predstavlja naziv aerodroma koji se dodaje u myairports tablicu
     * @return
     */
    public String dodajAerodromKorisniku(String icao) {
        preuzmiPodatkeKorisnika();
        Gson gson = new Gson();
        Icao icao1 = new Icao();
        icao1.setIcao(icao);

        Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka);
        Response dodajAerodrom = zadaca2_2RS.dodajAerodrom(icao1);
        return "";
    }

}
