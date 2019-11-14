package logintutkija;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.TimeZone;
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
	private static double la, kv, le, sa, sav, sal, ua, la_flm, kv_flm;
	private static int f1255_cfa_found = 0;
	int bt2_found = 0;
	boolean bt2_nolla = true;
	private ArrayList<ArrayList<Integer>> kayra_taulukko = new ArrayList<ArrayList<Integer>>();
	private ArrayList<String> kayra_taulukko_nimet = new ArrayList<String>();
	private ArrayList<GregorianCalendar> logiaika  = new ArrayList<GregorianCalendar>();
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
	//käyriä varten
	private ArrayList<Integer> bt2 = new ArrayList<Integer>();
	//autoupdatea varten
	private boolean running = false;
	//EP14 - 1. kompressori
	private ArrayList<Integer> bt3 = new ArrayList<Integer>();
	private ArrayList<Integer> bt10 = new ArrayList<Integer>();
	private ArrayList<Integer> bt11 = new ArrayList<Integer>();
	private ArrayList<Integer> bt12 = new ArrayList<Integer>();
	private ArrayList<Integer> bt14 = new ArrayList<Integer>();
	private ArrayList<Integer> bt17 = new ArrayList<Integer>();
	private ArrayList<Integer> cop = new ArrayList<Integer>();
	private ArrayList<Integer> teho = new ArrayList<Integer>();
	private ArrayList<Integer> cop_la = new ArrayList<Integer>();
	private ArrayList<Integer> cop_kv = new ArrayList<Integer>();
	private ArrayList<Integer> relaysPCAbase = new ArrayList<Integer>();
