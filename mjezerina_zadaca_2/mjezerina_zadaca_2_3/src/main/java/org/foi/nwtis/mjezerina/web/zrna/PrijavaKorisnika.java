/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mjezerina.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.xml.ws.WebServiceRef;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.mjezerina.ws.klijenti.Zadaca2_1WS;
import org.foi.nwtis.mjezerina.ws.serveri.Aerodrom;
import org.foi.nwtis.mjezerina.ws.serveri.AvionLeti;
import org.foi.nwtis.mjezerina.ws.serveri.Korisnik;
import org.foi.nwtis.mjezerina.ws.serveri.Zadaca2;
import org.foi.nwtis.mjezerina.ws.serveri.Zadaca2_Service;

/**
 *
 * @author Matija
 */
@Named(value = "prijavaKorisnika")
@SessionScoped
public class PrijavaKorisnika implements Serializable {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8084/mjezerina_zadaca_2_1/Zadaca2.wsdl")
    private Zadaca2_Service service;

    
    @Getter
    @Setter
    private String korisnik;
    @Getter
    @Setter
    private String lozinka;
    @Getter
    private String uspjesnoPrijavljenKorisnik=" korisnik nije prijavljen";

    /**
     * Creates a new instance of PrijavaKorisnika
     */
    public PrijavaKorisnika() {
        
    }
    
    /**
     * Metoda koja poziva web servis iz prve aplikacije za 
     * provjeru korisnika postoji li u bazi podataka
     * @return vraća boolean true ako je provjera uspješna, 
     * ako korisnik postoji u bazi podataka i odgovara mu lozinka
     */
    public boolean provjeraKorisnika(){
        Boolean rezultatProvjere=false;
        Zadaca2_1WS zadaca2_1WS = new Zadaca2_1WS();

        
        try { // Call Web Service Operation
            org.foi.nwtis.mjezerina.ws.serveri.Zadaca2 port = service.getZadaca2Port();
            rezultatProvjere = port.provjeraKorisnika(korisnik, lozinka);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        if(rezultatProvjere){
            uspjesnoPrijavljenKorisnik = korisnik;
        }
        return rezultatProvjere;
        
    }

}
