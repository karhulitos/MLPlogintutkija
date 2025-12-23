package logintutkija;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import au.com.bytecode.opencsv.CSVReader;

/************************************
 * MVC CONTROLLER: LogintutkijaOhjain
 ************************************/

public class LogintutkijaOhjain {
    /******************
     * LUOKKAMUUTTUJAT
     ******************/
	private static Saie saie;
	
	
	//analysoi
	@SuppressWarnings("unused")
	//graafintausta ja ristikko
	Color graafinTaustanVari = Color.YELLOW;
	Color graafinAsteikonVari = Color.BLUE;
	private static double la, kv, le, sa, sav, sal, ua, la_flm, kv_flm;
	private static int f1255_cfa_found = 0;
	private static int samatajat = 1;
	private static int max_samatajat = 1;
	int bt2_found = 0;
	boolean bt2_nolla = true;
	private ArrayList<ArrayList<Integer>> kayra_taulukko = new ArrayList<ArrayList<Integer>>();
	private ArrayList<String> kayra_taulukko_nimet = new ArrayList<String>();
	private ArrayList<GregorianCalendar> logiaika  = new ArrayList<GregorianCalendar>();
	GregorianCalendar edellinen_logiaika  = new GregorianCalendar();
	private static long mittausvali_ms = 0;
	private static int kokaika = 0;
	private static int vuosi, kuukausi, paiva, tunti, minuutti, sekunti;
	private static int kaytto_kv = 0;
	private static int kaytto_la = 0;
	private static int kaytto_le = 0;
	private static int kaytto_ua = 0;
	private static int kaytto_la_flm = 0;
	private static int kaytto_kv_flm = 0;
	private static String lokiVersio = "0000";
	private static String lokiRVersio = "0";
	private static String oletushakemisto = "";
	//hakutekijät muuttujissa
	private String kaynti_haku = "Relays PCA-Base";
	private String tia_haku = "Tot.Int.Add";
	private String pkt_haku = "Pyydetty komp.taajuus";
	private int tia_kerroin = 1;
	private String bt1_haku = "40004";
	private String bt2_haku = "40008";
	private String version_haku = "43001";
	boolean version_strict = false;
	boolean bt1_strict = false;
	boolean bt2_strict = false;
	boolean bt3_strict = false;
	boolean bt7_strict = false;
	private String bt3_haku = "40012";
	private String bt6_haku = "40014";
	private String bt7_haku = "40013";
	private String bt10_haku = "EP14-BT10";
	private String bt11_haku = "EP14-BT11";
	private String bt12_haku = "EP14-BT12";
	private String bt14_haku = "EP14-BT14";
	private String bt17_haku = "EP14-BT17";
	private String keruu_sisaan_haku = bt10_haku;
	private String keruu_ulos_haku = bt11_haku;
	private String ep14_gp1_haku = "GP1-speed EP14";
	private String ep14_gp2_haku = "GP2-speed EP14";
	private String cs_haku = "Calc. Supply";
	private String dm_haku = "Degree Minutes";
	private String be1_haku = "BE1";
	private String be2_haku = "BE2";
	private String be3_haku = "BE3";
	private String alarm_haku = "larm";
	//käyriä varten
	private ArrayList<Integer> bt2 = new ArrayList<Integer>();
	//autoupdatea varten
	private boolean running = false;
	//EP14 - 1. kompressori
	private ArrayList<Integer> bt3 = new ArrayList<Integer>();
	private ArrayList<Integer> eb101_bt3 = new ArrayList<Integer>();
	private ArrayList<Integer> bt10 = new ArrayList<Integer>();
	private ArrayList<Integer> bt11 = new ArrayList<Integer>();
	private ArrayList<Integer> bt12 = new ArrayList<Integer>();
	private ArrayList<Integer> bt14 = new ArrayList<Integer>();
	private ArrayList<Integer> bt16 = new ArrayList<Integer>();
	private ArrayList<Integer> bt17 = new ArrayList<Integer>();
	private ArrayList<Integer> cop = new ArrayList<Integer>();
	private ArrayList<Integer> teho = new ArrayList<Integer>();
	private ArrayList<Integer> cop_la = new ArrayList<Integer>();
	private ArrayList<Integer> cop_kv = new ArrayList<Integer>();
	private ArrayList<Integer> relaysPCAbase = new ArrayList<Integer>();
	//private ArrayList<Integer> ep15_relaysPCAbase = new ArrayList<Integer>();
	private ArrayList<Integer> bf1 = new ArrayList<Integer>();
	private int lastcop=0;
	//EP15 - 2. kompressori
	private ArrayList<Integer> ep15_bt3 = new ArrayList<Integer>();
	private ArrayList<Integer> ep15_bt10 = new ArrayList<Integer>();
	private ArrayList<Integer> ep15_bt11 = new ArrayList<Integer>();
	private ArrayList<Integer> ep15_bt12 = new ArrayList<Integer>();
	private ArrayList<Integer> ep15_bt14 = new ArrayList<Integer>();
	private ArrayList<Integer> ep15_bt17 = new ArrayList<Integer>();
	private ArrayList<Integer> ep15_cop = new ArrayList<Integer>();
	private ArrayList<Integer> ep15_cop_la = new ArrayList<Integer>();
	private ArrayList<Integer> ep15_cop_kv = new ArrayList<Integer>();
	private int ep15_lastcop=0;
	private ArrayList<Integer> ep15_prio = new ArrayList<Integer>();
	private ArrayList<Integer> bt25 = new ArrayList<Integer>();
	private ArrayList<Integer> bt28 = new ArrayList<Integer>();
	private ArrayList<Integer> dm = new ArrayList<Integer>();
	private ArrayList<Integer> cfa = new ArrayList<Integer>();
	private static int ep15_kaytto_kv = 0;
	private static int ep15_kaytto_la = 0;
	private static int ep15_kaytto_le = 0;
	//maaviinan alin ja ylin
	private static int bt10_min = 500;
	private static int bt10_max = -500;
	private static int bt11_min = 500;
	private static int bt11_max = -500;
	private static int ep15_bt10_min = 500;
	private static int ep15_bt10_max = -500;
	private static int ep15_bt11_min = 500;
	private static int ep15_bt11_max = -500; 
	//keskiarvojen laskentaan
	private static ArrayList<Integer> kw_sahko = new ArrayList<Integer>();
	private static ArrayList<Integer> add_step = new ArrayList<Integer>();
	private static ArrayList<Integer> kv_sahko = new ArrayList<Integer>();
	private static ArrayList<Integer> lamm_sahko = new ArrayList<Integer>();
	private static ArrayList<Integer> lampo_kv = new ArrayList<Integer>();
	private static ArrayList<Integer> bt6 = new ArrayList<Integer>();
	private static ArrayList<Integer> lampo_delta_la = new ArrayList<Integer>();
	private static ArrayList<Integer> lampo_delta_kv = new ArrayList<Integer>();
	private static ArrayList<Integer> lampo_delta_keruu = new ArrayList<Integer>();
	private static ArrayList<Integer> ep15_lampo_delta_la = new ArrayList<Integer>();
	private static ArrayList<Integer> ep15_lampo_delta_kv = new ArrayList<Integer>();
	private static ArrayList<Integer> ep15_lampo_delta_keruu = new ArrayList<Integer>();
	private static ArrayList<Integer> bt50 = new ArrayList<Integer>();
	//2016-11-06 GP1 ja GP2 tulleet lisää oletuslogiin
	private static ArrayList<Integer> gp1 = new ArrayList<Integer>();
	private static ArrayList<Integer> gp2 = new ArrayList<Integer>();
	//2017-01-16 EP15 GP1 tullut lisää oletuslogiin
	private static ArrayList<Integer> ep15_gp1 = new ArrayList<Integer>();
	//2019-02-17 EP15 GP2 tullut lisää oletuslogiin
	private static ArrayList<Integer> ep15_gp2 = new ArrayList<Integer>();
	//2018-03-24 BT51, 53 ja 54 lisälaittesta mukaan oletuslokiin
	private static ArrayList<Integer> bt51 = new ArrayList<Integer>();
	private static ArrayList<Integer> bt53 = new ArrayList<Integer>();
	private static ArrayList<Integer> bt54 = new ArrayList<Integer>();
	private static ArrayList<Integer> bt63 = new ArrayList<Integer>();
	private static ArrayList<Integer> prio = new ArrayList<Integer>();
	private static int kompr_kaynnistyksia = 0;
	private static int ep15_kompr_kaynnistyksia = 0;
	private static ArrayList<Integer> bt1 = new ArrayList<Integer>();
	private static ArrayList<Integer> cs = new ArrayList<Integer>();
	private double cop_035 = 0.0;
	private double cop_045 = 0.0;
	private static String bt67_label = "";
	private boolean paikallinen_tietokanta_tallennus = true;
	private boolean laske_kaynnistys_jos_kaynnissa=true;
	private boolean cfa_fake = false;
	private int F1345LisaysAskel = 0;
	private int tietueen_pituus =  0;
	private LogintutkijaMalli malli;
	private LogintutkijaGUI ikkuna;
	private static ArrayList<Integer> bt20 = new ArrayList<Integer>();
	private static ArrayList<Integer> bt21 = new ArrayList<Integer>();
	private static ArrayList<Integer> bt71 = new ArrayList<Integer>();
	private boolean hdd = false;
	private String ctcmappi = "GSi12,0,68,1,7,10,11,14,16,17,18,19,22,50,51,53,55,56,57,58";
	private String nibecontroller = "F";
	private int kerroin = 1;
	private int gpkerroin = 1;
	private String bf1_haku = "EP14-BF1";
	private String cfa_haku = "compr. freq. act";
	private String bt50_haku = "BT50";
	private boolean iIsStopped = false;
	//virtamuuntajat
	private static ArrayList<Integer> be1 = new ArrayList<Integer>();
	private static ArrayList<Integer> be2 = new ArrayList<Integer>();
	private static ArrayList<Integer> be3 = new ArrayList<Integer>();
	//hälytykset
	private static ArrayList<Integer> alarm = new ArrayList<Integer>();
	
	
	public LogintutkijaOhjain(final LogintutkijaMalli malli, final LogintutkijaGUI ikkuna) {
		this.malli = malli;
		this.ikkuna = ikkuna;
		malli.addPropertyChangeListener(new PropertyChangeListener() {
	         @Override
			public void propertyChange(PropertyChangeEvent pce) {
	            if (LogintutkijaMalli.PROGRESS.equals(pce.getPropertyName())) {
	               ikkuna.setProgress((Integer)pce.getNewValue());
	            }
	         }
	      });
	}
	
	
    /***********************************************
     * Säie, joka ajetaan taustalla.
     * Täällä haetaan data ja lasketaan yhteenvedot.
     ***********************************************/
	class Saie extends SwingWorker<Void, Void> {
        @Override
		public Void doInBackground() {
        	ArrayList<String> logit = null;
            //alustetaan edistymisarvo.
            setProgress(0);
            //alustetaan logimuuttuja
            ArrayList<String[][]> kaikkilogit = new ArrayList<String[][]>();
            //BT2 Zero pitää nollata ennen mitään uutta hakua
            bt2_nolla=true;
            cfa_fake=false;
            
            //logifileet
            if (ikkuna.getTietolahde() == 0) {
            	//bt2 nolla pitää asettaa falseksi kun haetaan logitiedostoja
				bt2_nolla=false;
				cfa_fake=false;
		        try {
		        	logit=ikkuna.avaa(); //luetaan logifileet hakemistosta
		        	ikkuna.nollaaNumerot();
					if(logit.size() == 0){
						ikkuna.kirjoitaKonsolille("Tarkista hakemistovalintasi.\n");
						ikkuna.getLblMLPMalli().setText("MLP");
						ikkuna.naytaGraafi(0, 0, 0, 0, 0, 0, 0, 100);
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

		        char separator = ';';
	            for (int i=0;i<logit.size();i++){
	            	try {
	            		//luetaan kaikki tiedostot taulukkoon joka sisältää taulukon jossa yhden tiedoston rivit
						//pitää testata mikä pumppu kyseessä
						if (LogintutkijaGUI.getValittu_Tiedosto_Filtteri().equals("ctc")) {
							kaikkilogit.add(tabulaattoriJakaja(logit.get(i),',',LogintutkijaGUI.getValittu_Tiedosto_Filtteri()));
						} else if (LogintutkijaGUI.getValittu_Tiedosto_Filtteri().equals("nibes")) {
							if (i==0) {
								BufferedReader br = new BufferedReader(new FileReader(new File(logit.get(i))));
								String ekarivi =  br.readLine();
								if (ekarivi.contains("\t")) {
									separator='\t';
								}
								br.close();
							}
							kaikkilogit.add(tabulaattoriJakaja(logit.get(i),separator,LogintutkijaGUI.getValittu_Tiedosto_Filtteri()));
						} else {
							//ikkuna.kirjoitaKonsolille("Logit size: " + logit.size());
							kaikkilogit.add(tabulaattoriJakaja(logit.get(i),'\t',LogintutkijaGUI.getValittu_Tiedosto_Filtteri()));
						}
						paikallinen_tietokanta_tallennus=true;
					} catch (IOException e) {
						e.printStackTrace();
					}
	            	//päivitetään edistysPötkö per luettu tiedosto
	            	ikkuna.setProgress((i+1)*100/logit.size());
	            }
            } else if (ikkuna.getTietolahde()  == 1) {
        		running=true;
            	ikkuna.kirjoitaKonsolille("Haetaan logit tietokannasta..");
            	//ikkuna.setProgress(1);
            	ikkuna.nollaaNumerot();
            	//metodi mallin puolelle
            	if (ikkuna.getTietokanta_dbms().equals("sqlite")) {
            		paikallinen_tietokanta_tallennus=false;
            		String[][] sqllitedata = malli.haeSQLiteKannasta(ikkuna.getTietokanta_pvm_mista(), ikkuna.getTietokanta_pvm_mihin());
            		if (sqllitedata !=  null) {
            			kaikkilogit.add(sqllitedata);
            		}
            	} else {
            		paikallinen_tietokanta_tallennus=false;      		
            		String[][] sqldata = malli.haeMySQLKannasta(ikkuna.getTietokanta_osoite(), ikkuna.getTietokanta_nimi(), ikkuna.getTietokanta_kayttaja(), ikkuna.getTietokanta_salasana(), ikkuna.getTietokanta_pvm_mista(), ikkuna.getTietokanta_pvm_mihin());
            		if (sqldata !=  null) {
            			kaikkilogit.add(sqldata);
            		}
            	}
            }

            if(kaikkilogit.size()>0){
            	ikkuna.kirjoitaKonsolille("Logiaineisto luettu.\n");
            	//analysoidaan
	            ikkuna.kirjoitaKonsolille(analysoi(kaikkilogit));
	            ikkuna.getRadioNappulaEP14().setSelected(true);
	            ikkuna.getRadioNappulaEP15().setSelected(false);
	            ikkuna.getLuoKayrat().setEnabled(true);
            	//tallennetaan SQLite kantaan myöhempää tarkastelua varten
            	malli.tallennaSQLiteKantaan();
            } else {
        		ikkuna.nollaaNumerot();
            }
            return null; //Tausta-ajo (doInBackground) päättyy
        }
        /************************************
         * TAUSTASÄIKEEN METODIT
         ************************************/
        // TAUSTASÄIE: done
        // Done ajetaan kun taustasäie lopettaa työnsä eli ilmoittaa tapahtumankäsittelijälle saapuneensa maaliin
        //
        @Override
		public void done() {
        	ikkuna.getLuoKayrat().setSelected(true);
        	//kursorin kellotus lopetetaan
        	ikkuna.setCursor(null);

        	ikkuna.naytaGraafi(ikkuna.getLa(), ikkuna.getSal(), ikkuna.getUal(), ikkuna.getLaFlm(), ikkuna.getKvFlm(), ikkuna.getKv(), ikkuna.getSav(), ikkuna.getLe());
        	running=false;
        }
        
        // analysoi
    	// Analysoi logirivit tietolähteestä
        //
        String analysoi(ArrayList<String[][]> tiedostot) {
        	if (tiedostot == null){
        		kv = 0;
        		sa = 0;
        		la = 0;
        		ua = 0;
        		la_flm = 0;
        		kv_flm = 0;
        		le = 100;
        		return "Ei yhtään tietuetta analysoitavaksi.\n";
        	}
        	GregorianCalendar edellinenriviaika = new GregorianCalendar();
        	edellinenriviaika.setTimeInMillis(0);
        	//divisoreja ei sitten käytettykään, pidetään jemmassa tulevaisuutta varten 
        	//int div_bt1, div_bt2, div_bt3, div_bt6, div_bt7, div_bt10, div_bt11, div_bt12, div_bt14, div_bt17, div_bt25, div_bt50, div_add, div_supply, div_degmin, div_bt1, div_avg = 1;
        	//kaytto
        	//nollataan taulukkoja jos analysoidaan uudelleen
        	samatajat=1;
        	max_samatajat=1;
        	logiaika.clear();
        	edellinen_logiaika = new GregorianCalendar(1970, 0, 1, 0, 0, 0);
        	kayra_taulukko.clear();
        	kayra_taulukko_nimet.clear();
        	mittausvali_ms = 0;
        	kokaika = 0;
        	vuosi = kuukausi = paiva = tunti = minuutti = sekunti = 0;
        	kaytto_kv = 0;
        	kaytto_la = 0;
        	kaytto_le = 0;
        	kaytto_ua = 0;
        	kaytto_kv_flm = 0;
        	kaytto_la_flm = 0;
        	ep15_kaytto_kv = 0;
        	ep15_kaytto_la = 0;
        	ep15_kaytto_le = 0;
        	cs.clear();
        	dm.clear();
        	bt1.clear();
        	bt2.clear();
        	bt3.clear();
        	bt10.clear();
        	bt11.clear();
        	bt12.clear();
        	bt14.clear();
        	bt17.clear();
        	bf1.clear();
        	relaysPCAbase.clear();
        	ep15_bt3.clear();
        	ep15_bt10.clear();
        	ep15_bt11.clear();
        	ep15_bt12.clear();
        	ep15_bt14.clear();
        	ep15_bt17.clear();
        	ep15_prio.clear();
        	bt25.clear();
        	bt50.clear();
        	kompr_kaynnistyksia = 0;
        	ep15_kompr_kaynnistyksia = 0;
        	bt67_label = "";
        	kw_sahko.clear();
        	add_step.clear();
        	kv_sahko.clear();
        	lamm_sahko.clear();
        	lampo_kv.clear();
        	bt6.clear();
        	lampo_delta_la.clear();
        	lampo_delta_kv.clear();
        	lampo_delta_keruu.clear();
        	ep15_lampo_delta_la.clear();
        	ep15_lampo_delta_kv.clear();
        	ep15_lampo_delta_keruu.clear();
        	cfa.clear();
        	bt1.clear();
        	teho.clear();
        	cop.clear();
        	cop_la.clear();
        	cop_kv.clear();
        	ep15_cop.clear();
        	ep15_cop_la.clear();
        	ep15_cop_kv.clear();
        	lokiVersio = "0000";
        	//maaviinan alin ja ylin reset
        	bt10_min = 500;
        	bt10_max = -500;
        	bt11_min = 500;
        	bt11_max = -500;
        	ep15_bt10_min = 500;
        	ep15_bt10_max = -500;
        	ep15_bt11_min = 500;
        	ep15_bt11_max = -500;
        	f1255_cfa_found=0;
        	ikkuna.getLblMLPMalli().setText("MLP");
        	gp1.clear();
        	gp2.clear();
        	ep15_gp1.clear();
        	ep15_gp2.clear();
        	bt51.clear();
        	bt53.clear();
        	bt54.clear();
        	prio.clear();
        	tietueen_pituus =  0;
        	bt21.clear();
        	bt20.clear();
        	bt71.clear();
        	bt28.clear();
        	eb101_bt3.clear();
        	bt16.clear();
        	bt63.clear();
        	ikkuna.getLblLammonKeruu().setText(" ∆ lämmönkeruupiiri");
        	//hakutekijät muuttujissa
        	kaynti_haku = "Relays PCA-Base";
        	tia_haku = "Tot.Int.Add";
        	tia_kerroin = 1;
        	bt1_haku = "40004";
        	bt2_haku = "40008";
        	version_haku = "43001";
        	version_strict = false;
        	bt1_strict = false;
        	bt2_strict = false;
        	bt3_strict = false;
        	bt7_strict = false;
        	bt3_haku = "40012";
        	bt6_haku = "40014";
        	bt7_haku = "40013";
        	bt10_haku = "EP14-BT10";
        	bt11_haku = "EP14-BT11";
        	bt12_haku = "EP14-BT12";
        	bt14_haku = "EP14-BT14";
        	bt17_haku = "EP14-BT17";
        	ep14_gp1_haku = "GP1-speed EP14";
        	ep14_gp2_haku = "GP2-speed EP14";
        	keruu_sisaan_haku = bt10_haku;
        	keruu_ulos_haku = bt11_haku;
        	nibecontroller = "F";
        	kerroin = 1;
        	gpkerroin = 1;
        	cs_haku = "Calc. Supply";
        	dm_haku = "Degree Minutes";
        	bf1_haku = "EP14-BF1";
        	cfa_haku = "compr. freq. act";
        	bt50_haku = "BT50";
        	//virtamuuntajat
        	be1.clear();
        	be2.clear();
        	be3.clear();
        	//hälytykset
        	alarm.clear();
        	
        	boolean kompr_kaynnissa = false;
        	boolean ep15_kompr_kaynnissa = false;
        	int prosessinalku = 0;
        	int ep15_prosessinalku = 0;
        	bt2_found = 0;
        	boolean aloitusaika=false;
        	boolean muistiloppumassa = false;
        	long free2=50000000;
        	int kaikkirivit = 0;        	
        	ikkuna.getLblLammonKeruu().setText(" ∆ lämmönkeruupiiri");
        	iIsStopped = false;
        	
        	//kesäajan aiheuttaman käyrien taiteellisen piirron poisto
			TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
			
			//vilp hack
			if (ikkuna.getLblSuos5().isVisible() == false ) {
				ikkuna.getLblSuos5().setVisible(true);
			}
			
			//Nibe controller
			if (getValittu_Tiedosto_Filtteri().equals("nibes") && ikkuna.getTietolahde() == 0) {
				nibecontroller = "S";
				//otsikkorivin hämäys - asetetaan tyhjään pvm otsikkoon Date
				for (int i=0;i<tiedostot.size();i++) {
					tiedostot.get(i)[0][0] = "DateTime";
				}
			}
			
    		//String edellinentila = "0";
    		int edellinentila = 0;
    		String ep15_edellinentila = "0";
        	String mlpfound="Tietolähteestä ei löytynyt tuettujen pumppujen dataa.\n";
    		//lasketaan login arvoista avainarvoja
        	//yksi tiedosto = i
        	exit:
    		for (int i=0;i<tiedostot.size();i++) {
    			//tarkistetaan koko
    			if (tiedostot.get(0).length < 4) {
    				ikkuna.kirjoitaKonsolille("Liian lyhyt logi (" + tiedostot.get(0).length + ") - anna pitempi hakuaika.\n");
    				break;
    			}

    			//jos ensimmäisen rivin eka solu on "Divisors" niin puhutaan Nibe-lämpöpumpun logista
	            if(tiedostot.get(i)[0][0].equalsIgnoreCase("Divisors") ||
	            	tiedostot.get(i)[0][0].equalsIgnoreCase("DateTime") //Nibe S hämä
	            		){
            		mlpfound="Analysointi tehty";
            		
            		
//            		// Get the Java runtime
//            		Runtime runtime = Runtime.getRuntime();
//            		// Calculate the used memory
//            		long memory = runtime.totalMemory() - runtime.freeMemory();
//            		System.out.println("Used memory is bytes: " + memory);
//            		System.out.println("Used memory is megabytes: "
//            		+ bytesToMegabytes(memory));
//            		System.out.println("Free memory is megabytes: "
//                    		+ bytesToMegabytes(getFreeMemory()));

            		//CTC hämä
            		if(tiedostot.get(i)[1][1].equalsIgnoreCase("CTC")) {
            			ikkuna.getLblMLPMalli().setText("CTC");
            		} else {
                		ikkuna.getLblMLPMalli().setText(nibecontroller + "1x45");	
            		}
 
    				//kenttien hashmappi
    				Map<String, Integer> kentat = new HashMap<String, Integer>();
					//alkutarkistukset sarakeotsikoista
    				//nibe F vs S tarkistus
    				int headeroffset = 1;
    				if (nibecontroller.equals("S") || LogintutkijaGUI.getValittu_Tiedosto_Filtteri().equals("ctc")
    					) {
    					headeroffset = 0;
    				}
        			for (int k=0; k<tiedostot.get(i)[headeroffset].length;k++) {
        				kentat.put(tiedostot.get(i)[headeroffset][k], k);
        				//ikkuna.kirjoitaKonsolille("Kenttä " + k + ": " + tiedostot.get(i)[headeroffset][k] + "\n");
        			}
	        			


            		//F1145 login esimerkki
            		//Divisors		1	10	10	10	10	10	10	10	10	10	10	10	10	100	1	10	10	10	1
            		//Date	Time	version	BT1	BT2	BT3	BT6	BT7	BT10	BT11	BT12	BT14	BT17	BT25	BT50	Tot.Int.Add	Alarm number	Calc. Supply	Degree Minutes	BT1 Average	Relays PCA-Base
            		//0		1		2		3	4	5	6	7	8		9		10		11		12		13		14		15			16				17				18				19			20
            		//div			1	10	10	10	10	10	10		10		10		10		10		10		10		100			1				10				10				10			1
            		//label	time	v	BT1	BT2	BT3	BT6	BT7	BT10	BT11	BT12	BT14	BT17	BT25	BT50	Tot.Int.Add	Alarm number	Calc. Supply	Degree Minutes	BT1 Average	Relays PCA-Base
            		//rivi tiedostossa = j+1
        			rivi:
            		for (int j=0; j<tiedostot.get(i).length;j++) {
                		//tarkistetaan muistinkäyttö
        
            			
            			
            			//DEBUGGG
//            	        for (String variableName : kentat.keySet())
//            	        {
//            	            String variableKey = variableName;
//            	            int variableValue = kentat.get(variableName);
//                			//ikkuna.kirjoitaKonsolille(variableKey + "(" + variableValue + ");");
//                			ikkuna.kirjoitaKonsolille(tiedostot.get(i)[j][haeMapista(kentat,variableKey,false)] + ";");
//            	        }
//            	        ikkuna.kirjoitaKonsolille("\n");
            			
                		//tarkasta muistinkäyttö joka 10,000 rivin jälkeen
            			if (kaikkirivit % 10000 == 0 && kaikkirivit != 0) {
                    		long free  = getFreeMemory(); 
                    		if (free < (free2+1) && free < free2) { //alle 50M vapaana
                    			ikkuna.kirjoitaKonsolille("Varoitus: muisti loppumassa, vapaana: " + bytesToMegabytes(free) + "\n");
                    			free2 = free;
                    			muistiloppumassa = true;
                    		}
            			}
                		kaikkirivit++;
            			
                		if (muistiloppumassa && kaikkirivit % 10000 == 0) {
                       		if (GCTime() > 60000) {
                    			ikkuna.kirjoitaKonsolille("Virhe: muistin putsaus hidastui liikaa. Lopetetaan tiedostojen lukeminen ja  analysoidaan " + (i+1) + " tiedostolla.\n");
                    			ikkuna.kirjoitaKonsolille("Kokeile kasvattaa muistivarausta Javalle ajamalla Logintutkija komennolla: java -Xms1024m -Xmx2048m -jar Logintutkija.jar\n");
                    			ikkuna.kirjoitaKonsolille("Älä kuitenkaan ylitä koneesi vapaan keskusmuistin määrää.\n");
                    			break exit;
                    		}
                		}
            			
            			//dump
//            			for (int k=0; k<tiedostot.get(i)[j].length;k++) {
//            				ikkuna.kirjoitaKonsolille("Arvo " + k + ": " + tiedostot.get(i)[j][k] + " ");
//            			}
//            			ikkuna.kirjoitaKonsolille("\n");
            			
            			
						//tarkistetaan tietueen pituus
						if (tietueen_pituus == 0) { // 1. logi, sen elemettien määrä muistiin
							tietueen_pituus=tiedostot.get(i)[j].length;
						} else {
							if (tietueen_pituus != tiedostot.get(i)[j].length) {
			    				ikkuna.kirjoitaKonsolille("Eri määrä elementtejä tiedostossa " + (i+1) + ": alunperin " + tietueen_pituus + ", nyt " + tiedostot.get(i)[j].length + " - ohitetaan tiedosto\n");
			    				break;
							}
						}
			    			
            			//etsitään tyhjä rivi
            			if (tiedostot.get(i)[j][0].equals("") && 
            					!getValittu_Tiedosto_Filtteri().equals("nibes") &&
            					!getValittu_Tiedosto_Filtteri().equals("ctc")) {
            				ikkuna.kirjoitaKonsolille("Tyhjä rivi " + (j+1) + " tiedostossa " + (i+1) + " - ei huomioida.\n");
            				continue rivi;
            			}

            			
            			if (tiedostot.get(i)[0].length > tiedostot.get(i)[j].length || Arrays.toString(tiedostot.get(i)[j]).contains("NULL")){
            				if (ikkuna.getTietolahde() == 1) {
            					ikkuna.kirjoitaKonsolille("Vajaa tietue (" + (j+1) + ") - ei huomioida.\n");
            				} else {
            					ikkuna.kirjoitaKonsolille("Vajaa rivi (" + (j+1) + ") tiedostossa " + (i+1) + " - ei huomioida. (Otithan USB tikun ulos hallitusti?)\n");
            				}
        	        		kv = 0;
        	        		sa = 0;
        	        		la = 0;
        	        		le = 100;
        	        		ikkuna.nollaaNumerot();
            				continue rivi;
            			}
	            		
            			if ( ! LogintutkijaGUI.getValittu_Tiedosto_Filtteri().equals("ctc")) {
	            			//etsitään validit merkit (F1145 tulosti ei-merkin)
	            			for (int a = 0; a < tiedostot.get(i)[j].length; ++a) {
	            				if (j>1 && !tiedostot.get(i)[j][a].matches("^[a-zA-Z0-9,.;:()°˚/\\-_'\\s]+$")) {
	            					ikkuna.kirjoitaKonsolille(tiedostot.get(i)[j][a] + "\n");
	            					ikkuna.kirjoitaKonsolille( "Korruptoitunut merkkijono " + (i+1) + ". tiedostossa, rivillä " + (j+1) + " - tietuetta ei huomioida.\n" );
	            					continue rivi;
	            				}
	                			//tarkistetaan validit arvot (-32768 on piuha irti anturista)
	            				if (tiedostot.get(i)[j][a].equals("-32768")) {
	            					ikkuna.kirjoitaKonsolille( "Laiton arvo \"" + tiedostot.get(i)[j][a] + "\" " + (i+1) + ". tiedostossa, rivillä " + (j+1) + " kentässä " + (a+1) + "  - tietuetta ei huomioida. Onko anturin piuha irti?\n" );
	            					continue rivi;
	            				} 
	            			}
            			}
            			
            			// Malliarvaukset datasta
            			//
            			//
            			//
            			//Jos paikallinen kanta, luetaan malli
						if (ikkuna.getTietolahde()  == 1 && ikkuna.getTietokanta_dbms().equals("sqlite")) { //sqlite
							if (haeMapista(kentat,"Model",false) != -1) {
    							ikkuna.getLblMLPMalli().setText(tiedostot.get(i)[j][haeMapista(kentat,"Model",false)]);
							} else {
								ikkuna.getLblMLPMalli().setText("MLP");
							}
						} else {
							//VILP
	            			if (haeMapista(kentat,"compr. state",false) != -1 &&
	            					haeMapista(kentat,"compr. freq. calc",false) != -1 &&
	            					haeMapista(kentat,"compr. protection mode",false) != -1 &&
	            					ikkuna.getTietolahde()  == 0) {
	            				//VMM mukana
	            				if (haeMapista(kentat,"BT63",false) != -1) {
	            					ikkuna.getLblMLPMalli().setText(nibecontroller + "-VILPVMM");
	            				} else {
		        					ikkuna.getLblMLPMalli().setText(nibecontroller + "-VILP");
		        					kaynti_haku = "compr. state(44457)";
		        					tia_haku="Add.Step"; //spartani lisäys
	            				}

	        				} else if (nibecontroller.equals("S") &&
	        						haeMapista(kentat,"Suojaustila (EB101)",false) != -1 &&
	            					ikkuna.getTietolahde()  == 0) {
	        					ikkuna.getLblMLPMalli().setText(nibecontroller + "-VILP");
	        					kaynti_haku = "Kompressori (EB101)";
	        				} else if ((haeMapista(kentat,"BS1-ref",false) != -1 || haeMapista(kentat,"BT20",false) != -1 ) &&
	            					ikkuna.getTietolahde()  == 0) {
	        					ikkuna.getLblMLPMalli().setText(nibecontroller + "-PILP");
	        				} else if (haeMapista(kentat,"EB100-EP15 Prio",false) != -1) {
								//luultavasti F1345
								ikkuna.getLblMLPMalli().setText(nibecontroller + "1345");
			        			tia_haku="Add.Step";
							//1x55
							//
							//
	        				} else if ( haeMapista(kentat,"BF1 EP14",false) != -1  || //vanhat firmikset vain BF1 EP14
        						(haeMapista(kentat,"EP14-BF1", false) != -1
        						&& haeMapista(kentat,"EP14-BT11",false) != -1
        						&& haeMapista(kentat,"compr. freq. act",false) != -1) || //erotellaan 1x45 BF1:llä
        						( nibecontroller.equals("S") &&
        								haeMapista(kentat,"BF1",false) != -1) //S-ohjain
	    							) { //erotellaan VILPeistä
	        						//luultavasti F1x55
	        						ikkuna.getLblMLPMalli().setText(nibecontroller + "1x55");
	            			// CTC
	        				} else if (haeMapista(kentat,"COMPRESSOR_RUNNING",false) != -1) {
	        					ikkuna.getLblMLPMalli().setText("CTC " + ctcmappi.split(",")[0]);
	        				}


	    					//1768 tarkistus
	    					int version = 0;
	    					if (j==2 &&
	    							!nibecontroller.equals("S") &&
	    							!getValittu_Tiedosto_Filtteri().equals("ctc")) { // eka tai toka datarivi
	    						version = Integer.parseInt(tiedostot.get(i)[j][2]); //version = 3. kenttä	    						
	        					if (version < 3105 && ikkuna.getTietolahde() == 0) {
	        						//lopetetaan kun versio 1768 löytyi
	            					ikkuna.kirjoitaKonsolille(version + " -versio löydetty. Logintutkija tukee vain 3105 ja tuoreempia logeja. Toimii tai ei toimi.\n");
	            					keruu_sisaan_haku = "BT10";
	            					keruu_ulos_haku = "BT11";
	        					}
	    					}
							//1126 hack
							if (haeMapista(kentat,"version",false) != -1) {
	    						if (tiedostot.get(i)[j][haeMapista(kentat,"version",false)].equals("3111")) {
	    							ikkuna.getLblMLPMalli().setText(nibecontroller + "1226");
	    						}
							}
						}
						
						
						//säädöt per malli
						//
						//
						//
						if (ikkuna.getLblMLPMalli().getText().contains("VILP")) { //VILP
							if (ikkuna.getTietolahde()  == 0) {
        					keruu_sisaan_haku = "BT28"; //ulkoilma
        					keruu_ulos_haku = "BT16"; //lämmönvaihdin
							} else {
	        					keruu_sisaan_haku = "EP14-BT10"; //ulkoilma "keruu tulo" kannassa  BT28
	        					keruu_ulos_haku = "EP14-BT11"; //lämmönvaihdin "keruu meno" kannassa BT16
							}
						}
						if (ikkuna.getLblMLPMalli().getText().contains("PILP")) { //PILP
							if (ikkuna.getTietolahde()  == 0) {
        					keruu_sisaan_haku = "BT20"; //poisto
        					keruu_ulos_haku = "BT21"; //jäte
							} else {
	        					keruu_sisaan_haku = "EP14-BT10"; //poistoilma "keruu tulo" kannassa  BT20
	        					keruu_ulos_haku = "EP14-BT11"; //jäteilma kannassa BT21
							}
						}
						if (nibecontroller.equalsIgnoreCase("S")) { //S-ohjain
							if (ikkuna.getTietolahde()  == 0) {
	        					bt1_haku = "(BT1)";
	        					bt2_haku = "(BT2)";
	        					bt3_haku = "(BT3)";
	        					bt6_haku = "(BT6)";
	        					bt7_haku = "(BT7)";
	        					bt10_haku = "(BT10)";
	        					bt11_haku = "(BT11)";
	        					bt12_haku = "(BT12)";
	        					bt14_haku = "(BT14)";
	        					bt17_haku = "(BT17)";
	        					ep14_gp1_haku = "(GP1)";
	        					ep14_gp2_haku = "(GP2)";
	        					tia_haku = "Teho sis";
	        		        	cs_haku = "Laskettu menol";
	        		        	dm_haku = "[AM]";
	        		        	bf1_haku = "(BF1)";
	    		        		cfa_haku = "Nykyinen kompressorin taajuus (EB101)";
	        		        	bt50_haku = "Roomsensor 1-1";
	        					kerroin=10;
	        					gpkerroin=1;
	        					tia_kerroin=100;
	        		        	if ( ! ikkuna.getLblMLPMalli().getText().contains("VILP")) {
	            		        	cfa_haku = "Kompressorin taajuus, nykyinen [Hz]";
	            		        	kaynti_haku = "ASB";
	            					keruu_sisaan_haku = "BT10";
	            					keruu_ulos_haku = "BT11";
	        		        	}
							} else {
	        					keruu_sisaan_haku = "EP14-BT10"; //ulkoilma "keruu tulo" kannassa  BT28
	        					keruu_ulos_haku = "EP14-BT11"; //lämmönvaihdin "keruu meno" kannassa BT16
							}
						}
						//MySQL
						if (ikkuna.getTietolahde()  == 1 && ikkuna.getTietokanta_dbms().equals("mysql")) {
	    					tia_haku = "Tot.Int.Add";
	    					kaynti_haku = "EB100-EP14-PCA";
						}
						//CTC
						if (ikkuna.getLblMLPMalli().getText().contains("CTC")) { //CTC
							if (ikkuna.getTietolahde()  == 0) {
								kerroin = 10;
								gpkerroin = kerroin;
								tia_kerroin = 100; 
							}
						}
						
        				//datarivi
            			if ( (!tiedostot.get(i)[j][0].equalsIgnoreCase("Divisors") &&
	    					!tiedostot.get(i)[j][0].equalsIgnoreCase("Date")) && 
	    					!tiedostot.get(i)[j][0].equalsIgnoreCase("DateTime") //S-ohjain
            					) {
            				
            				String [] paivays;
            				String [] aika;

            				if (nibecontroller.equals("S")) {
		            			String [] datetime = tiedostot.get(i)[j][haeMapista(kentat,"DateTime",false)].split(" ");
		            			//pvm
		            			paivays=datetime[0].split("[-]");
		            			//kello
		            			aika = datetime[1].split("[:]");
            				} else if (ikkuna.getLblMLPMalli().getText().contains("CTC")) {
		            			String [] datetime = tiedostot.get(i)[j][haeMapista(kentat,"DATETIME",false)].split(" ");

		            			//pvm
		            			paivays = new String[3];
		            			paivays[0]=datetime[0].substring(0,4);
		            			paivays[1]=datetime[0].substring(4,6);
		            			paivays[2]=datetime[0].substring(6,8);

		            			//kello
		            			aika = new String[3];
		            			aika[0] = datetime[1].substring(0,2);
		            			aika[1] = datetime[1].substring(3,5);
		            			aika[2] = "0";

            				} else {

	            				//muutetaan teksti date ja time
		            			//pvm
		            			paivays = tiedostot.get(i)[j][haeMapista(kentat,"Date",false)].split("[-]");
		            			//kello
		            			aika = tiedostot.get(i)[j][haeMapista(kentat,"Time",false)].split("[:]");
            				}

	            			paiva = Integer.parseInt(paivays[2]);
	            			kuukausi = (Integer.parseInt(paivays[1])-1);
	            			vuosi = Integer.parseInt(paivays[0]);
	            			tunti = Integer.parseInt(aika[0]);
	            			minuutti = (Integer.parseInt(aika[1]));
	            			sekunti = Integer.parseInt(aika[2]);
	            			
	            			kokaika++;

	            			if (edellinen_logiaika.equals(new GregorianCalendar(vuosi, kuukausi, paiva, tunti, minuutti, sekunti))) {
	            				//ikkuna.kirjoitaKonsolille("Sama aika!: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(edellinen_logiaika.getTime()) + "\n");
            					samatajat++;
            					//ikkuna.kirjoitaKonsolille("Samat: " + samatajat + "\n");
	            				if (samatajat > max_samatajat) {
	            					max_samatajat = samatajat;
	            				}
	            				int vali = 60/max_samatajat;
	            				sekunti = vali * (samatajat-1);
	            				if (sekunti > 59) {
	            					sekunti = 59;
	            				}
	            				//ikkuna.kirjoitaKonsolille("SAMA Sekunnit: " + sekunti + "\n");
	            				logiaika.add(new GregorianCalendar(vuosi, kuukausi, paiva, tunti, minuutti, sekunti));
	            			} else {
	            				//ikkuna.kirjoitaKonsolille("EI SAMA Sekunnit: " + sekunti + "\n");
	            				logiaika.add(new GregorianCalendar(vuosi, kuukausi, paiva, tunti, minuutti, sekunti));
	            				edellinen_logiaika = new GregorianCalendar(vuosi, kuukausi, paiva, tunti, minuutti, sekunti);
	            				samatajat=1;
	            			}            				

	            			//version
	            			if (!ikkuna.getLblMLPMalli().getText().contains("CTC")) {
		    	        		if (lokiVersio.equalsIgnoreCase("0000")) {
		    	        			if (haeMapista(kentat,version_haku,false) != -1){ //version=43001
			    	        			ikkuna.getLblLokiVersio().setText("v" + tiedostot.get(i)[j][haeMapista(kentat,version_haku,false)]); //version=43001
			    	        			lokiVersio=tiedostot.get(i)[j][haeMapista(kentat,version_haku,false)];
		    	        			} else if ( haeMapista(kentat,"Ohjelmistoversio",false) != -1 ) {
		    	        				version_haku = "Ohjelmistoversio";
		    	        				ikkuna.getLblLokiVersio().setText("v" + tiedostot.get(i)[j][haeMapista(kentat,"Ohjelmistoversio",false)]); //version=Ohjelmistoversio
			    	        			lokiVersio=tiedostot.get(i)[j][haeMapista(kentat,"Ohjelmistoversio",false)];
		    	        			} else if ( haeMapista(kentat,"id:60529",false) != -1 ) {
		    	        				version_haku = "id:60529";
		    	        				ikkuna.getLblLokiVersio().setText("v" + tiedostot.get(i)[j][haeMapista(kentat,"id:60529",false)]); //version=id:60529
			    	        			lokiVersio=tiedostot.get(i)[j][haeMapista(kentat,"id:60529",false)];
		    	        			} else {
		    	        				version_haku = "version";
		    	        				version_strict=true;
			    	        			ikkuna.getLblLokiVersio().setText("v" + tiedostot.get(i)[j][haeMapista(kentat,version_haku,true)]); //version strict
			    	        			lokiVersio=tiedostot.get(i)[j][haeMapista(kentat,version_haku,true)];
		    	        			}
		    	        		}

		    	        		//version check
		    	        		if (!lokiVersio.equalsIgnoreCase(tiedostot.get(i)[j][haeMapista(kentat,version_haku,version_strict)]) && ikkuna.getTietolahde()  == 0) {
		    	        			if (!lokiVersio.equalsIgnoreCase("9999")) {
		    	        				ikkuna.kirjoitaKonsolille("Edellisen login versio " + lokiVersio + " ei vastaa nykyistä (" + tiedostot.get(i)[j][haeMapista(kentat,version_haku,version_strict)] + ") tiedostossa " + (i+1) + "\n");
		    	        				ikkuna.kirjoitaKonsolille("Nykyistä ja seuraavia logitiedostoja ei käsitellä. Pidä hakemistossa vain saman versiotason logeja.\n");
		    	        				lokiVersio="9999";
		    	        			}
		    	        			continue rivi;
		    	        		}
	            			}

	    	        		//r-version
	    	        		if (haeMapista(kentat,"R-version",false) != -1){
	    	        			if (haeMapista(kentat,"43001",false) != -1){ //version=43001
		    	        			ikkuna.getLblLokiVersio().setText("v" + tiedostot.get(i)[j][haeMapista(kentat,"43001",false)] //version=43001
		    	        					+ "R" + tiedostot.get(i)[j][haeMapista(kentat,"R-version",false)]);
	    	        			} else {
		    	        			ikkuna.getLblLokiVersio().setText("v" + tiedostot.get(i)[j][haeMapista(kentat,"version",true)] //version strict
		    	        					+ "R" + tiedostot.get(i)[j][haeMapista(kentat,"R-version",false)]);
	    	        			}
	    	        			lokiRVersio=tiedostot.get(i)[j][haeMapista(kentat,"R-version",false)];
	    	        		}
 	
	            			//kirjoitetaan aloitusaika Käyrät-ikkunaan
	            			if (aloitusaika != true) {
	            				LogintutkijaGUI.setAikaAloitus(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(logiaika.get(0).getTime()));
	            				aloitusaika=true;
	            			}

	    	        		//bt1
	            			if (haeMapista(kentat,bt1_haku,bt1_strict) != -1) { //mittauspisteen numero on tarkin
	            				//bt1.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,bt1_haku,bt1_strict)]));
	            				bt1.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt1_haku,bt1_strict)],kerroin));
	            			} else {
	            				bt1_haku = "BT1";
	            				if (haeMapista(kentat,bt1_haku,true) != -1) { //1768, strict
	            					//bt1.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,bt1_haku,true)])); //strict
	            					bt1.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt1_haku,true)],kerroin));
	            					bt1_strict = true;
	            				} else {
	            					bt1.add(0);
	            				}
	            			}
	            			
