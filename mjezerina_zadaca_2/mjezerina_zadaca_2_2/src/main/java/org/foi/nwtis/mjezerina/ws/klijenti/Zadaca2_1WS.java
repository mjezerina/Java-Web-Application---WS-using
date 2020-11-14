/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mjezerina.ws.klijenti;

import java.util.List;
import org.foi.nwtis.mjezerina.ws.serveri.Aerodrom;

/**
 *
 * @author Matija
 */
public class Zadaca2_1WS {

    /**
     * Metoda koja poziva web servis prve aplikacije te dohvaća aerodrome koje
     * prati korisnik iz tablice myairports
     *
     * @param korisnik parametar koji se provjerava za autentikaciju
     * @param lozinka parametar koji koristi za autentikaciju
     * @return Lista tipa aerodrom, lista aerodroma koje prati uneseni korisnik.
     */
    public List<Aerodrom> dajAerodomeKorisnika(String korisnik, String lozinka) {
        List<Aerodrom> listaAerodroma = null;

        try {
            org.foi.nwtis.mjezerina.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.mjezerina.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.mjezerina.ws.serveri.Zadaca2 port = service.getZadaca2Port();
            listaAerodroma = port.dajAerodromeKorisnika(korisnik, lozinka);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return listaAerodroma;
    }

    /**
     * Metoda koja dohvaća sve aerodrome prema nazivu koji je unesen
     *
     * @param korisnik parametar koji služi za autentikaciju
     * @param lozinka parametar koji služi za autentikaciju
     * @param naziv parametar koji sadrži naziv prema kojem se dohvaćaju
     * aerodromi sličnog naziva
     * @return lista tipa Aerodrom koja sadrži aerodrome sličnog naziva unesenom
     * parametru.
     */
    public List<Aerodrom> dajAerodromeNaziv(String korisnik, String lozinka, String naziv) {
        List<Aerodrom> listaAerodroma = null;

        try { // Call Web Service Operation
            org.foi.nwtis.mjezerina.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.mjezerina.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.mjezerina.ws.serveri.Zadaca2 port = service.getZadaca2Port();

            listaAerodroma = port.dajAerodromeNaziv(naziv, korisnik, lozinka);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return listaAerodroma;
    }

    /**
     * Metoda koja dohvaća sve aerodrome prema državi koja je unesena
     *
     * @param korisnik parametar koji služi za autentikaciju
     * @param lozinka parametar koji služi za autentikaciju
     * @param isoDrzave parametar koji sadrži ime države prema kojem se
     * dohvaćaju aerodromi
     * @return lista tipa Aerodrom koja sadrži aerodrome iz države unesenog
     * parametra.
     */
    public List<Aerodrom> dajAerodromeDrzava(String korisnik, String lozinka, String isoDrzave) {
        List<Aerodrom> listaAerodroma = null;

        try { // Call Web Service Operation
            org.foi.nwtis.mjezerina.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.mjezerina.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.mjezerina.ws.serveri.Zadaca2 port = service.getZadaca2Port();
            listaAerodroma = port.dajAerodromeDrzava(isoDrzave, korisnik, lozinka);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return listaAerodroma;
    }

    /**
     * Metoda koja dohvaća aerodrom ako se nalazi u listi aerodroma korisnika
     *
     * @param korisnik parametar koji služi za autentikaciju
     * @param lozinka parametar koji služi za autentikaciju
     * @param icao parametar koji sadrži naziv aerodroma koji se dohvaća
     * @return vraća se Objekt tipa Aerodrom
     */
    public Aerodrom dajAerodrom(String korisnik, String lozinka, String icao) {
        Aerodrom aerodrom = new Aerodrom();

        try { // Call Web Service Operation
            org.foi.nwtis.mjezerina.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.mjezerina.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.mjezerina.ws.serveri.Zadaca2 port = service.getZadaca2Port();

            aerodrom = port.dajAerodrom(korisnik, lozinka, icao);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return aerodrom;
    }

    /**
     * Metoda koja dodaje aerodrom u list Myairports korisnika
     *
     * @param korisnik parametar koji služi za autentikaciju
     * @param lozinka parametar koji služi za autentikaciju
     * @param icao icao kod aerodroma koji se dodaje
     * @return boolean true ako je unesen podatak, false ako nije
     */
    public boolean dodajAerodrom(String korisnik, String lozinka, String icao) {
        boolean dodajAerodrom = false;

        try {
            org.foi.nwtis.mjezerina.ws.serveri.Zadaca2_Service service = new org.foi.nwtis.mjezerina.ws.serveri.Zadaca2_Service();
            org.foi.nwtis.mjezerina.ws.serveri.Zadaca2 port = service.getZadaca2Port();
            dodajAerodrom = port.dodajAerodrom(korisnik, lozinka, icao);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return dodajAerodrom;
    }
}
