/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mjezerina.rest.serveri;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.foi.nwtis.mjezerina.ws.klijenti.Zadaca2_1WS;
import org.foi.nwtis.mjezerina.ws.serveri.Aerodrom;
import org.foi.nwtis.podaci.Icao;

import org.foi.nwtis.podaci.Odgovor;

/**
 * REST Web Service
 *
 * @author Matija
 */
@Path("aerodromi")
public class Zadaca2RestAerodromi {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Zadaca2RestAerodromi
     */
    public Zadaca2RestAerodromi() {
    }

    /**
     * GET metoda web servisa koja poziva metodu dajAerodrome iz ws klijenta
     * i dohvaća sve aerodrome koje korisnik ima
     * dodane da prati (myairports tablica)
     *
     * @param korisnik
     * @param lozinka
     * @return an instance of Response
     */
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodomeKorisnika(
            @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {

        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        List<Aerodrom> aerodromi = zadaca2_1WS.dajAerodomeKorisnika(korisnik, lozinka);
        if (aerodromi == null) {
            aerodromi = new ArrayList<>();
        }

        Odgovor odgovor = new Odgovor();
        odgovor.setStatus("10");
        odgovor.setPoruka("OK");
        odgovor.setOdgovor(aerodromi.toArray());
        return Response.ok(odgovor).status(200).build();
    }

    /**
     * POST metoda koja šalje zahtjev koji u svome tijelu sadrži icao aviona koji 
     * se dodaje trenutno ulogiranom korisniku u avione koje prati. 
     * @param icao parametar koji sadrži naziv aviona, nalazi se u body-u zahtjeva
     * @param korisnik parametar koji se koristi za autentikaciju
     * @param lozinka parametar koji se koristi za autentikaciju
     * @return 
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dodajAerodrom(String icao,
            @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {

        List<Boolean> listaOdgovora = null;
        Gson gson = new Gson();
        Icao icaoObjekt = gson.fromJson(icao, Icao.class);
        String icaoAerodroma = icaoObjekt.getIcao();
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        boolean dodajAerodrom = zadaca2_1WS.dodajAerodrom(korisnik, lozinka, icaoAerodroma);

        if (listaOdgovora == null) {
            listaOdgovora = new ArrayList<>();
        }
        Odgovor odgovor = new Odgovor();
        listaOdgovora.add(dodajAerodrom);
        odgovor.setStatus("10");
        odgovor.setPoruka("OK");
        odgovor.setOdgovor(listaOdgovora.toArray());
        return Response.ok(odgovor).status(200).build();

    }

    /**
     * Metoda koja se dohvaća sa putanjom /svi , dohvaćaju se aerodromi sličnog naziva koji je unesen,
     * iste države koja je izabrana ili ukoliko nema parametara dohvaćaju se svi aerodromi.
     * @param korisnik parametar koji se koristi za autentikaciju
     * @param lozinka parametar koji se koristi za autentikaciju
     * @param naziv parametar koji nije obavezan, nalazi se u query parametrima url zahtjeva, pretraživanje po nazivu
     * @param drzava parametar koji nije obavezan, nalazi se u query parametrima url zahtjeva- pretraživanje po državi
     * @return Objekt klase Odgovor koji u svom tijelu sadrži listu dohvaćenih aerodroma.
     */
    @GET
    @Path("/svi")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodromeNaziv(
            @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @QueryParam("naziv") String naziv,
            @QueryParam("drzava") String drzava) {

        List<Aerodrom> aerodromi = null;
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        if (naziv  != null && !naziv.isEmpty()) {
            aerodromi = zadaca2_1WS.dajAerodromeNaziv(korisnik, lozinka, naziv);
        }
        else if (drzava != null && !drzava.isEmpty()) {
            aerodromi = zadaca2_1WS.dajAerodromeDrzava(korisnik, lozinka, drzava);
        }
        else{
            aerodromi = zadaca2_1WS.dajAerodromeNaziv(korisnik, lozinka, "%");
        }
        
        
        if (aerodromi == null) {
            aerodromi = new ArrayList<>();
        }

        Odgovor odgovor = new Odgovor();
        odgovor.setStatus("10");
        odgovor.setPoruka("OK");
        odgovor.setOdgovor(aerodromi.toArray());
        return Response.ok(odgovor).status(200).build();
    }
    
    /**
     * Metoda koja dohvaća aerodrom ako se nalazi u listi aerodroma korisnika
     * @param korisnik parametar koji služi za autentikaciju
     * @param lozinka parametar koji služi za autentikaciju
     * @param icao parametar koji sadrži naziv aerodroma koji se dohvaća
     * @return vraća odgovor koji u svome tijelu sadrži listu objekatas tipa Aerodrom
     */
    @GET
    @Path("/{icao}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodrom(
            @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka,
            @PathParam("icao") String icao) {

        Aerodrom aerodrom = null;
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();
        aerodrom = zadaca2_1WS.dajAerodrom(korisnik, lozinka, icao);
        List<Aerodrom> aerodromi = null;
        if (aerodromi == null) {
            aerodromi = new ArrayList<>();
        }
        if (aerodrom != null) {
            aerodromi.add(aerodrom);
        }

        Odgovor odgovor = new Odgovor();
        odgovor.setStatus("10");
        odgovor.setPoruka("OK");
        odgovor.setOdgovor(aerodromi.toArray());
        return Response.ok(odgovor).status(200).build();
    }

    /**
     * PUT method for updating or creating an instance of Zadaca2RestAerodromi
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
    }
}
