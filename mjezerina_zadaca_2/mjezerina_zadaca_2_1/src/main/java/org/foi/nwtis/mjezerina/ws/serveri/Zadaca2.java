package org.foi.nwtis.mjezerina.ws.serveri;

import java.util.List;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import org.foi.nwtis.mjezerina.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.mjezerina.web.podaci.AirportDAO;
import org.foi.nwtis.mjezerina.web.podaci.KorisnikDAO;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.podaci.AvionLeti;

@WebService(serviceName = "Zadaca2")
public class Zadaca2 {

    KorisnikDAO korisnikDAO = new KorisnikDAO();
    AirportDAO airportDao = new AirportDAO();

    @Inject
    ServletContext context;

    /**
     *
     * @param noviKorisnik
     * @param lozinka
     * @return
     */
    @WebMethod(operationName = "dodajKorisnika")
    public Boolean dodajKorisnika(@WebParam(name = "noviKorisnik") Korisnik noviKorisnik) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");

        return korisnikDAO.dodajKorisnika(noviKorisnik, bpk);
    }

    @WebMethod(operationName = "provjeraKorisnika")
    public Boolean provjeraKorisnika(@WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");

        return korisnikDAO.dohvatiKorisnika(korisnik, lozinka, bpk);
    }

    @WebMethod(operationName = "dajAerodromeNaziv")
    public List<Aerodrom> dajAerodromeNaziv(@WebParam(name = "nazivAerodroma") String nazivAerodroma, @WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka) {

        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        if (korisnikDAO.dohvatiKorisnika(korisnik, lozinka, bpk)) {
            return airportDao.dajAerodromeNaziv(nazivAerodroma, bpk);
        }
        return null;
    }

    @WebMethod(operationName = "dajAerodromeDrzava")
    public List<Aerodrom> dajAerodromeDrzava(@WebParam(name = "iso_drzave") String iso_drzave, @WebParam(name = "korisnik") String korisnik,
            @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        if (korisnikDAO.dohvatiKorisnika(korisnik, lozinka, bpk)) {
            return airportDao.dajAerodromeDrzava(iso_drzave, bpk);
        }
        return null;
    }

    @WebMethod(operationName = "dajAerodromeKorisnika")
    public List<Aerodrom> dajAerodromeKorisnika(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        if (korisnikDAO.dohvatiKorisnika(korisnik, lozinka, bpk)) {
            return airportDao.dajAerodromeKorisnika(korisnik, bpk);
        }
        return null;
    }

    @WebMethod(operationName = "korisniciAerodroma")
    public boolean korisniciAerodroma(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "icao") String icao) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        if (korisnikDAO.dohvatiKorisnika(korisnik, lozinka, bpk)) {
            return airportDao.korisniciAerodroma(korisnik, icao, bpk);
        }
        return false;
    }

    @WebMethod(operationName = "dajAerodrom")
    public Aerodrom dajAerodrom(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "icao") String icao) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        if (korisnikDAO.dohvatiKorisnika(korisnik, lozinka, bpk)) {
            return airportDao.dajAerodrom(korisnik, icao, bpk);
        }
        return null;

    }

    @WebMethod(operationName = "dodajAerodrom")
    public Boolean dodajAerodrom(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "icao") String icao) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        if (korisnikDAO.dohvatiKorisnika(korisnik, lozinka, bpk)) {
            return airportDao.dodajAerodrom(korisnik, icao, bpk);
        }
        return false;

    }

    @WebMethod(operationName = "dajAvioniLete")
    public List<AvionLeti> dajAvioniLete(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "icao") String icao, @WebParam(name = "odVremena") long odVremena, @WebParam(name = "doVremena") long doVremena) {
        BP_Konfiguracija bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        if (korisnikDAO.dohvatiKorisnika(korisnik, lozinka, bpk)) {
            return airportDao.dajAvioniLete(icao, odVremena, doVremena, bpk);
        }
        return null;
    }

}
