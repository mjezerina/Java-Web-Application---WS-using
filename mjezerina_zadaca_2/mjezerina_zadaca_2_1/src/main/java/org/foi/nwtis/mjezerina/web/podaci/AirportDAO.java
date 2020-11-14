package org.foi.nwtis.mjezerina.web.podaci;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.mjezerina.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.mjezerina.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.podaci.Icao;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.Lokacija;

public class AirportDAO {

    public AirportDAO() {
        dohvatiPodatkeZaBazu();
    }

    private BP_Konfiguracija bpk;
    private ServletContext sc;
    private String url;
    private String bpkorisnik;
    private String bplozinka;

    private void dohvatiPodatkeZaBazu() {
        sc = SlusacAplikacije.getSc();
        bpk = (BP_Konfiguracija) sc.getAttribute("BP_Konfig");
        url = bpk.getServerDatabase() + bpk.getUserDatabase();
        bpkorisnik = bpk.getUserUsername();
        bplozinka = bpk.getUserPassword();
    }

    /**
     * Metoda koja dohvaća sve aerodrome koji su uneseni u tablicu MYAIRPORTS
     * @return lista tipa Icao koja sadrži listu icao naziva aerodroma iz tablice myairports
     */
    public List<Icao> dohvatiAerodromeKorisnika() {

        String upit = "SELECT IDENT FROM MYAIRPORTS";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Icao> listaAerodroma = new ArrayList<>();

                while (rs.next()) {
                    String icao24 = rs.getString("ident");
                    Icao icao = new Icao(icao24);
                    listaAerodroma.add(icao);
                }
                return listaAerodroma;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Metoda koja prima nazivAerodroma i datum i provjerava u myairportslog tablici postoji li preuzimanje
     * određenog aerodroma za određeni datum
     * @param nazivAerodroma naziv aerodroma za koji se provjerava postoji li
     * @param datum datum za koji se provjerava postoji li unos u tablici
     * @return boolean true ako avion već postoji, false ako ne postoji u tablici.
     */
    public Boolean provjeriPostojanjePreuzimanjaDatum(String nazivAerodroma, String datum) {
        boolean AvionPostoji = false;

        String upit = "SELECT IDENT, FLIGHTDATE FROM MYAIRPORTSLOG WHERE IDENT = '" + nazivAerodroma + "'" + " AND FLIGHTDATE = '" + datum + "'";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                if (rs.next()) {
                    AvionPostoji = true;
                }

                return AvionPostoji;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Metoda koja dodaje aerodrom u tablicu Myairportslog, podaci koje unosi su
     * nazivAerodroma, datum za koji je obavljeno preuzimanje i current timestamp vremena kad je
     * obavljena operacija.
     * @param nazivAerodroma naziv aerodroma icao koji se unosi u tablicu myairportslog
     * @param datumUnosa datum koji predstavlja kada su podaci za letove preuzeti
     * @return boolean true ako je podatak unesen
     */
    public boolean dodajAerodromULog(String nazivAerodroma, String datumUnosa) {

        String upit = "INSERT INTO myairportslog (ident, flightdate, stored) VALUES ("
                + "'" + nazivAerodroma + "', '" + datumUnosa
                + "',CURRENT_TIMESTAMP)";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement()) {

                int brojAzuriranja = s.executeUpdate(upit);

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Metoda koja dodaje avion u tablicu Airplanes
     * @param avionLeti objekt tipa avionLeti, sadrži sve potrebne informacije o letu aviona
     * @return vraća true ako je podatak ispravno unesen
     */
    public boolean dodajAvion(AvionLeti avionLeti) {

        String upit = "INSERT INTO airplanes (icao24, firstseen, estdepartureairport, lastseen, estarrivalairport, callsign,"
                + "estdepartureairporthorizdistance, estdepartureairportvertdistance, estarrivalairporthorizdistance, estarrivalairportvertdistance,"
                + "departureairportcandidatescount, arrivalairportcandidatescount, stored) VALUES(" + "'" + avionLeti.getIcao24() + "', " + avionLeti.getFirstSeen() + ", '"
                + avionLeti.getEstDepartureAirport() + "', " + avionLeti.getLastSeen() + ", '" + avionLeti.getEstArrivalAirport() + "','" + avionLeti.getCallsign() + "', "
                + avionLeti.getEstDepartureAirportHorizDistance() + "," + avionLeti.getEstDepartureAirportVertDistance() + "," + avionLeti.getEstArrivalAirportHorizDistance() + ", "
                + avionLeti.getEstArrivalAirportVertDistance() + ", " + avionLeti.getDepartureAirportCandidatesCount() + ", " + avionLeti.getArrivalAirportCandidatesCount() + ", CURRENT_TIMESTAMP)";
        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement()) {

                int brojAzuriranja = s.executeUpdate(upit);

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Metoda koja prima naziv aviona te prema upitu provjerava u bazi podataka postoji li
     * avion koji je sličnog naziva kao unesen naziv
     * @param naziv unesen naziv prema kojem se obavlja provjera
     * @param bpk
     * @return lista tipa Aerodrom koja sadrži sve aerodrome sličnog naziva unesenom nazivu pretrage
     */
    public List<Aerodrom> dajAerodromeNaziv(String naziv, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT * FROM AIRPORTS WHERE NAME LIKE '" + naziv + "'";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Aerodrom> listaAerodroma = new ArrayList<>();
                while (rs.next()) {
                    String icao24 = rs.getString("ident");
                    String nazivAerodroma = rs.getString("name");
                    String drzava = rs.getString("iso_country");
                    String koordinate = rs.getString("coordinates");
                    String[] splited = koordinate.trim().split(",");
                    String latituda = splited[1].trim();
                    String longituda = splited[0].trim();
                    Lokacija lokacija = new Lokacija(latituda, longituda);
                    Aerodrom aerodrom = new Aerodrom(icao24, nazivAerodroma, drzava, lokacija);
                    listaAerodroma.add(aerodrom);
                }

                return listaAerodroma;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Aerodrom> dajAerodromeDrzava(String iso_drzave, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT * FROM AIRPORTS WHERE ISO_COUNTRY = '" + iso_drzave + "'";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Aerodrom> listaAerodroma = new ArrayList<>();
                while (rs.next()) {
                    String icao24 = rs.getString("ident");
                    String nazivAerodroma = rs.getString("name");
                    String drzava = rs.getString("iso_country");
                    String koordinate = rs.getString("coordinates");
                    String[] splited = koordinate.trim().split(",");
                    String latituda = splited[1].trim();
                    String longituda = splited[0].trim();
                    Lokacija lokacija = new Lokacija(latituda, longituda);
                    Aerodrom aerodrom = new Aerodrom(icao24, nazivAerodroma, drzava, lokacija);
                    listaAerodroma.add(aerodrom);
                }

                return listaAerodroma;

            } catch (SQLException ex) {
                Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KorisnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Metoda koja dohvaća aerodrome korisnika, kao parametar prima korisnicko ime
     * i iz baze podataka pomoću upita dohvaća sve aerodrome koje korisnik prati
     * @param korime korisničko ime pomoću kojeg dohvaća podatke iz tablice myairports
     * @param bpk
     * @return lista tipa Aerodrom koja sadrži sve aerodrome koje prati određen korisnik.
     */
    public List<Aerodrom> dajAerodromeKorisnika(String korime, BP_Konfiguracija bpk) {

        String upit = "SELECT * FROM AIRPORTS JOIN MYAIRPORTS ON AIRPORTS.IDENT = MYAIRPORTS.IDENT WHERE MYAIRPORTS.USERNAME= '" + korime + "'";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<Aerodrom> listaAerodroma = new ArrayList<>();

                while (rs.next()) {
                    String icao24 = rs.getString("ident");
                    String nazivAerodroma = rs.getString("name");
                    String drzava = rs.getString("iso_country");
                    String koordinate = rs.getString("coordinates");
                    String[] splited = koordinate.trim().split(",");
                    String latituda = splited[1].trim();
                    String longituda = splited[0].trim();
                    Lokacija lokacija = new Lokacija(latituda, longituda);
                    Aerodrom aerodrom = new Aerodrom(icao24, nazivAerodroma, drzava, lokacija);
                    listaAerodroma.add(aerodrom);
                }
                return listaAerodroma;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Metoda koja kao parametar prima korisničko ime i icao kod aerodroma
     * pomoću upita ako se aerodrom nalazi u tablici myairports odnosno među aerodromima
     * koje uneseni korisnik prati, vraća objekt tipa Aerodrom s podacima tog aerodroma
     * @param korime korisničko ime koje se koristi u upitu
     * @param icao icao kod aerodroma za koji se dohvaćaju podaci o aerodromu
     * @param bpk
     * @return objekt tipa Aerodrom koji sadrži podatke specifičnog traženog aerodroma ako postoji.
     */
    public Aerodrom dajAerodrom(String korime, String icao, BP_Konfiguracija bpk) {
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT * FROM MYAIRPORTS  LEFT JOIN AIRPORTS ON MYAIRPORTS.IDENT = AIRPORTS.IDENT WHERE MYAIRPORTS.IDENT = '" + icao + "' AND MYAIRPORTS.USERNAME = '" + korime + "'";
        Aerodrom aerodrom = null;
        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                if (rs.next()) {
                    String icao24 = rs.getString("ident");
                    String nazivAerodroma = rs.getString("name");
                    String drzava = rs.getString("iso_country");
                    String koordinate = rs.getString("coordinates");
                    String[] splited = koordinate.trim().split(",");
                    String latituda = splited[1].trim();
                    String longituda = splited[0].trim();
                    Lokacija lokacija = new Lokacija(latituda, longituda);
                    aerodrom = new Aerodrom(icao24, nazivAerodroma, drzava, lokacija);
                }

                return aerodrom;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Metoda koja provjerava nalazi li se traženi aerodrom u popisu aerodroma koje prati korisnik
     * odnosno u tablici myairports.
     * @param korime korisnicko ime prema kojem se obavlja upit
     * @param icao naziv aerodroma za koji se provjerava
     * @param bpk
     * @return true ako aerodrom postoji u popisu aerodroma korisnika.
     */
    public boolean korisniciAerodroma(String korime, String icao, BP_Konfiguracija bpk) {
        boolean aerodromPostoji = false;
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT * FROM MYAIRPORTS WHERE USERNAME = '" + korime + "' AND IDENT = '" + icao + "'";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                if (rs.next()) {
                    aerodromPostoji = true;
                }

                return aerodromPostoji;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Metoda koja prvim upitom provjerava postoji li uneseni icao kod iz parametara
     * u tablici svih aerodroma te ako postoji unosi novi aerodrom u tablicu myairports 
     * za korisnika koji je unesen i spomenuti aerodrom.
     * @param korime korisnicko ime za koje se unosi novi aerodrom u tablicu myairports
     * @param icao naziv aerodroma za koji se obavlja provjera postoji li te ako postoji moguć je unos 
     * u tablicu myairports aerodroma koje prati korisnik
     * @param bpk
     * @return boolean true ako je aerodrom uspješno unesen u listu aerodroma koje prati korisnik (myairports)
     */
    public boolean dodajAerodrom(String korime, String icao, BP_Konfiguracija bpk) {
        boolean aerodromPostoji = false;
        String url = bpk.getServerDatabase() + bpk.getUserDatabase();
        String bpkorisnik = bpk.getUserUsername();
        String bplozinka = bpk.getUserPassword();
        String upit = "SELECT IDENT FROM AIRPORTS WHERE IDENT = '" + icao + "'";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                if (rs.next()) {
                    aerodromPostoji = true;
                }

                if (aerodromPostoji) {
                    upit = "INSERT INTO MYAIRPORTS (IDENT, USERNAME, STORED) VALUES ("
                            + "'" + icao + "', '" + korime
                            + "',CURRENT_TIMESTAMP)";

                    Statement s2 = con.createStatement();
                    int brojAzuriranja = s2.executeUpdate(upit);

                    return brojAzuriranja == 1;

                }

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Metoda koja dohvaća sve avione iz tablice airplanes koji sadrže icao naziv aerodroma polazišta koji 
     * je unesen ko parametar, te je let između unesenog vremena parametra odVremena i doVremena
     * @param icao naziv aerodroma s kojeg su poletjeli avioni
     * @param odVremena vrijeme koje se uzima za vrijeme leta od polijetanja aviona
     * @param doVremena vrijeme koje se uzima kao krajnje vrijeme dohvata letova aviona
     * @param bpk
     * @return lista tipa AvionLeti koja sadrži sve podatke o letovima koji odgovaraju unesenim parametrima.
     */
    public List<AvionLeti> dajAvioniLete(String icao, long odVremena, long doVremena, BP_Konfiguracija bpk) {

        String upit = "SELECT * FROM AIRPLANES WHERE ESTDEPARTUREAIRPORT = '" + icao + "' AND FIRSTSEEN BETWEEN " + odVremena + " AND " + doVremena + "";

        try {
            Class.forName(bpk.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                List<AvionLeti> listaAviona = new ArrayList<>();

                while (rs.next()) {
                    String icao24 = rs.getString("icao24");
                    int firstSeen = Integer.parseInt(rs.getString("firstSeen"));
                    String estDepartureAirport = rs.getString("estdepartureairport");
                    int lastSeen = Integer.parseInt(rs.getString("lastSeen"));
                    String estArrivalAirport = rs.getString("estArrivalAirport");
                    String callsign = rs.getString("callsign");
                    int estDepartureAirportHorizDistance = Integer.parseInt(rs.getString("estDepartureAirportHorizDistance"));
                    int estDepartureAirportVertDistance = Integer.parseInt(rs.getString("estDepartureAirportVertDistance"));
                    int estArrivalAirportHorizDistance = Integer.parseInt(rs.getString("estArrivalAirportHorizDistance"));
                    int estArrivalAirportVertDistance = Integer.parseInt(rs.getString("estArrivalAirportVertDistance"));
                    int departureAirportCandidatesCount = Integer.parseInt(rs.getString("departureAirportCandidatesCount"));
                    int arrivalAirportCandidatesCount = Integer.parseInt(rs.getString("arrivalAirportCandidatesCount"));
                    AvionLeti avionLeti = new AvionLeti(icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance,
                    estArrivalAirportHorizDistance, estArrivalAirportVertDistance, departureAirportCandidatesCount, arrivalAirportCandidatesCount);
                    listaAviona.add(avionLeti);
                }
                return listaAviona;

            } catch (SQLException ex) {
                Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