    	        			//bt2
	            			if (haeMapista(kentat,bt2_haku,bt2_strict) != -1) {
	            				bt2.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt2_haku,bt2_strict)],kerroin));
	            			} else {
	            				bt2_haku = "BT2";
	            				if (haeMapista(kentat,bt2_haku,true) != -1) { //1768 ja db
	            					bt2.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt2_haku,true)],kerroin));
	            					bt2_strict = true;
	            				} else {
	            					bt2.add(0);
	            				}
	            			}
            				  		
	    	        		//bt3
	            			if (haeMapista(kentat,bt3_haku,false) != -1) {
	            				bt3.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt3_haku,false)],kerroin));
	            			} else {
	            				bt3_haku = "BT3";
	            				if (haeMapista(kentat,bt3_haku,false) != -1) { //1768
	            					bt3.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt3_haku,false)],kerroin));
	            				} else {
	            					bt3.add(0);
	            				}
	            			}

	            			//EB101-BT3
	            			if (haeMapista(kentat,"EB101-BT3",false) != -1) {
	            				eb101_bt3.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,"EB101-BT3",false)],kerroin));
	            			} else {
	            				eb101_bt3.add(0);
	            			}
			
	            			//bt6 (varo bt63)		            			
	            			if (haeMapista(kentat,bt6_haku,false) != -1) {
	            				bt6.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt6_haku,false)],kerroin));
	            			} else {
	            				bt6_haku = "BT6";
	            				if (haeMapista(kentat,bt6_haku,true) != -1) { //1768
	            					bt6.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt6_haku,true)],kerroin));
	            				} else {
	            					bt6.add(0);
	            				}
	            			}
      			           			
	            			//kv bt7 (varo bt71) ja bt6
	            			if (haeMapista(kentat,bt7_haku,bt7_strict) != -1) {
	            				bt67_label = "ylävaraaja";
	            				lampo_kv.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt7_haku,bt7_strict)],kerroin));
	            			} else {
	            				bt7_haku = "BT7";
	            				if (haeMapista(kentat,bt7_haku,true) != -1) { //db
		            				bt67_label = "ylävaraaja";
		            				lampo_kv.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt7_haku,true)],kerroin));
		            				bt7_strict=true;
	            				} else {
		            				bt7_haku = "BT6"; // jos bt7 ei löydy, haetaan sitten bt6:lla
		            				if (haeMapista(kentat,bt7_haku,false) != -1) { //1768
		            					bt67_label = "alavaraaja";
			            				lampo_kv.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt7_haku,false)],kerroin));
		            				} else {
		            					lampo_kv.add(0);
		            				}
		            			}
	            			}

	            			//bt10
	            			if (haeMapista(kentat,bt10_haku,false) != -1) {
	            				bt10.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt10_haku,false)],kerroin));
	            			} else {
	            				bt10_haku = "BT10";
	            				if (haeMapista(kentat,bt10_haku,false) != -1) { //1768
	            					bt10.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt10_haku,false)],kerroin));
	            				} else {
	            					bt10.add(0);
	            				}
	            			}
	            			
	            			//bt11	            			
	            			if (haeMapista(kentat,bt11_haku,false) != -1) {
	            				bt11.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt11_haku,false)],kerroin));
	            			} else {
	            				bt11_haku = "BT11";
	            				if (haeMapista(kentat,bt11_haku,false) != -1) { //1768
	            					bt11.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt11_haku,false)],kerroin));
	            				} else {
	            					bt11.add(0);
	            				}
	            			}

	            			//bt12
	            			if (haeMapista(kentat,bt12_haku,false) != -1) { //EP14-BT12
	            				bt12.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt12_haku,false)],kerroin));
	            			} else {
	            				bt12_haku = "BT12";
	            				if (haeMapista(kentat,bt12_haku,false) != -1) { //1768
	            					bt12.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt12_haku,false)],kerroin));
	            				} else {
	            					bt12.add(0);
	            				}
	            			}
	            			
	            			//bt14
	            			if (haeMapista(kentat,bt14_haku,false) != -1) {
	            				bt14.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt14_haku,false)],kerroin));
	            			} else {
	            				bt14_haku = "BT14";
	            				if (haeMapista(kentat,bt14_haku,false) != -1) { //1768
	            					bt14.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt14_haku,false)],kerroin));
	            				} else {
	            					bt14.add(0);
	            				}
	            			}
	            			
	            			//bt16
	            			if (haeMapista(kentat,"EB101-BT16",false) != -1) {
	            				bt16.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,"EB101-BT16",false)],kerroin));
	            			} else {
	            				if (ikkuna.getTietolahde()  == 1 &&
	            						ikkuna.getTietokanta_dbms().equals("sqlite") &&
	            						ikkuna.getLblMLPMalli().getText().contains("VILP")) { //sqlite & VILP
	            					bt16.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,"BT11",false)],kerroin));
	            				} else {
	            					bt16.add(0);	
	            				}
	            			}
	            			
	            			//bt17
	            			if (haeMapista(kentat,bt17_haku,false) != -1) {
	            				bt17.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt17_haku,false)],kerroin));
	            			} else {
	            				bt17_haku = "BT17";
	            				if (haeMapista(kentat,bt17_haku,false) != -1) { //1768
	            					bt17.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt17_haku,false)],kerroin));
	            				} else {
	            					bt17.add(0);
	            				}
	            			}

	            			//bt25
	            			if (haeMapista(kentat,"BT25",false) != -1) {
	            				bt25.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,"BT25",false)],kerroin));
	            			} else {
	            				bt25.add(0);
	            			}

	            			//bt28
	            			if (haeMapista(kentat,"BT28",false) != -1) {
	            				bt28.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,"BT28",false)],kerroin));
	            			} else {
	            				bt28.add(0);
	            			}

	            			//bt50, sisälämpö
	            			if (haeMapista(kentat,bt50_haku,false) != -1) {
	            				if (muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt50_haku,false)],kerroin) < -100) {
	            					//CTC ja muillekin järkevyystsekki
	            					bt50.add(0);
	            				} else {
		            				bt50.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt50_haku,false)],kerroin));
	            				}
	            			} else {
	            				bt50.add(0);
	            			}
	            			//bt51, 53 ja 54
	            			if (haeMapista(kentat,"BT51",false) != -1) {
	            				bt51.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,"BT51",false)],kerroin));
	            			} else {
	            				bt51.add(0);
	            			}
	            			if (haeMapista(kentat,"BT53",false) != -1) {
	            				bt53.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,"BT53",false)],kerroin));
	            			} else {
	            				bt53.add(0);
	            			}
	            			if (haeMapista(kentat,"BT54",false) != -1) {
	            				bt54.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,"BT54",false)],kerroin));
	            			} else {
	            				bt54.add(0);
	            			}
	            			
	            			//bt63,VILP meno sähkövastuksen jälkeen            			
	            			if (haeMapista(kentat,"BT63",false) != -1) {
	            				bt63.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,"BT63",false)],kerroin));
	            			} else {
	            				bt63.add(0);
	            			}
	            			
	            			//bt71
	            			if (haeMapista(kentat,"BT71",false) != -1) {
	            				bt71.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,"BT71",false)],kerroin));
	            			} else {
	            				bt71.add(0);
	            			}

	            			//gp1 EP14 
	            			//if (haeMapista(kentat,"GP1-speed EP14",false) != -1) {
	            			if (haeMapista(kentat,ep14_gp1_haku,false) != -1) {
	            				gp1.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,ep14_gp1_haku,false)],gpkerroin));
	            			} else {
	            				gp1.add(0);
	            			}
	            			
	            			//gp2 EP14
	            			//if (haeMapista(kentat,"GP2-speed EP14",false) != -1) {
	            			if (haeMapista(kentat,ep14_gp2_haku,false) != -1) {
	            				gp2.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,ep14_gp2_haku,false)],gpkerroin));
	            			} else {
	            				gp2.add(0);
	            			}
	            			
	            			//prio
	            			if (haeMapista(kentat,"Prio",false) != -1) {
		            			//prio uima-allaslämmityksen laskentaa varten
		            			prio.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,"Prio",false)],1));
	            			} else {
	            				prio.add(0);
	            			}

	            			//virtamuuntajat
	            			if (haeMapista(kentat,be1_haku,false) != -1) {
	            				be1.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,be1_haku,false)],1));
	            			} else {
	            				be1.add(0);
	            			}
	            			if (haeMapista(kentat,be2_haku,false) != -1) {
	            				be2.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,be2_haku,false)],1));
	            			} else {
	            				be2.add(0);
	            			}
	            			if (haeMapista(kentat,be3_haku,false) != -1) {
	            				be3.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,be3_haku,false)],1));
	            			} else {
	            				be3.add(0);
	            			}
	            			
	            			//hälytykset
	            			if (haeMapista(kentat,alarm_haku,false) != -1) {
	            				alarm.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,alarm_haku,false)],1));
	            			} else {
	            				alarm.add(0);
	            			}
	            			

	            			//
	            			//
	            			//
	            			//EP15
	            			//F1345 EP15 Prio
	            			if (haeMapista(kentat,"EB100-EP15 Prio",false) != -1) {
	                			ep15_prio.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15 Prio",false)]));
	            			}
	            			//EP15 gp1 EP15
	            			if (haeMapista(kentat,"GP1-speed EP15",false) != -1) {
	            				ep15_gp1.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"GP1-speed EP15",false)]));
	            			} else {
	            				ep15_gp1.add(0);
	            			}
	            			//EP15 gp2 EP15
	            			if (haeMapista(kentat,"GP2-speed EP15",false) != -1) {
	            				ep15_gp2.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"GP2-speed EP15",false)]));
	            			} else {
	            				ep15_gp2.add(0);
	            			}
         			
	            			//EP15 BT3
	            			if (haeMapista(kentat,"EP15-BT3",false) != -1) {
	            				ep15_bt3.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EP15-BT3",false)]));
	    	        			//EP15-bt3 saa negatiivisen arvon jos kyselty Modbussilla F11/245 koneesta
	    	        			if (Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EP15-BT3",false)]) == 32768) {
	    	        				if (cfa_fake) {
	    	        					ikkuna.getLblMLPMalli().setText("F1x45");
	    	        				}
	    	        			}
	            			} else {
	            				ep15_bt3.add(0);
	            			}

	            			//EP15 BT10
	            			if (haeMapista(kentat,"EP15-BT10",false) != -1) {
	            				ep15_bt10.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EP15-BT10",false)]));
	            			} else {
	            				ep15_bt10.add(0);
	            			}
	            			//EP15 BT11
	            			if (haeMapista(kentat,"EP15-BT11",false) != -1) {
	            				ep15_bt11.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EP15-BT11",false)]));
	            			} else {
	            				ep15_bt11.add(0);
	            			}
	            			//EP15 BT12
	            			if (haeMapista(kentat,"EP15-BT12",false) != -1) {
	            				ep15_bt12.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EP15-BT12",false)]));
	            			} else {
	            				ep15_bt12.add(0);
	            			}
	            			//EP15 BT14
	            			if (haeMapista(kentat,"EP15-BT14",false) != -1) {
	            				ep15_bt14.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EP15-BT14",false)]));
	            			} else {
	            				ep15_bt14.add(0);
	            			}
	            			//EP15 BT14
	            			if (haeMapista(kentat,"EP15-BT17",false) != -1) {
	            				ep15_bt17.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EP15-BT17",false)]));
	            			} else {
	            				ep15_bt17.add(0);
	            			}

	            			//F1345 sähkö lisäys
	            			if (F1345LisaysAskel == 0 && ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345")) {
	            				if (ikkuna.getTietolahde()  == 0) {
		            				//F1345 Add.Step
		            				add_step.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"Add.Step",false)])*100);
	            				} else {
		            				//F1345 DB
	            					//tia_haku = "Add.Step";
		            				add_step.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,tia_haku,false)]));
	            				}

	             			} else {
	                   			//suorasähkö
	             				if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345")) {
	             					kw_sahko.add(10*F1345LisaysAskel*Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"Add.Step",false)]));
	             				} else {
	             					//Tot.Int.Add
	             					kw_sahko.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,tia_haku,false)],tia_kerroin));
	             				}
	            			}
	            			//EP15 END
	            			
	            			
	            			//Calc. Supply
	            			if (haeMapista(kentat,cs_haku,false) != -1) {
	            				cs.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,cs_haku,false)],kerroin));
	            			} else {
	            				cs.add(0);
	            			}
           			
	            			//Degree Minutes
	            			if (haeMapista(kentat,dm_haku,false) != -1) {
	            				dm.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,dm_haku,false)],kerroin));
	            			} else {
	            				dm.add(0);
	            			}
	            			
	            			//bf1 Flow
	            			if (haeMapista(kentat,bf1_haku,false) != -1) {
		            			bf1.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bf1_haku,false)],kerroin));
	            			} else {
	            				bf1.add(0);
	            			}
	            			
	            			//compressor frequency active
	            			if (haeMapista(kentat,cfa_haku,false) != -1 && cfa_fake==false){
	            				cfa.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,cfa_haku,false)],kerroin));
	            				//S-sarjalainen, ASB sanoo kompressorin olevan päällä, mutta kierrokset ovat nollissa
	            				if (muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,cfa_haku,false)],kerroin) == 0) {
	            					//invertteri ei käy
	            					iIsStopped=true;
	            					//ikkuna.kirjoitaKonsolille("Invertteri: " + cfa_haku + ": " + muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,cfa_haku,false)],kerroin) + "\n");
	            				} else {
	            					iIsStopped=false;
	            				}
	            			} else {
	            				//F/S1x45 CFA = 50
	            				if (kompr_kaynnissa) {
	            					cfa.add(500);
	            				} else {
	            					cfa.add(0);
	            				}
	            			}

	            			//FLM ilman lämpötilat
	            			//bt20
	            			if (haeMapista(kentat,"BT20",false) != -1) {
	            				bt20.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,"BT20",false)],kerroin));
	            			} else {
	            				bt20.add(0);
	            			}
	            			//bt21
	            			if (haeMapista(kentat,"BT21",false) != -1) {
	            				bt21.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,"BT21",false)],kerroin));
	            			} else {
	            				bt21.add(0);
	            			}
	            			
	            			//Relays PCA-Base
	            			//VILPissä PCA ei kerro tilaa, ainakaan luotettavasti, kannasta luettuna PCA jo kunnossa
	            			//S-VILP
	            			//uudessa S-VILP:ssä ei enää Kompressori 1/0 kenttää
	            			if (ikkuna.getLblMLPMalli().getText().contains("VILP") && ikkuna.getTietolahde() == 0) {
	            				boolean isSVILPRunning = false;
	            				if ( haeMapista(kentat,kaynti_haku,false) == -1 ) {
	            					//uusi S-VILP
	            					if (haeMapista(kentat,pkt_haku,false) != -1) {
	            						if (!tiedostot.get(i)[j][haeMapista(kentat,pkt_haku,false)].equalsIgnoreCase("0")) {
	            							//kompressorin pyyntötaajuus jotain muuta kuin 0
	            							isSVILPRunning = true;
	            						}
		            				}
	            				} else {
	            					//vanha S-VILP
	            					if (tiedostot.get(i)[j][haeMapista(kentat,kaynti_haku,false)].equalsIgnoreCase("1")) {
	            						isSVILPRunning = true;
	            					}
	            					
	            				}
	            				
	            				//if (tiedostot.get(i)[j][haeMapista(kentat,kaynti_haku,false)].equalsIgnoreCase("1")) {
	            				if (isSVILPRunning) {
	            					if (tiedostot.get(i)[j][haeMapista(kentat,"Prio",false)].equalsIgnoreCase("30")) { //pca 7
	            						relaysPCAbase.add(7);
	            					} else if (tiedostot.get(i)[j][haeMapista(kentat,"Prio",false)].equalsIgnoreCase("20")) { //pca 15
	            						relaysPCAbase.add(15);
	            					} else if (tiedostot.get(i)[j][haeMapista(kentat,"Prio",false)].equalsIgnoreCase("10")) { //pca 0
	            						relaysPCAbase.add(0);
	            					}
	            				} else { //kompressori ei käy
	            					if (tiedostot.get(i)[j][haeMapista(kentat,"Prio",false)].equalsIgnoreCase("20")) { //pca 10
	            						relaysPCAbase.add(10);
	            					} else if (tiedostot.get(i)[j][haeMapista(kentat,"Prio",false)].equalsIgnoreCase("20")) { //pca 2
	            						relaysPCAbase.add(2);
	            					} else { //kompura ja prio 10/off
	            						relaysPCAbase.add(0);
	            					}
	            				}
	            			} else if ((nibecontroller.equals("S") &&
	            					!ikkuna.getLblMLPMalli().getText().contains("VILP") &&
	            					ikkuna.getTietolahde()  == 0)) { //S-ohjain, ei VILP
	            				if (tiedostot.get(i)[j][haeMapista(kentat,kaynti_haku,false)].equalsIgnoreCase("3") ||
	            					tiedostot.get(i)[j][haeMapista(kentat,kaynti_haku,false)].equalsIgnoreCase("11")) {
	            					if (tiedostot.get(i)[j][haeMapista(kentat,"Prio",false)].equalsIgnoreCase("30") ||
	            							tiedostot.get(i)[j][haeMapista(kentat,"Prio",false)].equalsIgnoreCase("40")) { //pca 7
	            						relaysPCAbase.add(7);
	            					} else if (tiedostot.get(i)[j][haeMapista(kentat,"Prio",false)].equalsIgnoreCase("20")) { //pca 15
	            						relaysPCAbase.add(15);
	            					} else if (tiedostot.get(i)[j][haeMapista(kentat,"Prio",false)].equalsIgnoreCase("10")) { //pca 0
	            						relaysPCAbase.add(0);
	            					}
	            				} else { //kompressori ei käy -mistä tiedän S-sarjalaisessa?
	            					if (tiedostot.get(i)[j][haeMapista(kentat,"Prio",false)].equalsIgnoreCase("20")) { //pca 10
	            						relaysPCAbase.add(10);
	            					} else if (tiedostot.get(i)[j][haeMapista(kentat,"Prio",false)].equalsIgnoreCase("20")) { //pca 2
	            						relaysPCAbase.add(2);
	            					} else { //kompura ja prio 10/off
	            						relaysPCAbase.add(0);
	            					}
	            				}
            				} else if (ikkuna.getLblMLPMalli().getText().contains("CTC")) { //CTC
            					if (tiedostot.get(i)[j][haeMapista(kentat,"COMPRESSOR_RUNNING",false)].equalsIgnoreCase("ON")) {
            						if (tiedostot.get(i)[j][haeMapista(kentat,"HEATING",false)].equalsIgnoreCase("1")) {
            							relaysPCAbase.add(7);
            							
            						} else if (tiedostot.get(i)[j][haeMapista(kentat,"HOTWATER",false)].equalsIgnoreCase("1")) {
            							relaysPCAbase.add(15);
            						} else {
            							relaysPCAbase.add(0);
            						}
            					} else {
            						relaysPCAbase.add(0);
            					}
            				} else {
	            				relaysPCAbase.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,kaynti_haku,false)]));
    	            			//ikkuna.kirjoitaKonsolille("BEBUG1: " + kaynti_haku + " " + haeMapista(kentat,kaynti_haku,false) + " " +  tiedostot.get(i)[j][haeMapista(kentat,kaynti_haku,false)] + "\n");
	            			}

	            			//ikkuna.kirjoitaKonsolille(j + ": pca(" + relaysPCAbase.size()  + "): " + relaysPCAbase.get(relaysPCAbase.size()-1) + " ed: " + edellinentila + "\n");
	            			//jos pca muuttuu, resetoi
	            			if(relaysPCAbase.get(relaysPCAbase.size()-1) != edellinentila){
	            				prosessinalku=0;
	            			}
	            			
	            			//ikkuna.kirjoitaKonsolille("PCA: " + relaysPCAbase.get(relaysPCAbase.size()-1) + "\n");
							/*
							 * if
							 * (tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP14 Prio",false)].equals("2"))
							 * { ikkuna.kirjoitaKonsolille("Prio EP14: " +
							 * tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP14 Prio",false)] + "\n"); } if
							 * (tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15 Prio",false)].equals("2"))
							 * { ikkuna.kirjoitaKonsolille("Prio EP15: " +
							 * tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15 Prio",false)] + "\n"); }
							 */
							 

	            			
	            			// Käyttötilat
	            			// Relays PCA Base, on binäärilukuna 0b00000000
	            			// bitti 0 = kompressorin tila, 0=pois, 1=päällä
	            			// bitti 1 = lämpöjohtopumpun tila, 0=pois, 1=päällä
	            			// bitti 2 = keruupumpun tila, 0=pois, 1=päällä
	            			// bitti 3 = vaihtoventtiilin ohjaus, 0 = venttiili auki ja tehdään lämmitysvettä, 1 = venttiili kiinni ja tehdään käyttövettä
	            			// jos nykyinen tila ja edellinen tila ovat eri, resetoidaan prosessin alku
	    	        		// F1345 Prio arvo kertoo käyttötilan EP15 kompressorille, EP14 saa Relays PCA Basen arvon
	            			// 1 tarkoittaa lämmitystä ja 2 käyttövettä, nolla näyttäisi olevan kun kompura ei käy.
	            			//
	            			// Relays PCA-Base 7
	            			// otetaan PCA-Base ylös tallennusta varten
	            			
	            			if ((relaysPCAbase.get(relaysPCAbase.size()-1) == 7 || relaysPCAbase.get(relaysPCAbase.size()-1) == 3)
	            					&& iIsStopped == false //invertteri pysyhtynyt vai ei?
	            					) {
	            				//0b00000111 = 7, F2040 = 3
	            				//käyntiaika, Relays PCA-Base on idx 20, arvo 7 = lämmitys
	            				//jos prio 40, lämmitetään uima-allasta
	            				//uima-allaskin on lämmitystä, mutta erotellaan se käyntiaikasuhteessa
            				
	            				if (haeMapista(kentat,"Prio",false) != -1) {
		            				if (Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"Prio",false)]) == 40) {
		            					kaytto_ua++;
		            				}
	            				}
	    	        			kaytto_la++;

	    	        			//skipataan pari ensimmäistä arvoa koska prosessi ei ole stabiloitunut	
	    	        			if(prosessinalku>=120){		
	    	        				if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345")
	    	        						) {
	    	        					if (ikkuna.getTietolahde()  == 0) {
	    	        						//delta lämmitysvesi meno idx9 (BT12) - tulo idx4 (BT3), ei jaeta niin ei tarvita doublea
	    	        						bt2_haku="EP14-BT12";
	    	        						bt3_haku="EP14-BT3";
	    	        					} else {
	    	        						bt2_haku="BT2";
	    	        						bt3_haku="BT3";
	    	        					}
	    	        				} else if (ikkuna.getLblMLPMalli().getText().contains("VILP")) {
	    	        					if (ikkuna.getTietolahde()  == 0) {
	    	        						bt2_haku="EB101-BT12";
	    	        						bt3_haku="EB101-BT3";
	    	        					} else {
	    	        						bt2_haku="EB100-EP14-BT12";
	    	        						bt2_strict=true;
	    	        						bt3_haku="BT3";
	    	        						bt3_strict=true;
	    	        					}
	    	        				} else {
	    	        					//ei-F1345
	    	        					//delta lämmitysvesi meno idx4 (BT2) - tulo idx5 (BT3), ei jaeta niin ei tarvita doublea
	    	        					if (ikkuna.getTietolahde()  == 1) {
	    	        						bt2_haku="BT2";
	    	        						bt2_strict=true;
	    	        						bt3_haku="BT3";
	    	        						bt3_strict=true;
	    	        					}
	    	        					//lampo_delta_la.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"BT2",bt2_strict)]) - Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,bt3_haku,false)]));
	    	        				}
	    	        				
	    	        				lampo_delta_la.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt2_haku,bt2_strict)],kerroin) - muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt3_haku,bt3_strict)],kerroin));

	    	        				//cop
	    	        				cop_la.add(lastcop=laskeCOP(bt12.get(bt12.size()-1),bt3.get(bt3.size()-1),bt14.get(bt14.size()-1),bt17.get(bt17.size()-1),bt10.get(bt10.size()-1),cop_035, cop_045));
		            				
	    	        				//delta keruu tulo idx8 (BT10) - tulo idx9 (BT11)
		            				lampo_delta_keruu.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_ulos_haku,false)],kerroin) - muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_sisaan_haku,false)],kerroin));

		            				//etsitään matalin ja korkein bt10
	            					//matalin
	            					if (bt10_min > muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_sisaan_haku,false)],kerroin)) {
	            						bt10_min = muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_sisaan_haku,false)],kerroin);
	            					}
	            					//korkein
	            					if (bt10_max < muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_sisaan_haku,false)],kerroin)) {
	            						bt10_max = muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_sisaan_haku,false)],kerroin);
	            					}
	            					//etsitään matalin ja korkein bt11
	            					//matalin
	            					if (bt11_min > muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_ulos_haku,false)],kerroin)) {
	            						bt11_min = muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_ulos_haku,false)],kerroin);
	            					}
	            					//korkein
	            					if (bt11_max < muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_ulos_haku,false)],kerroin)) {
	            						bt11_max = muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_ulos_haku,false)],kerroin);
	            					}
	            					
	    	        				if (1 < muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,tia_haku,false)],tia_kerroin)) {

	    		        				//lisälämpö
	    	        					if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345") &&
	    	        							Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,tia_haku,false)]) != 32768){
	    	        						lamm_sahko.add(F1345LisaysAskel*Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,tia_haku,false)]));
	    	        					} else {
	    	        						lamm_sahko.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,tia_haku,false)],tia_kerroin));
	    	        					}
	    	        				}

		        	        		if(!kompr_kaynnissa){
		    	        				kompr_kaynnistyksia++;
		    	        				kompr_kaynnissa=true;
		    	        				if (!laske_kaynnistys_jos_kaynnissa && i==0 && j==2) {
		    	        					ikkuna.kirjoitaKonsolille("käynnistystä ei lasketa (" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(logiaika.get(j-2).getTime()) + ")\n");
			    	        					kompr_kaynnistyksia--;
			    	        				}
			    	        			}

		    	        			} // jos mennyt 120 sek
	    	        		// Relays PCA-Base 15 tai 11
	            			} else if ((relaysPCAbase.get(relaysPCAbase.size()-1) == 15 || relaysPCAbase.get(relaysPCAbase.size()-1) == 11)
	            					&& iIsStopped == false //invertteri pysähtynyt vai ei?
	            					) {
	    	        			//0b00001111 = 15
	    	        			//käyntiaika, Relays PCA-Base on idx 20, arvo 15 = käyttövesi, 11 = kayttovesi FLM:n patterilla (huuhaata?)
	    	        			kaytto_kv++;
	    	        			
	    	        			if(prosessinalku>=120){
	    	        				if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345")) {
	    	        					if (ikkuna.getTietolahde()  == 0) {
	    	        						//delta lämmitysvesi meno idx9 (BT12) - tulo idx4 (BT3), ei jaeta niin ei tarvita doublea
	    	        						bt2_haku="EP14-BT12";
	    	        						bt3_haku="EP14-BT3";
	    	        					} else {
	    	        						bt2_haku="BT2";
	    	        						bt3_haku="BT3";
	    	        					}
	    	        				} else if (ikkuna.getLblMLPMalli().getText().contains("VILP")) {
	    	        					if (ikkuna.getTietolahde()  == 0) {
	    	        						bt2_haku="EB101-BT12";
	    	        						bt3_haku="EB101-BT3";
	    	        					} else {
	    	        						bt2_haku="EB100-EP14-BT12";
	    	        						bt2_strict=true;
	    	        						bt3_haku="BT3";
	    	        						bt3_strict=true;
	    	        					}
	    	        				} else {
	    	        					//ei-F1345
	    	        					//delta lämmitysvesi meno idx4 (BT2) - tulo idx5 (BT3), ei jaeta niin ei tarvita doublea
	    	        					if (ikkuna.getTietolahde()  == 1) {
	    	        						bt2_haku="BT2";
	    	        						bt2_strict=true;
	    	        						bt3_haku="BT3";
	    	        						bt3_strict=true;
	    	        					}
	    	        					//lampo_delta_la.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"BT2",bt2_strict)]) - Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,bt3_haku,false)]));
	    	        				}

		                			//ikkuna.kirjoitaKonsolille("DEBUG " + j + " \n");
		                			//ikkuna.kirjoitaKonsolille("bt2 haku: " + bt2_haku + " " + bt2_strict + " " + tia_kerroin + " \n");
		                			//ikkuna.kirjoitaKonsolille("bt3 haku: " + bt3_haku + " " + bt3_strict + " " + tia_haku + " \n");  
		                			
	    	        				lampo_delta_kv.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt2_haku,bt2_strict)],kerroin) - muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,bt3_haku,bt3_strict)],kerroin));
 	        				
	    	        				//cop käyttövesi
	    	        				cop_kv.add(lastcop=laskeCOP(bt12.get(bt12.size()-1),bt3.get(bt3.size()-1),bt14.get(bt14.size()-1),bt17.get(bt17.size()-1),bt10.get(bt10.size()-1),cop_035, cop_045));
	    	        				
	    	        				
	    	        				//delta keruu tulo idx8 (BT10) - tulo idx9 (BT11), ei jaeta niin ei tarvita doublea
		            				//lampo_delta_keruu.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_ulos_haku,false)]) - Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_sisaan_haku,false)]));
		            				lampo_delta_keruu.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_ulos_haku,false)],kerroin) - muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_sisaan_haku,false)],kerroin));
		            				
	   
		            				
	            					//etsitään matalin ja korkein bt10
	            					//matalin
	            					if (bt10_min > muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_sisaan_haku,false)],kerroin)) {
	            						bt10_min = muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_sisaan_haku,false)],kerroin);
	            					}
	            					//korkein
	            					if (bt10_max < muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_sisaan_haku,false)],kerroin)) {
	            						bt10_max = muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_sisaan_haku,false)],kerroin);
	            					}
	            					//etsitään matalin ja korkein bt11
	            					//matalin
	            					if (bt11_min > muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_ulos_haku,false)],kerroin)) {
	            						bt11_min = muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_ulos_haku,false)],kerroin);
	            					}
	            					//korkein
	            					if (bt11_max < muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_ulos_haku,false)],kerroin)) {
	            						bt11_max = muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,keruu_ulos_haku,false)],kerroin);
	            					}


	            					
	        	        			if (muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,tia_haku,false)],tia_kerroin) > 1) { //onko lisäys päällä
	    		        				//lisälämpö
	    	        					if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345") &&
	    	        							Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"Add.Step",false)]) != 32768){
	    	        						kv_sahko.add(F1345LisaysAskel*Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"Add.Step",false)]));
	    	        					} else {
	    	        						kv_sahko.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,tia_haku,false)],tia_kerroin));
	    	        					}
	    	        				}

	    	        			}
	    	        			if(!kompr_kaynnissa){
	    	        				kompr_kaynnistyksia++;
	    	        				kompr_kaynnissa=true;
	    	        				if (!laske_kaynnistys_jos_kaynnissa && i==0 && j==2) {
	    	        					ikkuna.kirjoitaKonsolille("käynnistystä ei lasketa (" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(logiaika.get(j-2).getTime()) + ")\n");
	    	        					kompr_kaynnistyksia--;
	    	        				}
	    	        			}
	    	        		// Relays PCA-Base 10
	            			} else if (relaysPCAbase.get(relaysPCAbase.size()-1) == 10) {
	    	        			//0b00000111 = 10
	    	        			//vastusaika, Relays PCA-Base on idx20, arvo 10 = tehdään käyttövettä suoralla sähköllä
	    	        			//sähkövoima löytyy idx 15, jakaja on div_add (ei toistaiseksi tulosteta)
	    	        			//korjaus 1.2.15: kaytto_kv tarvitaan koska kyseessä on käyttövesituotanto
	    	        			kaytto_kv++;
	    	        			kompr_kaynnissa=false;
        	        			
        	        			if (muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,tia_haku,false)],tia_kerroin) > 1) { //onko lisäys päällä	
        	        				//lämmitetään suoralla sähköllä
        		        			kv_sahko.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,tia_haku,false)],tia_kerroin));
        	        			}
	    	        			
        	        			

            				// #########################
	    	        		// Relays PCA-Base 2
	    	        		// kiertopumppu päällä
	    	        		// ja tarkistetaan sähkö
	    	        		// #########################
	            			} else if (relaysPCAbase.get(relaysPCAbase.size()-1) == 2) {
        	        			//0b00000100 = 2
        	        			//vastusaika, Relays PCA-Base on idx20, arvo 2 = lämmitetään suoralla sähköllä tai kiertopumppu pyörii
        	        			//sähkövoima löytyy idx 15, jakaja on div_add (ei toistaiseksi tulosteta)
        	        			if (muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,tia_haku,false)],1) > 1) { //onko lisäys päällä	
            	        			//korjaus 1.2.15: kaytto_la tarvitaan koska kyseessä on lämmitysvesituotanto
            	        			kaytto_la++;
        	        				//lämmitetään suoralla sähköllä
            	        			if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345") && ikkuna.getTietolahde()  == 0) {
            	        				lamm_sahko.add(F1345LisaysAskel*Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"Add.Step",false)]));
            	        			} else {
            	        				lamm_sahko.add(muunnaInt(tiedostot.get(i)[j][haeMapista(kentat,tia_haku,false)],tia_kerroin));
        	        				}
	        					} else {
        	        				//tulkitaan lepotilaksi
        		        			kaytto_le++;
        		        			kompr_kaynnissa=false;
        	        			}
        	        		} else {
        	        			//kompuran lepoaika, Relays PCA-Base on idx20, arvo on mitä on
        	        			kaytto_le++;
        	        			kompr_kaynnissa=false;
        	        		}
    	        			
	            			//COP laskenta
                			//int bt12, int bt3, int bt14, int bt17, int bt10, int cop35, int cop45
                			if (kompr_kaynnissa) {
                				cop.add(lastcop=laskeCOP(bt12.get(bt12.size()-1),bt3.get(bt3.size()-1),bt14.get(bt14.size()-1),bt17.get(bt17.size()-1),bt10.get(bt10.size()-1),cop_035, cop_045));
                				//ikkuna.kirjoitaKonsolille(lastcop + "\n");
                			} else {
                				cop.add(0);
                				//cop.add(lastcop);
                				//cop.add((int)(((cop_035+cop_045)/2)*100.0));
                			}
    
                			//TEHO laskenta
                			//int bt12, int bt3, int bt14, int bt17, int bt10, int cop35, int cop45
                			//int bt12/bt2, int bt3, double ominaislampokapasiteetti_vesi, double virtaus
                			if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1x55") ||
                					nibecontroller.equals("S")) {
                				teho.add(laskeTEHO(bt2.get(bt2.size()-1), bt3.get(bt3.size()-1), bf1.get(bf1.size()-1)));
                			} else if (ikkuna.getLblMLPMalli().getText().contains("VILP")) {
                				if (ikkuna.getTietolahde() == 0) {
                					teho.add(laskeTEHO(bt12.get(bt12.size()-1), eb101_bt3.get(eb101_bt3.size()-1), bf1.get(bf1.size()-1)));
                				} else {
                					teho.add(laskeTEHO(bt12.get(bt12.size()-1), bt3.get(bt3.size()-1), bf1.get(bf1.size()-1)));
                				}
                			} else {
                				teho.add(0);
                			}
                			
	            			//***********************************************
	            			//
	            			//
	            			//F1345 EP15
	            			//***********************************************
	            			//LÄMMITYS
	            			if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345") && ikkuna.getTietolahde()  == 0){
	    	        			if(!tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15 Prio",false)].equalsIgnoreCase(ep15_edellinentila)){
	    	        				ep15_prosessinalku=0;
	    	        			}
	    	        			//otetaan EP15 ja PCA prio ylös tallennusta varten
	                			if(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15 Prio",false)].equalsIgnoreCase("1")){
	    	        				ep15_kaytto_la++;
	    	        				ep15_cop_la.add(ep15_lastcop=laskeCOP(ep15_bt12.get(ep15_bt12.size()-1),ep15_bt3.get(ep15_bt3.size()-1),ep15_bt14.get(ep15_bt14.size()-1),ep15_bt17.get(ep15_bt17.size()-1),ep15_bt10.get(ep15_bt10.size()-1),cop_035, cop_045));
	        	        			if(!ep15_kompr_kaynnissa){
	        	        				ep15_kompr_kaynnistyksia++;
	        	        				ep15_kompr_kaynnissa=true;
	        	        				if (!laske_kaynnistys_jos_kaynnissa && i==0 && j==2) {
	        	        					ikkuna.kirjoitaKonsolille("käynnistystä ei lasketa (" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(logiaika.get(j-2).getTime()) + ")\n");
	        	        					ep15_kompr_kaynnistyksia--;
	        	        				}
	        	        			}
	    		        			if(ep15_prosessinalku>=120){
	    			        			//F1345
	    	        					//delta lämmitysvesi meno (BT12) - tulo (BT3), ei jaeta niin ei tarvita doublea
	    	        					ep15_lampo_delta_la.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT12",false)]) - Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT3",false)]));
	    	        					//delta keruu tulo (BT10) - tulo (BT11)
	    	        					ep15_lampo_delta_keruu.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT11",false)]) - Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT10",false)]));
	    	        					//etsitään matalin ja korkein ep15-bt10
	    	        					//matalin
	    	        					if (ep15_bt10_min > Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT10",false)])) {
	    	        						ep15_bt10_min = Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT10",false)]);
	    	        					}
	    	        					//korkein
	    	        					if (ep15_bt10_max < Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT10",false)])) {
	    	        						ep15_bt10_max = Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT10",false)]);
	    	        					}
	    	        					//etsitään matalin ja korkein ep15-bt11
	    	        					//matalin
	    	        					if (ep15_bt11_min > Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT11",false)])) {
	    	        						ep15_bt11_min = Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT11",false)]);
	    	        					}
	    	        					//korkein
	    	        					if (ep15_bt11_max < Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT11",false)])) {
	    	        						ep15_bt11_max = Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT11",false)]);
	    	        					}
	    		        			}
	    		        			
	    		        		//KÄYTTÖVESI
	    	        			} else if (tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15 Prio",false)].equalsIgnoreCase("2")) {
	    	        				ep15_kaytto_kv++;
	    	        				ep15_cop_kv.add(ep15_lastcop=laskeCOP(ep15_bt12.get(ep15_bt12.size()-1),ep15_bt3.get(ep15_bt3.size()-1),ep15_bt14.get(ep15_bt14.size()-1),ep15_bt17.get(ep15_bt17.size()-1),ep15_bt10.get(ep15_bt10.size()-1),cop_035, cop_045));
	    		        			if(!ep15_kompr_kaynnissa){
	    		        				ep15_kompr_kaynnistyksia++;
	    		        				ep15_kompr_kaynnissa=true;
	    		        			}
		        					//delta lämmitysvesi meno (BT12) - tulo (BT3), ei jaeta niin ei tarvita doublea
		        					ep15_lampo_delta_la.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT12",false)]) - Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT3",false)]));
		        					//delta keruu tulo (BT10) - tulo (BT11)
		        					ep15_lampo_delta_keruu.add(Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT11",false)]) - Integer.parseInt(tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15-BT10",false)]));
	    	        			} else {
	    	        				//LEPO
	    	        				ep15_kaytto_le++;
	    	        				ep15_kompr_kaynnissa=false;
	    	        			}
	            			} else {
	            				ep15_prio.add(0);
	            			}
	            			

	            			//COP laskenta EP15
	            			//int bt12, int bt3, int bt14, int bt17, int bt10, int cop35, int cop45
	            			if (ep15_kompr_kaynnissa) {
	            				ep15_cop.add(ep15_lastcop=laskeCOP(ep15_bt12.get(ep15_bt12.size()-1),ep15_bt3.get(ep15_bt3.size()-1),ep15_bt14.get(ep15_bt14.size()-1),ep15_bt17.get(ep15_bt17.size()-1),ep15_bt10.get(ep15_bt10.size()-1),cop_035, cop_045));
	            				//ikkuna.kirjoitaKonsolille(ep15_lastcop + "\n");
	            			} else {
	            				ep15_cop.add(0);
	            				//ep15_cop.add(ep15_lastcop);
	            				//cop.add((int)(((cop_035+cop_045)/2)*100.0));
	            			}
	            			
	            			//prosessin alkutarkistuksen sekuntimäärä
	            			prosessinalku=prosessinalku+(int)(mittausvali_ms/1000);
	            			if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345") && ikkuna.getTietolahde()  == 0){
	            				//ep15 prosessinalku
	            				ep15_prosessinalku=ep15_prosessinalku+(int)(mittausvali_ms/1000);
	            				//otetaan edellinen EP15 tila muistiin
	            				ep15_edellinentila = tiedostot.get(i)[j][haeMapista(kentat,"EB100-EP15 Prio",false)];
	            			}
	            			
	            			//laskentaa varten
	    	        		if(edellinenriviaika.getTimeInMillis()==0){
	    	        			//eka rivi
	    	        			edellinenriviaika.set(vuosi, kuukausi, paiva, tunti, minuutti, sekunti);
	    	        		} else {
	    	        			if (mittausvali_ms == 0) {
	    	        				mittausvali_ms = logiaika.get(logiaika.size()-1).getTimeInMillis() - logiaika.get(0).getTimeInMillis();
	    	        				LogintutkijaGUI.getLblMittausvaliKentta().setText(parseMittausvali((Math.round((double)mittausvali_ms/1000*10.0)/10.0)*1000));
	    	        			}
	    	        			if(Math.abs(((logiaika.get(logiaika.size()-1).getTimeInMillis()) - (edellinenriviaika.getTimeInMillis())) - mittausvali_ms) > 10000 && ikkuna.isJatkuvatlogit()){
	    	        				//todennäköisesti ei peräkkäinen tiedosto, progress 100:n ja lopetus
	    		        			ikkuna.kirjoitaKonsolille("Ei peräkkäinen logirivi " + tiedostot.get(i)[j][0] + " " + tiedostot.get(i)[j][1] + " (" + (((logiaika.get(logiaika.size()-1).getTimeInMillis()) - (edellinenriviaika.getTimeInMillis())) - mittausvali_ms)/1000 + ") löydetty. Laita samaan hakemistoon vain peräkkäisiä logeja.\nTai ota \"jatkuvat logit\" -valinta pois.\n");
	    		        			//yhteenvetoikkunan numerot
	    		        			ikkuna.paivitaNumerot(kaytto_la, kaytto_kv, kaytto_le, kv_sahko, mittausvali_ms,
	    		    						kokaika, kompr_kaynnistyksia, bt50, bt1, cs,
	    		    						lampo_kv, lampo_delta_la, lampo_delta_kv, lampo_delta_keruu, bt67_label, lamm_sahko,
	    		    						bt10_min, bt10_max, bt11_min, bt11_max, cop_la, cop_kv, cfa, kaytto_ua, kaytto_la_flm,
	    		    						kaytto_kv_flm, alarm);
	    		        			ikkuna.setProgress(100);
	    		        			//poistetaan viimeinen aika jottei tule hassuja graafeja
	    		        			logiaika.remove(logiaika.size()-1);
	    		            		//Käyrien nayttöä varten
	    		            		teeKayraTaulukko(); 
	    		            		if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345")) {
	    		            			ikkuna.getRadioNappulaEP14().setEnabled(true);
	    		            			ikkuna.getRadioNappulaEP15().setEnabled(true);
	    		                    } else {
	    		                    	ikkuna.getRadioNappulaEP14().setEnabled(false);
	    		                    	ikkuna.getRadioNappulaEP15().setEnabled(false);
	    		                    }
	    		        			return mlpfound + " " + logiaika.size() + " tietueella.\n";
	    		        		}
	    	        			edellinenriviaika.set(vuosi, kuukausi, paiva, tunti, minuutti, sekunti);
	    	        		}
	    	        		
	                    	if(kokaika==2){
	                    		//mittausväli tarvitaan laskemiseen sekä jatkuvien logien tarkistukseen
	                    		//ikkuna.kirjoitaKonsolille("lokiaika: " + (edellinenriviaika.getTimeInMillis() - logiaika.get(0).getTimeInMillis()) + "\n");
	                    		mittausvali_ms = logiaika.get(1).getTimeInMillis() - logiaika.get(0).getTimeInMillis();
	                    	}
	            			//ota tila ylös muutoksen huomioimiseksi
	            			edellinentila = relaysPCAbase.get(relaysPCAbase.size()-1);
	            		}//datarivi (ei header) loppu
            			//ikkuna.kirjoitaKonsolille("koepalaY: " + j + "\n");    
	        			//ikkuna.kirjoitaKonsolille(tiedostot.get(i)[j][haeMapista(kentat,"Relays PCA-Base",false)] + "\n");
	        		}// Rivin loppu
			        ikkuna.setProgress((i+1)*100/tiedostot.size());
			        //kirjoitetaan lopetusaika
			        LogintutkijaGUI.setAikaLopetus("  <->  " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(logiaika.get(logiaika.size()-1).getTime()));
	            } //Divisors Date (Nibe) ehto loppu   			

            } //tiedoston loppu		

    		//VILP labelit
    		if (ikkuna.getLblMLPMalli().getText().contains("VILP") || ikkuna.getLblMLPMalli().getText().contains("PILP")) {
    			ikkuna.getLblLammonKeruu().setText(" ∆ lämmönkeruuilma");
    			ikkuna.getLblSuos5().setVisible(false);
    		}
    		
    		//yhteenvetoikkunan numerot
    		ikkuna.paivitaNumerot(kaytto_la, kaytto_kv, kaytto_le, kv_sahko, mittausvali_ms,
    				kokaika, kompr_kaynnistyksia, bt50, bt1, cs,
    				lampo_kv, lampo_delta_la, lampo_delta_kv, lampo_delta_keruu, bt67_label,
    				lamm_sahko, bt10_min, bt10_max, bt11_min, bt11_max, cop_la, cop_kv, cfa, kaytto_ua,
    				kaytto_la_flm, kaytto_kv_flm, alarm);

    		//Käyrien näyttöä varten
    		teeKayraTaulukko();
    		
    		if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345")) {
    			ikkuna.getRadioNappulaEP14().setEnabled(true);
    			ikkuna.getRadioNappulaEP15().setEnabled(true);
            } else {
            	ikkuna.getRadioNappulaEP14().setEnabled(false);
            	ikkuna.getRadioNappulaEP15().setEnabled(false);
            }

    		ikkuna.getRadioNappulaEP14().addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                	if (!ikkuna.getRadioNappulaEP14().isSelected()){
                		//ikkuna.kirjoitaKonsolille("EP15\n");
                		ikkuna.paivitaNumerot(ep15_kaytto_la, ep15_kaytto_kv, ep15_kaytto_le, kv_sahko, mittausvali_ms,
        						kokaika, ep15_kompr_kaynnistyksia, bt50, bt1, cs,
        						lampo_kv, ep15_lampo_delta_la, ep15_lampo_delta_kv, ep15_lampo_delta_keruu, bt67_label,
        						lamm_sahko, ep15_bt10_min, ep15_bt10_max, ep15_bt11_min, ep15_bt11_max, ep15_cop_la, ep15_cop_kv, cfa, kaytto_ua,
        						kaytto_la_flm, kaytto_kv_flm, alarm);
                		//ei näytetä lisäystä jos F1345
                		ikkuna.setSal(0);
                		ikkuna.setSav(0);
                		ikkuna.setUal(0);
                		ikkuna.setLaFlm(0);
                		ikkuna.setKvFlm(0);
                		ikkuna.naytaGraafi(ikkuna.getLa(), ikkuna.getSal(), ikkuna.getUal(), ikkuna.getLaFlm(), ikkuna.getKvFlm(), ikkuna.getKv(), ikkuna.getSav(), ikkuna.getLe());
                	}
                	else if (ikkuna.getRadioNappulaEP14().isSelected()){
                		//ikkuna.kirjoitaKonsolille("EP14\n");
                		ikkuna.paivitaNumerot(kaytto_la, kaytto_kv, kaytto_le, kv_sahko, mittausvali_ms,
        						kokaika, kompr_kaynnistyksia, bt50, bt1, cs,
        						lampo_kv, lampo_delta_la, lampo_delta_kv, lampo_delta_keruu, bt67_label,
        						lamm_sahko, bt10_min, bt10_max, bt11_min, bt11_max, cop_la, cop_kv, cfa,
        						kaytto_ua, kaytto_la_flm, kaytto_kv_flm, alarm);
                		ikkuna.naytaGraafi(ikkuna.getLa(), ikkuna.getSal(), ikkuna.getUal(), ikkuna.getLaFlm(), ikkuna.getKvFlm(), ikkuna.getKv(), ikkuna.getSav(), ikkuna.getLe());
                	}
                }
            }); //itemlistener loppu
	    	return mlpfound + " " + logiaika.size() + " tietueella. Login alku: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(logiaika.get(0).getTime()) + ", loppu: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(logiaika.get(logiaika.size()-1).getTime()) + ".\n";
    	} //analysoi2 loppu

        // laskeTEHO
    	// lasketaan TEHO
        //        
        public int laskeTEHO(int bt2, int bt3, int bf1) {
        	//P=m*c*dt, jossa:
			//P= teho (kW)
			//m= massavirta (kg/s), Nibeltä saadaan litraa minuutissa eli jaettava 60sek sekä kerrottava 1l veden massalla 0,997kg
			//c= lämmönsiirtonesteen ominaislämpökapasiteeti kJ/(kgC) = vesi = 4.182
			//dt= tulevan ja lähtevän nesteen lämpötilaero (oC) = bt2 -bt3
        	return (int)((bf1 * 0.997/60) * 4.182 * (bt2 - bt3));
        }
        
        // laskeCOP
    	// lasketaan COP
        //        
        public int laskeCOP(int bt12, int bt3, int bt14, int bt17, int bt10, double cop35, double cop45) {
        	//tarvitaan:
        	//COP EN14511
        	//ΔTLM lämmönjaon erotus testiolosuhteissa = 5C
        	//ΔTLS lämmönjaon erotus mitoitusolosuhteissa = BT2/BT12 - BT3
        	//THJ menovesi, BT2 tai BT12
        	//ΔTL kylmäaineen ja veden lt ero lauhdutin = BT14 - BT2
        	//TLÄH keruu paluu = BT10
        	//ΔTH  kylmäaineen ja veden lt ero höyrystin = BT17 - BT10
        	//interpoloidaan lineaarisesti kahden tunnetun toimintapisteen avulla ja vasta sitten lämpötilakorjaus
        	double cop = lerp(cop35,cop45,(((45.0-35.0)*(bt12/10.0-35.0))/100));
        	double copt = 100 * cop *
        			(1 -
        					(
        							((5.0 -((double)bt12 - (double)bt3))/2)
        							/
        							(
        									(bt12 -														//THJ
        		        					(5.0/2) +													//ΔTLM
        		        					Math.abs(((double)bt14-(double)bt12))) -					//ΔTL
        		        					(
        		        							bt10 -												//TLÄH
        		        							Math.abs(
        		        									((double)bt17 - (double)bt10)				//ΔTH
        		        									)
        		        					)
        							)
        		        	)
        		      );
        	//ikkuna.kirjoitaKonsolille("copt35: " + cop35 + " cop45: " + cop45 + "\n");
        	//palautetaan 100 kertainen luku ja jaetaan vasta myöhemmin
        	return (int)copt;
        }
        
        //lerp
    	//lineaarinen interpolaatio kahden COP arvon välistä ja ulkopuolelta
        //
        double lerp(double v0, double v1, double t) {
        	  return (1-t)*v0 + t*v1;
        }
        
        // teeKayraTaulukko
    	// arvosarjat taukukkoon
        //
        public void teeKayraTaulukko(){
    		kayra_taulukko.add(0,bt50);
    		kayra_taulukko_nimet.add(0,"sisält bt50");
			kayra_taulukko.add(1,bt1);
			kayra_taulukko_nimet.add(1,"ulkolt bt1");
			
			//bt2
			if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345") ||
					bt2_nolla) {
				kayra_taulukko.add(2,bt12);
				kayra_taulukko_nimet.add(2,"meno bt12");
			} else if (ikkuna.getLblMLPMalli().getText().contains("VILP") && ikkuna.getTietolahde()  == 0) { //VILP
				if (ikkuna.getLblMLPMalli().getText().contains("VMM")) { //VMMllä
					kayra_taulukko.add(2,bt63);
					kayra_taulukko_nimet.add(2,"meno bt63");
				} else { //ilman VMMää
					kayra_taulukko.add(2,bt12);
					kayra_taulukko_nimet.add(2,"meno bt12");
				}
			} else {
				if (ikkuna.getLblMLPMalli().getText().contains("VILP")) {
					if (ikkuna.getLblMLPMalli().getText().contains("VMM")) { //VMMllä
						kayra_taulukko.add(2,bt63);
						kayra_taulukko_nimet.add(2,"meno bt63");
					} else { //ilman VMMää
						kayra_taulukko.add(2,bt12);
						kayra_taulukko_nimet.add(2,"meno bt12");
					}
				} else {
					kayra_taulukko.add(2,bt2);
					kayra_taulukko_nimet.add(2,"meno bt2");
				}
			}
			
			//bt3
			if ( ikkuna.getLblMLPMalli().getText().contains("VILP") &&  ikkuna.getTietolahde()  == 0) {
				kayra_taulukko.add(3,eb101_bt3);
				kayra_taulukko_nimet.add(3,"tulo eb101-bt3");
			} else {
				kayra_taulukko.add(3,bt3);
				if (ikkuna.getLblMLPMalli().getText().contains("VILP")) {
					kayra_taulukko_nimet.add(3,"tulo eb101-bt3");
				} else {
					kayra_taulukko_nimet.add(3,"tulo bt3");
				}
			}

			kayra_taulukko.add(4,bt25);
			kayra_taulukko_nimet.add(4,"meno bt25");
			
			//bt10 - sisään
			if ( ikkuna.getLblMLPMalli().getText().contains("VILP") &&  ikkuna.getTietolahde()  == 0) {
				kayra_taulukko.add(5,bt28);
				kayra_taulukko_nimet.add(5,"keruu t. bt28");
			} else {
				kayra_taulukko.add(5,bt10);
				if (ikkuna.getLblMLPMalli().getText().contains("VILP")) {
					kayra_taulukko_nimet.add(5,"keruu t. bt28");
				} else {
					kayra_taulukko_nimet.add(5,"keruu t. bt10");
				}
			}

			//bt11 - ulos
			if ( ikkuna.getLblMLPMalli().getText().contains("VILP") &&  ikkuna.getTietolahde()  == 0) {
				kayra_taulukko.add(6,bt16);
				kayra_taulukko_nimet.add(6,"keruu m. bt16");
			} else {
				kayra_taulukko.add(6,bt11);
				if (ikkuna.getLblMLPMalli().getText().contains("VILP")) {
					kayra_taulukko_nimet.add(6,"keruu m. bt16");
				} else {
					kayra_taulukko_nimet.add(6,"keruu m. bt11");
				}
				
			}

			kayra_taulukko.add(7,bt12);
			kayra_taulukko_nimet.add(7,"lauh. meno bt12");
			kayra_taulukko.add(8,bt14);
			kayra_taulukko_nimet.add(8,"kuumakaasu bt14");
			kayra_taulukko.add(9,bt17);
			kayra_taulukko_nimet.add(9,"imukaasu bt17");
			kayra_taulukko.add(10,dm);
			kayra_taulukko_nimet.add(10,"asteminuutit");  
			kayra_taulukko.add(11,cs);
			kayra_taulukko_nimet.add(11,"pyynti");
			//deltat
			kayra_taulukko.add(12,lampo_delta_la);
			kayra_taulukko_nimet.add(12,"∆ l.jako");
			kayra_taulukko.add(13,lampo_delta_keruu);
			kayra_taulukko_nimet.add(13,"∆ keruu");
    		//COP
    		kayra_taulukko.add(14,cop);
    		kayra_taulukko_nimet.add(14,"COP*10");
    		//EP15
    		kayra_taulukko.add(15,ep15_bt3);
    		kayra_taulukko_nimet.add(15,"tulo ep15-bt3");
    		kayra_taulukko.add(16,ep15_bt10);
    		kayra_taulukko_nimet.add(16,"keruu t. ep15-bt10");
    		kayra_taulukko.add(17,ep15_bt11);
    		kayra_taulukko_nimet.add(17,"keruu m. ep15-bt11");
    		kayra_taulukko.add(18,ep15_bt12);
    		kayra_taulukko_nimet.add(18,"lauh. meno ep15-bt12");
    		kayra_taulukko.add(19,ep15_bt14);
    		kayra_taulukko_nimet.add(19,"kuumakaasu ep15-bt14");
    		kayra_taulukko.add(20,ep15_bt17);
    		kayra_taulukko_nimet.add(20,"imukaasu ep15-bt17");
    		//deltat EP15
    		kayra_taulukko.add(21,ep15_lampo_delta_la);
    		kayra_taulukko_nimet.add(21,"ep15-∆ l.jako");
    		kayra_taulukko.add(22,lampo_delta_keruu);
    		kayra_taulukko_nimet.add(22,"ep15-∆ keruu");
    		//COP
    		kayra_taulukko.add(23,ep15_cop);
    		kayra_taulukko_nimet.add(23,"ep15-COP*10");
    		//käyntitilat tallennusta varten
    		kayra_taulukko.add(24,relaysPCAbase);
    		kayra_taulukko_nimet.add(24,"RelaysPCABase");
    		kayra_taulukko.add(25,ep15_prio);
    		kayra_taulukko_nimet.add(25,"ep15-prio");
    		//bt7
    		kayra_taulukko.add(26,lampo_kv);
    		kayra_taulukko_nimet.add(26,"kv ylä bt7");
    		//bt6
    		kayra_taulukko.add(27,bt6);
    		kayra_taulukko_nimet.add(27,"kv ala bt6");
    		//tia/add.step
    		if (F1345LisaysAskel == 0 && ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345")) {
	    		kayra_taulukko.add(28,add_step);
	    		kayra_taulukko_nimet.add(28,"Lisäysporras*10");
			} else {
	    		kayra_taulukko.add(28,kw_sahko);
	    		if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345")) {
	    			kayra_taulukko_nimet.add(28,"lisäteho kW");
	    		} else {
	    			kayra_taulukko_nimet.add(28,"lisäteho kW*10");
	    		}
			}
    		kayra_taulukko.add(29,bf1);
    		kayra_taulukko_nimet.add(29,"virtaus l/m bf1");
    		kayra_taulukko.add(30,cfa);
    		kayra_taulukko_nimet.add(30,"kompr. taajuus (on/off=50)");
    		kayra_taulukko.add(31,teho);
    		kayra_taulukko_nimet.add(31,"teho kW");
    		kayra_taulukko.add(32,gp1);
    		kayra_taulukko_nimet.add(32,"l-jako nopeus EP14");
    		kayra_taulukko.add(33,gp2);
    		kayra_taulukko_nimet.add(33,"keruu nopeus EP14");
    		kayra_taulukko.add(34,ep15_gp1);
    		kayra_taulukko_nimet.add(34,"l-jako nopeus EP15");
    		kayra_taulukko.add(35,bt51);
    		kayra_taulukko_nimet.add(35,"uima-allas bt51");
    		kayra_taulukko.add(36,bt53);
    		kayra_taulukko_nimet.add(36,"aurinkokeräin bt53");
    		kayra_taulukko.add(37,bt54);
    		kayra_taulukko_nimet.add(37,"aurinkokierukka bt54");
    		kayra_taulukko.add(38,prio);
    		kayra_taulukko_nimet.add(38,"prioriteetti");
    		kayra_taulukko.add(39,bt20);
    		kayra_taulukko_nimet.add(39,"poistoilma bt20");
    		kayra_taulukko.add(40,bt21);
    		kayra_taulukko_nimet.add(40,"jäteilma bt21");
    		kayra_taulukko.add(41,bt71);
    		kayra_taulukko_nimet.add(41,"paluu bt71");
    		kayra_taulukko.add(42,ep15_gp2);
    		kayra_taulukko_nimet.add(42,"keruu nopeus EP15");
    		//virtamuuntajat
    		kayra_taulukko.add(43,be1);
    		kayra_taulukko_nimet.add(43,"L1 A be1");
    		kayra_taulukko.add(44,be2);
    		kayra_taulukko_nimet.add(44,"L2 A be2");
    		kayra_taulukko.add(45,be3);
    		kayra_taulukko_nimet.add(45,"L3 A be3");
    		//halytys
    		kayra_taulukko.add(46,alarm);
    		kayra_taulukko_nimet.add(46,"Hälytys");
        }
           	
    	// muunnaCTC
    	// mäpätään CTC:n arvot Niben vastaaviin analyysiä varten
        //
    	public String[][] muunnaCTC(String[][] logtable) {
    		//NIBE
    		//Date
    		//Time
    		//version(43001)
    		//R-version(44331)
    		//BT1(40004)
    		//EB100-EP14-BT3(40012)
    		//BT6(40014)
    		//BT7(40013)
    		//EB100-EP14-BT10(40015)
    		//EB100-EP14-BT11(40016)
    		//EB100-EP14-BT12(40017)
    		//EB100-EP14-BT14(40018)
    		//EB100-EP14-BT17(40022) 
    		//EB100-EP15-BT3(40011)
    		//EB100-EP15-BT10(40100)
    		//EB100-EP15-BT11(40085)
    		//EB100-EP15-BT12(40086)
    		//EB100-EP15-BT14(40087)
    		//EB100-EP15-BT17(40089)
    		//BT25(40071)
    		//BT50(40033)
    		//EB100-EP14-BP8(40124)
    		//EB100-EP15-BP8(40123)
    		//Add.Step(43091)			*
    		//Alarm number(45001)
    		//Calc. Supply(43009)
    		//Degree Minutes(43005)
    		//BT1-Average(40067)
    		//Relays PCA-Base EP14(43514)
    		//Relays PCA-Base EP15(43513)
    		//GP1-speed EP14(43437)
    		//GP1-speed EP15(43436)
    		//GP2-speed EP14(43439)
    		//Prio(43086)
    		//EB100-EP15 Prio(44242)
    		//EB100-EP14 Prio(44243)
    		//

    		//CTC CSV		
			//////////	
			//Datum Tid			idx0	
			//Workmode			idx1	PCA
			//Tank Ã¶vre		idx2	BT7
			//Tank mitten		idx3	
			//Tank Nedre		idx4	BT6
			//Rumstemp 1		idx5	BT50
			//Rumstemp 2		idx6	
			//Framledningstemp	idx7	BT25 ja BT2 ja BT12
			//Returtemp			idx8	BT3
			//Radiatorpump 1	idx9	GP1
			//Eleffekt			idx10	Add.Step
			//Ström L1			idx11	
			//Ström L2			idx12	
			//Ström L3			idx13	
			//HP alarmregister	idx14	Alarm number
			//HP alarmregister 2idx15	
			//HP alarmregister 3idx16	
			//HP alarmregister 4idx17	
			//Alarmregister tankidx18	
			//Brinetemp in		idx19	BT10
			//Brinetemp ut		idx20	BT11
			//Laddpump %		idx21	
			//Brinepump			idx22	GP2
			//Laddkrets in		idx23	latauspiiri
			//Laddkrets Ut		idx24	latauspiiri
			//Hetgastemp		idx25	BT14
			//Suggastemp		idx26	BT17
			//Överhettning	idx27
			//HetgasÃ¶verhettninidx28
			//Expensionsventil%	idx29
			//LÃ¥gtryck			idx30	BP8?
			//HÃ¶gtryck			idx31	BP8
			//Mjukstart strÃ¶m	idx32	
			//DriftlÃ€ge		idx33	PCA workmoden kanssa


    		ArrayList<String []> file = new ArrayList<String []>();
    		//String[][] file = 
    		ikkuna.kirjoitaKonsolille("koko: " + logtable.length + "\n");
    		//ikkuna.kirjoitaKonsolille("koko toinen: " + logtable.[0].length + "\n");
    		//Alkujutut jotta menee läpi
    		String [] eka = new String [23];
    		eka[0]="Divisors";
    		for (int i=1;i<23;i++){
    			eka[i]="10";
    		}
    		file.add(eka);
    		String [] toka = new String [23];
    		toka[0]="Date";toka[1]="CTC";toka[2]="BT2";toka[3]="BT6";toka[4]="R-version";toka[5]="GP1-speed EP14";toka[6]="BT7";
    		for (int i=7;i<23;i++){
    			toka[i]="F" + i;
    		}
    		file.add(toka);
			String mmb = "";
    		for (int i=0;i<logtable.length;i++){
    			//Niben logissa ainakin 23 elementtiä
        		String [] line = new String[23];
    			String rs = logtable[i][0].trim();
    			String prio = "0";
        		if (rs.equals("")) continue;
    			String yyyy = rs.substring(0,4);
    			String mo = rs.substring(4,6);
    			String dd = rs.substring(6,8);
    			String hh = rs.substring(9,11);
    			String mm = rs.substring(12,14);
    			String ss = "00";
    			
        		//ikkuna.kirjoitaKonsolille("PVM: " + " " + yyyy + "-" + mo + "-" + dd + " " + hh + ":" + mm + "\n" +
        			//	"TIME: " + logtable[i][0] + " len.: " + logtable[i][0].length() +  "\n" +
        				//"");
        		//pvm
        		line[0] = yyyy + "-" + mo + "-" + dd;
        		//klo
        		//ikkuna.kirjoitaKonsolille("Kierros: " + i + "MM: " + mm + " MMB: " + mmb + "\n");
        		if (!mmb.equals(mm)) {
        			ss="00";
        			mmb = mm;
        		} else {
        			ss="30";
        			mmb = mm;
        		}
        		line[1] = hh + ":" + mm + ":" + ss;
        		//version ja Release
        		line[2] = "0000";line[3] = "0";
        		//BT1 ulkolt
        		line[4] = "0";
        		//BT2
//        		line[5] = "" + (int)(Double.parseDouble(logtable.get(i)[7])*10);
//        		//BT3
//        		line[6] = "" + (int)(Double.parseDouble(logtable.get(i)[8])*10);
//        		//BT6
//        		line[7] = "" + (int)(Double.parseDouble(logtable.get(i)[4])*10);
//        		//BT7
//        		line[8] = "" + (int)(Double.parseDouble(logtable.get(i)[2])*10);
//        		//BT10
//        		line[9] = "" + (int)(Double.parseDouble(logtable.get(i)[19])*10);
//        		//BT11
//        		//ikkuna.kirjoitaKonsolille("BT11: " + (int)(Double.parseDouble(logtable.get(i)[20])*10) + "\n");
//        		line[10] = "" + (int)(Double.parseDouble(logtable.get(i)[20])*10);
//        		//BT12
//        		line[11] = "" + (int)(Double.parseDouble(logtable.get(i)[7])*10);
//        		//BT14
//        		line[12] = "" + (int)(Double.parseDouble(logtable.get(i)[25])*10);
//        		//BT17
//        		line[13] = "" + (int)(Double.parseDouble(logtable.get(i)[26])*10);
//        		//Add.step
//        		line[14] = "" + (int)(Double.parseDouble(logtable.get(i)[10])*100);
//        		//Alarm
//        		line[15] = logtable.get(i)[14];
//        		//Calc. supply
//        		line[16] = "0";
//        		//Degree mins
//        		line[17] = "0";
//        		//BT1 avg
//        		line[18] = "0";
//        		//PCA
//
//        		if (logtable.get(i)[33].equals("ON")) {
//        			if (logtable.get(i)[1].equals("1")) {
//            			line[19] = "7";
//            			prio="1";
//        			} else {
//        				line[19] = "15";
//        				prio="2";
//        			}
//        		} else {
//        			line[19] = "0";
//        		}
//        		//GP1 lämmönjako
//        		line[20] = "" + (int)(Double.parseDouble(logtable.get(i)[21])*1);
//        		//GP2 brine
//        		line[21] = "" + (int)(Double.parseDouble(logtable.get(i)[22])*10);
        		//Prio
        		line[22] = prio;
        		
        		file.add(line);
    		}
    		return logtable;
    	}
        
        // otsikotCTC
    	// mäpätään CTC:n arvot Niben vastaaviin analyysiä varten
        // luetaan asetustiedostosta kenttien indeksit
    	public String[] otsikotCTC(String sample, char erotin) {
    		//String[] sarakeotsikot2 = sample.split(String.valueOf(erotin));
    		String[] indexit = ctcmappi.split(",");
    		//ikkuna.kirjoitaKonsolille("CTC:ssä kenttiä: " + indexit.length + "\n");
    		String[] sarakeotsikot = new String[sample.split(String.valueOf(erotin)).length];
    		//tehdään otsikkorivi
    		for (int i=0;i<sarakeotsikot.length;i++) {
    			sarakeotsikot[i]="FIELD" + i;
    		}
    		//malli,
    		// DATETIME 0,COMPRESSOR_RUNNING 68,HEATING 1,HOTWATER 7,tot.int.add 10,
    		// BT7 11, BT6 14, BT1 16, BT12 18, calc.supply 19,
    		// DM 22, BT10 50, BT11 51, CFA 53, BT3 55,
    		// BT2 56, BT14 57, BT17 58
    		sarakeotsikot[Integer.parseInt(indexit[1])]="DATETIME";
    		sarakeotsikot[Integer.parseInt(indexit[2])]="COMPRESSOR_RUNNING"; // PCA 1
    		sarakeotsikot[Integer.parseInt(indexit[3])]="HEATING"; //PCA + 6
    		sarakeotsikot[Integer.parseInt(indexit[4])]="HOTWATER"; //PCA + 14
    		sarakeotsikot[Integer.parseInt(indexit[5])]="Tot.Int.Add";
    		
    		sarakeotsikot[Integer.parseInt(indexit[6])]="BT7";
    		sarakeotsikot[Integer.parseInt(indexit[7])]="BT6";
    		sarakeotsikot[Integer.parseInt(indexit[8])]="BT1";
    		sarakeotsikot[Integer.parseInt(indexit[9])]="BT12";
    		sarakeotsikot[Integer.parseInt(indexit[10])]="Calc. Supply";
    		
    		sarakeotsikot[Integer.parseInt(indexit[11])]="BT50";
    		sarakeotsikot[Integer.parseInt(indexit[12])]="Degree Minutes";
    		sarakeotsikot[Integer.parseInt(indexit[13])]="EP14-BT10";
    		sarakeotsikot[Integer.parseInt(indexit[14])]="EP14-BT11";
    		sarakeotsikot[Integer.parseInt(indexit[15])]="compr. freq. act.";
    		
    		sarakeotsikot[Integer.parseInt(indexit[16])]="BT3"; 		
    		sarakeotsikot[Integer.parseInt(indexit[17])]="BT2";
    		sarakeotsikot[Integer.parseInt(indexit[18])]="BT14";
    		sarakeotsikot[Integer.parseInt(indexit[19])]="BT17";
    		
    		return sarakeotsikot;
    	}
    	
        // tabulaattoriJakaja
    	// 2-tasoinen lista helpottamaan eri mallien logien lukemista
    	public String[][] tabulaattoriJakaja(String logitiedosto, char erotin, String type) throws IOException{
    		List<List<String>> sarakelista = new ArrayList<List<String>>();
    		BufferedReader br = new BufferedReader(new FileReader(logitiedosto));
    		String rivi = br.readLine();   		
    		if (type.equals("ctc")) {
        		//ctc kiinalaiset pois
        		rivi = rivi.replaceAll("[^\\x20-\\x7e]", "");
    			//ei vielä muita CTC malleja
    			String[] sarakeotsikot_ctc = otsikotCTC(rivi,erotin);
	    		//ikkuna.kirjoitaKonsolille("SARAKKEET: " + sarakeotsikot_ctc.length + "\n");
	    		for(String sarakeotsikko: sarakeotsikot_ctc) {
	    		    List<String> aliLista = new ArrayList<String>();
	    		    aliLista.add(sarakeotsikko);
	    		    sarakelista.add(aliLista);
	    		}
	    		while((rivi = br.readLine()) != null) {
	    			rivi = rivi.replaceAll("[^\\x20-\\x7e]", "");
	    			if (rivi.length() == 0) { continue; }
	    			//ikkuna.kirjoitaKonsolille(" uusi rivi " + rivi.length() + " ------------------------\n");
	    		    String[] elementit = rivi.split(String.valueOf(erotin));
	    		    for(int i = 0; i < elementit.length; i++) {
	    		    	sarakelista.get(i).add(elementit[i]);
	    		    }
	    		    if (sarakeotsikot_ctc.length > elementit.length) {
	    		    	int missing = sarakeotsikot_ctc.length - elementit.length;
	    		    	for (int m=0; m<missing; m++) {
	    		    		sarakelista.get(elementit.length + m).add("NULL");
	    		    	}
	    		    }
	    		}
    		} else {
	    		String[] sarakeotsikot = rivi.split(String.valueOf(erotin));
	    		//ikkuna.kirjoitaKonsolille("SARAKKEET: " + sarakeotsikot.length + "\n");
	    		for(String sarakeotsikko: sarakeotsikot) {
	    		    List<String> aliLista = new ArrayList<String>();
	    		    aliLista.add(sarakeotsikko);
	    		    sarakelista.add(aliLista);
	    		}
	    		//int g=1;
	    		while((rivi = br.readLine()) != null) {
	    			//g++;
	    			//ikkuna.kirjoitaKonsolille(" uusi rivi  ------------------------\n");
	    		    String[] elementit = rivi.split(String.valueOf(erotin));
	    		    //ikkuna.kirjoitaKonsolille("ELEMENTTEJÄ: " + elementit.length + "\n");
	    		    for(int i = 0; i < elementit.length; i++) {
	    		    	sarakelista.get(i).add(elementit[i]);
	    		    }
	    		    if (sarakeotsikot.length > elementit.length) {
	    		    	int missing = sarakeotsikot.length - elementit.length;
	    		    	for (int m=0; m<missing; m++) {
	    		    		sarakelista.get(elementit.length + m).add("NULL");
	    		    	}
	    		    }
	    		}
    		}
    		br.close();

    		int sarakkeet = sarakelista.size();
    		int rivit = sarakelista.get(0).size();
    		String[][] taulukko2D = new String[rivit][sarakkeet];
    		for (int arvorivi = 0; arvorivi < rivit; arvorivi++) {
    		    for (int sarake = 0; sarake < sarakkeet; sarake++) {
    		    	taulukko2D[arvorivi][sarake] = sarakelista.get(sarake).get(arvorivi);
    		    }
    		}
			
    		//printMatrix(taulukko2D);
    		return taulukko2D;
    	}	
    	
    	//hae hasmapista osa-avaimella tai tarkalla
		public int haeMapista(Map<String, Integer> map, String hakuehto, boolean strict) {
			List<Integer> values = null;
    	    if (strict) {			
	    	values =
	    	    map.keySet().stream()
	    	       .filter(key -> key.equals(hakuehto))
	    	       .map(map::get)
	    	       .collect(Collectors.toList());
    	    } else {
    	    	values =
    		    	    map.keySet().stream()
    		    	       .filter(key -> key.contains(hakuehto))
    		    	       .map(map::get)
    		    	       .collect(Collectors.toList());
    	    }
	    	if (values.size() == 0) {
	    		return -1;
	    	} else {
		    	return values.get(0);
	    	}
    	}
    	
    	//Displays a 2d array in the console, one line per row.
    	public void printMatrix(String[][] grid) {
    	    for(int r=0; r<grid.length; r++) {
    	       for(int c=0; c<grid[r].length; c++)
    	       ikkuna.kirjoitaKonsolille(grid[r][c] + " ");
    	       ikkuna.kirjoitaKonsolille(grid[r].length + " kpl kenttiä");
    	       ikkuna.kirjoitaKonsolille("\n");
    	    }
    	}
    	
        // tabulaattoriJakaja
    	// tabulaattorierotetut arvot ArrayListiin
        //
    	public ArrayList<String []> tabulaattoriJakajaOFF(String logfile, char separator) throws IOException{
    		// http://opencsv.sourceforge.net/
    		//CSVReader tabFormatReader = new CSVReader(new FileReader(logfile), '\t');
    		CSVReader tabFormatReader = new CSVReader(new FileReader(logfile), separator, '\'');
    		String [] nextLine;
    		ArrayList<String []> file = new ArrayList<String []>();
    	    while ((nextLine = tabFormatReader.readNext()) != null) {
    	        // nextLine[] on yksi tiedoston rivi,
    	    	//työnnetään koko rivi tiedosto-taulukkoon
    	    	file.add(nextLine);
    	    }
    	    tabFormatReader.close();
    		return file;
    	}
    	
    	// parseMittausvali
        // Muokataan millisekunnit näyttämään kellonajalta
        //
    	private String parseMittausvali(double mvinms) {
    		int ms = (int)mvinms;
    		int h = ms / 1000 / 60 / 60;
    		int m = (ms - (h * 60 * 60 * 1000))/1000/60;
    		int s = Math.round((ms - ((h * 60 * 60 * 1000) + (m * 60 * 1000)))/1000);
    		
    		String kello = String.format("%02d", m) + ":" + String.format("%02d", s);
    		return kello;
    	}
    	
    	
    	// muunnaInt
        // Muunnetaan String intiksi
        //
    	private int muunnaInt(String arvo, int kerroin) {
    		int arvoint = 0;
    		if (arvo.contains(".") || nibecontroller.equals("S")) { //double ehkä
    			arvoint = (int)(Double.parseDouble(arvo)*kerroin);
    		} else {
    			arvoint = Integer.parseInt(arvo)  * kerroin;
    		}
    		return arvoint;
    	}
    	
    // SÄIE LOPPU	
	// Säie-aliluokka päättyy tähän
	}
	
	/************************************
     * TAPAHTUMANKÄSITTELYSÄIKEEN METODIT
     ************************************/
    // aloitaSaie
	// tapahtumankäsittelijän puolelta tuleva pyyntö aloittaa säie lokien lukemista varten 
    //
	public void aloitaSaie() {
		//luo säikeen datan lukua varten
		 saie = new Saie();
		 saie.execute();
	}
	// kirjoitaKonsolille
    // välitysmetodi mallilta ikkunalle
    //
	public void kirjoitaKonsolille(String t) {
		ikkuna.kirjoitaKonsolille(t);
	}
	
	// kysyTallennusta
    // välitysmetodi mallilta ikkunalle
    //
	public boolean kysyTallennusta() {
		return ikkuna.kysyTallennusta();
	}
	
	//graafin värit
    Color getGraafinTaustanVari() {
		return graafinTaustanVari;
	}
	
	public void setGraafinTaustanVari(Color color) {
		graafinTaustanVari = color;
	}

    Color getGraafinAsteikonVari() {
		return graafinAsteikonVari;
	}
	
	public void setGraafinAsteikonVari(Color color) {
		graafinAsteikonVari = color;
	}
	
	//db-ikkunan kentät
	public void setTietokanta_osoite(String t) {
		ikkuna.setTietokanta_osoite(t);
	}
	
	public void setTietokanta_nimi(String t) {
		ikkuna.setTietokanta_nimi(t);
	}
	
	public void setTietokanta_dbms(String t) {
		ikkuna.setTietokanta_dbms(t);
	}
	
	public String getTietokanta_dbms() {
		return ikkuna.getTietokanta_dbms();
	}
	
	public String getTietokanta_nimi() {
		return ikkuna.getTietokanta_nimi();
	}
	
	public String getTietokanta_osoite() {
		return ikkuna.getTietokanta_osoite();
	}

	public String getTietokanta_salasana() {
		return ikkuna.getTietokanta_salasana();
	}
	
	public void setPaikallinenTietokanta_tallennus(String t) {
		if (t.equalsIgnoreCase("0")) {
			this.paikallinen_tietokanta_tallennus = false;
		} else if (t.equalsIgnoreCase("1")) {
			this.paikallinen_tietokanta_tallennus = true;
		}
	}
	
	public void setTietokanta_kayttaja(String t) {
		ikkuna.setTietokanta_kayttaja(t);
	}

	public String getTietokanta_kayttaja() {
		return ikkuna.getTietokanta_kayttaja();
	}

	public JLabel getLblMLPMalli() {
		return ikkuna.getLblMLPMalli();
	}
	
	public JLabel getLblLammonKeruu() {
		return ikkuna.getLblLammonKeruu();
	}
	
	public void setTietokanta_salasana(String t) {
		ikkuna.setTietokanta_salasana(t);
	}
	public void set35COP(String s) {
		this.cop_035 = Double.parseDouble(s.replace(",","."));;
	}
	public void set45COP(String s) {
		this.cop_045 = Double.parseDouble(s.replace(",","."));;
	}
	public ArrayList<String> getKayra_taulukko_nimet() {
		return kayra_taulukko_nimet;
	}
	public void setKayra_taulukko_nimet(ArrayList<String> kayra_taulukko_nimet) {
		this.kayra_taulukko_nimet = kayra_taulukko_nimet;
	}
	public ArrayList<ArrayList<Integer>> getKayra_taulukko() {
		return kayra_taulukko;
	}
	public void setKayra_taulukko(ArrayList<ArrayList<Integer>> kayra_taulukko) {
		this.kayra_taulukko = kayra_taulukko;
	}
	public ArrayList<GregorianCalendar> getLogiaika() {
		return logiaika;
	}
	public void setLogiaika(ArrayList<GregorianCalendar> logiaika) {
		this.logiaika = logiaika;
	}
	public boolean isPaikallinen_tietokanta_tallennus() {
		return paikallinen_tietokanta_tallennus;
	}
	public void setPaikallinen_tietokanta_tallennus(
			boolean paikallinen_tietokanta_tallennus) {
		this.paikallinen_tietokanta_tallennus = paikallinen_tietokanta_tallennus;
	}
	public static String getLokiVersio() {
		return lokiVersio;
	}
	public static void setLokiVersio(String lokiVersio) {
		LogintutkijaOhjain.lokiVersio = lokiVersio;
	}
	public static String getLokiRVersio() {
		return lokiRVersio;
	}
	public static void setLokiRVersio(String lokiRVersio) {
		LogintutkijaOhjain.lokiRVersio = lokiRVersio;
	}
	public String getOletushakemisto() {
		return oletushakemisto;
	}
	public void setOletushakemisto(String oletushakemisto) {
		LogintutkijaOhjain.oletushakemisto = oletushakemisto;
	}
	public boolean isJatkuvatLogit() {
		return ikkuna.isJatkuvatlogit();
	}
	public boolean isRunning() {
		return running;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
	public static long getMittausvali_ms() {
		return mittausvali_ms;
	}
	public static void setMittausvali_ms(long mittausvali_ms) {
		LogintutkijaOhjain.mittausvali_ms = mittausvali_ms;
	}
	public static int getF1255_cfa_found() {
		return f1255_cfa_found;
	}
	public static void setF1255_cfa_found(int f1255_cfa_found) {
		LogintutkijaOhjain.f1255_cfa_found = f1255_cfa_found;
	}
	public boolean isBt2_nolla() {
		return bt2_nolla;
	}
	public void setBt2_nolla(boolean bt2_nolla) {
		this.bt2_nolla = bt2_nolla;
	}
	public boolean isLaske_kaynnistys_jos_kaynnissa() {
		return laske_kaynnistys_jos_kaynnissa;
	}
	public void setLaske_kaynnistys_jos_kaynnissa(boolean kaynnistys_jos_kaynnissa) {
		this.laske_kaynnistys_jos_kaynnissa = kaynnistys_jos_kaynnissa;
	}
	public boolean isCfa_fake() {
		return cfa_fake;
	}
	public void setCfa_fake(boolean cfa_fake) {
		this.cfa_fake = cfa_fake;
	}
	public void setValittu_Tiedosto_Filtteri(String f) {
		ikkuna.setValittu_Tiedosto_Filtteri(f);
	}
	public String getValittu_Tiedosto_Filtteri() {
		return LogintutkijaGUI.getValittu_Tiedosto_Filtteri();
	}
	public void setF1345LisaysAskel(String f) {
		this.F1345LisaysAskel = Integer.parseInt(f);
	}
	public void setCTCmappi(String ctcmappi) {
			this.ctcmappi = ctcmappi;
	}
	public void setHDDGet(String enabled) {
		if (enabled.equals("1")) {
			this.hdd = true;
		}
	}
	public boolean getHDDGet() {
		return hdd;
	}

	public static long getMaxMemory() {
	    return Runtime.getRuntime().maxMemory();
	}
	
	public static long getUsedMemory() {
	    return getMaxMemory() - getFreeMemory();
	}
	
	public static long getTotalMemory() {
	    return Runtime.getRuntime().totalMemory();
	}
	
	public static long getFreeMemory() {
	    return Runtime.getRuntime().freeMemory();
	}
	
	public int getLastcop() {
		return lastcop;
	}
	public int getEp15_lastcop() {
		return ep15_lastcop;
	}
	public static double getLa() {
		return la;
	}
	public static double getKv() {
		return kv;
	}
	public static double getLe() {
		return le;
	}
	public static double getSa() {
		return sa;
	}
	public static double getSav() {
		return sav;
	}
	public static double getSal() {
		return sal;
	}
	public static double getUa() {
		return ua;
	}
	public static double getLa_flm() {
		return la_flm;
	}
	public static double getKv_flm() {
		return kv_flm;
	}
	
	public static String bytesToMegabytes(long bytes) {
	    long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
	    if (absB < 1024) {
	        return bytes + " B";
	    }
	    long value = absB;
	    CharacterIterator ci = new StringCharacterIterator("KMGTPE");
	    for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
	        value >>= 10;
	        ci.next();
	    }
	    value *= Long.signum(bytes);
	    return String.format("%.1f %ciB", value / 1024.0, ci.current());
	}
	
	public long GCTime() {
	    long garbageCollectionTime = 0;
	    for(GarbageCollectorMXBean gc :
	            ManagementFactory.getGarbageCollectorMXBeans()) {
	        long time = gc.getCollectionTime();

	        if(time >= 0) {
	            garbageCollectionTime += time;
	        }
	    }
	    //System.out.println("Total Garbage Collection Time (ms): " + garbageCollectionTime);
	    return garbageCollectionTime;
	}
}
