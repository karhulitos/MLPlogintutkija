package logintutkija;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
//import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
//import java.net.MalformedURLException;
//import java.net.URL;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//import org.apache.commons.io.FileUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/************************************
 * MVC MODEL: LogintutkijaMalli
 * 
 ************************************/
public class LogintutkijaMalli {
	
    /******************
     * LUOKKAMUUTTUJAT
     ******************/
	private LogintutkijaOhjain ohjain;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	public static final String PROGRESS = "progress";
	private int progress = 0;
	private Properties asetukset = new Properties();
	private String asetustiedosto = "logintutkija.asetukset";
	//private ArrayList<ArrayList<Integer>> kayra_taulukko = new ArrayList<ArrayList<Integer>>();
	//private ArrayList<GregorianCalendar> logiaika  = new ArrayList<GregorianCalendar>();

		// haeKonfikki
		// luetaan konfiguraatiotiedot tiedostosta
		//
		public boolean haeKonfiggi() throws UnsupportedEncodingException {
					
			InputStream input = null;
			OutputStream output = null;
		 
			//try-catch lohkon sisään koska tiedostotoiminto
			try {
		 
				input = new FileInputStream(asetustiedosto);
		 
				//ladataan asetustiedosto
				asetukset.load(input);

				//asetetaan DB ikkunan oletukset asetustiedostosta (jos avainsana tyhjä, käytetään oletusta)
				ohjain.setTietokanta_dbms(asetukset.getProperty("tietokanta_dbms", "sqlite"));
				ohjain.setTietokanta_osoite(asetukset.getProperty("tietokanta_osoite", "10.0.0.254"));
				ohjain.setTietokanta_nimi(asetukset.getProperty("tietokanta_nimi","talologger"));
				ohjain.setTietokanta_kayttaja(asetukset.getProperty("tietokanta_kayttaja","tunnus"));
				ohjain.setTietokanta_salasana(asetukset.getProperty("tietokanta_salasana","salasana"));
				ohjain.setPaikallinenTietokanta_tallennus(asetukset.getProperty("paikallinen_tietokanta_tallennus","1"));
				ohjain.setValittu_Tiedosto_Filtteri(asetukset.getProperty("logifiltteri","nibe"));
				
				//asetetaan COP laskennan oletukset
				ohjain.set35COP(asetukset.getProperty("cop_035","4.81"));
				ohjain.set45COP(asetukset.getProperty("cop_045","3.77"));
				
				//oletushakemisto
				ohjain.setOletushakemisto(asetukset.getProperty("oletushakemisto",""));
				
				//käynnistysten luennan aloitus
				ohjain.setLaske_kaynnistys_jos_kaynnissa(muutaTotuusarvoksi(asetukset.getProperty("laske_kaynnistys_jos_kaynnissa","1")));
				
				//F1345 lisäys
				ohjain.setF1345LisaysAskel(asetukset.getProperty("F1345_lisaysaskel","0"));
				
				//Lämmitystarveluku
				//ohjain.setHDDGet(asetukset.getProperty("lammitystarvelukujenhaku","0"));
			}
		    catch(FileNotFoundException ex)
		    {
		    	ohjain.kirjoitaKonsolille(ex.getMessage() + " yritetään luoda asetustiedosto..");
				//luodaan asetustiedosto
		    	try {
		    		//kaikki asetukset mitä tuetaan
		    		asetaOletusasetukset();
		    		//ohjain.kirjoitaKonsolille("Virhe: " + System.getProperty(".") + asetustiedosto);
		    		output = new FileOutputStream(asetustiedosto);
		    		asetukset.store(output, asetustiedosto);
		    		return false;
		    		
		    	} catch (FileNotFoundException e) {
					ohjain.kirjoitaKonsolille("Virhe: " + e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					ohjain.kirjoitaKonsolille("Virhe: " + e.getMessage());
					e.printStackTrace();
				} finally {
					ohjain.kirjoitaKonsolille("OK.\n");
		    	}
		    } catch (IOException ex) {
				ohjain.kirjoitaKonsolille("Virhe: " + ex.getMessage());
				return false;
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						ohjain.kirjoitaKonsolille("Virhe: " + e.getMessage());
					}
				}
			}
			return true;
		}

		// tallennaKonfikki
		// muuta asetusten 1 ja 0 true falseksi
		// 
		private boolean muutaTotuusarvoksi(String value) {
		    boolean returnValue = false;
		    if ("1".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || 
		        "true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value))
		        returnValue = true;
		    return returnValue;
		}
		
		
		// haeHDD
		// haetaan lämmitystarveluku Ilmatieteen laitokselta ellei jo ole haettu
		// 
//		public boolean haeHDDGet() {
//			if (ohjain.getHDDGet()) { //onko asetuksissa merkitty haettavaksi?
//				try {
//					ohjain.kirjoitaKonsolille("Haetaan lämmitystarveluvut Ilmatieteen laitokselta..");
//					FileUtils.copyURLToFile(
//							  new URL("https://cdn.fmi.fi/weather-observations/products/heating-degree-days/lammitystarveluvut-2019.utf8.csv"), 
//							  new File("hdd/lammitystarveluvut-2019.utf8.csv"), 
//							  15, 
//							  60);
//					return true;
//				} catch (MalformedURLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return false;
//			} else {
//				return false;
//			}
//		}
		
		// tallennaKonfikki
		// kirjoitetaan konfiguraatiotiedot tiedostoon
		//
		public boolean tallennaKonfiggi() {
			
			OutputStream output = null;
		 
			//try-catch lohkon sisään koska tiedostotoiminto
			try {
		 
				output = new FileOutputStream(asetustiedosto);
		 
				//kirjoitetaan DB ikkunan oletukset asetustiedostosta (jos avainsana )
				setProgress(1);
				ohjain.kirjoitaKonsolille("Yritetään tallentaa asetukset..");
				asetukset.setProperty("tietokanta_dbms", ohjain.getTietokanta_dbms());
				setProgress(10);
				asetukset.setProperty("tietokanta_nimi", ohjain.getTietokanta_nimi());
				setProgress(20);
				asetukset.setProperty("tietokanta_kayttaja", ohjain.getTietokanta_kayttaja());
				setProgress(30);
				asetukset.setProperty("tietokanta_salasana", ohjain.getTietokanta_salasana());
				setProgress(40);
				asetukset.setProperty("tietokanta_osoite", ohjain.getTietokanta_osoite());
				setProgress(50);
				asetukset.setProperty("oletushakemisto", ohjain.getOletushakemisto());
				setProgress(60);
				asetukset.setProperty("logifiltteri", ohjain.getValittu_Tiedosto_Filtteri());
				asetukset.store(output, asetustiedosto);
				setProgress(100);
				ohjain.kirjoitaKonsolille("OK.\n");
			}
		    catch(FileNotFoundException ex)
		    {
		    	ohjain.kirjoitaKonsolille(ex.getMessage() + " yritetään luoda asetustiedosto..");
				//luodaan asetustiedosto
		    	try {
		    		//kaikki asetukset mitä tuetaan
		    		asetaOletusasetukset();
		    		output = new FileOutputStream(asetustiedosto);
		    		asetukset.store(output, asetustiedosto);
		    		return false;
		    		
		    	} catch (FileNotFoundException e) {
					ohjain.kirjoitaKonsolille("Virhe: " + e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					ohjain.kirjoitaKonsolille("Virhe: " + e.getMessage());
					e.printStackTrace();
				} finally {
					ohjain.kirjoitaKonsolille("OK.\n");
		    	}
		    } catch (IOException ex) {
				ohjain.kirjoitaKonsolille("Virhe: " + ex.getMessage());
				return false;
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						ohjain.kirjoitaKonsolille("Virhe: " + e.getMessage());
					}
				}
			}
			return true;
		}	
		
		// asetaOletusasetukset
   		// jos properties tiedostoa ei ollut, luodaan oletukset
    	//
		public void asetaOletusasetukset() {
			//tietokanta
			asetukset.setProperty("tietokanta_dbms", "sqlite");
			asetukset.setProperty("tietokanta_nimi", "talologger");
			asetukset.setProperty("tietokanta_kayttaja", "tunnus");
			asetukset.setProperty("tietokanta_salasana", "salasana");
			asetukset.setProperty("tietokanta_osoite", "demopumppaaja.asuscomm.com");
			
			//paikallinen tallennus
			asetukset.setProperty("paikallinen_tietokanta_tallennus", "1");
			asetukset.setProperty("paikallinen_tietokanta_osoite","logintutkija.db");
			
			//COP laskentaa varten EN14511
			asetukset.setProperty("cop_035", "4.81");
			asetukset.setProperty("cop_045", "3.77");
			
			//DB-kentät
			asetukset.setProperty("tietokanta_arvo_TotIntAdd", "TotIntAdd");
			asetukset.setProperty("tietokanta_arvo_BT50", "BT50");
			asetukset.setProperty("tietokanta_arvo_BT1", "BT1");
			asetukset.setProperty("tietokanta_arvo_BT2", "BT2");
			asetukset.setProperty("tietokanta_arvo_BT6", "BT6");
			asetukset.setProperty("tietokanta_arvo_BT7", "BT7");
			asetukset.setProperty("tietokanta_arvo_BT3", "EB100-EP14-BT3");
			asetukset.setProperty("tietokanta_arvo_BT10", "EB100-EP14-BT10");
			asetukset.setProperty("tietokanta_arvo_BT11", "EB100-EP14-BT11");
			asetukset.setProperty("tietokanta_arvo_BT12", "EB100-EP14-BT12");
			asetukset.setProperty("tietokanta_arvo_BT14", "EB100-EP14-BT14");
			asetukset.setProperty("tietokanta_arvo_BT17", "EB100-EP14-BT17");
			
			asetukset.setProperty("tietokanta_arvo_EP15-BT3", "EB100-EP15-BT3");
			asetukset.setProperty("tietokanta_arvo_EP15-BT10", "EB100-EP15-BT10");
			asetukset.setProperty("tietokanta_arvo_EP15-BT11", "EB100-EP15-BT11");
			asetukset.setProperty("tietokanta_arvo_EP15-BT12", "EB100-EP15-BT12");
			asetukset.setProperty("tietokanta_arvo_EP15-BT14", "EB100-EP15-BT14");
			asetukset.setProperty("tietokanta_arvo_EP15-BT17", "EB100-EP15-BT17");
			
			asetukset.setProperty("tietokanta_arvo_BT25", "BT25");
			asetukset.setProperty("tietokanta_arvo_CS", "CalcSupply");
			asetukset.setProperty("tietokanta_arvo_DM", "DegreeMinutes");
			
			asetukset.setProperty("tietokanta_arvo_EP14-Prio", "EB100-EP14-Prio");
			asetukset.setProperty("tietokanta_arvo_EB100-EP14-PCA", "EB100-EP14-PCA");
			asetukset.setProperty("tietokanta_arvo_EP15-Prio", "EB100-EP15-Prio");
			asetukset.setProperty("tietokanta_arvo_EP14-Frequency", "ComprFreq");
			
			asetukset.setProperty("tietokanta_arvo_EP14-GP1", "EB100-EP14-GP1");
			asetukset.setProperty("tietokanta_arvo_EP14-GP2", "EB100-EP14-GP2");
			
			asetukset.setProperty("tietokanta_arvo_EP14-BF1", "EP14-BF1");
			
			//logihakemisto
			asetukset.setProperty("oletushakemisto", "");
			
			//käynnistysten luennan aloitus
			asetukset.setProperty("laske_kaynnistys_jos_kaynnissa", "1");
			
			//F1345 ulkoinen lisäys yksi askel kW
			asetukset.setProperty("F1345_lisaysaskel", "0");
			
			//lämmitystarveluku, haetaanko?
			//asetukset.setProperty("lammitystarvelukujenhaku", "0");
			
		}
	
		// setProgress
   		// edistymispalkin eteneminen
    	//
		public void setProgress(int progress) {
	      int oldProgress = this.progress;
	      this.progress = progress;

	      PropertyChangeEvent evt = new PropertyChangeEvent(this, PROGRESS, oldProgress, progress);
	      pcs.firePropertyChange(evt);
		}

	   public void reset() {
	      setProgress(0);
	   }

	   public void addPropertyChangeListener(PropertyChangeListener listener) {
	      pcs.addPropertyChangeListener(listener);
	   }
	
		//tallennaSQLiteKantaan
  		//tallennetaan logitiedosto sqlite kantaan
   		//
	   @SuppressWarnings("static-access")
	public void tallennaSQLiteKantaan() {
		   
           String yhteysosoite = "jdbc:sqlite:" + asetukset.getProperty("paikallinen_tietokanta_osoite","logintutkija.db");
           Connection yhteys = null;
           String edellinenvirhe = "";
		   //jos meillä on lokitiedostosta luetut tiedot, sekä paikallinen tallennusmuuttuja sallii sen
           
           if (asetukset.getProperty("paikallinen_tietokanta_tallennus","1").equalsIgnoreCase("1") && ohjain.getLogiaika().size() > 0 && ohjain.isPaikallinen_tietokanta_tallennus()) {
    		   //kysytään käyttäjältä haluaako tallennuksen
    		   if (ohjain.kysyTallennusta() == false) {
    			   return;
    		   }
        	   ohjain.kirjoitaKonsolille("Tallennetaan paikalliseen tietokantaan.." );
			   try {
		            Class.forName("org.sqlite.JDBC");
		            yhteys = DriverManager.getConnection(yhteysosoite);
		            if (yhteys != null) {
		            	Statement statement = yhteys.createStatement();
		                statement.setQueryTimeout(15);
                //statement.executeUpdate("drop table if exists logit");
                //statement.executeUpdate("drop index if exists time_idx");
		                statement.executeUpdate("create table if not exists logit (id INTEGER PRIMARY KEY," +
		                		"time TIMESTAMP UNIQUE," +
		                		"version INTEGER," +
		                		"rversion INTEGER," +
		                		"bt1 INTEGER," +
		                		"bt2 INTEGER," +
		                		"bt3 INTEGER," +
		                		"bt67 INTEGER," +	// = BT6. BT6 ja BT7 olivat samassa jostain historiasyystä. Nimi jätetty vanhaksi vaikka kyseessä on BT6.
		                		"bt25 INTEGER," +
		                		"bt10 INTEGER," +
		                		"bt11 INTEGER," +
		                		"bt12 INTEGER," +
		                		"bt14 INTEGER," +
		                		"bt17 INTEGER," +
		                		"degreeminutes INTEGER," +
		                		"calcsupply INTEGER," +
		                		"RelaysPCABase INTEGER," +
								"ep15_bt3 INTEGER," +
		                		"ep15_bt10 INTEGER," +
		                		"ep15_bt11 INTEGER," +
		                		"ep15_bt12 INTEGER," +
		                		"ep15_bt14 INTEGER," +
		                		"ep15_bt17 INTEGER," +
		                		"ep15_prio INTEGER," +
		                		"tia INTEGER," +
		                		"bf1 INTEGER," +
		                		"cfa INTEGER," +
		                		"bt50 INTEGER," +
		                		"model TEXT," +
		                		"ep14_gp1 INTEGER," +
		                		"ep14_gp2 INTEGER," +
		                		"ep15_gp1 INTEGER," +
		                		"bt7 INTEGER," +
		                		"bt51 INTEGER," +
		                		"bt53 INTEGER," +
		                		"bt54 INTEGER," +
		                		"prio INTEGER," +
		                		"bt20 INTEGER," +
		                		"bt21 INTEGER," +
		                		"bt71 INTEGER," +
		                		"ep15_gp2 INTEGER" +
		                		")");

		                //tarkastetaan uusien sarakkeiden olemassaolo
		                DatabaseMetaData md = yhteys.getMetaData();
		                String[] uudet_sarakkeet = {"tia", "bf1", "cfa", "bt50", "model","ep14_gp1","ep14_gp2","ep15_gp1","bt7","bt51","bt53","bt54","prio","bt20","bt21","bt71","ep15_gp2"};
		                String[] uudet_sarakkeet_tyyppi = {"INTEGER","INTEGER", "INTEGER", "INTEGER", "TEXT", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER"};
		                for (int i=0;uudet_sarakkeet.length>i;i++) {
		                	ResultSet rs = md.getColumns(null, null, "logit", uudet_sarakkeet[i]);
							if (!rs.next()) {
								statement.executeUpdate("ALTER TABLE logit ADD COLUMN " + uudet_sarakkeet[i] + " " + uudet_sarakkeet_tyyppi[i]);
								statement.executeUpdate("UPDATE logit SET " + uudet_sarakkeet[i] + " = 0");
							  //ohjain.kirjoitaKonsolille(uudet_sarakkeet[i] + " ei löydy\n");
							}
		                }
		                //indeksi aikaleimalle koska sitä haetaan
		                statement.executeUpdate("create index if not exists time_idx on logit (time)");
		                //aloitetaan transaktio jotta kantaankirjoitus ei ole hidasta (muutoin joka update/insert on oma commit:nsa)
		                statement.executeUpdate("begin transaction");
		                for (int i=0;i<ohjain.getLogiaika().size();i++) {
		                	//rivit kayra_taulukossa
		                	try {
		                		statement.executeUpdate("insert into logit values(NULL, '" +
		                		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ohjain.getLogiaika().get(i).getTime()) + "'" + //date
		                				"," + LogintutkijaOhjain.getLokiVersio() +		 				//version
		                				"," + LogintutkijaOhjain.getLokiRVersio() +						//R version
		                				"," + ohjain.getKayra_taulukko().get(1).get(i) + 	//BT1
		                				"," + ohjain.getKayra_taulukko().get(2).get(i) + 	//BT2
		                				"," + ohjain.getKayra_taulukko().get(3).get(i) + 	//BT3
		                				"," + ohjain.getKayra_taulukko().get(27).get(i) + 	//BT6
		                				"," + ohjain.getKayra_taulukko().get(4).get(i) + 	//BT25
		                				"," + ohjain.getKayra_taulukko().get(5).get(i) + 	//BT10
		                				"," + ohjain.getKayra_taulukko().get(6).get(i) + 	//BT11
		                				"," + ohjain.getKayra_taulukko().get(7).get(i) + 	//BT12
		                				"," + ohjain.getKayra_taulukko().get(8).get(i) + 	//BT14
		                				"," + ohjain.getKayra_taulukko().get(9).get(i) + 	//BT17
		                				"," + ohjain.getKayra_taulukko().get(10).get(i) +	//ASTEMINUUTIT
		                				"," + ohjain.getKayra_taulukko().get(11).get(i) +	//LASKETTU MENO
		                				"," + ohjain.getKayra_taulukko().get(24).get(i) +	//EP14 TILA
		                				"," + ohjain.getKayra_taulukko().get(15).get(i) +	//EP15_BT3
		                				"," + ohjain.getKayra_taulukko().get(16).get(i) +	//EP15_BT10
		                				"," + ohjain.getKayra_taulukko().get(17).get(i) +	//EP15_BT11
		                				"," + ohjain.getKayra_taulukko().get(18).get(i) +	//EP15_BT12
		                				"," + ohjain.getKayra_taulukko().get(19).get(i) +	//EP15_BT14
		                				"," + ohjain.getKayra_taulukko().get(20).get(i) +	//EP15_BT17
		                				"," + ohjain.getKayra_taulukko().get(25).get(i) +	//EP15 prio
		                				"," + ohjain.getKayra_taulukko().get(28).get(i) +	//Tot Int Add
		                				"," + ohjain.getKayra_taulukko().get(29).get(i) +	//BF1
		                				"," + ohjain.getKayra_taulukko().get(30).get(i) +	//CFA
		                				"," + ohjain.getKayra_taulukko().get(0).get(i) +	//BT50
		                				",\"" + ohjain.getLblMLPMalli().getText() + "\"" +	//model
		                				"," + ohjain.getKayra_taulukko().get(32).get(i) +	//GP1
		                				"," + ohjain.getKayra_taulukko().get(33).get(i) +	//GP2
		                				"," + ohjain.getKayra_taulukko().get(34).get(i) +	//EP15_GP1
		                				"," + ohjain.getKayra_taulukko().get(26).get(i) + 	//BT7
		                				"," + ohjain.getKayra_taulukko().get(35).get(i) + 	//BT51
		                				"," + ohjain.getKayra_taulukko().get(36).get(i) + 	//BT53
		                				"," + ohjain.getKayra_taulukko().get(37).get(i) + 	//BT54
		                				"," + ohjain.getKayra_taulukko().get(38).get(i) + 	//prio
		                				"," + ohjain.getKayra_taulukko().get(39).get(i) + 	//BT20
		                				"," + ohjain.getKayra_taulukko().get(40).get(i) + 	//BT21
		                				"," + ohjain.getKayra_taulukko().get(41).get(i) + 	//BT71
		                				"," + ohjain.getKayra_taulukko().get(42).get(i) +	//EP15_GP2
		                				")");
		                	} catch (SQLException ex) {
		                			if (!ex.getMessage().equals(edellinenvirhe)) {
		                				edellinenvirhe=ex.getMessage();
		                				if (ex.getMessage().contains("UNIQUE")) {
		                					ohjain.kirjoitaKonsolille("\nPaikallinen tietokantavirhe: logitieto on jo tallennettu aiemmin.\n");
		                				} else {
		                					ohjain.kirjoitaKonsolille("\nPaikallinen tietokantavirhe: " + ex.getMessage() + "\n");
		                				}
		                			}
		                	}
		                }
		                //lopetetaan transaktio eli tämä on commit
		                statement.executeUpdate("end transaction");
		                yhteys.close();
		                if (edellinenvirhe.isEmpty()) {
		                	ohjain.kirjoitaKonsolille("OK.\n");
		                }
		            }
		        } catch (ClassNotFoundException ex) {
		            ex.printStackTrace();
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		        } finally {
		            try
		            {
		              if(yhteys != null)
		                yhteys.close();
		            }
		            catch(SQLException e)
		            {
		            	ohjain.kirjoitaKonsolille("Virhe: " + e.getMessage() + "\n");
		            }
		        }
           }
	   }
	   
	    // haeSQLiteKannasta
		// haetaan logirivit paikallisesta tietokannasta
	    //	
	   public ArrayList<ArrayList<String[]>> haeSQLiteKannasta(String tietokanta_pvm_mista, String tietokanta_pvm_mihin) {
	      //alustetaan logimuuttujat
		  ArrayList<ArrayList<String[]>> kaikkilogit = new ArrayList<ArrayList<String[]>>();
          ArrayList<String []> kaikkidbrivit = new ArrayList<String []>();
          ArrayList<String> kentat = new ArrayList<String>();
          boolean dbhit = false;
		   String yhteysosoite = "jdbc:sqlite:" + asetukset.getProperty("paikallinen_tietokanta_osoite","logintutkija.db");
           Connection yhteys = null;
           String edellinenvirhe = "";

           //Niben hämä
           kentat.add("Divisors");
           kentat.add("0"); //date
           kentat.add("0"); //time
           kentat.add("0");
           kentat.add("0");
           kentat.add("10");
           kentat.add("10");
           kentat.add("10");
           kentat.add("10");
           kentat.add("10");
           kentat.add("10");//11
           kentat.add("10");
           kentat.add("10");
           kentat.add("10");
           kentat.add("10");
           kentat.add("10");
           kentat.add("10");           //17
           kentat.add("10");
           kentat.add("10");
           kentat.add("10");
           kentat.add("10");           //21
           kentat.add("10");
           kentat.add("10");
           kentat.add("10");
           kentat.add("10");           //25
           kentat.add("10");
           kentat.add("10");
           kentat.add("10");
           kentat.add("10");           //29
           kentat.add("10");
           kentat.add("10");
           kentat.add("1");
           kentat.add("1"); //33
           kentat.add("10");
           kentat.add("1");
           //kentat.add("1"); //pca ep15
           kentat.add("10");
           kentat.add("10");
           kentat.add("10"); //38
           kentat.add("10");
           kentat.add("10");
           kentat.add("10"); //41
           kentat.add("10");
           kentat.add("10");
           kentat.add("10"); //44
           kentat.add("10"); //45
   
           String[] tietue = new String[kentat.size()];
           tietue = kentat.toArray(tietue);
           //ohjain.kirjoitaKonsolille("divisor pituus: " + tietue.length + "\n");
           kaikkidbrivit.add(tietue);
           kentat.clear();

           //otsikkorivi
           kentat.add("Date");
           kentat.add("Time");
           kentat.add("version");
           kentat.add("BT1");
           kentat.add("BT2");
           kentat.add("BT3");
           kentat.add("BT6");
           kentat.add("BT7");
           kentat.add("BT10");
           kentat.add("BT11");//10
           kentat.add("EB100-EP14-BT12");
           kentat.add("EB100-EP14-BT14");
           kentat.add("EB100-EP14-BT17");
           kentat.add("EB100-EP15-BT3");
           kentat.add("EB100-EP15-BT10");
           kentat.add("EB100-EP15-BT11");
           kentat.add("EB100-EP15-BT12");
           kentat.add("EB100-EP15-BT14");
           kentat.add("EB100-EP15-BT17");
           kentat.add("BT25");//20
           kentat.add("BT71");
           kentat.add("BT50");
           kentat.add("BT51");
           kentat.add("BT53");
           kentat.add("BT54");
           kentat.add("EB100-EP14-BP8");
           kentat.add("EB100-EP15-BP8");
           kentat.add("Tot.Int.Add");
           kentat.add("BF1 EP14");
           kentat.add("Alarm");//30
           kentat.add("Calc.Supply");
           kentat.add("Degree Minutes");
           kentat.add("BT1-Average");
           kentat.add("compr. freq. act.");
           kentat.add("Relays PCA-Base EP14");
           //kentat.add("Relays PCA-Base EP15");
           kentat.add("GP1-speed EP14");
           kentat.add("GP1-speed EP15");
           kentat.add("GP2-speed EP14");
           kentat.add("GP2-speed EP15");
           kentat.add("Prio");//40
           kentat.add("AZ1-BT20");
           kentat.add("AZ1-BT21");
           kentat.add("EB100-EP15 Prio");
           kentat.add("EB100-EP14 Prio");
           kentat.add("Model");//45
           
           
           tietue = new String[kentat.size()];
           tietue = kentat.toArray(tietue);
           //ohjain.kirjoitaKonsolille("otsikko pituus: " + tietue.length + "\n");
           kaikkidbrivit.add(tietue);
           kentat.clear();
           
		   try {
	            Class.forName("org.sqlite.JDBC");
	            yhteys = DriverManager.getConnection(yhteysosoite);
	            if (yhteys != null) {
	            	Statement statement = yhteys.createStatement();
	                statement.setQueryTimeout(60);
	            	  ohjain.kirjoitaKonsolille(" yhteys OK. ");
                	try {
                		Statement s = yhteys.createStatement();

		                ResultSet rs = statement.executeQuery("select * from logit where " +
		                         "time between '"+ tietokanta_pvm_mista +"' and '" + tietokanta_pvm_mihin + "' " +
		                         		"order by time");
                		ResultSet r = s.executeQuery("SELECT COUNT(*) AS rowcount FROM logit" +
                				" where time between '" + tietokanta_pvm_mista +
                				"' and '" + tietokanta_pvm_mihin + "'");
                		r.next();
                		int rowcount = r.getInt("rowcount");
                		r.close();

		                int row = 0;

		                 //haun rivien määrä progress baria varten
//		                 if (rs.last()) {
//		                	 rowcount = rs.getRow();
//		                	 rs.beforeFirst();
//		                	 }
		                setProgress(1);
		                while(rs.next())
		                {
		                	dbhit=true;
		                	String [] paivays = rs.getString(2).split("[ ]");
		                	kentat.add(paivays[0]);				//date idx 0
		                	kentat.add(paivays[1]);				//time idx 1
		                   	kentat.add(rs.getString(3));		//versio idx 2
		                   	kentat.add(rs.getString(5));		//BT1 idx 3
							//tarkistetaan onko BT2 tyhjä vaikka olemassa
							if (ohjain.isBt2_nolla()) {
							 //kerrotaan että BT2 on kannassa
							 ohjain.setBt2_nolla(false);
							}
		                   	kentat.add(rs.getString(6));		//BT2 idx 4
		                   	kentat.add(rs.getString(7));		//BT3 idx 5
							kentat.add(rs.getString(8));		//BT6 BT67:sta idx 6
							kentat.add(rs.getString(33));		//BT7 idx 7
							kentat.add(rs.getString(10));		//BT10 idx 8
							kentat.add(rs.getString(11));		//BT11 idx 9
							kentat.add(rs.getString(12));		//EP14-BT12 idx 10 
                   			kentat.add(rs.getString(13));		//EP14-BT14 idx 11
                   			kentat.add(rs.getString(14));		//EB100-EP14-BT17 idx 12
                   			//EP15
                  			//Tot Int Add laskentaa varten hämäys, kertoo ettei olekaan F1345 vaikka EP15 kentät löytyvät
                  			if (rs.getString(18).equalsIgnoreCase("0")) {
                  				kentat.add("32768");
                  			} else {
                  				kentat.add(rs.getString(18));		//EB100-EP15-BT3 //idx 13
                  			}
                   			kentat.add(rs.getString(19));		//EB100-EP15-BT10 //idx 14
                   			kentat.add(rs.getString(20));		//EB100-EP15-BT11 //idx 15
                   			kentat.add(rs.getString(21));		//EB100-EP15-BT12 //idx 16
                   			kentat.add(rs.getString(22));		//EB100-EP15-BT14 //idx 17
                   			kentat.add(rs.getString(23));		//EB100-EP15-BT17 //idx 18
                   			//EP15 off
                   			kentat.add(rs.getString(9));		//BT25  //idx 19
                   			kentat.add(rs.getString(40));		//BT71 //idx 20
                   			
                   			
 		                   	kentat.add(rs.getString(28));		//BT50 //idx 21
                  			kentat.add(rs.getString(34));		//BT51 //idx 22
                  			kentat.add(rs.getString(35));		//BT53 //idx 23
                  			kentat.add(rs.getString(36));		//BT54 //idx 24
                   			kentat.add("0");					//EP14BP8 //idx 25
                   			kentat.add("0");					//EP15BP8 //idx 26
                   			kentat.add(""+(int)(Double.parseDouble(rs.getString(25))/100));		//Tot.Int.Add //idx 27 kenttamappi.put("TotIntAdd", ""+(int)(Double.parseDouble(rs.getString(3))*100));
		                   	kentat.add(rs.getString(26));		//BF1 //idx 28
                   			kentat.add("0");		  			//alarm  //idx 29
		                   	kentat.add(rs.getString(16));		//Calc.Supply //idx 30
		                   	kentat.add(rs.getString(15));		//DM //idx 31
		                   	kentat.add(rs.getString(5));		//BT1 Avg //idx 32
		                   	kentat.add(rs.getString(27));		//CFA //idx 33
		                   	kentat.add(rs.getString(17));		//PCA EP14 //idx 34
		                   	//kentat.add("0");					//PCA EP15 //idx 35
		                   	kentat.add(rs.getString(30));		//GP1 EP14 //idx 35
		                   	kentat.add(rs.getString(32));		//GP1 EP15 //idx 36
		                   	kentat.add(rs.getString(31));		//GP2 EP14 //idx 37
                  			kentat.add(rs.getString(41));		//GP2 EP15
		                   	kentat.add(rs.getString(37));		//Prio
                  			kentat.add(rs.getString(38));		//bt20 //40
                  			kentat.add(rs.getString(39));		//bt21 
		                   	kentat.add(rs.getString(24));		//EP15-Prio
                  			kentat.add("0");					//EP14-Prio
                  			kentat.add(rs.getString(29));		//Model 44
                  			
               			 tietue = new String[kentat.size()];
               			 tietue = kentat.toArray(tietue);
							//ohjain.kirjoitaKonsolille("data pituus: " + tietue.length + "\n");
							//for (int i = 0;tietue.length>i;i++) {
							//ohjain.kirjoitaKonsolille("idx: " + i + ": " + tietue[i].toString() + "\n");
							//}
               			 kaikkidbrivit.add(tietue);
               			 kentat.clear();
               			 
               			 
                     	//päivitetään progress bar per luettu tietue
                    	 row++;
    		            setProgress((row)*100/rowcount);
		                }
		                
		                 //laitetaan kannasta haetut tavarat yhteen taulukkoon joka sisältää kaikki haun rivit
		                 if (kaikkidbrivit.size()>2){
		                	 kaikkilogit.add(kaikkidbrivit);
		                 }
		                
		                if (dbhit == false){
		                	ohjain.kirjoitaKonsolille("Logeja ei löytynyt.\n");
		                }
                	} catch (SQLException ex) {
                		ohjain.kirjoitaKonsolille("\nPaikallinen tietokantavirhe: " + ex.getMessage() + "\n");
                			if (!ex.getMessage().equals(edellinenvirhe)) {
                				edellinenvirhe=ex.getMessage();
                				if (ex.getMessage().contains("UNIQUE")) {
                					ohjain.kirjoitaKonsolille("\nPaikallinen tietokantavirhe: logitieto on jo tallennettu aiemmin.\n");
                				} else {
                					ohjain.kirjoitaKonsolille("\nPaikallinen tietokantavirhe: " + ex.getMessage() + "\n");
                				}
                			}
                	}

	                yhteys.close();
	                if (edellinenvirhe.isEmpty()) {
	                	ohjain.kirjoitaKonsolille("Tietokantakysely suoritettu.\n");
	                }
	            }
	        } catch (ClassNotFoundException ex) {
	        	ohjain.kirjoitaKonsolille("Virhe: " + ex.getMessage() + "\n");
	        } catch (SQLException ex) {
	        	ohjain.kirjoitaKonsolille("Virhe: " + ex.getMessage() + "\n");
	        } finally {
	            try
	            {
	              if(yhteys != null)
	                yhteys.close();
	            }
	            catch(SQLException e)
	            {
	            	ohjain.kirjoitaKonsolille("Virhe: " + e.getMessage() + "\n");
	            }
	        }
		   setProgress(100);
		   return kaikkilogit;
	   }
	   
	   
	    // haeKannasta
		// haetaan logirivit tietokannasta
	    //
	   	public ArrayList<ArrayList<String[]>> haeMySQLKannasta(String tietokanta_osoite, String tietokanta_nimi, String tietokanta_kayttaja, String tietokanta_salasana, String tietokanta_pvm_mista, String tietokanta_pvm_mihin) {
	        Connection con = null;
	        Statement st = null;
	        ResultSet rs = null;
        	//alustetaan logimuuttuja
	        ArrayList<ArrayList<String[]>> kaikkilogit = new ArrayList<ArrayList<String[]>>();
            ArrayList<String []> kaikkidbrivit = new ArrayList<String []>();
            SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdft = new SimpleDateFormat("HH:mm:ss");
            ArrayList<String> kentat = new ArrayList<String>();
            Map<String, String> kenttamappi = new HashMap<String, String>();
            //prio vs PCA Relays Base
            //Prio: 10=Off 20=Hot Water 30=Heat 40=Pool 41=Pool 2 50=Transfer 60=Cooling
            int [] pca = {0,7,15};
            
        	 try {
        		 //ohjain.kirjoitaKonsolille(tietokanta_url + tietokanta_kayttaja + tietokanta_salasana + "\n");
        		 MysqlDataSource ds = new MysqlDataSource();
        		 ds.setServerName(tietokanta_osoite);
        		 ds.setDatabaseName(tietokanta_nimi);
        		 ds.setUser(tietokanta_kayttaja);
        		 ds.setPassword(tietokanta_salasana);
        		 con = ds.getConnection();
        		 st = con.createStatement();
        		 if (con != null) {
            		 ohjain.kirjoitaKonsolille(" yhteys OK. ");
        		 }

        		 setProgress(1);
        		 rs = st.executeQuery("select time, position_name, value  from (SELECT time, position_name, value, talo_data.id  from talo_positions, talo_data where talo_positions.id=position_id" +
                 " and time between '"+ tietokanta_pvm_mista +"' and '" + tietokanta_pvm_mihin + "' " +
                 		" order by talo_data.id DESC) sub ORDER BY id ASC");
                 int rowcount = 0;
                 int row = 0;
                 //haun rivien määrä progress baria varten
                 if (rs.last()) {
                	 rowcount = rs.getRow();
                	 rs.beforeFirst();
                	 }
                 long rowtime = 0;
                 
                 //Niben hämä
                 kentat.add("Divisors"); //1
                 kentat.add("");
                 kentat.add("0");
                 kentat.add("0");
                 kentat.add("0");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 //10
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 //20
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 //30
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("10");
                 kentat.add("1");
                 kentat.add("1");
                 kentat.add("1");
                 
                 
                 String[] tietue = new String[kentat.size()];
                 tietue = kentat.toArray(tietue);
                 kaikkidbrivit.add(tietue);
                 kentat.clear();

                 //otsikkorivi
                 kentat.add("Date");
                 kentat.add("Time");
                 kentat.add("version");
                 kentat.add("BT1");
                 kentat.add("BT2");
                 kentat.add("BT3");
                 kentat.add("BT6");
                 kentat.add("BT7");
                 kentat.add("BT10");
                 kentat.add("BT11");
                 //10
                 kentat.add("EB100-EP14-BT12");
                 kentat.add("EB100-EP14-BT14");
                 kentat.add("EB100-EP14-BT17");
                 kentat.add("EB100-EP15-BT3");
                 kentat.add("EB100-EP15-BT10");
                 kentat.add("EB100-EP15-BT11");
                 kentat.add("EB100-EP15-BT12");
                 kentat.add("EB100-EP15-BT14");
                 kentat.add("EB100-EP15-BT17");
                 kentat.add("BT25");
                 //20
                 kentat.add("BT50");
                 kentat.add("EB100-EP14-BP8");
                 kentat.add("EB100-EP15-BP8");
                 kentat.add("Tot.Int.Add");
                 kentat.add("EP14-BF1"); 
                 kentat.add("Alarm");
                 kentat.add("Calc.Supply");
                 kentat.add("Degree Minutes");
                 kentat.add("BT1 Avg"); // virheellisesti?
                 kentat.add("EB100-EP14-PCA");
                 //30
                 kentat.add("Prio");
                 kentat.add("EB100-EP15 Prio");
                 kentat.add("EB100-EP14 Prio");
                 kentat.add("compr. freq. act.");
                 kentat.add("GP1-speed EP14");
                 kentat.add("GP2-speed EP14");
                 //Tee joskus F1345 GP1-2 talologgerissa?
                 
                 tietue = new String[kentat.size()];
                 tietue = kentat.toArray(tietue);
                 kaikkidbrivit.add(tietue);
                 int kenttacheck = kentat.size();
                 kentat.clear();
    			 
                 while (rs.next()) {
                     //ohjain.kirjoitaKonsolille(rs.getString(1) + "\n");
                	 //koska kaikki arvot samassa sarakkeessa, pitää hakea samat kellonajat
                	 //ohjain.kirjoitaKonsolille(" "+rs.getTimestamp("time").getTime() + "\n");
                	 if (rs.getTimestamp("time").getTime() > rowtime) {
                		 //ohjain.kirjoitaKonsolille("rivi " + row + "\n");
                		 //ohjain.kirjoitaKonsolille("kenttämäppi koko: " + kenttamappi.size() + " kenttacheck: " + (kenttacheck-7) + "\n");
                		 //tk yhteensopivuus -6 tai -7
                		 if (rowtime > 0 && (kenttamappi.size() == (kenttacheck-7) || kenttamappi.size() == (kenttacheck-6))) {
                    		 //alustetaan F1x55 arvot jos kyseessä on muu kone. Ei tarvitse löytyä Talologger kannasta.
                     		 if (!kenttamappi.containsKey("compr. freq. act.")) {
                     			 //ohjain.kirjoitaKonsolille("ComprFreq: " + kenttamappi.toString() + "\n");
                     			 ohjain.setCfa_fake(true);
                     			 kenttamappi.put("compr. freq. act.", "500");
                     		 } 
                			 //uusi rivi muttei eka
                			 kentat=teeKentat(kentat, kenttamappi);
                			 tietue = new String[kentat.size()];
                			 tietue = kentat.toArray(tietue);
                			 kaikkidbrivit.add(tietue);
                			 kentat.clear();
                			 kenttamappi.clear();
                		 }
                		 rowtime = rs.getTimestamp("time").getTime();
                		 //ohjain.kirjoitaKonsolille(sdft.format(rs.getTimestamp("time").getTime()) + " uusi tietue alkaa\n");
                		 kenttamappi.put("date", sdfd.format(rs.getTimestamp("time").getTime()));
                		 kenttamappi.put("time", sdft.format(rs.getTimestamp("time").getTime()));
                	 }

                	 if (rs.getTimestamp("time").getTime() == rowtime || rowtime == 0) {
                		 //ohjain.kirjoitaKonsolille("kenttämappi start\n");
                		 //ohjain.kirjoitaKonsolille(rs.getString(2) + ": " + rs.getString(3) + "\n");
                		 if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_BT1","BT1")) && rs.getString(3) != null) {
                			 kenttamappi.put("BT1", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_BT2","BT2")) && rs.getString(3) != null) {
                			 kenttamappi.put("BT2", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                			 //tarkistetaan onko BT2 tyhjä vaikka olemassa
                			 if (ohjain.isBt2_nolla()) {
                				 //kerrotaan että BT2 on talologgerissa
                				 ohjain.setBt2_nolla(false);
                			 }
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_BT3","EB100-EP14-BT3")) && rs.getString(3) != null) {
                			 kenttamappi.put("BT3", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_BT6","BT6")) && rs.getString(3) != null) {
                			 kenttamappi.put("BT6", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_BT7","BT7")) && rs.getString(3) != null) {
                			 kenttamappi.put("BT7", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_BT10","EB100-EP14-BT10")) && rs.getString(3) != null) {
                			 kenttamappi.put("BT10", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_BT11","EB100-EP14-BT11")) && rs.getString(3) != null) {
                			 kenttamappi.put("BT11", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_BT12","EB100-EP14-BT12")) && rs.getString(3) != null) {
                			 kenttamappi.put("EB100-EP14-BT12", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_BT14","EB100-EP14-BT14")) && rs.getString(3) != null) {
                			 kenttamappi.put("EB100-EP14-BT14", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                			 kenttamappi.put("EB100-EP14-BT17", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_BT17","EB100-EP14-BT17")) && rs.getString(3) != null) {
                			 kenttamappi.put("EB100-EP14-BT17", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		//ep15
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_EP15-BT3","EB100-EP15-BT3")) && rs.getString(3) != null) {
                			 kenttamappi.put("EB100-EP15-BT3", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_EP15-BT10","EB100-EP15-BT10")) && rs.getString(3) != null) {
                			 kenttamappi.put("EB100-EP15-BT10", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_EP15-BT11","EB100-EP15-BT11")) && rs.getString(3) != null) {
                			 kenttamappi.put("EB100-EP15-BT11", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_EP15-BT12","EB100-EP15-BT12")) && rs.getString(3) != null) {
                			 kenttamappi.put("EB100-EP15-BT12", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_EP15-BT14","EB100-EP15-BT14")) && rs.getString(3) != null) {
                			 kenttamappi.put("EB100-EP15-BT14", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                			 kenttamappi.put("EB100-EP15-BT17", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_EP15-BT17","EB100-EP15-BT17")) && rs.getString(3) != null) {
                			 kenttamappi.put("EB100-EP15-BT17", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_BT25","BT25")) && rs.getString(3) != null) {
                			 if (rs.getString(3).equalsIgnoreCase("-3276.8")) {
                				 kenttamappi.put("BT25", "0");
                			 } else {
                				 kenttamappi.put("BT25", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                			 }
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_BT50","BT50")) && rs.getString(3) != null) {
                			 kenttamappi.put("BT50", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_CS","CalcSupply")) && rs.getString(3) != null) {
                			 kenttamappi.put("Calc.Supply", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_DM","DegreeMinutes")) && rs.getString(3) != null) {
                			 kenttamappi.put("Degree Minutes", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_EP14-Prio","EP100-EP14-Prio")) && rs.getString(3) != null) {
                			 //noble fix
                			 String paluu = rs.getString(3);
                	            //Prio: 10=Off 20=Hot Water 30=Heat 40=Pool 41=Pool 2 50=Transfer 60=Cooling
                			 if (Integer.parseInt(paluu) == 10) {
                				 paluu="0";
                			 } else if (Integer.parseInt(paluu) == 20) {
                				 paluu="2";
                			 } else if (Integer.parseInt(paluu) == 30 || Integer.parseInt(paluu) == 40 || Integer.parseInt(paluu) == 41) {
                				 paluu="1";
                			 }
                			 kenttamappi.put("EB100-EP14 Prio", paluu);
                			 //yhteensopivuus kantoihin ilman PCAta ja TotIndAdd
                			 kenttamappi.put("EB100-EP14-PCA", ""+pca[Integer.parseInt(paluu)]);
                			 //ohjain.kirjoitaKonsolille("tk2 PCA: " + kenttamappi.get("EB100-EP14-PCA") + "\n");
                			 kenttamappi.put("TotIntAdd", "0");
                			 //ohjain.kirjoitaKonsolille("Prio: " + Integer.parseInt(rs.getString(3)) + " PCA: " + pca[Integer.parseInt(rs.getString(3))] + ".\n");
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_EP15-Prio","EP100-EP15-Prio")) && rs.getString(3) != null) {
                			 kenttamappi.put("EB100-EP15 Prio", rs.getString(3));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_TotIntAdd","TotIntAdd")) && rs.getString(3) != null) {
                			 kenttamappi.put("TotIntAdd", ""+(int)(Double.parseDouble(rs.getString(3))*100));
                			 //ohjain.kirjoitaKonsolille("TIA: " + ""+(int)(Double.parseDouble(rs.getString(3))*100) + " ");
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_EP14-BF1","EP14-BF1")) && rs.getString(3) != null) {
                			 kenttamappi.put("EP14-BF1", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_EB100-EP14-PCA","EB100-EP14-PCA")) && rs.getString(3) != null) {
                			 kenttamappi.put("EB100-EP14-PCA", rs.getString(3));
                			 //ohjain.kirjoitaKonsolille("PCA: " + Integer.parseInt(rs.getString(3)) + "\n");
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_EP14-Frequency","ComprFreq")) && rs.getString(3) != null) {
                			 kenttamappi.put("compr. freq. act.", ""+(int)(Double.parseDouble(rs.getString(3))*10));
                			 //ohjain.kirjoitaKonsolille("ComprFreq: " + (int)(Double.parseDouble(rs.getString(3))*10) + "\n");
                		 //GPt
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_EP14-GP1","EB100-EP14-GP1")) && rs.getString(3) != null) {
                			 kenttamappi.put("EB100-EP14-GP1", ""+(int)(Double.parseDouble(rs.getString(3))));
                		 } else if (rs.getString(2).equalsIgnoreCase(asetukset.getProperty("tietokanta_arvo_EP14-GP2","EB100-EP14-GP2")) && rs.getString(3) != null) {
                			 kenttamappi.put("EB100-EP14-GP2", ""+(int)(Double.parseDouble(rs.getString(3))));
                		 } 
                		 
                		 
                		 //F1140 fix
                		 if (!kenttamappi.containsKey("EB100-EP14 Prio")) {
                			 kenttamappi.put("EB100-EP14 Prio", "0");
                		 }
                		 if (!kenttamappi.containsKey("BT25")) {
                			 kenttamappi.put("BT25", "0");
                		 }
                		 if (!kenttamappi.containsKey("TotIntAdd")) {
                			 kenttamappi.put("TotIntAdd", "0");
                		 }
                		 if (!kenttamappi.containsKey("BT7")) {
                			 kenttamappi.put("BT7", "0");
                		 } 
                         //alustetaan F1345 arvot jos kyseessä on F11/245 kone. Ei tarvitse löytyä Talologger kannasta
                		 if (!kenttamappi.containsKey("EB100-EP15-BT3")) {
                             kenttamappi.put("EB100-EP15-BT3", "32768");
                		 }
                		 if (!kenttamappi.containsKey("EB100-EP15-BT10")) {
                			 kenttamappi.put("EB100-EP15-BT10", "0");
                		 }
                		 if (!kenttamappi.containsKey("EB100-EP15-BT11")) {
                			 kenttamappi.put("EB100-EP15-BT11", "0");
                		 }
                		 if (!kenttamappi.containsKey("EB100-EP15-BT12")) {
                			 kenttamappi.put("EB100-EP15-BT12", "0");
                		 }
                		 if (!kenttamappi.containsKey("EB100-EP15-BT14")) {
                			 kenttamappi.put("EB100-EP15-BT14", "0");
                		 }
                		 if (!kenttamappi.containsKey("EB100-EP15-BT17")) {
                			 kenttamappi.put("EB100-EP15-BT17", "0");
                		 }
                		 if (!kenttamappi.containsKey("EB100-EP15 Prio")) {
                			 kenttamappi.put("EB100-EP15 Prio", "0");
                		 }
                		 if (!kenttamappi.containsKey("BT2")) {
                			 kenttamappi.put("BT2", "0");
                		 }
                		 if (!kenttamappi.containsKey("EB100-EP14-GP1")) {
                			 kenttamappi.put("EB100-EP14-GP1", "0");
                		 }
                		 if (!kenttamappi.containsKey("EB100-EP14-GP2")) {
                			 kenttamappi.put("EB100-EP14-GP2", "0");
                		 }
                		 if (!kenttamappi.containsKey("EP14-BF1")) {
                			 kenttamappi.put("EP14-BF1", "0");
                		 }
                	 }
                     //ohjain.kirjoitaKonsolille("kenttämappi end\n");
                	 //päivitetään progress bar per luettu tietue
                	 row++;
                	 setProgress((row)*100/rowcount);
                 }


        		 //alustetaan F1x55 arvot jos kyseessä on muu kone. Ei tarvitse löytyä Talologger kannasta.
         		 if (!kenttamappi.containsKey("compr. freq. act.")) {
         			 //ohjain.kirjoitaKonsolille("ComprFreq: " + kenttamappi.toString() + "\n");
         			 ohjain.setCfa_fake(true);
         			 kenttamappi.put("compr. freq. act.", "500");
         		 }
                 
                 //viimeinen rivi kyselystä, jota ei muuten kirjoitettaisi taulukkoon
    			 //uusi rivi muttei eka
    			 kentat=teeKentat(kentat, kenttamappi);
    			 tietue = new String[kentat.size()];
    			 tietue = kentat.toArray(tietue);
    			 kaikkidbrivit.add(tietue);
                 
                 //laitetaan kannasta haetut tavarat yhteen taulukkoon joka sisältää kaikki haun rivit
                 if (kaikkidbrivit.size()>2){
                	 kaikkilogit.add(kaikkidbrivit);
                 } else {
                	 ohjain.kirjoitaKonsolille("Ei logitietueita - tarkista hakuaika. ");
                	 setProgress(100);
                 }
                 ohjain.kirjoitaKonsolille("Tietokantakysely suoritettu.\n");
             } catch (SQLException ex) {
            	 ohjain.kirjoitaKonsolille("Virhe: " + ex.getMessage() + "\n");
			} finally {
                 try {
                     if (rs != null) {
                         rs.close();
                     }
                     if (st != null) {
                         st.close();
                     }
                     if (con != null) {
                         con.close();
                     }

                 } catch (SQLException ex) {
                     ohjain.kirjoitaKonsolille("Virhe: " + ex.getMessage() + "\n");
                     return null;
                 }
             }
        	 return kaikkilogit;
	    }

	    // teeKentat
		// luo kannasta haettujen rivien pohjalta tietueen
	    //
		public ArrayList<String> teeKentat(ArrayList<String> kentat, Map<String, String> kenttamappi){
			 //oikea järjestys kentille
			 kentat.add(kenttamappi.get("date"));
			 //ohjain.kirjoitaKonsolille("km: " + kenttamappi.get("date")+".\n");
			 kentat.add(kenttamappi.get("time"));
			 //ohjain.kirjoitaKonsolille("km: " + kenttamappi.get("time")+".\n");
			 //versio
			 kentat.add("0000");
			 kentat.add(kenttamappi.get("BT1"));
			 //ohjain.kirjoitaKonsolille(kenttamappi.get("BT1")+"\n");
			 kentat.add(kenttamappi.get("BT2")); 
			 kentat.add(kenttamappi.get("BT3"));
			 kentat.add(kenttamappi.get("BT6"));
			 kentat.add(kenttamappi.get("BT7"));
			 kentat.add(kenttamappi.get("BT10"));
			 kentat.add(kenttamappi.get("BT11"));
			 kentat.add(kenttamappi.get("EB100-EP14-BT12"));
			 //10
			 kentat.add(kenttamappi.get("EB100-EP14-BT14"));
			 kentat.add(kenttamappi.get("EB100-EP14-BT17"));
			 kentat.add(kenttamappi.get("EB100-EP15-BT3"));
			 kentat.add(kenttamappi.get("EB100-EP15-BT10"));
			 kentat.add(kenttamappi.get("EB100-EP15-BT11"));
			 kentat.add(kenttamappi.get("EB100-EP15-BT12"));
			 //16
			 kentat.add(kenttamappi.get("EB100-EP15-BT14"));
			 kentat.add(kenttamappi.get("EB100-EP15-BT17")); 
			 kentat.add(kenttamappi.get("BT25"));
			 //tk yhteensopivuus
			 if (kenttamappi.get("BT50") == null) {
				 kentat.add("0");
			 } else {
				 kentat.add(kenttamappi.get("BT50"));
			 }
			 //20
			 //EP14BP8
			 kentat.add("0");
			//EP15BP8
			 kentat.add("0");
			 //addstep
			 //kentat.add("0");
			 kentat.add(kenttamappi.get("TotIntAdd"));
			 kentat.add(kenttamappi.get("EP14-BF1"));
			 //alarm
			 kentat.add("0");
			 //24
			 kentat.add(kenttamappi.get("Calc.Supply"));
			 kentat.add(kenttamappi.get("Degree Minutes"));
			 kentat.add(kenttamappi.get("BT1")); //BT1 Avg
			 kentat.add(kenttamappi.get("compr. freq. act."));
			 kentat.add(kenttamappi.get("EB100-EP14-PCA"));
			 //28
			 //GPt
			 kentat.add(kenttamappi.get("EB100-EP14-GP1"));
			 kentat.add(kenttamappi.get("EB100-EP14-GP2"));
			 //prio
			 kentat.add("0");
			 kentat.add(kenttamappi.get("EB100-EP15 Prio"));
			 kentat.add(kenttamappi.get("EB100-EP14 Prio"));
			 return kentat;
		}
	   
	    // rekisteröiOhjain
		// mahdollistetaan ohjaimen näkyvyys Mallille
	    //
		public void rekisteröiOhjain(LogintutkijaOhjain ohjain){
			this.ohjain=ohjain;
		}
		
		// XML harjoituksia NordPoolia varten
		//
		static class Hanskaaja extends DefaultHandler
	    {
	        boolean newItem = false;
	        String title = null;
	         
	        @Override
			public void startElement(String uri, String localName, 
	          String qName, Attributes attributes) throws SAXException
	        {
	            // System.out.println("Start Element :" + qName);
	            if (qName.equals("item"))
	                newItem = true;
	            else if (qName.equals("title") && newItem)
	                title = "";
	        }
	 
	        @Override
			public void endElement(String uri, String localName, 
	            String qName) throws SAXException
	        {
	            // System.out.println("End Element :" + qName);
	            if (qName.equals("title") && newItem)
	            {
	                System.out.println(title);
	                title = null;
	            }
	        }
	         
	        @Override
			public void characters(char ch[], int start, int length)
	                throws SAXException
	        {
	            if (title != null)
	                title += new String(ch, start, length);
	        }
	    }
}