//	private ArrayList<Integer> ep15_relaysPCAbase = new ArrayList<Integer>();
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
	//2016-11-06 GP1 ja GP2 tullut lisää oletuslogiin
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
	private static ArrayList<Integer> prio = new ArrayList<Integer>();
	private static int kompr_kaynnistyksia = 0;
	private static int ep15_kompr_kaynnistyksia = 0;
	private static ArrayList<Integer> bt1 = new ArrayList<Integer>();
	private static ArrayList<Integer> cs = new ArrayList<Integer>();
	private double cop_035 = 0.0;
	private double cop_045 = 0.0;
	private static String bt67_label = "";
	private boolean paikallinen_tietokanta_tallennus = true;
	private int f1345_ep15_found =  0;
	private boolean laske_kaynnistys_jos_kaynnissa=true;
	private boolean cfa_fake = false;
	private int F1345LisaysAskel = 0;
	private int bt53_found =  0;
	private int bt54_found =  0;
	private int bt51_found =  0;
	private int tietueen_pituus =  0;
	private LogintutkijaMalli malli;
	private LogintutkijaGUI ikkuna;
	private int bt20_found =  0;
	private int bt21_found =  0;
	private static ArrayList<Integer> bt20 = new ArrayList<Integer>();
	private static ArrayList<Integer> bt21 = new ArrayList<Integer>();
	private int bt71_found =  0;
	private int prio_found =  0;
	private static ArrayList<Integer> bt71 = new ArrayList<Integer>();
	private boolean hdd = false;
	
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
            ArrayList<ArrayList<String[]>> kaikkilogit = new ArrayList<ArrayList<String[]>>();
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

	            for (int i=0;i<logit.size();i++){
	            	try {
	            		//luetaan kaikki tiedostot taulukkoon joka sisältää taulukon jossa yhden tiedoston rivit
						//pitää testata mikä pumppu kyseessä
						if (LogintutkijaGUI.getValittu_Tiedosto_Filtteri().equals("ctc")) {
							kaikkilogit.add(muunnaCTC(tabulaattoriJakaja(logit.get(i),',')));
							//kaikkilogit.add(tabulaattoriJakaja(logit.get(i),','));
						} else {
							//ikkuna.kirjoitaKonsolille("Logit size: " + logit.size());
							kaikkilogit.add(tabulaattoriJakaja(logit.get(i),'\t'));
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
            		kaikkilogit=malli.haeSQLiteKannasta(ikkuna.getTietokanta_pvm_mista(), ikkuna.getTietokanta_pvm_mihin());
            	} else {
            		paikallinen_tietokanta_tallennus=false;
            		kaikkilogit=malli.haeMySQLKannasta(ikkuna.getTietokanta_osoite(), ikkuna.getTietokanta_nimi(), ikkuna.getTietokanta_kayttaja(), ikkuna.getTietokanta_salasana(), ikkuna.getTietokanta_pvm_mista(), ikkuna.getTietokanta_pvm_mihin());
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
    	// Analysoi logirivit tiedolähteestä
        //
        String analysoi(ArrayList<ArrayList<String[]>> tiedostot) {
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
        	logiaika.clear();
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
        	f1345_ep15_found =  0;
        	gp1.clear();
        	gp2.clear();
        	ep15_gp1.clear();
        	ep15_gp2.clear();
        	bt53_found =  0;
        	bt54_found =  0;
        	bt51_found =  0;
        	bt51.clear();
        	bt53.clear();
        	bt54.clear();
        	prio_found =  0;
        	prio.clear();
        	tietueen_pituus =  0;
        	bt21_found =  0;
        	bt21.clear();
        	bt20_found =  0;
        	bt20.clear();
        	bt71_found =  0;
        	bt71.clear();
        	
        	boolean kompr_kaynnissa = false;
        	boolean ep15_kompr_kaynnissa = false;
        	int prosessinalku = 0;
        	int ep15_prosessinalku = 0;
        	int bt25_found =  0;
        	int bt50_found =  0;
        	int bt7_found =  0;
        	int bt6_found =  0;
        	int f1345_bp_found =  0;
        	int f1255_bf1_found =  0;
        	int f1345_ep15pca_found =  0;
        	//6629 Release versio
        	int rversion_found = 0;
        	int gp_found = 0;
        	int f1345_gp1_found = 0;
        	int f1345_gp2_found = 0;
        	int f1345_gp_fix = 0;
        	bt2_found = 0;
        	boolean aloitusaika=false;
        	
        	
        	//kesäajan aiheuttaman käyrien taiteellisen piirron poisto
			TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    	
    		String edellinentila = "0";
    		String ep15_edellinentila = "0";
        	String mlpfound="Tietolähteestä ei löytynyt tuettujen pumppujen dataa.\n";
    		//lasketaan login arvoista avainarvoja
        	//yksi tiedosto = i
        	exit:
    		for (int i=0;i<tiedostot.size();i++){
    			//tarkistetaan koko
    			if (tiedostot.get(0).size() < 4) {
    				ikkuna.kirjoitaKonsolille("Liian lyhyt logi (" + tiedostot.get(0).size() + ") - anna pitempi hakuaika.\n");
    				break;
    			}
			
    			//jos ensimmäisen rivin eka solu on "Divisors" niin puhutaan Nibe-lämpöpumpun logista
            	if(tiedostot.get(i).get(0)[0].equalsIgnoreCase("Divisors")){
            		mlpfound="Analysointi tehty";
            		
            		//CTC hämä
            		if(tiedostot.get(i).get(1)[1].equalsIgnoreCase("CTC")) {
            			ikkuna.getLblMLPMalli().setText("CTC");
            		} else {
                			ikkuna.getLblMLPMalli().setText("F1x45");	
            		}
            		
            		//ikkuna.kirjoitaKonsolille("tiedosto " + (i+1) + "\n");
            		//F1145 login esimerkki
            		//Divisors		1	10	10	10	10	10	10	10	10	10	10	10	10	100	1	10	10	10	1
            		//Date	Time	version	BT1	BT2	BT3	BT6	BT7	BT10	BT11	BT12	BT14	BT17	BT25	BT50	Tot.Int.Add	Alarm number	Calc. Supply	Degree Minutes	BT1 Average	Relays PCA-Base
            		//0		1		2		3	4	5	6	7	8		9		10		11		12		13		14		15			16				17				18				19			20
            		//div			1	10	10	10	10	10	10		10		10		10		10		10		10		100			1				10				10				10			1
            		//label	time	v	BT1	BT2	BT3	BT6	BT7	BT10	BT11	BT12	BT14	BT17	BT25	BT50	Tot.Int.Add	Alarm number	Calc. Supply	Degree Minutes	BT1 Average	Relays PCA-Base
            		//rivi tiedostossa = j+1
            		rivi:
            		for (int j=0; j<tiedostot.get(i).size();j++) {

            			//debug printtaus kaikki mitä sisään tulee
//            			for (int o = 0;tiedostot.get(i).get(j).length>o;o++) {
//            				ikkuna.kirjoitaKonsolille("idx_" + o + ": " + tiedostot.get(i).get(j)[o] + " ");
//            			}
//            			ikkuna.kirjoitaKonsolille("\n");
            			
            			
						//tarkistetaan tietueen pituus
						if (tietueen_pituus == 0) { // 1. logi, sen elemettien määrä muistiin
							tietueen_pituus=tiedostot.get(i).get(j).length;
							//ikkuna.kirjoitaKonsolille("koko " + (j+1) + ": " + tiedostot.get(i).get(j).length + "\n");
							//ikkuna.kirjoitaKonsolille("rivi " + (j+2) + ": " + Arrays.asList(tiedostot.get(i).get(j+1)) + "\n");
							//ikkuna.kirjoitaKonsolille("rivi " + (j+3) + ": " + Arrays.asList(tiedostot.get(i).get(j+2)) + "\n");
						} else {
							if (tietueen_pituus != tiedostot.get(i).get(j).length) {
			    				ikkuna.kirjoitaKonsolille("Eri määrä elementtejä tietueessa " + j + ": alunperin " + tietueen_pituus + ", nyt " + tiedostot.get(i).get(j).length + " - ohitetaan tiedosto\n");
			    				break;
							}
						}

            			//etsitään tyhjä rivi
            			if (tiedostot.get(i).get(j)[0].equals("")) {
            				ikkuna.kirjoitaKonsolille("Tyhjä rivi " + (j+1) + " tiedostossa " + (i+1) + " - ei huomioida.\n");
            				continue rivi;
            			}
            			
            			if (tiedostot.get(i).get(0).length > tiedostot.get(i).get(j).length || Arrays.asList(tiedostot.get(i).get(j)).contains(null)){
            				if (ikkuna.getTietolahde() == 1) {
            					ikkuna.kirjoitaKonsolille("Vajaa tietue (" + (j+1) + ") - ei huomioida.\n");
            				} else {
            					ikkuna.kirjoitaKonsolille("Vajaa rivi (" + (j+1) + ") - ei huomioida. (Otithan USB tikun ulos hallitusti?)\n");
            				}
        	        		kv = 0;
        	        		sa = 0;
        	        		la = 0;
        	        		le = 100;
        	        		ikkuna.nollaaNumerot();
            				continue rivi;
            			}
            			//etsitään validit merkit (F1145 tulosti ei-merkin)
            			for (int a = 0; a < tiedostot.get(i).get(j).length; ++a) {
            				//ikkuna.kirjoitaKonsolille("cpc=" + tiedostot.get(i).get(j)[a].codePointCount(0,tiedostot.get(i).get(j)[a].length()) + "\n");
                			//System.out.println( "cpc=" + x.codePointCount(0,x.length()) );
                			//for( int b = 0; b < tiedostot.get(i).get(j)[a].length(); ++b ){
                				//ikkuna.kirjoitaKonsolille("char: " + tiedostot.get(i).get(j)[a].charAt(b) + "\n");
                				//if (onkoAsciiMerkki(tiedostot.get(i).get(j)[a].charAt(b)) == false || j == 134) {
                				if (j>1 && !tiedostot.get(i).get(j)[a].matches("^[a-zA-Z0-9,.;:()°˚/\\-_'\\s]+$") && !tiedostot.get(i).get(j)[a].isEmpty()) {
                					ikkuna.kirjoitaKonsolille( "Korruptoitunut merkkijono \"" + tiedostot.get(i).get(j)[a] + "\" " + (i+1) + ". tiedostossa, rivillä " + (j+1) + " - tietuetta ei huomioida.\n" );
                					continue rivi;
                				}
                    			//tarkistetaan validit arvot (-32768 on piuha irti anturista)
                				if (tiedostot.get(i).get(j)[a].equals("-32768")) {
                					ikkuna.kirjoitaKonsolille( "Laiton arvo \"" + tiedostot.get(i).get(j)[a] + "\" " + (i+1) + ". tiedostossa, rivillä " + (j+1) + " kentässä " + (a+1) + "  - tietuetta ei huomioida. Onko anturin piuha irti?\n" );
                					continue rivi;
                				}  
                			//}
            			}

            			if (tiedostot.get(i).get(j)[0].equalsIgnoreCase("Divisors") ||
            					tiedostot.get(i).get(j)[0].equalsIgnoreCase("Date")){
            					//etsitään bt25 ja/tai bt50 tai jotain muuta
  
            					for (int k=0; k<tiedostot.get(i).get(j).length;k++){
            						//ikkuna.kirjoitaKonsolille(k + ": " + tiedostot.get(i).get(j)[k] + "\n");
            						if (tiedostot.get(i).get(j)[k].contains("BT25")) {
            							bt25_found=1;
            						} else if (tiedostot.get(i).get(j)[k].equals("BT2") || tiedostot.get(i).get(j)[k].equals("BT2(40008)")) {//BT2x eivät toimi muuten
            							bt2_found=1;
            							//ikkuna.kirjoitaKonsolille("BT2 found!!");
    	        					} else if (tiedostot.get(i).get(j)[k].contains("BT50")) {
            							bt50_found=1;
            							//ikkuna.kirjoitaKonsolille("BT50 found!!");
    	        					}  else if (tiedostot.get(i).get(j)[k].equals("BT7") || tiedostot.get(i).get(j)[k].equals("BT7(40013)")) {//BT7x eivät toimi muuten
            							bt7_found=1;
    	        					} else if (tiedostot.get(i).get(j)[k].contains("BT6")) {
            							bt6_found=1;
    	        					} else if (tiedostot.get(i).get(j)[k].contains("R-version")) {
            							rversion_found=1;
    	        					} else if (tiedostot.get(i).get(j)[k].contains("BF1 EP14") || tiedostot.get(i).get(j)[k].contains("EP14-BF1")) {
    	        						//luultavasti F1x55
    	        						f1255_bf1_found = 1;
    	        						ikkuna.getLblMLPMalli().setText("F1x55");
    	        						//ikkuna.kirjoitaKonsolille("BF1 found!!");
    	        					//F1255 uusi firmis
    	        					} else if (tiedostot.get(i).get(j)[k].contains("compr. freq. act.")) {
    	        						if (!cfa_fake) {
    	        							ikkuna.getLblMLPMalli().setText("F1x55");
    	        						}
    	        						f1255_cfa_found = 1;
            						} else if (tiedostot.get(i).get(j)[k].contains("EB100-EP15 Prio")) {
    									//luultavasti F1345
        								f1345_ep15_found=6;
    									f1345_bp_found=2;
    									ikkuna.getLblMLPMalli().setText("F1345");
    									//ikkuna.kirjoitaKonsolille("EP15 Prio found!!");
            						} else if (tiedostot.get(i).get(j)[k].contains("Relays PCA-Base EP15")) {
        								f1345_ep15pca_found=1;
        							} else if (tiedostot.get(i).get(j)[k].contains("GP1-speed EP14")) {
            							gp_found=2;
            							//ikkuna.kirjoitaKonsolille("EP14GP1 found!!");
            						//f1345 GP 7774R2
    	        					} else if (tiedostot.get(i).get(j)[k].contains("GP1-speed EP15")) {
            							f1345_gp1_found=1;
            							//ikkuna.kirjoitaKonsolille("EP15GP1 found!!");
    	        					} else if (tiedostot.get(i).get(j)[k].contains("GP2-speed EP15")) {
            							f1345_gp2_found=1;
	        							//ikkuna.kirjoitaKonsolille("EP15GP2 found!!");
    	        					} else if (tiedostot.get(i).get(j)[k].contains("BT53")) { //aurinkokeräin
            							bt53_found=1;
	        							//ikkuna.kirjoitaKonsolille("BT53 found!!");
    	        					} else if (tiedostot.get(i).get(j)[k].contains("BT54")) { //aurinkokierukka
            							bt54_found=1;
	        							//ikkuna.kirjoitaKonsolille("BT54 found!!");
		        					} else if (tiedostot.get(i).get(j)[k].contains("BT51")) { //uima-allas
	        							bt51_found=1;
	        							//ikkuna.kirjoitaKonsolille("BT51 found!!");
		        					} else if (tiedostot.get(i).get(j)[k].contains("BT20")) { //FLM jäte
	        							bt20_found=1;
	        							//ikkuna.kirjoitaKonsolille("BT20 found!!");
		        					} else if (tiedostot.get(i).get(j)[k].contains("BT21")) { //FLM poisto
	        							bt21_found=1;
	        							//ikkuna.kirjoitaKonsolille("BT21 found!!");
    	        					} else if (tiedostot.get(i).get(j)[k].contains("BT71")) { //paluu ulkoinen
	        							bt71_found=1;
	        							//ikkuna.kirjoitaKonsolille("BT71 found!!");
    	        					} else if (tiedostot.get(i).get(j)[k].contains("Prio")) { //prio tuli firmiksessä 5539
    	        						prio_found=1;
    	        						//ikkuna.kirjoitaKonsolille("Prio found!!");
    	        					}
	        					}
            					//1768 tarkistus
            					int version = 0;
            					if (tiedostot.get(i).get(j)[0].equalsIgnoreCase("Date")) {
            						version = Integer. parseInt(tiedostot.get(i).get(j+1)[2]);
                					if (version < 3105 && ikkuna.getTietolahde() == 0) {
                						//lopetetaan kun versio 1768 löytyi
                    					ikkuna.kirjoitaKonsolille(version + " -versio löydetty. Logintutkija tukee vain 3105 ja tuoreempia logeja. Toimii tai ei toimi.\n");
                    					//break exit;
                					}
            					}
            				continue rivi;
            			}
     			
            			//datarivi. Jos paikallinen kanta, luetaan malli
						if (ikkuna.getTietolahde()  == 1 && ikkuna.getTietokanta_dbms().equals("sqlite")) { //sqlite
							//ikkuna.kirjoitaKonsolille(tiedostot.get(i).get(j)[tiedostot.get(i).get(j).length - 1]);
							//pikakorjaus F1345 EP 15 GP2 osumattomuuteen
							f1345_gp_fix = 2;
							if (tiedostot.get(i).get(j)[tiedostot.get(i).get(j).length - 1].contains("F1345")) {
    							ikkuna.getLblMLPMalli().setText("F1345");
							} else if (tiedostot.get(i).get(j)[tiedostot.get(i).get(j).length - 1].contains("F1x55")) {
								ikkuna.getLblMLPMalli().setText("F1x55");
							} else if (tiedostot.get(i).get(j)[tiedostot.get(i).get(j).length - 1].contains("F11/245")) {
								ikkuna.getLblMLPMalli().setText("F1x45");
							} else {
								ikkuna.getLblMLPMalli().setText("MLP");
							}
						}

 
						
            			//muutetaan teksti date ja time, pvm idx0 ja aika idx1
            			//pvm
            			String [] paivays = tiedostot.get(i).get(j)[0].split("[-]");
            			paiva = Integer.parseInt(paivays[2]);
            			kuukausi = (Integer.parseInt(paivays[1])-1);
            			vuosi = Integer.parseInt(paivays[0]);
            			//kello
            			paivays = tiedostot.get(i).get(j)[1].split("[:]");
            			tunti = Integer.parseInt(paivays[0]);
            			minuutti = (Integer.parseInt(paivays[1]));
            			sekunti = Integer.parseInt(paivays[2]);
        			
    	        		//otetaan versio ylös
    	        		if (lokiVersio.equalsIgnoreCase("0000")) { 
    	        			ikkuna.getLblLokiVersio().setText("v" + tiedostot.get(i).get(j)[2]);
    	        			lokiVersio=tiedostot.get(i).get(j)[2];
    	        		}
    	        		if (!lokiVersio.equalsIgnoreCase(tiedostot.get(i).get(j)[2]) && ikkuna.getTietolahde()  == 0) {
    	        			if (!lokiVersio.equalsIgnoreCase("9999")) {
    	        				ikkuna.kirjoitaKonsolille("Edellisen login versio " + lokiVersio + " ei vastaa nykyistä (" + tiedostot.get(i).get(j)[2] + ") tiedostossa " + (i+1) + "\n");
    	        				ikkuna.kirjoitaKonsolille("Nykyistä ja seuraavia logitiedostoja ei käsitellä. Pidä hakemistossa vain saman versiotason logeja.\n");
    	        				lokiVersio="9999";
    	        			}
    	        			continue rivi;
    	        		}
    	        		if (rversion_found == 1){
    	        			ikkuna.getLblLokiVersio().setText("v" + tiedostot.get(i).get(j)[2] + "R" + tiedostot.get(i).get(j)[3]);
    	        			lokiRVersio=tiedostot.get(i).get(j)[3];
    	        		}
            			kokaika++;
            			logiaika.add(new GregorianCalendar(vuosi, kuukausi, paiva, tunti, minuutti, sekunti));
            			//kirjoitetaan aloitusaika Käyrät-ikkunaan
            			if (aloitusaika != true) {
            				LogintutkijaGUI.setAikaAloitus(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(logiaika.get(0).getTime()));
            				aloitusaika=true;
            			}
//						  for (int y=0; y<tiedostot.get(i).get(j).length ;y++) {
//							  ikkuna.kirjoitaKonsolille("idx " + y + ": " + tiedostot.get(i).get(j)[y] + ", ");
//						  }
//						  ikkuna.kirjoitaKonsolille("\n");
  
            			//KÄYTTÖTILAT
            			//Relays PCA Base, on binäärilukuna 0b00000000
            			//bitti 0 = kompressorin tila, 0=pois, 1=päällä
            			//bitti 1 = lämpöjohtopumpun tila, 0=pois, 1=päällä
            			//bitti 2 = keruupumpun tila, 0=pois, 1=päällä
            			//bitti 3 = vaihtoventtiilin ohjaus, 0 = venttiili auki ja tehdään lämmitysvettä, 1 = venttiili kiinni ja tehdään käyttövettä
            			//jos nykyinen tila ja edellinen tila ovat eri, resetoidaan prosessin alku
    	        		//F1345 Prio arvo kertoo käyttötilan EP15 kompressorille, EP14 saa Relays PCA Basen arvon
            			//1 tarkoittaa lämmitystä ja 2 käyttövettä, nolla näyttäisi olevan kun kompura ei käy.

            			//Käyttövesivaraaja idx 6, ei jaeta niin ei tarvita doublea
            			if (bt7_found > 0) {
            				bt67_label = "ylävaraaja";
            				lampo_kv.add(Integer.parseInt(tiedostot.get(i).get(j)[6+rversion_found+bt2_found]));
    
            			} else if (bt6_found > 0) {
            				bt67_label = "alavaraaja";
            				lampo_kv.add(Integer.parseInt(tiedostot.get(i).get(j)[5+rversion_found+bt2_found]));
            			} else {
            				lampo_kv.add(0);
            			}

            			if (bt6_found > 0) {
            				bt6.add(Integer.parseInt(tiedostot.get(i).get(j)[5+rversion_found+bt2_found]));
            			} else {
            				bt6.add(0);
            			}

            			//ulkolämpö idx 3, ei jaeta niin ei tarvita doublea
            			bt1.add(Integer.parseInt(tiedostot.get(i).get(j)[3+rversion_found]));
    	        		if (bt2_found==1) {
    	        			//bt2
    	        			bt2.add(Integer.parseInt(tiedostot.get(i).get(j)[4+rversion_found]));
    	        		} else {
            				//ikkuna.kirjoitaKonsolille("bt25 0\n");
            				bt2.add(0);
            			}
            			//bt3
            			bt3.add(Integer.parseInt(tiedostot.get(i).get(j)[4+rversion_found+bt2_found]));
      			
            			if (bt25_found==1) {
            				//bt25
            				bt25.add(Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt2_found+f1345_ep15_found]));
            			} else {
            				bt25.add(0);
            			}
            			//bt71
            			if (bt71_found==1) {
            				bt71.add(Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt2_found+f1345_ep15_found+bt25_found]));
            			} else {
            				bt71.add(0);
            			}
            			//gp
            			if (gp_found==2) {
            				//gp1 & gp2         				
            				gp1.add(Integer.parseInt(tiedostot.get(i).get(j)[14+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found]));
            				gp2.add(Integer.parseInt(tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found+f1345_gp1_found]));
//            				ikkuna.kirjoitaKonsolille( "" +
//            						" gp2: " + tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found+f1345_gp1_found] +
//                    				" GP found: " + gp_found + " EP15GP found: " + f1345_gp1_found + " BT50 found: " + bt50_found +
//                    				"\n"
//                    				);
//            				ikkuna.kirjoitaKonsolille( "gp1 idx: " + (14+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found) + "\n");
//            				ikkuna.kirjoitaKonsolille( "gp2 idx: " + (15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found+f1345_gp1_found) + "\n");
            			} else {
            				//ikkuna.kirjoitaKonsolille("GPt 0\n");
            				gp1.add(0);
            				gp2.add(0);
            			}
            			
            			//bt51, 53 ja 54
            			if (bt51_found > 0) {
            				bt51.add(Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt25_found+bt2_found+f1345_ep15_found+bt71_found+bt50_found]));
            				//ikkuna.kirjoitaKonsolille("BT51: " + tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt25_found+bt2_found+f1345_ep15_found+bt50_found] +  "\n");
            			} else {
            				bt51.add(0);
            			}
            			if (bt53_found > 0) {
            				bt53.add(Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt25_found+bt2_found+f1345_ep15_found+bt71_found+bt50_found+bt51_found]));
            				//ikkuna.kirjoitaKonsolille("BT53: " + tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt25_found+bt2_found+f1345_ep15_found+bt50_found+bt51_found] +  "\n");
            			} else {
            				bt53.add(0);
            			}
            			if (bt54_found > 0) {
            				bt54.add(Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt25_found+bt2_found+f1345_ep15_found+bt71_found+bt50_found+bt51_found+bt53_found]));
            				//ikkuna.kirjoitaKonsolille("BT54: " + tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt25_found+bt2_found+f1345_ep15_found+bt50_found+bt51_found+bt53_found] +  "\n");
            			} else {
            				bt54.add(0);
            			}
            			
            			//bt10
            			bt10.add(Integer.parseInt(tiedostot.get(i).get(j)[5+rversion_found+bt6_found+bt7_found+bt2_found]));
            			//bt11
            			bt11.add(Integer.parseInt(tiedostot.get(i).get(j)[6+rversion_found+bt6_found+bt7_found+bt2_found]));
            			//bt12
            			bt12.add(Integer.parseInt(tiedostot.get(i).get(j)[7+rversion_found+bt6_found+bt7_found+bt2_found]));
            			//bt14
            			bt14.add(Integer.parseInt(tiedostot.get(i).get(j)[8+rversion_found+bt6_found+bt7_found+bt2_found]));
            			//bt17
            			bt17.add(Integer.parseInt(tiedostot.get(i).get(j)[9+rversion_found+bt6_found+bt7_found+bt2_found]));
