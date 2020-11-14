/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mjezerina.web.zrna;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.mjezerina.rest.klijenti.Zadaca2_2RS;
import org.foi.nwtis.mjezerina.ws.klijenti.Zadaca2_1WS;
import org.foi.nwtis.mjezerina.ws.serveri.AvionLeti;
import org.foi.nwtis.podaci.Aerodrom;

import org.foi.nwtis.podaci.OdgovorAerodrom;

/**
 *
 * @author Matija
 */
@Named(value = "pregledAerodroma")
@SessionScoped
public class PregledAerodroma implements Serializable {

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
    public PregledAerodroma() {
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
     * Metoda koja dohvaća aerodrome koje korisnik prati iz tablice myairports
     * @return lista tipa Aerodrom koja sadrži aerodrome koje korisnik prati.
     */
    public List<Aerodrom> dohvatiAerodromeKorisnika() {
        preuzmiPodatkeKorisnika();
        Zadaca2_2RS zadaca2_2RS = new Zadaca2_2RS(korisnik, lozinka);
        OdgovorAerodrom odgovor = zadaca2_2RS.dajAerodomeKorisnika(OdgovorAerodrom.class);
        aerodromiKorisnika = Arrays.asList(odgovor.getOdgovor());
        return aerodromiKorisnika;
    }

    /**
     * Metoda koja dohvaća Avione iz web servisa prve aplikacije na temelju icao koda i odVremena i doVremena 
     * @param icao parametar koji nosi naziv aerodroma za koji se dohvaćaju avioni u određenom datumu
     * @return lista tipa AvionLeti koja sadrži sve avione koji su poletjeli u zadanom vremenskom intervalu.
     * @throws ParseException
     */
    public List<AvionLeti> dohvatiAvione(String icao) throws ParseException {
        listaAviona = null;
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        if(icao != null){
        listaAviona = zadaca2_1WS.dohvatiAvione(korisnik, lozinka, icao, konvertirajDatumUnixTimestamp(vrijemeOd), konvertirajDatumUnixTimestamp(vrijemeDo));
        }
        return listaAviona;
    }

    /**
     * Metoda koja konvertira uneseni datum u string formatu te vraća long podatak
     * @param datum datum koji je unesen u string formatu
     * @return long epoch, unix timestamp vrijeme u sekundama
     * @throws ParseException
     */
    public long konvertirajDatumUnixTimestamp(String datum) throws ParseException {
        Date formatiraniDatum = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(datum);
        long epoch = formatiraniDatum.getTime() / 1000;
        return epoch;
    }

}
