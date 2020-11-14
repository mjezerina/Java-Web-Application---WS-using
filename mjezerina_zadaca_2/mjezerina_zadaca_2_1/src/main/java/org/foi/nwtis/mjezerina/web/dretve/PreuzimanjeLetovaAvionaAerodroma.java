package org.foi.nwtis.mjezerina.web.dretve;

import com.sun.xml.wss.util.DateUtils;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mjezerina.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.mjezerina.web.podaci.AirportDAO;
import org.foi.nwtis.podaci.Icao;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 * Klasa koja koristeći dretvu obavlja rad dohvaćanja podataka o letovima aviona 
 * za aerodrome  korisnika iz tablice myairports za definirani datum.
 * @author Matija
 */
public class PreuzimanjeLetovaAvionaAerodroma extends Thread {

    private BP_Konfiguracija konf;
    int intervalCiklusa = 100 * 1000;
    Boolean krajPreuzimanja = false;
    private String username;
    private String lozinka;
    private String pocetniDatumPreuzimanja;
    private String datumKrajPreuzimanja;
    private Integer trajanjeCiklusaDretve;
    private long trajanjePauzeDretve;
    private long odVremena;
    private long krajVremena;
    private long doVremena;
    private AirportDAO airportDAO;
    private List<Icao> listaAerodroma;
    List<AvionLeti> listaAviona = null;
    private long sutrasnjiDatum;

    public PreuzimanjeLetovaAvionaAerodroma(BP_Konfiguracija konf) {
        this.konf = konf;
    }

    @Override
    public void interrupt() {
        krajPreuzimanja = true;
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void start() {
        username = konf.getOpenSkyNetworkKorisnik();
        lozinka = konf.getOpenSkyNetworkLozinka();
        trajanjeCiklusaDretve = Integer.parseInt(konf.getPreuzimanjeCiklus());
        trajanjePauzeDretve = Integer.parseInt(konf.getPreuzimanjePauza());
        pocetniDatumPreuzimanja = konf.getPreuzimanjePocetak();
        datumKrajPreuzimanja = konf.getPreuzimanjeKraj();
        airportDAO = new AirportDAO();
        listaAerodroma = airportDAO.dohvatiAerodromeKorisnika();
        try {
            odVremena = konvertirajDatumUnixTimestamp(pocetniDatumPreuzimanja);
            krajVremena = konvertirajDatumUnixTimestamp(datumKrajPreuzimanja);
            sutrasnjiDatum = dohvatiSutrašnjiDatumUnix();
            if (odVremena < krajVremena) {
                doVremena = odVremena + 86400;
            }
        } catch (ParseException ex) {
            Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class.getName()).log(Level.SEVERE, null, ex);
        }

        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        OSKlijent oSKlijent = new OSKlijent(username, lozinka);
        int brojac = 0;
        while (!krajPreuzimanja) {
            System.out.println("Brojac: " + brojac++);
            try {
                String datumZaProvjeru = formatirajUDatumZaProvjeru(odVremena);
                for (Icao icao : listaAerodroma) {
                    boolean postoji = airportDAO.provjeriPostojanjePreuzimanjaDatum(icao.getIcao(), datumZaProvjeru);
                    if (!postoji) {
                        listaAviona = oSKlijent.getDepartures(icao.getIcao(), odVremena, doVremena);
                        if (!listaAviona.isEmpty()) {
                            for (AvionLeti avionLeti : listaAviona) {
                                if(avionLeti.getEstArrivalAirport() != null){
                                    airportDAO.dodajAvion(avionLeti);
                                }
                            }
                        }
                        airportDAO.dodajAerodromULog(icao.getIcao(), datumZaProvjeru);
                    }
                    Thread.sleep(trajanjePauzeDretve);
                }
                if (doVremena <= krajVremena) {
                    odVremena = doVremena;
                    doVremena = odVremena + 86400;
                }
                else{
                    System.out.println("Dretva je došla do krajnjeg datuma preuzimanja i prestaje provjeravati i preuzimati podatke");
                    krajPreuzimanja = true;
                }

                if (odVremena >= sutrasnjiDatum) {
                    System.out.println("Cekanje podataka 24h");
                    Thread.sleep(86400000);
                }
                Thread.sleep(trajanjeCiklusaDretve*1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PreuzimanjeLetovaAvionaAerodroma.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * Metoda koja kao parametar prima datum u string formatu koji se preuzima iz konfiguracije
     * Parsira datum iz stringa u potrebni date format te konvertira datum iz date formata u unix timestamp 
     * odnosno epoch long tip podatka
     * @param datum datum string formata koji se preuzima iz konfiguracije
     * @return epoch long format datuma koji je sadržan u parametru datum.
     * @throws ParseException
     */
    public long konvertirajDatumUnixTimestamp(String datum) throws ParseException {
        Date formatiraniDatum = new SimpleDateFormat("dd.MM.yyyy").parse(datum);
        long epoch = formatiraniDatum.getTime() / 1000;
        return epoch;
    }

    /**
     * Metoda koja kao parametar prima datum u long formatu i vraća isti u Date formatu,
     * koristi se za unos podatak u tablicu myairportslog
     * @param datum parametar koji preuzima iz odvremena trenutni datum za koji se obavlja provjera
     * @return datum odvremena u date formatu, potreban za unos podataka u myairportslog tablicu.
     */
    public String formatirajUDatumZaProvjeru(long datum) {
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(datum * 1000));
        return date;
    }

    /**
     * Metoda koja dohvaća dohvaća sutrašnji datum u odabranom formatu datuma
     * i pretvara taj datum u epoch time odnosno unix time stamp long.
     * @return long epoch time vrijednost sutrašnjeg datuma.
     * @throws ParseException
     */
    public long dohvatiSutrašnjiDatumUnix() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date danasnjiDatum = new Date();
        Date danasnjiDatumBezSati = formatter.parse(formatter.format(danasnjiDatum));
        long sutrasnjiDatumEpoch = ((danasnjiDatumBezSati.getTime() / 1000) + 86400);
        return sutrasnjiDatumEpoch;
    }

}