//            			ikkuna.kirjoitaKonsolille( "" +
//            				"bt10: " + tiedostot.get(i).get(j)[6+rversion_found+bt6_found+bt7_found] +
//            				" bt11: "  + tiedostot.get(i).get(j)[7+rversion_found+bt6_found+bt7_found] +
//            				" bt12: " + tiedostot.get(i).get(j)[8+rversion_found+bt6_found+bt7_found] +
//            				" bt14: " + tiedostot.get(i).get(j)[9+rversion_found+bt6_found+bt7_found] +
//            				" bt17: " + tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found] +
//            				" gp1: " + tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+gp_found] +
//            				" ep15_gp1: " + tiedostot.get(i).get(j)[16+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+gp_found] +
//            				" gp2: " + tiedostot.get(i).get(j)[16+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+gp_found+f1345_gp_found] +
//            				" GP found: " + gp_found + " EP15GP found: " + f1345_gp_found + " BT50 found: " + bt50_found +
//            				"BF1: " +  Integer.parseInt(tiedostot.get(i).get(j)[6+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt2_found+f1345_ep15_found+f1345_bp_found]) +
//            				"\n"
//            				);
            			
            			if (prio_found == 1) {
	            			//prio uima-allaslämmityksen laskentaa varten
	            			prio.add(Integer.parseInt(tiedostot.get(i).get(j)[16+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found+f1345_gp1_found+f1345_gp2_found]));
            			} else {
            				prio.add(0);
            			}
      			
            			
            			if (f1345_ep15_found!=0) {
    	        			//EP15
    	        			//bt3
    	        			ep15_bt3.add(Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt2_found+bt6_found+bt7_found]));
    	        			//EP15-bt3 saa negatiivisen arvon jos kyselty Modbussilla F11/245 koneesta
    	        			if (Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt2_found+bt6_found+bt7_found]) == 32768) {
    	        				if (cfa_fake) {
    	        					ikkuna.getLblMLPMalli().setText("F1x45");
    	        				}
    	        				//ikkuna.kirjoitaKonsolille("ep15bt3: " + i + ": " + Integer.parseInt(tiedostot.get(i).get(j)[10+bt6_found+bt7_found+bt2_found]) + "\n");
    	        			}
    	        			//bt10
    	        			ep15_bt10.add(Integer.parseInt(tiedostot.get(i).get(j)[11+rversion_found+bt6_found+bt7_found+bt2_found]));
    	        			//ikkuna.kirjoitaKonsolille("ep15bt3: " + i + ": " + Integer.parseInt(tiedostot.get(i).get(j)[12+bt6_found+bt7_found]) + "\n");
    	        			//bt11
    	        			ep15_bt11.add(Integer.parseInt(tiedostot.get(i).get(j)[12+rversion_found+bt6_found+bt7_found+bt2_found]));
    	        			//bt12
    	        			ep15_bt12.add(Integer.parseInt(tiedostot.get(i).get(j)[13+rversion_found+bt6_found+bt7_found+bt2_found]));
    	        			//bt14
    	        			ep15_bt14.add(Integer.parseInt(tiedostot.get(i).get(j)[14+rversion_found+bt6_found+bt7_found+bt2_found]));
    	        			//bt17
    	        			ep15_bt17.add(Integer.parseInt(tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt2_found]));
    	        			//f1345 GP1
    	        			if (f1345_gp1_found==1){
    	        				ep15_gp1.add(Integer.parseInt(tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+gp_found+f1345_gp1_found+f1345_gp_fix]));
								//ikkuna.kirjoitaKonsolille("EP15GP1: " + tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+gp_found+f1345_gp1_found+f1345_gp_fix] + "\n");
								//ikkuna.kirjoitaKonsolille("EP15GP1 idx: " + (15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+gp_found+f1345_gp1_found+f1345_gp_fix) + "\n");
    	        			} else {
    	        				ep15_gp1.add(0);
    	        			}
    	        			
    	        			if (f1345_gp2_found==1) { //GP2 EP15
    	        				ep15_gp2.add(Integer.parseInt(tiedostot.get(i).get(j)[16+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+gp_found+f1345_gp1_found+f1345_gp2_found+f1345_gp_fix]));
    	        				//ikkuna.kirjoitaKonsolille("EP15GP2: " + tiedostot.get(i).get(j)[16+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+gp_found+f1345_gp1_found+f1345_gp2_found+f1345_gp_fix] + "\n");    	        				
    	        			} else {
    	        				ep15_gp2.add(0);
    	        			}
    	        			
    	        			
            			} else {
            				ep15_bt3.add(0);
            				ep15_bt10.add(0);
            				ep15_bt11.add(0);
            				ep15_bt12.add(0);
            				ep15_bt14.add(0);
            				ep15_bt17.add(0);
            				ep15_gp1.add(0);
            				ep15_gp2.add(0);
            			}
            			//sisälämpö idx 14, ei jaeta niin ei tarvita doublea
            			if (bt50_found > 0) {
            				bt50.add(Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt2_found+f1345_ep15_found]));
            			} else {
            				bt50.add(0);
            			}
            			
            			
            			//lämpöpyynti idx 17, ei jaeta niin ei tarvita doublea
            			cs.add(Integer.parseInt(tiedostot.get(i).get(j)[12+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found]));
            			//asteminuutit
            			dm.add(Integer.parseInt(tiedostot.get(i).get(j)[13+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found]));
            			if (F1345LisaysAskel == 0 && ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345")) {
            				//F1345 add step
            				add_step.add(Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found])*100);
             			} else {
                   			//suorasähkö
             				if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345")) {
             					kw_sahko.add(10*F1345LisaysAskel*Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found]));
             				} else {
             					kw_sahko.add(Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found]));
             					//ikkuna.kirjoitaKonsolille(tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt2_found+f1345_ep15_found+f1345_bp_found] + "\n");
             				}
            			}
            			//F1x55 Flow
            			if (f1255_bf1_found > 0) {
	            			bf1.add(Integer.parseInt(tiedostot.get(i).get(j)[11+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found]));
            			} else {
            				bf1.add(0);
            			}
            			//F1x55 compressor frequency active
            			if (f1255_cfa_found==1 && cfa_fake==false){
            				cfa.add(Integer.parseInt(tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found]));
            			} else {
            				if (kompr_kaynnissa) {
            					cfa.add(500);
            				} else {
            					cfa.add(0);
            				}
            			}

            			//FLM ilman lämpötilat
            			if (bt20_found > 0) {
            				bt20.add(Integer.parseInt(tiedostot.get(i).get(j)[16+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found+f1345_gp1_found+f1345_gp2_found+bt20_found]));
            				//ikkuna.kirjoitaKonsolille("bt20: " + tiedostot.get(i).get(j)[16+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found+f1345_gp1_found+f1345_gp2_found+bt20_found] + "\n");
            			} else {
            				bt20.add(0);
            			}
            			if (bt21_found > 0) {
            				bt21.add(Integer.parseInt(tiedostot.get(i).get(j)[16+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found+f1345_gp1_found+f1345_gp2_found+bt20_found+bt21_found]));
            				//ikkuna.kirjoitaKonsolille("bt21: " + tiedostot.get(i).get(j)[16+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found+f1345_gp1_found+f1345_gp2_found+bt20_found+bt21_found] + "\n");
            			} else {
            				bt21.add(0);
            			}
      			
            			if(!tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found].equalsIgnoreCase(edellinentila)){
            				prosessinalku=0;
            			}
            			//ikkuna.kirjoitaKonsolille("PCA " + tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found] + "\n");				
            			// Relays PCA-Base 7
            			// otetaan PCA-Base ylös tallennusta varten
            			relaysPCAbase.add(Integer.parseInt(tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found]));
            			if(tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found].equalsIgnoreCase("7"))
            			{
            				//0b00000111 = 7
            				//käyntiaika, Relays PCA-Base on idx 20, arvo 7 = lämmitys
            				//jos prio 40, lämmitetään uima-allasta
            				//uima-allaskin on lämmitystä, mutta erotellaan se käyntiaikasuhteessa
            				if (prio_found==1) {
	            				if (Integer.parseInt(tiedostot.get(i).get(j)[16+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found+f1345_gp1_found+f1345_gp2_found]) == 40) {
	            					kaytto_ua++;
	            				}
            				}

    	        			kaytto_la++;
    	        			//skipataan pari ensimmäistä arvoa koska prosessi ei ole stabiloitunut
    	        			//ikkuna.kirjoitaKonsolille(prosessinalku + "\n");
    	        			if(prosessinalku>=120){
    	        				if (f1345_ep15_found!=0){
    	        					//F1345
    	        					//delta lämmitysvesi meno idx9 (BT12) - tulo idx4 (BT3), ei jaeta niin ei tarvita doublea
    	        					lampo_delta_la.add(Integer.parseInt(tiedostot.get(i).get(j)[7+rversion_found+bt2_found+bt6_found+bt7_found]) - Integer.parseInt(tiedostot.get(i).get(j)[4+rversion_found+bt2_found]));
    	        					//ikkuna.kirjoitaKonsolille("Lämm 1345: " + bt2_found + ": " + Integer.parseInt(tiedostot.get(i).get(j)[7+rversion_found+bt2_found+bt6_found+bt7_found]) + "\n");    	
    	        				} else  {
    	        					//ei-F1345
    	        					//delta lämmitysvesi meno idx4 (BT2) - tulo idx5 (BT3), ei jaeta niin ei tarvita doublea
    	        					lampo_delta_la.add(Integer.parseInt(tiedostot.get(i).get(j)[3+rversion_found+bt2_found]) - Integer.parseInt(tiedostot.get(i).get(j)[4+rversion_found+bt2_found]));
    	        					//ikkuna.kirjoitaKonsolille("Lämm: " + i + ": " + Integer.parseInt(tiedostot.get(i).get(j)[3+rversion_found+bt2_found]) + "\n");
    	        				}
    	        				//cop
    	        				cop_la.add(lastcop=laskeCOP(bt12.get(bt12.size()-1),bt3.get(bt3.size()-1),bt14.get(bt14.size()-1),bt17.get(bt17.size()-1),bt10.get(bt10.size()-1),cop_035, cop_045));
    	        				//delta keruu tulo idx8 (BT10) - tulo idx9 (BT11), ei jaeta niin ei tarvita doublea
            					lampo_delta_keruu.add(Integer.parseInt(tiedostot.get(i).get(j)[6+rversion_found+bt6_found+bt7_found+bt2_found]) - Integer.parseInt(tiedostot.get(i).get(j)[5+rversion_found+bt6_found+bt7_found+bt2_found]));
            					//etsitään matalin ja korkein bt10
            					//matalin
            					if (bt10_min > Integer.parseInt(tiedostot.get(i).get(j)[5+rversion_found+bt6_found+bt7_found+bt2_found])) {
            						bt10_min = Integer.parseInt(tiedostot.get(i).get(j)[5+rversion_found+bt6_found+bt7_found+bt2_found]);
            					}
            					//korkein
            					if (bt10_max < Integer.parseInt(tiedostot.get(i).get(j)[5+rversion_found+bt6_found+bt7_found+bt2_found])) {
            						bt10_max = Integer.parseInt(tiedostot.get(i).get(j)[5+rversion_found+bt6_found+bt7_found+bt2_found]);
            					}
            					//etsitään matalin ja korkein bt11
            					//matalin
            					if (bt11_min > Integer.parseInt(tiedostot.get(i).get(j)[6+rversion_found+bt6_found+bt7_found+bt2_found])) {
            						bt11_min = Integer.parseInt(tiedostot.get(i).get(j)[6+rversion_found+bt6_found+bt7_found+bt2_found]);
            					}
            					//korkein
            					if (bt11_max < Integer.parseInt(tiedostot.get(i).get(j)[6+rversion_found+bt6_found+bt7_found+bt2_found])) {
            						bt11_max = Integer.parseInt(tiedostot.get(i).get(j)[6+rversion_found+bt6_found+bt7_found+bt2_found]);
            					}
            					
    	        				if (1<Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt2_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+f1345_ep15_found+f1345_bp_found])) {
    		        				//lisälämpö
    	        					if (f1345_ep15_found!=0 && Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt2_found+bt6_found+bt7_found]) != 32768){
    	        						lamm_sahko.add(F1345LisaysAskel*Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt2_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+f1345_ep15_found+f1345_bp_found]));
    	        						//ikkuna.kirjoitaKonsolille("Lämm 1345: " + i + ": " + F1345LisaysAskel + "/" + Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt2_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+f1345_ep15_found+f1345_bp_found]) + "\n");
    	        					} else {
    	        						lamm_sahko.add(Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt2_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+f1345_ep15_found+f1345_bp_found]));
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
    	        			

    	        		// Relays PCA-Base 15 tai 11
    	        		} else if (tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found].equalsIgnoreCase("15") ||
    	        				tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found].equalsIgnoreCase("11"))
    	        		{
    	        			//0b00001111 = 15
    	        			//käyntiaika, Relays PCA-Base on idx 20, arvo 15 = käyttövesi, 11 = kayttovesi FLM:n patterilla
            				//FLM - keruun pumppu on FLMssä, muutoin normi kvkäyttö - ei pidä paikkaansa, huuhaata
//            				if (tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found].equalsIgnoreCase("11")) {
//            					kaytto_kv_flm++;
//            					//ikkuna.kirjoitaKonsolille("FLM: " + tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found]);
//            				}
    	        			kaytto_kv++;
    	        			if(prosessinalku>=120){
    	        				if (f1345_ep15_found!=0){
    	        					//F1345
    	        					//delta lämmitysvesi meno idx9 (BT12) - tulo idx4 (BT3), ei jaeta niin ei tarvita doublea
    	        					lampo_delta_kv.add(Integer.parseInt(tiedostot.get(i).get(j)[7+rversion_found+bt2_found+bt6_found+bt7_found]) - Integer.parseInt(tiedostot.get(i).get(j)[4+rversion_found+bt2_found]));	
    	        				} else  {
    	        					//ei-F1345
    	        					//delta lämmitysvesi meno idx4 (BT2) - tulo idx5 (BT3), ei jaeta niin ei tarvita doublea
    	        					lampo_delta_kv.add(Integer.parseInt(tiedostot.get(i).get(j)[3+rversion_found+bt2_found]) - Integer.parseInt(tiedostot.get(i).get(j)[4+rversion_found+bt2_found]));
    	        				}
    	        				//cop
    	        				cop_kv.add(lastcop=laskeCOP(bt12.get(bt12.size()-1),bt3.get(bt3.size()-1),bt14.get(bt14.size()-1),bt17.get(bt17.size()-1),bt10.get(bt10.size()-1),cop_035, cop_045));
    	        				//delta keruu tulo idx8 (BT10) - tulo idx9 (BT11), ei jaeta niin ei tarvita doublea
            					lampo_delta_keruu.add(Integer.parseInt(tiedostot.get(i).get(j)[6+rversion_found+bt2_found+bt6_found+bt7_found]) - Integer.parseInt(tiedostot.get(i).get(j)[5+rversion_found+bt2_found+bt6_found+bt7_found]));
            					//etsitään matalin ja korkein bt10
            					//matalin
            					if (bt10_min > Integer.parseInt(tiedostot.get(i).get(j)[5+rversion_found+bt2_found+bt6_found+bt7_found])) {
            						bt10_min = Integer.parseInt(tiedostot.get(i).get(j)[5+rversion_found+bt2_found+bt6_found+bt7_found]);
            					}
            					//korkein
            					if (bt10_max < Integer.parseInt(tiedostot.get(i).get(j)[5+rversion_found+bt2_found+bt6_found+bt7_found])) {
            						bt10_max = Integer.parseInt(tiedostot.get(i).get(j)[5+rversion_found+bt2_found+bt6_found+bt7_found]);
            					}
            					//etsitään matalin ja korkein bt11
            					//matalin
            					if (bt11_min > Integer.parseInt(tiedostot.get(i).get(j)[6+rversion_found+bt2_found+bt6_found+bt7_found])) {
            						bt11_min = Integer.parseInt(tiedostot.get(i).get(j)[6+rversion_found+bt2_found+bt6_found+bt7_found]);
            					}
            					//korkein
            					if (bt11_max < Integer.parseInt(tiedostot.get(i).get(j)[6+rversion_found+bt2_found+bt6_found+bt7_found])) {
            						bt11_max = Integer.parseInt(tiedostot.get(i).get(j)[6+rversion_found+bt2_found+bt6_found+bt7_found]);
            					}

    	        				if (1<Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt2_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+f1345_ep15_found+f1345_bp_found])) {
    		        				//lisä kv:lle - jos asteminuutit ovat lisän puolella niin silloin sähköllä autetaan
    		        				kv_sahko.add(Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt2_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+f1345_ep15_found+f1345_bp_found]));
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
    	        			// puuttuu prio
    	        		} else if (tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found].equalsIgnoreCase("10")){
    	        			//0b00000111 = 10
    	        			//vastusaika, Relays PCA-Base on idx20, arvo 10 = tehdään käyttövettä suoralla sähköllä
    	        			//sähkövoima löytyy idx 15, jakaja on div_add (ei toistaiseksi tulosteta)
    	        			//korjaus 1.2.15: kaytto_kv tarvitaan koska kyseessä on käyttövesituotanto
    	        			kaytto_kv++;
    	        			kompr_kaynnissa=false;
    	        			//ikkuna.kirjoitaKonsolille("TIA: " + tiedostot.get(i).get(j)[11+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+f1345_ep15_found+f1345_bp_found] + "\n");
            				if (1<Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt2_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+f1345_ep15_found+f1345_bp_found])) {
    		        			kv_sahko.add(Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt2_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+f1345_ep15_found+f1345_bp_found]));
    	        			}
            				// Relays PCA-Base 2
    	        		} else if (tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found].equalsIgnoreCase("2")){
    	        			//0b00000100 = 2
    	        			//vastusaika, Relays PCA-Base on idx20, arvo 2 = lämmitetään suoralla sähköllä tai kiertopumppu pyörii
    	        			//sähkövoima löytyy idx 15, jakaja on div_add (ei toistaiseksi tulosteta)
    	        			if (!tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found].equalsIgnoreCase("0")) {
        	        			//korjaus 1.2.15: kaytto_la tarvitaan koska kyseessä on lämmitysvesituotanto
        	        			kaytto_la++;
    	        				//lämmitetään suoralla sähköllä
    		        			lamm_sahko.add(Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found]));
    	        			} else { //vain kiertopumppu
    	        				//tulkitaan lepotilaksi
    		        			kaytto_le++;
    		        			kompr_kaynnissa=false;
    	        			}
    	        			//ei ole kompura päällä
    	        			kompr_kaynnissa=false;
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
            				//cop.add(0);
            				cop.add(lastcop);
            				//cop.add((int)(((cop_035+cop_045)/2)*100.0));
            			}

            			//TEHO laskenta
            			//int bt12, int bt3, int bt14, int bt17, int bt10, int cop35, int cop45
            			//int bt12/bt2, int bt3, double ominaislampokapasiteetti_vesi, double virtaus
            			if (f1255_bf1_found != 0) {
            				teho.add(laskeTEHO(bt2.get(bt2.size()-1), bt3.get(bt3.size()-1), bf1.get(bf1.size()-1)));
            			} else {
            				teho.add(0);
            			}

            			//***********************************************
            			//
            			//
            			//F1345 EP15-Prio
            			//***********************************************
            			//LÄMMITYS
            			if (f1345_ep15_found!=0){
    	        			if(!tiedostot.get(i).get(j)[17+rversion_found+bt2_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found+f1345_gp1_found+f1345_gp2_found+bt20_found+bt21_found].equalsIgnoreCase(ep15_edellinentila)){
    	        				ep15_prosessinalku=0;
    	        			}
    	        			//otetaan EP15 ja PCA prio ylös tallennusta varten
                			ep15_prio.add(Integer.parseInt(tiedostot.get(i).get(j)[17+rversion_found+bt2_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found+f1345_gp1_found+f1345_gp2_found+bt20_found+bt21_found]));
                			//ikkuna.kirjoitaKonsolille(ep15_prio.get(ep15_prio.size()-1) + "\n");
                			if(tiedostot.get(i).get(j)[17+rversion_found+bt2_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found+f1345_gp1_found+f1345_gp2_found+bt20_found+bt21_found].equalsIgnoreCase("1")){
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
    	        					//delta lämmitysvesi meno idx9 (BT12) - tulo idx4 (BT3), ei jaeta niin ei tarvita doublea
    	        					ep15_lampo_delta_la.add(Integer.parseInt(tiedostot.get(i).get(j)[13+rversion_found+bt2_found+bt6_found+bt7_found]) - Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt2_found+bt6_found+bt7_found]));
    	        					//delta keruu
    	        					ep15_lampo_delta_keruu.add(Integer.parseInt(tiedostot.get(i).get(j)[12+rversion_found+bt2_found+bt6_found+bt7_found]) - Integer.parseInt(tiedostot.get(i).get(j)[11+rversion_found+bt2_found+bt6_found+bt7_found]));
    	        					//etsitään matalin ja korkein ep15-bt10
    	        					//matalin
    	        					if (ep15_bt10_min > Integer.parseInt(tiedostot.get(i).get(j)[11+rversion_found+bt2_found+bt6_found+bt7_found])) {
    	        						ep15_bt10_min = Integer.parseInt(tiedostot.get(i).get(j)[11+rversion_found+bt2_found+bt6_found+bt7_found]);
    	        					}
    	        					//korkein
    	        					if (ep15_bt10_max < Integer.parseInt(tiedostot.get(i).get(j)[11+rversion_found+bt2_found+bt6_found+bt7_found])) {
    	        						ep15_bt10_max = Integer.parseInt(tiedostot.get(i).get(j)[11+rversion_found+bt2_found+bt6_found+bt7_found]);
    	        					}
    	        					//etsitään matalin ja korkein ep15-bt11
    	        					//matalin
    	        					if (ep15_bt11_min > Integer.parseInt(tiedostot.get(i).get(j)[12+rversion_found+bt2_found+bt6_found+bt7_found])) {
    	        						ep15_bt11_min = Integer.parseInt(tiedostot.get(i).get(j)[12+rversion_found+bt2_found+bt6_found+bt7_found]);
    	        					}
    	        					//korkein
    	        					if (ep15_bt11_max < Integer.parseInt(tiedostot.get(i).get(j)[12+rversion_found+bt2_found+bt6_found+bt7_found])) {
    	        						ep15_bt11_max = Integer.parseInt(tiedostot.get(i).get(j)[12+rversion_found+bt2_found+bt6_found+bt7_found]);
    	        					}
    		        			}
    		        		//KÄYTTÖVESI
    	        			} else if (tiedostot.get(i).get(j)[17+rversion_found+bt2_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found+f1345_gp1_found+f1345_gp2_found+bt20_found+bt21_found].equalsIgnoreCase("2")) {
    	        				ep15_kaytto_kv++;
    	        				ep15_cop_kv.add(ep15_lastcop=laskeCOP(ep15_bt12.get(ep15_bt12.size()-1),ep15_bt3.get(ep15_bt3.size()-1),ep15_bt14.get(ep15_bt14.size()-1),ep15_bt17.get(ep15_bt17.size()-1),ep15_bt10.get(ep15_bt10.size()-1),cop_035, cop_045));
    		        			if(!ep15_kompr_kaynnissa){
    		        				ep15_kompr_kaynnistyksia++;
    		        				ep15_kompr_kaynnissa=true;
    		        			}
    		        			//delta lämmitysvesi meno idx9 (BT12) - tulo idx4 (BT3), ei jaeta niin ei tarvita doublea
            					ep15_lampo_delta_kv.add(Integer.parseInt(tiedostot.get(i).get(j)[13+rversion_found+bt2_found+bt6_found+bt7_found]) - Integer.parseInt(tiedostot.get(i).get(j)[10+rversion_found+bt2_found+bt6_found+bt7_found]));
            					//delta keruu
            					ep15_lampo_delta_keruu.add(Integer.parseInt(tiedostot.get(i).get(j)[12+rversion_found+bt2_found+bt6_found+bt7_found]) - Integer.parseInt(tiedostot.get(i).get(j)[11+rversion_found+bt2_found+bt6_found+bt7_found]));
    	        			
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
            				//cop.add(0);
            				ep15_cop.add(ep15_lastcop);
            				//cop.add((int)(((cop_035+cop_045)/2)*100.0));
            			}            			
            			
            			//prosessin alkutarkistuksen sekuntimäärä
            			prosessinalku=prosessinalku+(int)(mittausvali_ms/1000);
            			//ikkuna.kirjoitaKonsolille(prosessinalku + "\n");
            			//otetaan edellinen tila (idx20) muistiin
            			edellinentila = tiedostot.get(i).get(j)[15+rversion_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+bt2_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found];
            			if (f1345_ep15_found!=0){
            				//ep15 prosessinalku
            				ep15_prosessinalku=ep15_prosessinalku+(int)(mittausvali_ms/1000);
            				//otetaan edellinen EP15 tila muistiin
            				ep15_edellinentila = tiedostot.get(i).get(j)[17+rversion_found+bt2_found+bt6_found+bt7_found+bt25_found+bt71_found+bt50_found+bt51_found+bt53_found+bt54_found+f1345_ep15_found+f1345_bp_found+f1255_bf1_found+f1255_cfa_found+f1345_ep15pca_found+gp_found+f1345_gp1_found+f1345_gp2_found+bt20_found+bt21_found];
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
    		        			ikkuna.kirjoitaKonsolille("Ei peräkkäinen logirivi " + tiedostot.get(i).get(j)[0] + " " + tiedostot.get(i).get(j)[1] + " (" + (((logiaika.get(logiaika.size()-1).getTimeInMillis()) - (edellinenriviaika.getTimeInMillis())) - mittausvali_ms)/1000 + ") löydetty. Laita samaan hakemistoon vain peräkkäisiä logeja.\nTai ota \"jatkuvat logit\" -valinta pois.\n");
    		        			//yhteenvetoikkunan numerot
    		        			ikkuna.paivitaNumerot(kaytto_la, kaytto_kv, kaytto_le, kv_sahko, mittausvali_ms,
    		    						kokaika, kompr_kaynnistyksia, bt50, bt1, cs,
    		    						lampo_kv, lampo_delta_la, lampo_delta_kv, lampo_delta_keruu, bt67_label, lamm_sahko,
    		    						bt10_min, bt10_max, bt11_min, bt11_max, cop_la, cop_kv, cfa, kaytto_ua, kaytto_la_flm,
    		    						kaytto_kv_flm);
    		        			ikkuna.setProgress(100);
    		        			//poistetaan viimeinen aika jottei tule hassuja graafeja
    		        			logiaika.remove(logiaika.size()-1);
    		            		//Käyrien nayttöä varten
    		            		teeKayraTaulukko();
    		            		if (f1345_ep15_found != 0 && ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345")) {
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
    	        		
                    	if(j==1){
                    		//mittausväli tarvitaan laskemiseen sekä jatkuvien logien tarkistukseen
                    		//ikkuna.kirjoitaKonsolille("lokiaika: " + (edellinenriviaika.getTimeInMillis() - logiaika.get(0).getTimeInMillis()) + "\n");
                    		mittausvali_ms = logiaika.get(1).getTimeInMillis() - logiaika.get(0).getTimeInMillis();
                    	}
            		} //rivin loppu
            } //tiedoston loppu
    	        ikkuna.setProgress((i+1)*100/tiedostot.size());
    	      //kirjoitetaan lopetusaika
    	        LogintutkijaGUI.setAikaLopetus("  <->  " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(logiaika.get(logiaika.size()-1).getTime()));

    		}
    		//yhteenvetoikkunan numerot
    		ikkuna.paivitaNumerot(kaytto_la, kaytto_kv, kaytto_le, kv_sahko, mittausvali_ms,
    				kokaika, kompr_kaynnistyksia, bt50, bt1, cs,
    				lampo_kv, lampo_delta_la, lampo_delta_kv, lampo_delta_keruu, bt67_label,
    				lamm_sahko, bt10_min, bt10_max, bt11_min, bt11_max, cop_la, cop_kv, cfa, kaytto_ua,
    				kaytto_la_flm, kaytto_kv_flm);

    		//Käyrien nayttöä varten
    		teeKayraTaulukko();
    		
    		if (f1345_ep15_found != 0 && ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345")) {
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
        						kaytto_la_flm, kaytto_kv_flm);
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
        						kaytto_ua, kaytto_la_flm, kaytto_kv_flm);
                		ikkuna.naytaGraafi(ikkuna.getLa(), ikkuna.getSal(), ikkuna.getUal(), ikkuna.getLaFlm(), ikkuna.getKvFlm(), ikkuna.getKv(), ikkuna.getSav(), ikkuna.getLe());
                	}
                }
            });

    	    	return mlpfound + " " + logiaika.size() + " tietueella. Login alku: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(logiaika.get(0).getTime()) + ", loppu: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(logiaika.get(logiaika.size()-1).getTime()) + ".\n";
    	}
        
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
        		        					(5.0/2) +															//ΔTLM
        		        					Math.abs(((double)bt14-(double)bt12))) -							//ΔTL
        		        					(
        		        							bt10 -												//TLÄH
        		        							Math.abs(
        		        									((double)bt17 - (double)bt10)						//ΔTH
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
			if (bt2_found==1 && !bt2_nolla) {
				kayra_taulukko.add(2,bt2);
				kayra_taulukko_nimet.add(2,"meno bt2");
			} else {
				kayra_taulukko.add(2,bt12);
				kayra_taulukko_nimet.add(2,"meno bt12");
			}
			kayra_taulukko.add(3,bt3);
			kayra_taulukko_nimet.add(3,"tulo bt3");
			kayra_taulukko.add(4,bt25);
			kayra_taulukko_nimet.add(4,"meno bt25");
			kayra_taulukko.add(5,bt10);
			kayra_taulukko_nimet.add(5,"keruu t. bt10");
			kayra_taulukko.add(6,bt11);
			kayra_taulukko_nimet.add(6,"keruu m. bt11");
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
    		kayra_taulukko.add(26,lampo_kv);
    		kayra_taulukko_nimet.add(26,"kv ylälämpö");
    		kayra_taulukko.add(27,bt6);
    		kayra_taulukko_nimet.add(27,"kv alalämpö");
			//if (ikkuna.getLblMLPMalli().getText().equalsIgnoreCase("F1345")) {
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
        }
        
        // muunnaCTC
    	// mäpätään CTC:n arvot Niben vastaaviin analyysiä varten
        //
    	public ArrayList<String []> muunnaCTC(ArrayList<String[]> logtable) {
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
    		//Alkujutut jotta menee läpi
    		String [] eka = new String [23];
    		eka[0]="Divisors";
    		for (int i=1;i<23;i++){
    			eka[i]="10";
    		}
    		file.add(eka);
    		String [] toka = new String [23];
    		toka[0]="Date";toka[1]="CTC";toka[2]="BT2";toka[3]="BT6";toka[4]="R-version";toka[5]="GP1-speed EP14";
    		toka[6]="BT7";
    		for (int i=7;i<23;i++){
    			toka[i]="F" + i;
    		}
    		file.add(toka);
			String mmb = "";
    		for (int i=0;i<logtable.size();i++){
    			//Niben logissa ainakin 23 elementtiä
        		String [] line = new String[23];
    			String rs = logtable.get(i)[0].trim();
    			String prio = "0";
        		if (rs.equals("")) continue;
    			String yyyy = rs.substring(0,4);
    			String mo = rs.substring(4,6);
    			String dd = rs.substring(6,8);
    			String hh = rs.substring(9,11);
    			String mm = rs.substring(12,14);

    			String ss = "00";
        		//ikkuna.kirjoitaKonsolille("PVM: " + " " + yyyy + "-" + mo + "-" + dd + " " + hh + ":" + mm + "\n" +
        				//"TIME: " + logtable.get(i)[0] + " len.: " + logtable.get(i)[0].length() +  "\n" +
        				//"DEBUG: " + resultString.substring(0,14) + " len.: " + logtable.get(i)[0].length() +  "\n" +
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
        		line[5] = "" + (int)(Double.parseDouble(logtable.get(i)[7])*10);
        		//BT3
        		line[6] = "" + (int)(Double.parseDouble(logtable.get(i)[8])*10);
        		//BT6
        		line[7] = "" + (int)(Double.parseDouble(logtable.get(i)[4])*10);
        		//BT7
        		line[8] = "" + (int)(Double.parseDouble(logtable.get(i)[2])*10);
        		//BT10
        		line[9] = "" + (int)(Double.parseDouble(logtable.get(i)[19])*10);
        		//BT11
        		//ikkuna.kirjoitaKonsolille("BT11: " + (int)(Double.parseDouble(logtable.get(i)[20])*10) + "\n");
        		line[10] = "" + (int)(Double.parseDouble(logtable.get(i)[20])*10);
        		//BT12
        		line[11] = "" + (int)(Double.parseDouble(logtable.get(i)[7])*10);
        		//BT14
        		line[12] = "" + (int)(Double.parseDouble(logtable.get(i)[25])*10);
        		//BT17
        		line[13] = "" + (int)(Double.parseDouble(logtable.get(i)[26])*10);
        		//Add.step
        		line[14] = "" + (int)(Double.parseDouble(logtable.get(i)[10])*100);
        		//Alarm
        		line[15] = logtable.get(i)[14];
        		//Calc. supply
        		line[16] = "0";
        		//Degree mins
        		line[17] = "0";
        		//BT1 avg
        		line[18] = "0";
        		//PCA

        		if (logtable.get(i)[33].equals("ON")) {
        			if (logtable.get(i)[1].equals("1")) {
            			line[19] = "7";
            			prio="1";
        			} else {
        				line[19] = "15";
        				prio="2";
        			}
        		} else {
        			line[19] = "0";
        		}
        		//GP1 lämmönjako
        		line[20] = "" + (int)(Double.parseDouble(logtable.get(i)[21])*1);
        		//GP2 brine
        		line[21] = "" + (int)(Double.parseDouble(logtable.get(i)[22])*10);
        		//Prio
        		line[22] = prio;
        		
        		file.add(line);
    		}
    		return file;
    	}
        
        // tabulaattoriJakaja
    	// tabulaattorierotetut arvot ArrayListiin
        //
    	public ArrayList<String []> tabulaattoriJakaja(String logfile, char separator) throws IOException{
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
	public void setHDDGet(String enabled) {
		if (enabled.equals("1")) {
			this.hdd = true;
		}
	}
	public boolean getHDDGet() {
		return hdd;
	}
}
