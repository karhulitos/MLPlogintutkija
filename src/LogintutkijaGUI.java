package logintutkija;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.Rotation;

/************************************
 * MVC VIEW: LogintutkijaGUI
 * 
 ************************************/

public class LogintutkijaGUI extends JPanel
	implements ActionListener, PropertyChangeListener {
    /******************
     * LUOKKAMUUTTUJAT
     ******************/
	private double la, kv, le, sav, sal, ual, la_flm, kv_flm;
	//yleiset
	private static LogintutkijaOhjain ohjain;
	private static final long serialVersionUID = 1L;
	private static JPanel ylapaneeli, itapaneeli, lansipaneeli, keskipaneeli;
	private static JTextArea konsoli;
    JButton avaaLogit, luoKayrat, haeDB;
    public JProgressBar edistymisPalkki;
	private int tietolahde = 0; //0=logi, 1=db
	private static JComboBox<String> valintalaatikko = new JComboBox<String>();
	private static JCheckBox valNappulaEpajatkuvatLogit = new JCheckBox("jatkuvat logit");
	private JRadioButton radioNappulaEP14 = new JRadioButton("EP14");
	private JRadioButton radioNappulaEP15 = new JRadioButton("EP15");
	private static boolean jatkuvatlogit = false;
	private static boolean autoupdate = false;
	//Yhteenvetosivun kentät
	private JLabel lblMLPMalli = new JLabel("MLP");
	private JLabel suos5 = new JLabel("-2...-5°C");
	private static String alarm = "";
	private static String lokiVersio = "0000";
	private static String lokiRVersio = "0";
	private static String aikaAloitus = "Aika";
	private static String aikaLopetus = "";
	private JLabel lblAlarm = new JLabel(alarm);
	private JLabel lblLokiVersio = new JLabel("v" + lokiVersio + "R" + lokiRVersio);
	private JLabel lblLammonKeruu = new JLabel(" ∆ lämmönkeruupiiri");
	private static JLabel lblMittausvaliKentta = new JLabel("0 min");
	private static JLabel lblKayntiaikaKentta = new JLabel("0");
	private static JLabel lblKayntiaikaVrkKentta = new JLabel("00:00:00");
	private static JLabel lblKayntiaikaProsKentta = new JLabel("0%");
	private static JLabel lblKayntiaikaUAKentta = new JLabel("0");
	private static JLabel lblKayntiaikaUAVrkKentta = new JLabel("00:00:00");
	private static JLabel lblKayntiaikaUAProsKentta = new JLabel("0%");
	private static JLabel lblKayntiaikaKVKentta = new JLabel("0");
	private static JLabel lblKayntiaikaKVVrkKentta = new JLabel("00:00:00");
	private static JLabel lblKayntiaikaKVProsKentta = new JLabel("0%");
	private static JLabel lblKayntiaikaYHTKentta = new JLabel("0");
	private static JLabel lblKayntiaikaYHTVrkKentta = new JLabel("00:00:00");
	private static JLabel lblKayntiaikaYHTProsKentta = new JLabel("0%");
	private static JLabel lblSahkoYHTkWhKentta = new JLabel("0 kWh");
	private static JLabel lblLepoaikaKentta = new JLabel("0");
	private static JLabel lblLepoaikaVrkKentta = new JLabel("00:00:00");
	private static JLabel lblLepoaikaProsKentta = new JLabel("100%");
	private static JLabel lblSahkoKVKentta = new JLabel("0");
	private static JLabel lblSahkoKVVrkKentta = new JLabel("00:00:00");
	private static JLabel lblSahkoKVProsKentta = new JLabel("0%");
	private static JLabel lblSahkoKVkWhKentta = new JLabel("0 kWh");
	private static JLabel lblSahkoLammKentta = new JLabel("0");
	private static JLabel lblSahkoLammVrkKentta = new JLabel("00:00:00");
	private static JLabel lblSahkoLammProsKentta = new JLabel("0%");
	private static JLabel lblSahkoLammkWhKentta = new JLabel("0 kWh");
	private static JLabel lblJaksoKentta = new JLabel("0");
	private static JLabel lblJaksoProsKentta = new JLabel("100%");
	private static JLabel lblKaynnistyksiaKentta = new JLabel("0 kpl");
	private static JLabel lblKaynnistyksiaVRKKentta = new JLabel("0 kpl");
	private static JLabel lblKayntiKeskimKentta = new JLabel("00:00:00");
	private static JLabel lblLepoKeskimKentta = new JLabel("00:00:00");
	private static JLabel lblLampoSisaAvgKentta = new JLabel("0°C");
	private static JLabel lblLampoSisaMinKentta = new JLabel("0°C");
	private static JLabel lblLampoSisaMaxKentta = new JLabel("0°C");
	private static JLabel lblLampoUlkoAvgKentta = new JLabel("0°C");
	private static JLabel lblLampoUlkoMinKentta = new JLabel("0°C");
	private static JLabel lblLampoUlkoMaxKentta = new JLabel("0°C");
	private static JLabel lblLampoPyyntiAvgKentta = new JLabel("0°C");
	private static JLabel lblLampoPyyntiMinKentta = new JLabel("0°C");
	private static JLabel lblLampoPyyntiMaxKentta = new JLabel("0°C");
	private static JLabel lblLampoKVAvgKentta = new JLabel("0°C");
	private static JLabel lblLampoKVMinKentta = new JLabel("0°C");
	private static JLabel lblLampoKVMaxKentta = new JLabel("0°C");
	private static JLabel lblDeltaLampoAvgKentta = new JLabel("0°C");
	private static JLabel lblDeltaLampoMinKentta = new JLabel("0°C");
	private static JLabel lblDeltaLampoMaxKentta = new JLabel("0°C");		
	private static JLabel lblDeltaKVAvgKentta = new JLabel("0°C");
	private static JLabel lblDeltaKVMinKentta = new JLabel("0°C");
	private static JLabel lblDeltaKVMaxKentta = new JLabel("0°C");			
	private static JLabel lblDeltaKeruuAvgKentta = new JLabel("0°C");
	private static JLabel lblDeltaKeruuMinKentta = new JLabel("0°C");
	private static JLabel lblDeltaKeruuMaxKentta = new JLabel("0°C");
	private static JLabel lblKVYlaAlaKentta = new JLabel("");
	private static JLabel lblBT10MinKentta = new JLabel("0°C");
	private static JLabel lblBT10MaxKentta = new JLabel("0°C");
	private static JLabel lblBT11MinKentta = new JLabel("0°C");
	private static JLabel lblBT11MaxKentta = new JLabel("0°C");
	private static JLabel lblCOPLaAvgKentta = new JLabel("0.0");
	private static JLabel lblCOPLaMinKentta = new JLabel("0.0");
	private static JLabel lblCOPLaMaxKentta = new JLabel("0.0");
	private static JLabel lblCOPKvAvgKentta = new JLabel("0.0");
	private static JLabel lblCOPKvMinKentta = new JLabel("0.0");
	private static JLabel lblCOPKvMaxKentta = new JLabel("0.0");
	private static JLabel lblCFAAvgKentta = new JLabel("0 Hz");
	private static JLabel lblCFAMinKentta = new JLabel("0 Hz");
	private static JLabel lblCFAMaxKentta = new JLabel("0 Hz");
	private static int alarm_save = 0;
	private static ArrayList<Alarm> alarmlist = new ArrayList<Alarm>();
	private static boolean b2b_alarm =  false;
	
	//DB
	String tietokanta_dbms, tietokanta_osoite, tietokanta_nimi, tietokanta_kayttaja, tietokanta_salasana = "";
    String tietokanta_url = "jdbc:mysql://" + tietokanta_osoite + ":3306/" + tietokanta_nimi;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String tietokanta_pvm_mista = "";
    String tietokanta_pvm_mihin = "";
    boolean tietokanta_omahakuaika = false;
    boolean hae = false;
    String[] ajurit = {"SQLite","MySQL"};
    @SuppressWarnings({ "rawtypes", "unchecked" })
	JComboBox combo = new JComboBox(ajurit);
    Boolean ekayhteys = true;
    static String vtf = "nibe";
	
	public LogintutkijaGUI() {
		super(new BorderLayout());
		alustaKomponentit();
	}
	
	public Date jokuaikasitten() {
		//return new Date(nyt().getTime() - ((1000 * 60 * 60 * 24 * 1)+1000*60)); // 1vrk + 1 minuutti
		//1 vrk taakse
		return new Date(nyt().getTime() - (1000 * 60 * 60 * 24 * 1));
	}
	
	public Date nyt() {
		return new Date();
	}
	
	public void alustaKomponentit() {
		//Maalämpöfoorumin nimimerkin Bruno taulukkolaskentatiedoston hengessä näytettävät suureet
        //Luodaan käyttöliittymän nappulat
        avaaLogit = new JButton("Logit");
        //nappulalle kuuntelija jotta tehdään jotain sen painamisen jälkeen
        avaaLogit.addActionListener(this);
        
        haeDB = new JButton("DB");
        //nappulalle kuuntelija jotta tehdään jotain sen painamisen jälkeen
        haeDB.addActionListener(this);

        luoKayrat = new JButton("Trendit");
        //nappulalle kuuntelija jotta tehdään jotain sen painamisen jälkeen
        luoKayrat.addActionListener(this);
        luoKayrat.setEnabled(false);
        
        //progressBar eli edistymisPalkki
        edistymisPalkki = new JProgressBar(0, 100);
        edistymisPalkki.setValue(0);
        edistymisPalkki.setStringPainted(true);
        edistymisPalkki.setVisible(true);

        //"konsoli" - tähän tulostetaan ohjelman viestit, ei popuppeja
        konsoli = new JTextArea(8, 50);
        konsoli.setMargin(new Insets(5,5,5,5));
        konsoli.setEditable(false);
        //tekstialueen autoscrollaus pohjaan
        DefaultCaret caret = (DefaultCaret) konsoli.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        //paneelit
        ylapaneeli = new JPanel();
        ylapaneeli.setLayout(new BoxLayout(ylapaneeli, BoxLayout.LINE_AXIS));
        itapaneeli = new JPanel();
        itapaneeli.setLayout(new GridLayout(2,1));
        //itapaneeli.setLayout(new BorderLayout(2,1));
        lansipaneeli = new JPanel();
        lansipaneeli.setLayout(new GridLayout(33,1));
        keskipaneeli = new JPanel();
        keskipaneeli.setLayout(new GridLayout(33,4));
        
        //yläpaneeli, nappulat ja etenemisPötkö
        ylapaneeli.add(avaaLogit);
        ylapaneeli.add(haeDB);
        ylapaneeli.add(luoKayrat);
        ylapaneeli.add(new JLabel("  "));
        ylapaneeli.add(edistymisPalkki);

        
        //rajat
        //ylapaneeli.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //keskipaneeli.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //lansipaneeli.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //itapaneeli.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        //LÄNSIPANEELI
        //west 1 per rivi
        lansipaneeli.add(new JLabel(""));
        JLabel otsikko = new JLabel("Mitt.väli/versio/hälytys: ");
        //otsikko.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lansipaneeli.add(otsikko);
        //jatkuvien logien tarkistusnappula ja sen actionlistener
        valNappulaEpajatkuvatLogit.setSelected(false);
        lansipaneeli.add(valNappulaEpajatkuvatLogit);
        valNappulaEpajatkuvatLogit.addItemListener(new ItemListener() {
        	
            @Override
			public void itemStateChanged(ItemEvent e) {
            	if (!valNappulaEpajatkuvatLogit.isSelected()){
            		konsoli.append("Jatkuvien logien tarkistus pois.\n");
            		jatkuvatlogit=false;
            	}
            	else if (valNappulaEpajatkuvatLogit.isSelected()){
            		konsoli.append("Jatkuvien logien tarkistus päällä.\n");
            		jatkuvatlogit=true;
            	}
            }
        });

        lansipaneeli.add(new JLabel(""));
        lansipaneeli.add(new JLabel(""));
        lansipaneeli.add(new JLabel("Lämmitys: "));
        lansipaneeli.add(new JLabel(" - josta allasta: "));
        lansipaneeli.add(new JLabel(" - josta lisäystä: "));
        lansipaneeli.add(new JLabel("Käyttövesi: "));
        lansipaneeli.add(new JLabel(" - josta lisäystä: "));
        lansipaneeli.add(new JLabel("Käynti yhteensä:"));
        lansipaneeli.add(new JLabel("Lepoaika:"));
        lansipaneeli.add(new JLabel("Mittausjakso:"));
        lansipaneeli.add(new JLabel(""));
        
        lansipaneeli.add(new JLabel("Kompr. käynnistyksiä:"));
        lansipaneeli.add(new JLabel("Kompr. käynti keskim.:"));
        lansipaneeli.add(new JLabel("Kompr. lepo keskim.:"));
        lansipaneeli.add(new JLabel(""));
        //COP
        lansipaneeli.add(new JLabel("COP"));
        lansipaneeli.add(new JLabel(" lämmitys"));
        lansipaneeli.add(new JLabel(" KV"));
        lansipaneeli.add(new JLabel(""));
        
        lansipaneeli.add(new JLabel("Mittauksia"));
        lansipaneeli.add(new JLabel(" huonelämpö"));
        lansipaneeli.add(new JLabel(" ulkolämpö"));
        lansipaneeli.add(new JLabel(" lämmitysveden pyyntö"));
        lansipaneeli.add(new JLabel(" käyttöveden lämpö"));

        lansipaneeli.add(new JLabel(" ∆ lämmitysvesi"));
        lansipaneeli.add(new JLabel(" ∆ käyttövesi"));
        lansipaneeli.add(lblLammonKeruu);
        lansipaneeli.add(new JLabel(" keruu tulo BT10"));
        lansipaneeli.add(new JLabel(" keruu meno BT11"));
        lansipaneeli.add(new JLabel(" kompr. taajuus"));
        
        //KESKIPANEELI
        //tyhjä rivi
	    keskipaneeli.add(new JLabel(""));
	    keskipaneeli.add(new JLabel(""));
	    keskipaneeli.add(new JLabel(""));
	    keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
	    
        //4 saraketta per rivi
        keskipaneeli.add(lblMittausvaliKentta);
        keskipaneeli.add(lblLokiVersio);
        lblAlarm.setForeground(Color.RED);
        keskipaneeli.add(lblAlarm);
        keskipaneeli.add(new JLabel(""));
//        keskipaneeli.add(new JLabel("s-teho:"));
        //lisätehon valinta yhteenvetoa varten
        //Nibe loggaakin kaikki tehot aivan oikein
//        int count = 0;
//        for (int i = 0; i < 2; i++)
//        	alasvetoTeho.addItem(teho[count++]);
//        
//        keskipaneeli.add(alasvetoTeho);
        radioNappulaEP14.setSelected(true);
        radioNappulaEP14.setEnabled(false);
        radioNappulaEP15.setEnabled(false);
        keskipaneeli.add(radioNappulaEP14);
        keskipaneeli.add(radioNappulaEP15);
	    ButtonGroup valintaKompressori = new ButtonGroup( );
	    valintaKompressori.add(radioNappulaEP14);
	    valintaKompressori.add(radioNappulaEP15);
    	
	    JLabel kone = new JLabel("kone on");
	    kone.setForeground(Color.gray);
        keskipaneeli.add(kone);
        lblMLPMalli.setForeground(Color.gray);
        keskipaneeli.add(lblMLPMalli);
        //tyhjä rivi
	    keskipaneeli.add(new JLabel(""));
	    keskipaneeli.add(new JLabel(""));
	    keskipaneeli.add(new JLabel(""));
	    keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
	    
        keskipaneeli.add(new JLabel("vrk"));
        keskipaneeli.add(new JLabel("/vrk"));
        keskipaneeli.add(new JLabel("%"));
        keskipaneeli.add(new JLabel("lisäys")); //tarkoituksella tyhjä solu
        
        keskipaneeli.add(lblKayntiaikaKentta);
        keskipaneeli.add(lblKayntiaikaVrkKentta);
        keskipaneeli.add(lblKayntiaikaProsKentta);
        keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
        
        keskipaneeli.add(lblKayntiaikaUAKentta);
        keskipaneeli.add(lblKayntiaikaUAVrkKentta);
        keskipaneeli.add(lblKayntiaikaUAProsKentta);
        keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
        
        keskipaneeli.add(lblSahkoLammKentta);
        keskipaneeli.add(lblSahkoLammVrkKentta);
        keskipaneeli.add(lblSahkoLammProsKentta);
        keskipaneeli.add(lblSahkoLammkWhKentta);
        
        keskipaneeli.add(lblKayntiaikaKVKentta);
        keskipaneeli.add(lblKayntiaikaKVVrkKentta);
        keskipaneeli.add(lblKayntiaikaKVProsKentta);
        keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
        
        keskipaneeli.add(lblSahkoKVKentta);
        keskipaneeli.add(lblSahkoKVVrkKentta);
        keskipaneeli.add(lblSahkoKVProsKentta);
        keskipaneeli.add(lblSahkoKVkWhKentta);
        
        keskipaneeli.add(lblKayntiaikaYHTKentta);
        keskipaneeli.add(lblKayntiaikaYHTVrkKentta);
        keskipaneeli.add(lblKayntiaikaYHTProsKentta);
        keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
        
        keskipaneeli.add(lblLepoaikaKentta);
        keskipaneeli.add(lblLepoaikaVrkKentta);
        keskipaneeli.add(lblLepoaikaProsKentta);
        keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
        
        keskipaneeli.add(lblJaksoKentta);
        keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
        keskipaneeli.add(lblJaksoProsKentta);
        keskipaneeli.add(lblSahkoYHTkWhKentta);
        
        keskipaneeli.add(new JLabel(""));//tarkoituksella tyhjä rivi
        keskipaneeli.add(new JLabel(""));
        keskipaneeli.add(new JLabel(""));
        keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
        
        keskipaneeli.add(lblKaynnistyksiaKentta);
        keskipaneeli.add(lblKaynnistyksiaVRKKentta);
        keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
        JLabel suos = new JLabel("huomioita:");
        suos.setForeground(Color.gray);
        keskipaneeli.add(suos);
        
        keskipaneeli.add(lblKayntiKeskimKentta);

        keskipaneeli.add(new JLabel(""));
        keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
        JLabel suos2 = new JLabel(">20min");
        suos2.setForeground(Color.gray);
        keskipaneeli.add(suos2);
        
        keskipaneeli.add(lblLepoKeskimKentta);
        keskipaneeli.add(new JLabel(""));
        keskipaneeli.add(new JLabel(""));
        keskipaneeli.add(new JLabel(""));
        
        keskipaneeli.add(new JLabel(""));//tarkoituksella tyhjä rivi
        keskipaneeli.add(new JLabel(""));
        keskipaneeli.add(new JLabel(""));
        keskipaneeli.add(new JLabel(""));
        
        //COP
        keskipaneeli.add(new JLabel("ka"));
        keskipaneeli.add(new JLabel("min"));
        keskipaneeli.add(new JLabel("max"));
        JLabel suos8 = new JLabel("muuta ase-");
        suos8.setForeground(Color.gray);
        keskipaneeli.add(suos8);
        //lämmitys
        keskipaneeli.add(lblCOPLaAvgKentta);
        keskipaneeli.add(lblCOPLaMinKentta);
        keskipaneeli.add(lblCOPLaMaxKentta);
        JLabel suos6 = new JLabel("tuksista");
        suos6.setForeground(Color.gray);
        keskipaneeli.add(suos6);
        //KV
        keskipaneeli.add(lblCOPKvAvgKentta);
        keskipaneeli.add(lblCOPKvMinKentta);
        keskipaneeli.add(lblCOPKvMaxKentta);
        JLabel suos7 = new JLabel("cop_03/45");
        suos7.setForeground(Color.gray);
        keskipaneeli.add(suos7);
        
        //tyhjä rivi
        keskipaneeli.add(new JLabel(""));
        keskipaneeli.add(new JLabel(""));
        keskipaneeli.add(new JLabel(""));
        //JLabel suos9 = new JLabel("pumpullesi");
        //suos9.setForeground(Color.gray);
        //keskipaneeli.add(suos9);
        keskipaneeli.add(new JLabel(""));
        
        //Lämpötiloja
        keskipaneeli.add(new JLabel("ka"));
        keskipaneeli.add(new JLabel("min"));
        keskipaneeli.add(new JLabel("max"));
        keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
        keskipaneeli.add(lblLampoSisaAvgKentta);
        keskipaneeli.add(lblLampoSisaMinKentta);
        keskipaneeli.add(lblLampoSisaMaxKentta);
        keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
        keskipaneeli.add(lblLampoUlkoAvgKentta);
        keskipaneeli.add(lblLampoUlkoMinKentta);
        keskipaneeli.add(lblLampoUlkoMaxKentta);
        keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
        keskipaneeli.add(lblLampoPyyntiAvgKentta);
        keskipaneeli.add(lblLampoPyyntiMinKentta);
        keskipaneeli.add(lblLampoPyyntiMaxKentta);        
        keskipaneeli.add(new JLabel("")); //tarkoituksella tyhjä solu
        keskipaneeli.add(lblLampoKVAvgKentta);
        keskipaneeli.add(lblLampoKVMinKentta);
        keskipaneeli.add(lblLampoKVMaxKentta);        
        lblKVYlaAlaKentta.setForeground(Color.gray);
        keskipaneeli.add(lblKVYlaAlaKentta);
        keskipaneeli.add(lblDeltaLampoAvgKentta);
        keskipaneeli.add(lblDeltaLampoMinKentta);
        keskipaneeli.add(lblDeltaLampoMaxKentta);
        JLabel suos3 = new JLabel("5...10°C");
        suos3.setForeground(Color.gray);
        keskipaneeli.add(suos3);
        keskipaneeli.add(lblDeltaKVAvgKentta);
        keskipaneeli.add(lblDeltaKVMinKentta);
        keskipaneeli.add(lblDeltaKVMaxKentta); 
        JLabel suos4 = new JLabel("8...10°C");
        suos4.setForeground(Color.gray);
        keskipaneeli.add(suos4);
        keskipaneeli.add(lblDeltaKeruuAvgKentta);
        keskipaneeli.add(lblDeltaKeruuMinKentta);
        keskipaneeli.add(lblDeltaKeruuMaxKentta); 
        suos5.setForeground(Color.gray);
        keskipaneeli.add(suos5);
        //bt10 minmax
        keskipaneeli.add(new JLabel(""));
        keskipaneeli.add(lblBT10MinKentta);
        keskipaneeli.add(lblBT10MaxKentta);
        keskipaneeli.add(new JLabel(""));
        //bt11 minmax
        keskipaneeli.add(new JLabel(""));
        keskipaneeli.add(lblBT11MinKentta);
        keskipaneeli.add(lblBT11MaxKentta);
        keskipaneeli.add(new JLabel(""));
        //compr. freq. act.
        keskipaneeli.add(lblCFAAvgKentta);
        keskipaneeli.add(lblCFAMinKentta);
        keskipaneeli.add(lblCFAMaxKentta);
        keskipaneeli.add(new JLabel(""));
        
        //ITAPANEELI
        add(ylapaneeli, BorderLayout.PAGE_START);
        add(itapaneeli, BorderLayout.LINE_END);
        add(keskipaneeli, BorderLayout.CENTER);
        add(lansipaneeli, BorderLayout.LINE_START);
        add(new JScrollPane(konsoli), BorderLayout.PAGE_END);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        konsoli.append("Valitse hakemisto jossa peräkkäisiä lämpöpumpun logeja tai tee tietokantahaku.\n");
        naytaGraafi(0, 0, 0, 0, 0, 0, 0, 100);
        JTextArea txt = new JTextArea("\n\nKäyriä voi zoomata valitsemalla hiirellä alueen tai laittamalla kursori johonkin kohtaan ja lähentämällä rullalla. " +
        		"Käyrän voit palauttaa valitsemalla negatiivisen\nalueen oikea alakulma -> vasen yläkulma. " +
        		"Ajassa pystyy liikkumaan laittamalla hiiren aikajanalla eri kohtaan ja zoomaamalla hiirellä" +
        		" sisään ja toisessa ajassa ulos.\n\n" +
        		"",10,20);
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setBackground(new Color(0, 0, 0, 0));
        txt.getCaret().setVisible(false);
        //tällä sai kursorin pois vilkkumasta ja muutenkin teksti on vaan teksti eikä kenttä
        txt.setEnabled(false);
        txt.setDisabledTextColor(Color.black);
        txt.setMargin(new Insets(0,15,0,0));
        itapaneeli.add(txt);
        
//        JTextArea txt2 = new JTextArea("\nJee",10,1);
//        itapaneeli.add(txt2);
        //Lisätään panelit Borderlayouttiin
        lansipaneeli.setPreferredSize(new java.awt.Dimension(200, 720));
        itapaneeli.setPreferredSize(new java.awt.Dimension(300, 720));

	}
	
    /************************************
     * TAPAHTUMANKÄSITTELYSÄIKEEN METODIT
     ************************************/
    // setProgress
	// päivitetään yhteenvetonäytön numerot 
    //	
	public void setProgress(int progress) {
		      edistymisPalkki.setValue(progress);
	}
	
    // paivitaNumerot
	// päivitetään yhteenvetonäytön numerot 
    //
	public void paivitaNumerot(int kaytto_la,	int kaytto_kv,
			int kaytto_le, ArrayList<Integer> kv_sahko, double mittausvali_ms, int kokaika,
			int kompr_kaynnistyksia, ArrayList<Integer> lampo_sisa, ArrayList<Integer> lampo_ulko,
			ArrayList<Integer> lampo_pyynti, ArrayList<Integer> lampo_kv, ArrayList<Integer> lampo_delta_la,
			ArrayList<Integer> lampo_delta_kv, ArrayList<Integer> lampo_delta_keruu, String bt67_label, ArrayList<Integer> lamm_sahko,
			int bt10_min, int bt10_max, int bt11_min, int bt11_max, ArrayList<Integer> cop_la, ArrayList<Integer> cop_kv,
			ArrayList<Integer> cfa, int kaytto_ua, int kaytto_la_flm, int kaytto_kv_flm, ArrayList<Integer> alarms
			) {
		int mittausvali = (int)(Math.round(mittausvali_ms/1000*10.0)/10.0)*1000;


		
		//KV varaajan ylä- vai alamittaus
		lblKVYlaAlaKentta.setText(bt67_label);
		
		//käyntiajat lämmitys				
		double kaynti_la_vrk = ((double)kaytto_la * (double)mittausvali / 60.0 / 24 / 1000 / 60);
		double kaynti_la_per_vrk = ((double)kaytto_la * (double)mittausvali / 60.0 / 24) / (((double)kokaika * (double)mittausvali) / 60.0 / 24);
		lblKayntiaikaKentta.setText(String.valueOf((double)Math.round(kaynti_la_vrk * 1000) / 1000));
		lblKayntiaikaVrkKentta.setText(parseKello(kaynti_la_per_vrk));
		lblKayntiaikaProsKentta.setText(String.valueOf(la=(double)Math.round(kaynti_la_per_vrk * 100 * 10) / 10 ) + "%");

		//käyntiajat käyttövesi
		double kaynti_kv_vrk = ((double)kaytto_kv * (double)mittausvali / 60.0 / 24 / 1000 / 60);
		double kaynti_kv_per_vrk = ((double)kaytto_kv * (double)mittausvali / 60.0 / 24) / (((double)kokaika * (double)mittausvali) / 60.0 / 24);
		lblKayntiaikaKVKentta.setText(String.valueOf((double)Math.round(kaynti_kv_vrk * 1000) / 1000));
		lblKayntiaikaKVVrkKentta.setText(parseKello(kaynti_kv_per_vrk));
		lblKayntiaikaKVProsKentta.setText(String.valueOf(kv=(double)Math.round(kaynti_kv_per_vrk * 100 * 10) / 10 ) + "%");
		
		//kayntiajat yhteensä
		lblKayntiaikaYHTKentta.setText(String.valueOf((double)Math.round((kaynti_kv_vrk + kaynti_la_vrk) * 1000) / 1000));
		lblKayntiaikaYHTVrkKentta.setText(parseKello(kaynti_kv_per_vrk + kaynti_la_per_vrk));
		lblKayntiaikaYHTProsKentta.setText(String.valueOf((double)Math.round((kaynti_kv_per_vrk + kaynti_la_per_vrk) * 100 * 10) / 10 ) + "%");
		
		//lepoaika
		double kaynti_le_vrk = ((double)kaytto_le * (double)mittausvali / 60.0 / 24 / 1000 / 60);
		double kaynti_le_per_vrk = ((double)kaytto_le * (double)mittausvali / 60.0 / 24) / (((double)kokaika * (double)mittausvali) / 60.0 / 24);
		lblLepoaikaKentta.setText(String.valueOf((double)Math.round(kaynti_le_vrk * 1000) / 1000));
		lblLepoaikaVrkKentta.setText(parseKello(kaynti_le_per_vrk));
		lblLepoaikaProsKentta.setText(String.valueOf(le=(double)Math.round(kaynti_le_per_vrk * 100 * 10) / 10 ) + "%");

		//käyttöveden sähkölämmitys (legionella tai joku muu superluxus)
		double kaynti_sa_vrk = (kv_sahko.size() * mittausvali / 60.0 / 24 / 1000 / 60);
		double kaynti_sa_per_vrk = (kv_sahko.size() * (double)mittausvali / 60.0 / 24) / (((double)kokaika * (double)mittausvali) / 60.0 / 24);
		lblSahkoKVKentta.setText(String.valueOf((double)Math.round(kaynti_sa_vrk * 1000) / 1000));
		lblSahkoKVVrkKentta.setText(parseKello(kaynti_sa_per_vrk));
		lblSahkoKVProsKentta.setText(String.valueOf(sav=(double)Math.round(kaynti_sa_per_vrk * 100 * 10) / 10 ) + "%");
		
		//lämmityksen sähkölämmitys (vika tai tarkoituksella)
		double lamm_sahko_vrk = (lamm_sahko.size() * mittausvali / 60.0 / 24 / 1000 / 60);
		double lamm_sahko_per_vrk = (lamm_sahko.size() * (double)mittausvali / 60.0 / 24) / (((double)kokaika * (double)mittausvali) / 60.0 / 24);
		lblSahkoLammKentta.setText(String.valueOf((double)Math.round(lamm_sahko_vrk * 1000) / 1000));
		lblSahkoLammVrkKentta.setText(parseKello(lamm_sahko_per_vrk));
		lblSahkoLammProsKentta.setText(String.valueOf(sal=(double)Math.round(lamm_sahko_per_vrk * 100 * 10) / 10 ) + "%");
		//sa=sav+sal;
		//poistetaan käyntiajasta sähkön osuus jottei ylitetä 100%
		la=la-sal;
		kv=kv-sav;
		
		//lämmityksen uima-allaslämmitys
		double lamm_uima_vrk = ((double)kaytto_ua * (double)mittausvali / 60.0 / 24 / 1000 / 60);
		double lamm_uima_per_vrk = ((double)kaytto_ua * (double)mittausvali / 60.0 / 24) / (((double)kokaika * (double)mittausvali) / 60.0 / 24);
		lblKayntiaikaUAKentta.setText(String.valueOf((double)Math.round(lamm_uima_vrk * 1000) / 1000));
		lblKayntiaikaUAVrkKentta.setText(parseKello(lamm_uima_per_vrk));
		lblKayntiaikaUAProsKentta.setText(String.valueOf(ual=(double)Math.round(lamm_uima_per_vrk * 100 * 10) / 10 ) + "%");
		//poistetaan käyntiajasta uima-altaan osuus jottei ylitetä 100%
		la=la-ual;
		
		//käyttövesi poistoilmalämpöpumpulla
		double kaynti_kv_flm_per_vrk = ((double)kaytto_kv_flm * (double)mittausvali / 60.0 / 24) / (((double)kokaika * (double)mittausvali) / 60.0 / 24);
		kv_flm=(double)Math.round(kaynti_kv_flm_per_vrk * 100 * 10) / 10;
		kv=kv-kv_flm;
		
		//lämmitys poistoilmalämpöpumpulla
		double lamm_la_flm_per_vrk = ((double)kaytto_la_flm * (double)mittausvali / 60.0 / 24) / (((double)kokaika * (double)mittausvali) / 60.0 / 24);
		la_flm=(double)Math.round(lamm_la_flm_per_vrk * 100 * 10) / 10;
		//poistetaan käyntiajasta flm:n osuus jottei ylitetä 100%
		la=la-la_flm;
		
		//konsoli.append("kaytto kv flm: " + kv_flm + "\n");
		//konsoli.append("kv: " + kv + "\n");
		
		//yhteensä
		double kaynti_kok_vrk = ((double)kokaika * (double)mittausvali / 60.0 / 24 / 1000 / 60);
		double kaynti_kok_per_vrk = ((double)(kaytto_la + kaytto_kv + kaytto_le) * (double)mittausvali / 60.0 / 24) / (((double)kokaika * (double)mittausvali) / 60.0 / 24);
		lblJaksoKentta.setText(String.valueOf((double)Math.round(kaynti_kok_vrk * 1000) / 1000));
		lblJaksoProsKentta.setText(String.valueOf((double)Math.round(kaynti_kok_per_vrk * 100 * 10) / 10 ) + "%");
		
		//kompressorin käynnistyksiä
		double kompr_kaynnistyksia_per_vrk = kompr_kaynnistyksia / (((double)kokaika * (double)mittausvali) / 60.0 / 24 / 1000 / 60);
		lblKaynnistyksiaKentta.setText(kompr_kaynnistyksia + " kpl");
		lblKaynnistyksiaVRKKentta.setText(String.valueOf((double)Math.round(kompr_kaynnistyksia_per_vrk * 10) / 10 ) + " kpl");

		//kompressorin käynti- ja lepoaika keskimäärin
		double kompr_kaynti_per_vrk = ((double)Math.round((kaynti_kv_vrk + kaynti_la_vrk) * 1000) / 1000) / kompr_kaynnistyksia;
		lblKayntiKeskimKentta.setText(parseKello(kompr_kaynti_per_vrk));
		double kompr_lepo_per_vrk = ((double)Math.round((kaynti_le_vrk) * 1000) / 1000) / kompr_kaynnistyksia;
		if (kompr_kaynnistyksia == 0) {
			lblLepoKeskimKentta.setText(parseKello(0));
		} else {
			lblLepoKeskimKentta.setText(parseKello(kompr_lepo_per_vrk));
		}

		//desimaalit
		//GERMAN jotta saatiin desimaalierotin kohdilleen
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator('.'); 
		DecimalFormat df = new DecimalFormat("0.0", otherSymbols);
		
		//COPs
		double yht=0;
		if(cop_la.size()>0){
			for(int i=0;i<cop_la.size();i++){
				yht+=cop_la.get(i);
			}
			lblCOPLaAvgKentta.setText(df.format(yht/cop_la.size()/100));
			lblCOPLaMinKentta.setText(df.format((double)(Collections.min(cop_la))/100));
			lblCOPLaMaxKentta.setText(df.format((double)(Collections.max(cop_la))/100));
		} else {
			lblCOPLaAvgKentta.setText("0.0");
			lblCOPLaMinKentta.setText("0.0");
			lblCOPLaMaxKentta.setText("0.0");
		}
		if(cop_kv.size()>0){	
			yht=0;
			for(int i=0;i<cop_kv.size();i++){
				yht+=cop_kv.get(i);
			}
			lblCOPKvAvgKentta.setText(df.format(yht/cop_kv.size()/100));
			lblCOPKvMinKentta.setText(df.format((double)(Collections.min(cop_kv))/100));
			lblCOPKvMaxKentta.setText(df.format((double)(Collections.max(cop_kv))/100));
		} else {
			lblCOPKvAvgKentta.setText("0.0");
			lblCOPKvMinKentta.setText("0.0");
			lblCOPKvMaxKentta.setText("0.0");
		}
		
		//lampotilat
		//sisä
		yht=0;
		double kwh_yht=0;
		for(int i=0;i<lampo_sisa.size();i++){
			yht+=lampo_sisa.get(i);
		}
		lblLampoSisaAvgKentta.setText(df.format(yht/lampo_sisa.size()/10) + "°C");
		lblLampoSisaMinKentta.setText((double)(Collections.min(lampo_sisa))/10 + "°C");
		lblLampoSisaMaxKentta.setText((double)(Collections.max(lampo_sisa))/10 + "°C");
		//ulko
		yht=0;
		for(int i=0;i<lampo_ulko.size();i++){
			yht+=lampo_ulko.get(i);
		}
		lblLampoUlkoAvgKentta.setText(df.format(yht/lampo_ulko.size()/10) + "°C");
		lblLampoUlkoMinKentta.setText((double)(Collections.min(lampo_ulko))/10 + "°C");
		lblLampoUlkoMaxKentta.setText((double)(Collections.max(lampo_ulko))/10 + "°C");
		//pyynti
		yht=0;
		for(int i=0;i<lampo_pyynti.size();i++){
			yht+=lampo_pyynti.get(i);
		}
		lblLampoPyyntiAvgKentta.setText(df.format(yht/lampo_pyynti.size()/10) + "°C");
		lblLampoPyyntiMinKentta.setText((double)(Collections.min(lampo_pyynti))/10 + "°C");
		lblLampoPyyntiMaxKentta.setText((double)(Collections.max(lampo_pyynti))/10 + "°C");
		//käyttövesi
		yht=0;
		for(int i=0;i<lampo_kv.size();i++){
			yht+=lampo_kv.get(i);
		}
		lblLampoKVAvgKentta.setText(df.format(yht/lampo_kv.size()/10) + "°C");
		lblLampoKVMinKentta.setText((double)(Collections.min(lampo_kv))/10 + "°C");
		lblLampoKVMaxKentta.setText((double)(Collections.max(lampo_kv))/10 + "°C");

		//lämpötilaerot
		//lämmitysprosessi
		if(lampo_delta_la.size()>0){
			yht=0;
			for(int i=0;i<lampo_delta_la.size();i++){
				yht+=lampo_delta_la.get(i);
			}
			lblDeltaLampoAvgKentta.setText(df.format(yht/lampo_delta_la.size()/10) + "°C");
			lblDeltaLampoMinKentta.setText((double)(Collections.min(lampo_delta_la))/10 + "°C");
			lblDeltaLampoMaxKentta.setText((double)(Collections.max(lampo_delta_la))/10 + "°C");
		} else {
			lblDeltaLampoAvgKentta.setText("0°C");
			lblDeltaLampoMinKentta.setText("0°C");
			lblDeltaLampoMaxKentta.setText("0°C");
		}
		//käyttövesiprosessi
		if(lampo_delta_kv.size()>0){
			yht=0;
			for(int i=0;i<lampo_delta_kv.size();i++){
				yht+=lampo_delta_kv.get(i);
			}
			lblDeltaKVAvgKentta.setText(df.format(yht/lampo_delta_kv.size()/10) + "°C");
			lblDeltaKVMinKentta.setText((double)(Collections.min(lampo_delta_kv))/10 + "°C");
			lblDeltaKVMaxKentta.setText((double)(Collections.max(lampo_delta_kv))/10 + "°C");
		} else {
			lblDeltaKVAvgKentta.setText("0°C");
			lblDeltaKVMinKentta.setText("0°C");
			lblDeltaKVMaxKentta.setText("0°C");
		}
		//keruu
		if(lampo_delta_keruu.size()>0){
			yht=0;
			for(int i=0;i<lampo_delta_keruu.size();i++){
				yht+=lampo_delta_keruu.get(i);
			}
			lblDeltaKeruuAvgKentta.setText(df.format(yht/lampo_delta_keruu.size()/10) + "°C");
			lblDeltaKeruuMinKentta.setText((double)(Collections.min(lampo_delta_keruu))/10 + "°C");
			lblDeltaKeruuMaxKentta.setText((double)(Collections.max(lampo_delta_keruu))/10 + "°C");
		} else {
			lblDeltaKeruuAvgKentta.setText("0°C");
			lblDeltaKeruuMinKentta.setText("0°C");
			lblDeltaKeruuMaxKentta.setText("0°C");
		}
		
		yht=0;
		for(int i=0;i<lampo_delta_keruu.size();i++){
			yht+=lampo_delta_keruu.get(i);
		}
		//tarkistetaan defaultit
		if (bt10_min == 500) bt10_min=0;
		if (bt11_min == 500) bt11_min=0;
		if (bt10_max == -500) bt10_max=0;
		if (bt11_max == -500) bt11_max=0;
		//BT10 minmax
		lblBT10MinKentta.setText((double)(bt10_min)/10 + "°C");
		lblBT10MaxKentta.setText((double)(bt10_max)/10 + "°C");
		//BT11 minmax
		lblBT11MinKentta.setText((double)(bt11_min)/10 + "°C");
		lblBT11MaxKentta.setText((double)(bt11_max)/10 + "°C");
		
		//suorasähkö kWh käyttövesi
		if(kv_sahko.size()>0){
			yht=0;
			for(int i=0;i<kv_sahko.size();i++){
				yht+=((kv_sahko.get(i)*10)*(mittausvali*1.00/60/1000/60))/1000;
			}
			lblSahkoKVkWhKentta.setText(df.format(kwh_yht=yht) + " kWh");
		} else {
			lblSahkoKVkWhKentta.setText("0 kWh");
		}
		//suorasähkö kWh lämmitys
		//if(lamm_sahko.size()>0 && !lblMLPMalli.getText().equalsIgnoreCase("F1345")){
		if(lamm_sahko.size()>0){
			yht=0;
			for(int i=0;i<lamm_sahko.size();i++){
				//yht+=lamm_sahko.get(i)*10;
				yht+=(lamm_sahko.get(i)*10)*(mittausvali*1.00/60/1000/60)/1000;
				//(lamm_sahko.get(i)*10)*(mittausvali*1.00/60)/1000 + "\n");
			}
			kwh_yht+=yht;
			lblSahkoLammkWhKentta.setText(df.format(yht) + " kWh");
			//console.append((double)lamm_sahko.size()*(mittausvali*1.00/60) + "\n" + yht);

		} else {
			lblSahkoLammkWhKentta.setText("0 kWh");
		}
		if((lamm_sahko.size()>0 || kv_sahko.size()>0)){
			lblSahkoYHTkWhKentta.setText(df.format(kwh_yht) + " kWh");
		} else {
			lblSahkoYHTkWhKentta.setText("0 kWh");
		}
		//Compressor frequency active
		if (Collections.max(cfa)>0) {
			yht=0;
			ArrayList<Integer> cfatmp = new ArrayList<Integer>();;
			for(int i=0;i<cfa.size();i++){
				if (cfa.get(i)!=0) {
					yht+=cfa.get(i);
					cfatmp.add(cfa.get(i));
				}
			}
			lblCFAAvgKentta.setText(df.format(yht/cfatmp.size()/10) + " Hz");
			lblCFAMinKentta.setText((double)(Collections.min(cfatmp))/10+" Hz");
			lblCFAMaxKentta.setText((double)(Collections.max(cfatmp))/10+" Hz");
		} else {
			lblCFAAvgKentta.setText("0 Hz");
			lblCFAMinKentta.setText("0 Hz");
			lblCFAMaxKentta.setText("0 Hz");
		}
		//Hälytykset
		if (alarms.size() > 0) {
			for (int i =0; i<  alarms.size(); i++) {
				if (alarms.get(i) != 0) {
				    lblAlarm.setText("" + alarms. get(i));
				    break;
			    }
			}
		}
	}
	
	public void nollaaNumerot() {
		//hälytys
		lblAlarm.setText("");
		//lokiversio
		lblLokiVersio.setText("v0000");
		//piirakka sileäksi
		naytaGraafi(0, 0, 0, 0, 0, 0, 0, 100);
		//KV ylä/alamittaus
		lblKVYlaAlaKentta.setText("");
		//käyntiajat lämmitys				
		lblKayntiaikaKentta.setText("0");
		lblKayntiaikaVrkKentta.setText(parseKello(0));
		lblKayntiaikaProsKentta.setText("0%");
		//käyntiajat uima-allas
		lblKayntiaikaUAKentta.setText("0");
		lblKayntiaikaUAVrkKentta.setText(parseKello(0));
		lblKayntiaikaUAProsKentta.setText("0%");
		//käyntiajat käyttövesi
		lblKayntiaikaKVKentta.setText("0");
		lblKayntiaikaKVVrkKentta.setText(parseKello(0));
		lblKayntiaikaKVProsKentta.setText("0%");
		//kayntiajat yhteensä
		lblKayntiaikaYHTKentta.setText("0");
		lblKayntiaikaYHTVrkKentta.setText(parseKello(0));
		lblKayntiaikaYHTProsKentta.setText("0%");
		//lepoaika
		lblLepoaikaKentta.setText("0");
		lblLepoaikaVrkKentta.setText(parseKello(0));
		lblLepoaikaProsKentta.setText("100%");
		//käyttöveden sähkölämmitys (legionella tai joku muu superluxus)
		lblSahkoKVKentta.setText("0");
		lblSahkoKVVrkKentta.setText(parseKello(0));
		lblSahkoKVProsKentta.setText("0%");
		//lämmityksen sähkölämmitys (vika tai tarkoituksella)
		lblSahkoLammKentta.setText("0");
		lblSahkoLammVrkKentta.setText(parseKello(0));
		lblSahkoLammProsKentta.setText("0%");
		//yhteensä
		lblJaksoKentta.setText("0");
		lblJaksoProsKentta.setText("100%");
		//kompressorin käynnistyksiä
		lblKaynnistyksiaKentta.setText("0 kpl");
		lblKaynnistyksiaVRKKentta.setText("0 kpl");
		//kompressorin käynti- ja lepoaika keskimäärin
		lblKayntiKeskimKentta.setText(parseKello(0));
		lblLepoKeskimKentta.setText(parseKello(0));
		
		//COPit
		lblCOPLaAvgKentta.setText("0.0");
		lblCOPLaMinKentta.setText("0.0");
		lblCOPLaMaxKentta.setText("0.0");
		lblCOPKvAvgKentta.setText("0.0");
		lblCOPKvMinKentta.setText("0.0");
		lblCOPKvMaxKentta.setText("0.0");	
		
		
		//lampotilat
		//sisä
		lblLampoSisaAvgKentta.setText("0°C");
		lblLampoSisaMinKentta.setText("0°C");
		lblLampoSisaMaxKentta.setText("0°C");
		//ulko
		lblLampoUlkoAvgKentta.setText("0°C");
		lblLampoUlkoMinKentta.setText("0°C");
		lblLampoUlkoMaxKentta.setText("0°C");
		//pyynti
		lblLampoPyyntiAvgKentta.setText("0°C");
		lblLampoPyyntiMinKentta.setText("0°C");
		lblLampoPyyntiMaxKentta.setText("0°C");
		//käyttövesi
		lblLampoKVAvgKentta.setText("0°C");
		lblLampoKVMinKentta.setText("0°C");
		lblLampoKVMaxKentta.setText("0°C");

		//lämpötilaerot
		//lämmitysprosessi
		lblDeltaLampoAvgKentta.setText("0°C");
		lblDeltaLampoMinKentta.setText("0°C");
		lblDeltaLampoMaxKentta.setText("0°C");
		//käyttövesiprosessi
		lblDeltaKVAvgKentta.setText("0°C");
		lblDeltaKVMinKentta.setText("0°C");
		lblDeltaKVMaxKentta.setText("0°C");
		//keruu
		lblDeltaKeruuAvgKentta.setText("0°C");
		lblDeltaKeruuMinKentta.setText("0°C");
		lblDeltaKeruuMaxKentta.setText("0°C");
		//BT10 (keruu sisään) minmax
		lblBT10MinKentta.setText("0°C");
		lblBT10MaxKentta.setText("0°C");
		//BT11 (keruu ulos) minmax
		lblBT11MinKentta.setText("0°C");
		lblBT11MaxKentta.setText("0°C");
		//suorasähkö kWh käyttövesi
		lblSahkoKVkWhKentta.setText("0 kWh");
		//suorasähkö kWh lämmitys
		lblSahkoLammkWhKentta.setText("0 kWh");
		lblSahkoYHTkWhKentta.setText("0 kWh");
		//compr freq
		lblCFAAvgKentta.setText("0 Hz");
		lblCFAMinKentta.setText("0 Hz");
		lblCFAMaxKentta.setText("0 Hz");
	}
	
	// parseKello
    // Muokataan desimaali näyttämään kellonajalta
    //
	private String parseKello(double vrk) {
		long ms = (long)(vrk * 24 * 60 * 60 * 1000);
		long h = ms / 1000 / 60 / 60;
		long m = (ms - (h * 60 * 60 * 1000))/1000/60;
		long s = Math.round((ms - ((h * 60 * 60 * 1000) + (m * 60 * 1000)))/1000);
		double d = 0;
		String kello = "00:00:00";
		if (h > 23) {
			d = ((h/24)*10)/10;
			kello = d + " vrk";
		} else {
			kello = String.format("%02d", h) + ":" + String.format("%02d", m) + ":" + String.format("%02d", s);
		}
		
		
		return kello;
	}
	

    // rekisteröiOhjain
	// mahdollistetaan ohjaimen näkyvyys GUIlle
    //
	public void rekisteröiOhjain(LogintutkijaOhjain ohjain){
		LogintutkijaGUI.ohjain=ohjain;
	}
	
    //
	// Avaa logitiedosto
    //
	public ArrayList<String> avaa() throws IOException {
		final JFileChooser fc = new JFileChooser(ohjain.getOletushakemisto());
		//haetaan oletushakemisto
		//tiedoston valintaikkuna
		//kieli
		fc.setLocale(Locale.getDefault());
		UIManager.put("FileChooser.acceptAllFileFilterText","Kaikki tiedostot");
		UIManager.put("FileChooser.cancelButtonText", "Peruuta");
		SwingUtilities.updateComponentTreeUI(fc);
		fc.setMultiSelectionEnabled(true);
		fc.setApproveButtonText("Avaa");
		fc.setDialogTitle("Logitiedoston avaus");
		ArrayList<String> logit = new ArrayList<String>();
		
		//Mac OS X fix
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch(Exception e) {
				konsoli.append("Virhe asetettaessa Java Look And Feel -asetusta: " + e + "\n");
			}
		
		//Nibe mask
		ExtensionFileFilter ef_nibe = new ExtensionFileFilter(new String[] { ".LOG", ".log" },"Nibe F LOG tiedosto");
		ExtensionFileFilter ef_nibes = new ExtensionFileFilter(new String[] { ".CSV", ".csv" },"Nibe S CSV tiedosto");
		ExtensionFileFilter ef_ctc = new ExtensionFileFilter(new String[] { ".CSV", ".csv" },"CTC CSV tiedosto");
		if(vtf.equalsIgnoreCase("nibe")){
			fc.setFileFilter(ef_nibe);
			fc.addChoosableFileFilter(ef_nibes);
			fc.addChoosableFileFilter(ef_ctc);
		} else if (vtf.equalsIgnoreCase("ctc")) {
			fc.addChoosableFileFilter(ef_nibe);
			fc.addChoosableFileFilter(ef_nibes);
			fc.setFileFilter(ef_ctc);
		} else if (vtf.equalsIgnoreCase("nibes")) {
			fc.addChoosableFileFilter(ef_nibe);
			fc.setFileFilter(ef_nibes);
			fc.addChoosableFileFilter(ef_ctc);
		} else {
			fc.addChoosableFileFilter(ef_nibe);
			fc.addChoosableFileFilter(ef_nibes);
			fc.addChoosableFileFilter(ef_ctc);
		}
		
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setSelectedFile(new File(ohjain.getOletushakemisto()));
        //fc.setAcceptAllFileFilterUsed(false);
        //Nappulaa painettaessa
        //int returnVal = fc.showDialog(LogintutkijaGUI.this, "Avaa");
		int returnVal = fc.showOpenDialog(LogintutkijaGUI.this);
		ohjain.setOletushakemisto(fc.getSelectedFile().getPath());
		//fc.updateUI(); //korjaamaan ei aina avautuvaa JFileChooser dialogia - arvaus 
		//fc.repaint();

		if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            File[] files = fc.getSelectedFiles();
        	// hakemistopolku
      	  	String path = file.toString(); 
      	  	String filee = "";
            if(file.isDirectory()){
            	  int maara = 0;
            	  File folder = new File(path);
            	  File[] listOfFiles = folder.listFiles();           	 
            	  for (int i = 0; i < listOfFiles.length; i++) 
            	  {
            	   if (listOfFiles[i].isFile()) 
            	   {
            		   filee = listOfFiles[i].getName();
            		   if (fc.getFileFilter().getDescription().equals("Nibe F LOG tiedosto"))
            		   {
            			   //asetetaan vtf muuttuja asetusten tallennusta varten
            			   vtf="nibe";
            			   if(filee.endsWith(".log") || filee.endsWith(".LOG")){
                			   maara++;
                			   logit.add(path + "/" + filee);
            			   }
            		   } else if (fc.getFileFilter().getDescription().equals("Nibe S CSV tiedosto")) {
            			   vtf="nibes";
            			   if(filee.endsWith(".csv") || filee.endsWith(".CSV")){
                			   maara++;
                			   logit.add(path + "/" + filee);
            			   }
            		   } else if (fc.getFileFilter().getDescription().equals("CTC CSV tiedosto")) {
            			   vtf="ctc";
            			   if(filee.endsWith(".csv") || filee.endsWith(".CSV")){
                			   maara++;
                			   logit.add(path + "/" + filee);
            			   }
            		   } else {
            			   maara++;
            			   logit.add(path + "/" + filee);
            		   }
            	    }
            	  }
                  if (fc.getFileFilter().getDescription().equals("Kaikki tiedostot")) {
                  	vtf="kaikki";
                  }
				konsoli.append("Hakemistossa logitiedostoja: " + maara + ". ");
            }
            else if (files.length > 1) {
            	konsoli.append("Valittuja logitiedostoja: " + files.length + ". ");
            	for (int i = 0; i < files.length; i++) {
            		logit.add(""+files[i]);
            	}
            }
            else if (file.isFile()) {
            	logit.add(path + "/" + filee);
            }
			//mikä tiedostotyyppi valittu?
			if (fc.getFileFilter().getDescription().equals("Nibe F LOG tiedosto"))
			{
				//asetetaan vtf muuttuja asetusten tallennusta varten
				vtf="nibe";
			} else if (fc.getFileFilter().getDescription().equals("Nibe S CSV tiedosto")) {
				vtf="nibes";
			} else if (fc.getFileFilter().getDescription().equals("CTC CSV tiedosto")) {
				vtf="ctc";
			}
        } else {
        	//tiedostovalinta keskeytetty
        	//konsoli.append("Keskeytetty.\n");
            valintalaatikko.setEnabled(true);
            return null;
        }
		
		//filetyypin arvaus
		
		//sortataan filenimen mukaan mikä on VVKKPP.LOG
		Collections.sort(logit);
		return logit;
	}

    
    // actionPerformed
	// Ajetaan kun nappuloita painettu
    //
    @Override
	public void actionPerformed(ActionEvent evt) {
    	Object source = evt.getSource();
		 //laitetaan kursori kellottamaan haun ajan
		 setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    	 if(source == avaaLogit) {
    		 luoKayrat.setEnabled(false);
    		 valintalaatikko.setEnabled(false);
    		 tietolahde=0; //logitiedostot
    		//luo säikeen datan lukua varten
    	    if (ohjain != null) {
    	    	ohjain.aloitaSaie();
    	    }
    	 } else if(source == haeDB) {
       		 tietolahde=1; //db
       		kysyTietokantaTiedot();
       		 if (hae && ohjain != null){
       			 //luo säikeen datan hakua varten
       			ohjain.aloitaSaie();
       		 } 
    	 } else if(source == luoKayrat) {
    		 konsoli.append("Piirretään trendit.\n");
    		 teeKayratIkkuna();
    		 //setCursor(null);
    		 setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    	 }
    	 
    }
    
    // kysyTietokantaTiedot
	// Ajetaan kun nappuloita painettu
    //
    private void kysyTietokantaTiedot() {
	 //asetetaan DB ikkunan alasvetolaatikon oletus asetustiedostosta
	 for (int i=0;i<ajurit.length;i++) {
	   	if(ajurit[i].toString().equalsIgnoreCase(tietokanta_dbms) && ekayhteys) {
	   		combo.setSelectedItem(ajurit[i]);
	   		ekayhteys=false;
	   	}
	 }
	 
    final JTextField k1 = new JTextField(tietokanta_osoite);
    final JTextField k2 = new JTextField(tietokanta_nimi);
    final JTextField k3 = new JTextField(tietokanta_kayttaja);
    final JTextField k4 = new JPasswordField(tietokanta_salasana);
	final JTextField k5 = new JTextField(dateFormat.format(jokuaikasitten()));
	final JTextField k6 = new JTextField(dateFormat.format(nyt()));
    if (tietokanta_omahakuaika) {
    	k5.setText(tietokanta_pvm_mista);
    	k6.setText(tietokanta_pvm_mihin);
    }
    
    //kuunnellaan onko kenttiä editoitu
    k5.addMouseListener(new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e){
            tietokanta_omahakuaika = true;
        }
    });
    k6.addMouseListener(new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e){
            tietokanta_omahakuaika = true;
        }
    });
    
    
    if (tietokanta_dbms.equalsIgnoreCase("sqlite")) {
    	k1.setEnabled(false);
    	k2.setEnabled(false);
    	k3.setEnabled(false);
    	k4.setEnabled(false);
    } else if (tietokanta_dbms.equalsIgnoreCase("mysql")) {
 		k1.setEnabled(true);
    	k2.setEnabled(true);
    	k3.setEnabled(true);
    	k4.setEnabled(true);
    }
    
	 combo.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
        	 ohjain.setTietokanta_dbms(ajurit[combo.getSelectedIndex()]);
         	if (combo.getSelectedIndex() == 0){
         		k1.setEnabled(false);
            	k2.setEnabled(false);
            	k3.setEnabled(false);
            	k4.setEnabled(false);
         	}
         	else if (combo.getSelectedIndex() != 0){
         		k1.setEnabled(true);
            	k2.setEnabled(true);
            	k3.setEnabled(true);
            	k4.setEnabled(true);
         	}
         }
     });
    
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(combo);
    panel.add(new JLabel("Osoite"));
    panel.add(k1);
    panel.add(new JLabel("Tietokanta"));
    panel.add(k2);
    panel.add(new JLabel("Käyttäjä"));
    panel.add(k3);
    panel.add(new JLabel("Salasana"));
    panel.add(k4);
    panel.add(new JLabel("Ajankohdasta"));
    panel.add(k5);
    panel.add(new JLabel("Ajankohtaan"));
    panel.add(k6);
    
    int result = JOptionPane.showConfirmDialog(null, panel, "Tietokantayhteys",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
    	hae = true;
    	tietokanta_osoite = k1.getText();
    	tietokanta_nimi =  k2.getText();
    	tietokanta_kayttaja = k3.getText();
    	tietokanta_salasana = k4.getText();
    	tietokanta_pvm_mista = k5.getText();
    	tietokanta_pvm_mihin = k6.getText();
    	
    } else {
    	//konsoli.append("Tietokantahaku peruutettu.\n");
    	hae = false;
    	setCursor(null);
    }
}
    
    //
	// ajetaan setProgress kohdalla
    //
    @Override
	public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            edistymisPalkki.setValue(progress);
        }
    }
    

	
    // naytaGraafi
	// Näytä käyttö piirakan muodossa
    //
    public void naytaGraafi(double la, double sal, double ual, double la_flm, double kv_flm, double kv, double sav, double le) {
    	this.sav=sav;
    	this.sal=sal;
    	this.ual=ual;
    	this.la_flm=la_flm;
    	this.kv_flm=kv_flm;
    	this.la=la;
    	this.kv=kv;
    	this.le=le;
		// otetaan arvot vastaan ja tehdään tietue
        PieDataset dataset = createDataset(la,sal,ual,la_flm,kv,sav,le);
        //luodaan piirakka tietueen arvoilla
        JFreeChart chart = createChart(dataset,"käyntiaikasuhde");
        //taustaväri pois
        chart.setBackgroundPaint(null);
        //piirakka paneeliin
        ChartPanel chartPanel = new ChartPanel(chart, false);
		//piirakakan koko ja paikka
        chartPanel.setPreferredSize(new java.awt.Dimension(200, 200));
        //taustaväri pois
        chartPanel.setBackground(null);
        //putsataan paneeli - tarvitaan jos analyysi ajetaan uudelleen
        itapaneeli.removeAll();
        itapaneeli.add(chartPanel);
        // tämä vasta sai piirakan esille - jostain kumman syystä
        itapaneeli.revalidate();
	}
    
    // createDataset
    // YMPYRÄPIIRAKAN datasetti
    //
	private  PieDataset createDataset(double lammitys, double sahkol, double uimaallas, double flm, double kv, double sahkov, double lepo) {
        DefaultPieDataset result = new DefaultPieDataset();
        if (lammitys > 0) { result.setValue("lämmitys", lammitys); }
        if (uimaallas > 0) { result.setValue("allas", uimaallas); }
        if (la_flm > 0) { result.setValue("lä-pilp", la_flm); }
        if (sahkol > 0) { result.setValue("lä-lisäys", sahkol); }
        if (kv > 0) { result.setValue("kv", kv); }
        if (sahkov > 0) { result.setValue("kv-lisäys", sahkov); }
        if (kv_flm > 0) { result.setValue("kv-pilp", kv_flm); }
        if (lepo > 0) { result.setValue("lepo", lepo); }
        return result;
        
    }

	// createChart
	// Luodaan piirakka
	//
    private JFreeChart createChart(PieDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createPieChart3D(title, //otsikko
            dataset,                // data
            true,                   // selitteet
            true,					// tooltippi
            true);					// urlit
        //pienennetään otsikon kirjasinta
        chart.setTitle(
        		   new org.jfree.chart.title.TextTitle(title,
        		       new java.awt.Font("Arial", java.awt.Font.BOLD, 11)
        		   ));
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        //oletusvärit pois, tällä saadaan tausta väri läpi
        plot.setForegroundAlpha(0.7f);
        plot.setBackgroundAlpha(0.2f);
        //pultataan värit, jotteivat ne hypi jos kaikkia käyntimuotoja ei löydy
        plot.setSectionPaint("lepo", Color.green);
        plot.setSectionPaint("lä-lisäys", Color.magenta);
        plot.setSectionPaint("kv-lisäys", Color.magenta);
        plot.setSectionPaint("lämmitys", Color.red);
        plot.setSectionPaint("allas", Color.pink);
        plot.setSectionPaint("kv", Color.blue);
        plot.setSectionPaint("lä-pilp", Color.orange);
        plot.setSectionPaint("kv-pilp", Color.orange);
        plot.setSectionPaint("vain sähkö", Color.black);
        return chart;
    }

    // teenaytaGraafiIkkuna
	// Näytä käyrät datasta
    //      
    public static ChartPanel teeKayratPaneeli(ArrayList<String> kayrat_taulukko_nimet, final ArrayList<ArrayList<Integer>> kayrat_taulukko, ArrayList<GregorianCalendar> logiaika) {
        //super(title);
    	final XYDataset dataset = teeTietoJoukko(kayrat_taulukko_nimet, kayrat_taulukko, logiaika);
        final JFreeChart chart = teeKayrat(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 500));
        chartPanel.setMouseZoomable(true, true);
        chartPanel.setMouseWheelEnabled(true);
        return chartPanel;
    }
    
    // teeKayrat
	// Luo käyrästön tietojoukosta
    //      
    @SuppressWarnings("deprecation")
	private static JFreeChart teeKayrat(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Trendit", 
            aikaAloitus + aikaLopetus, 
            "Lämpötilat/arvot",
            dataset, 
            true, 
            true, 
            false
        );
        final XYItemRenderer renderer = chart.getXYPlot().getRenderer();
        final StandardXYToolTipGenerator generator = new StandardXYToolTipGenerator(
            StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
            new SimpleDateFormat("yyyy-MM-dd HH:mm"), new DecimalFormat("0.00")
        );
        //eri viivat
        BasicStroke semiLine = new BasicStroke( 1.5f );
        BasicStroke wideLine = new BasicStroke( 3.0f ); 
        BasicStroke dottedLine = new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {2.0f, 6.0f}, 0.0f);
        
        renderer.setSeriesStroke(0,semiLine);
        renderer.setSeriesStroke(1,semiLine);
        renderer.setSeriesStroke(2,semiLine);
        renderer.setSeriesStroke(3,semiLine);
        renderer.setSeriesStroke(4,semiLine);
        renderer.setSeriesStroke(5,semiLine);
        renderer.setSeriesStroke(6,semiLine);
        renderer.setSeriesPaint(0, Color.WHITE); //bt50
        renderer.setSeriesPaint(1, Color.BLACK); //bt1
        renderer.setSeriesPaint(2, Color.RED); //bt2
        renderer.setSeriesPaint(3, Color.BLUE); //bt3
        renderer.setSeriesPaint(4, Color.MAGENTA); //bt25
        renderer.setSeriesPaint(5, Color.GREEN); //bt10
        renderer.setSeriesPaint(6, Color.ORANGE); //bt11
        renderer.setSeriesPaint(7, Color.CYAN); //bt12
        renderer.setSeriesPaint(8, Color.PINK); //bt14
        renderer.setSeriesPaint(9, Color.GRAY); //bt17
        renderer.setSeriesPaint(10, Color.CYAN); //DM
        renderer.setSeriesPaint(11, Color.CYAN); //CS
        renderer.setSeriesPaint(12, Color.DARK_GRAY); //delta lämmönjako
        renderer.setSeriesPaint(13, Color.GRAY); //delta keruu
        renderer.setSeriesPaint(14, Color.BLACK); //COP
        //EP15 katkoviivalla, värit samat
        renderer.setSeriesStroke(15, dottedLine);
        renderer.setSeriesStroke(16, dottedLine);
        renderer.setSeriesStroke(17, dottedLine);
        renderer.setSeriesStroke(18, dottedLine);
        renderer.setSeriesStroke(19, dottedLine);
        renderer.setSeriesStroke(20, dottedLine);
        renderer.setSeriesStroke(21, dottedLine);
        renderer.setSeriesStroke(22, dottedLine);
        renderer.setSeriesStroke(23, dottedLine);
        renderer.setSeriesPaint(15, Color.BLUE); //EP15 BT3
        renderer.setSeriesPaint(16, Color.GREEN); //EP15 BT10
        renderer.setSeriesPaint(17, Color.ORANGE); //EP15 BT11
        renderer.setSeriesPaint(18, Color.RED); //EP15 BT12
        renderer.setSeriesPaint(19, Color.PINK); //EP15 BT14
        renderer.setSeriesPaint(20, Color.GRAY); //EP15 BT17
        renderer.setSeriesPaint(21, Color.DARK_GRAY); //EP15 delta lämmönjako
        renderer.setSeriesPaint(22, Color.DARK_GRAY); //EP15 delta keruu
        renderer.setSeriesPaint(23, Color.BLACK); //EP15 COP
        
        //KV
        renderer.setSeriesPaint(26, Color.RED);
        renderer.setSeriesPaint(27, Color.BLUE);
        
        //muut
        renderer.setSeriesPaint(28, Color.YELLOW); //lisäteho
        renderer.setSeriesPaint(30, Color.GRAY); //kompuran kierrokset
        renderer.setSeriesPaint(31, Color.RED); //teho
        renderer.setSeriesPaint(32, Color.YELLOW); //gp1
        renderer.setSeriesPaint(33, Color.MAGENTA); //gp2
        renderer.setSeriesPaint(34, Color.GRAY); //ep15_gp1
        renderer.setSeriesPaint(42, Color.WHITE); //ep15_gp2
        
        renderer.setSeriesPaint(35, Color.BLUE); //bt51
        renderer.setSeriesPaint(36, Color.YELLOW); //bt53
        renderer.setSeriesPaint(37, Color.RED); //bt54
        
        renderer.setSeriesPaint(39, Color.RED); //bt20
        renderer.setSeriesPaint(40, Color.BLUE); //bt21
        
        //virtamittarit
        renderer.setSeriesPaint(43, new Color(153,102,0)); //be1
        renderer.setSeriesPaint(44, Color.BLACK); //be2
        renderer.setSeriesPaint(45, Color.GRAY); //be3
        
        renderer.setSeriesStroke(10, wideLine); 
        renderer.setSeriesStroke(11, wideLine);
        renderer.setSeriesStroke(12, wideLine);
        renderer.setSeriesStroke(13, wideLine);
        renderer.setSeriesStroke(14, wideLine);
        renderer.setSeriesStroke(9, wideLine);
        renderer.setSeriesStroke(8, wideLine);
        renderer.setSeriesStroke(7, wideLine);
        //renderer.setSeriesStroke(23, wideLine);
        
        renderer.setToolTipGenerator(generator);
        final XYPlot plot = chart.getXYPlot();
		chart.getPlot().setBackgroundPaint(getGraafinTaustanVari());
        plot.setRangeGridlinePaint( getGraafinAsteikonVari() );
        plot.setDomainGridlinePaint( getGraafinAsteikonVari() );
        
        int start_angle = 250;
        int end_angle = 290;
        
        if (b2b_alarm) {
        	start_angle = 290;
        	end_angle = 250;
        }

        for( Alarm a : alarmlist) {
        	//konsoli.append("häly: " + a.getAlarmnr() + " alku: " + a.getAlarm_start_item().getPeriod().toString() + "\n");
        	//hälytys alkaa	
			double xs = a.getAlarm_start_item().getPeriod().getFirstMillisecond();
			double ys = a.getAlarm_start_item().getValue().doubleValue();
			XYPointerAnnotation alarm_start = new XYPointerAnnotation(a.getAlarmnr() + ">" , xs, ys, Math.toRadians(start_angle));
			alarm_start.setLabelOffset(10.0);
			plot.addAnnotation(alarm_start);
			
			//hälytys loppuu
			double xe = a.getAlarm_end_item().getPeriod().getFirstMillisecond();
			double ye = a.getAlarm_end_item().getValue().doubleValue();
			XYPointerAnnotation alarm_end = new XYPointerAnnotation("<" + a.getAlarmnr(), xe, ye, Math.toRadians(end_angle));
			alarm_end.setLabelOffset(10.0);
			plot.addAnnotation(alarm_end);
        }

        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);        
        final ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setAutoRange(true);
        //aika akselin fontti pienemmäksi
        Font font = new Font(domain.getLabelFont().getFontName(),Font.PLAIN, 12); 
        domain.setLabelFont(font);
        return chart;
    }
    
    // teeTietoJoukko
	// Näytä käyrät datasta
    //   
    @SuppressWarnings("deprecation")
	private static XYDataset teeTietoJoukko(ArrayList<String> kayra_taulukko_nimet, ArrayList<ArrayList<Integer>> kayra_taulukko, ArrayList<GregorianCalendar> logiaika) {
    		TimeSeries sisalt = new TimeSeries(kayra_taulukko_nimet.get(0), Second.class);
	        TimeSeries ulkolt = new TimeSeries(kayra_taulukko_nimet.get(1), Second.class);
	        TimeSeries bt2 = new TimeSeries(kayra_taulukko_nimet.get(2), Second.class);        
	        TimeSeries bt3 = new TimeSeries(kayra_taulukko_nimet.get(3), Second.class);
	        TimeSeries bt25 = new TimeSeries(kayra_taulukko_nimet.get(4), Second.class);
	        TimeSeries bt10 = new TimeSeries(kayra_taulukko_nimet.get(5), Second.class);
	        TimeSeries bt11 = new TimeSeries(kayra_taulukko_nimet.get(6), Second.class);
	        TimeSeries bt12 = new TimeSeries(kayra_taulukko_nimet.get(7), Second.class);
	        TimeSeries bt14 = new TimeSeries(kayra_taulukko_nimet.get(8), Second.class);
	        TimeSeries bt17 = new TimeSeries(kayra_taulukko_nimet.get(9), Second.class);
	        TimeSeries dm = new TimeSeries(kayra_taulukko_nimet.get(10), Second.class);
	        TimeSeries cs = new TimeSeries(kayra_taulukko_nimet.get(11), Second.class);
	        TimeSeries dtla = new TimeSeries(kayra_taulukko_nimet.get(12), Second.class);
	        TimeSeries dtbr = new TimeSeries(kayra_taulukko_nimet.get(13), Second.class);
	        TimeSeries cop = new TimeSeries(kayra_taulukko_nimet.get(14), Second.class);
	        //EP15
	        TimeSeries ep15_bt3 = new TimeSeries(kayra_taulukko_nimet.get(15), Second.class);
		    TimeSeries ep15_bt10 = new TimeSeries(kayra_taulukko_nimet.get(16), Second.class);
		    TimeSeries ep15_bt11 = new TimeSeries(kayra_taulukko_nimet.get(17), Second.class);
		    TimeSeries ep15_bt12 = new TimeSeries(kayra_taulukko_nimet.get(18), Second.class);
		    TimeSeries ep15_bt14 = new TimeSeries(kayra_taulukko_nimet.get(19), Second.class);
		    TimeSeries ep15_bt17 = new TimeSeries(kayra_taulukko_nimet.get(20), Second.class);
		    TimeSeries ep15_dtla = new TimeSeries(kayra_taulukko_nimet.get(21), Second.class);
		    TimeSeries ep15_dtbr = new TimeSeries(kayra_taulukko_nimet.get(22), Second.class);
		    TimeSeries ep15_cop = new TimeSeries(kayra_taulukko_nimet.get(23), Second.class);
		    //käynti
		    TimeSeries pca = new TimeSeries(kayra_taulukko_nimet.get(24), Second.class);
		    TimeSeries ep15_prio = new TimeSeries(kayra_taulukko_nimet.get(25), Second.class);
		    //KV
		    TimeSeries bt7 = new TimeSeries(kayra_taulukko_nimet.get(26), Second.class);
		    TimeSeries bt6 = new TimeSeries(kayra_taulukko_nimet.get(27), Second.class);
		    //Suorasähkö
		    TimeSeries kw_sahko = new TimeSeries(kayra_taulukko_nimet.get(28), Second.class);
		    //BF1
		    TimeSeries bf1 = new TimeSeries(kayra_taulukko_nimet.get(29), Second.class);
		    //CFA
		    TimeSeries cfa = new TimeSeries(kayra_taulukko_nimet.get(30), Second.class);
		    //teho
		    TimeSeries teho = new TimeSeries(kayra_taulukko_nimet.get(31), Second.class);
		    //gp1-2
		    TimeSeries gp1 = new TimeSeries(kayra_taulukko_nimet.get(32), Second.class);
		    TimeSeries gp2 = new TimeSeries(kayra_taulukko_nimet.get(33), Second.class);
		    TimeSeries ep15_gp1 = new TimeSeries(kayra_taulukko_nimet.get(34), Second.class);
		    //bt51, 53, 54
		    TimeSeries bt51 = new TimeSeries(kayra_taulukko_nimet.get(35), Second.class);
		    TimeSeries bt53 = new TimeSeries(kayra_taulukko_nimet.get(36), Second.class);
		    TimeSeries bt54 = new TimeSeries(kayra_taulukko_nimet.get(37), Second.class);
		    TimeSeries prio = new TimeSeries(kayra_taulukko_nimet.get(38), Second.class);
		    TimeSeries bt20 = new TimeSeries(kayra_taulukko_nimet.get(39), Second.class);
		    TimeSeries bt21 = new TimeSeries(kayra_taulukko_nimet.get(40), Second.class);
		    TimeSeries bt71 = new TimeSeries(kayra_taulukko_nimet.get(41), Second.class);
		    //ep15 GP2
		    TimeSeries ep15_gp2 = new TimeSeries(kayra_taulukko_nimet.get(42), Second.class);
		    
		    TimeSeries be1 = new TimeSeries(kayra_taulukko_nimet.get(43), Second.class);
		    TimeSeries be2 = new TimeSeries(kayra_taulukko_nimet.get(44), Second.class);
		    TimeSeries be3 = new TimeSeries(kayra_taulukko_nimet.get(45), Second.class);
		    //hälytykset
		    //TimeSeries alarm = new TimeSeries(kayra_taulukko_nimet.get(46), Second.class);

        try {
        	//new alarm entry
        	Alarm a = null;
        	alarmlist.clear();
        	alarm_save = 0;
        	b2b_alarm = false;
        	 	
	        for (int j=0; j<logiaika.size();j++) {
	        	sisalt.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(0).get(j))/10);
	        	ulkolt.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(1).get(j))/10);
	        	bt2.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(2).get(j))/10);
	        	bt3.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(3).get(j))/10);
	        	bt25.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(4).get(j))/10);
	        	bt10.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(5).get(j))/10);
	        	bt11.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(6).get(j))/10);
	        	bt12.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(7).get(j))/10);
	        	bt14.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(8).get(j))/10);
	        	bt17.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(9).get(j))/10);
	        	dm.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(10).get(j))/10);
	        	cs.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(11).get(j))/10);
	        	dtla.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), ((double)(kayra_taulukko.get(2).get(j))/10) - (double)(kayra_taulukko.get(3).get(j))/10);
	        	dtbr.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), ((double)(kayra_taulukko.get(6).get(j))/10) - (double)(kayra_taulukko.get(5).get(j))/10);
	        	cop.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(14).get(j))/10);
	        	
		        //EP15
		        ep15_bt3.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(15).get(j))/10);
		        ep15_bt10.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(16).get(j))/10);
		        ep15_bt11.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(17).get(j))/10);
		        ep15_bt12.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(18).get(j))/10);
		        ep15_bt14.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(19).get(j))/10);
		        ep15_bt17.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(20).get(j))/10);
		        ep15_dtla.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), ((double)(kayra_taulukko.get(18).get(j))/10) - (double)(kayra_taulukko.get(15).get(j))/10);
	        	ep15_dtbr.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), ((double)(kayra_taulukko.get(17).get(j))/10) - (double)(kayra_taulukko.get(16).get(j))/10);
	        	ep15_cop.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(23).get(j))/10);
	        	//käynti
	        	pca.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(24).get(j))/10);
	        	ep15_prio.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(25).get(j))/10);
	        	//KV
	        	bt7.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(26).get(j))/10);
	        	bt6.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(27).get(j))/10);
	        	//Suorasähkö
	        	kw_sahko.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(28).get(j))/10);
	        	//Virtaus
	        	bf1.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(29).get(j))/10);
	        	//Kompressorin nopeus
	        	cfa.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(30).get(j))/10);
	        	//MLP teho
	        	teho.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(31).get(j))/100);
	        	//GP pumput
	        	gp1.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(32).get(j)));
	        	gp2.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(33).get(j)));
	        	ep15_gp1.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(34).get(j)));
	        	bt51.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(35).get(j))/10);
	        	bt53.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(36).get(j))/10);
	        	bt54.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(37).get(j))/10);
	        	//38 = prio
	        	prio.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(38).get(j)));
	        	bt20.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(39).get(j))/10);
	        	bt21.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(40).get(j))/10);
	        	bt71.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(41).get(j))/10);
	        	ep15_gp2.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(42).get(j)));
	        	be1.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(43).get(j)));
	        	be2.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(44).get(j)));
	        	be3.add(new Second(Integer.parseInt(new SimpleDateFormat("ss").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("mm").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("HH").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("dd").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("MM").format(logiaika.get(j).getTime())), Integer.parseInt(new SimpleDateFormat("yyyy").format(logiaika.get(j).getTime()))), (double)(kayra_taulukko.get(45).get(j)));
	        	//hälytys - index 46 - käytetään BT2:n viitteenä näyttämään virheet
	            //etsi mahdolliset virheet
	            if (kayra_taulukko.get(46).get(j) > 0 && alarm_save == 0 && alarm_save != kayra_taulukko.get(46).get(j)) {
	            	//konsoli.append("Uusi! Oli: " + alarm_save + ", on: " + kayra_taulukko.get(46).get(j) +  ".\n");
	            	a = new Alarm();
	            	//alarm start
	            	alarm_save = kayra_taulukko.get(46).get(j);
	            	a.setAlarmnr(alarm_save);
	            	if (j > 0) { //eka rivi pois - jos hälytys jatkuu edelliseltä logilta, ei kirjata alkua
	            		a.setAlarm_start_item(bt2.getDataItem(j));
	            	}
	            } else if (kayra_taulukko.get(46).get(j) != alarm_save) { //hälytys muuttui, otetaan loppuaika ylös
	            	//konsoli.append("Muuttui! Oli: " + alarm_save + ", on: " + kayra_taulukko.get(46).get(j) +  ".\n");
	            	//alarm end
	            	a.setAlarm_end_item(bt2.getDataItem(j));
	            	alarm_save = kayra_taulukko.get(46).get(j);
	            	//add to alarm list
	            	alarmlist.add(a);
	            	//jos uusi häly, luodaan uusi objekti
	            	if (kayra_taulukko.get(46).get(j) != 0) {
	            		//peräkkäinen hälytys
	            		b2b_alarm = true;
		            	//new alarm start
		            	a = new Alarm();
		            	alarm_save = kayra_taulukko.get(46).get(j);
		            	a.setAlarmnr(alarm_save);
		            	a.setAlarm_start_item(bt2.getDataItem(j));
	            	}
	            }
	            //viimeinen ja hälytys jatkuu
	            if (j+1 == kayra_taulukko.get(46).size() && kayra_taulukko.get(46).get(j) == alarm_save && alarm_save != 0) {
	            	//konsoli.append("Viimeinen rivi: " + alarm_save + ".\n");
	            	alarmlist.add(a);
	            }
	        }
        }       
        catch (Exception e) {
        	konsoli.append("Trendien datavirhe: " + e.getMessage());
        }
        
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(sisalt);
        dataset.addSeries(ulkolt);
        if (kayra_taulukko.get(2).size()>0) dataset.addSeries(bt2);
        dataset.addSeries(bt3);
        dataset.addSeries(bt25);
        dataset.addSeries(bt10);
        dataset.addSeries(bt11);
        dataset.addSeries(bt12);
        dataset.addSeries(bt14);
        dataset.addSeries(bt17);//10
        dataset.addSeries(dm);
        dataset.addSeries(cs);
        dataset.addSeries(dtla);
        dataset.addSeries(dtbr);
        dataset.addSeries(cop);
        //EP15
        dataset.addSeries(ep15_bt3);
        dataset.addSeries(ep15_bt10);
        dataset.addSeries(ep15_bt11);
        dataset.addSeries(ep15_bt12);
        dataset.addSeries(ep15_bt14);//20
        dataset.addSeries(ep15_bt17);
        dataset.addSeries(ep15_dtla);
        dataset.addSeries(ep15_dtbr);
        dataset.addSeries(ep15_cop);
        //sälät
        dataset.addSeries(pca);
        dataset.addSeries(ep15_prio);     
        //KV
        dataset.addSeries(bt7);
        dataset.addSeries(bt6);
        //Suorasähkö
        dataset.addSeries(kw_sahko);
        //BF1
        dataset.addSeries(bf1);//30
        //CFA
        dataset.addSeries(cfa);
        //teho
        dataset.addSeries(teho);
        //GPt
        dataset.addSeries(gp1);
        dataset.addSeries(gp2);
        dataset.addSeries(ep15_gp1);
        dataset.addSeries(bt51);
        dataset.addSeries(bt53);
        dataset.addSeries(bt54);
        dataset.addSeries(prio);
        dataset.addSeries(bt20);//40
        dataset.addSeries(bt21);
        dataset.addSeries(bt71);
        dataset.addSeries(ep15_gp2);
        dataset.addSeries(be1);
        dataset.addSeries(be2);
        dataset.addSeries(be3);
        return dataset;
    }
    
    // teeKayratIkkuna
	// Näytä käyrät datasta
    //  
    public void teeKayratIkkuna()
    {
    	final ChartPanel kayrat = teeKayratPaneeli(ohjain.getKayra_taulukko_nimet(), ohjain.getKayra_taulukko(),ohjain.getLogiaika());
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
            	final JFrame kayrakehys = new JFrame("Trendit");
                kayrakehys.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                final JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setOpaque(true);
                JPanel panel2 = new JPanel();
                panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
                panel2.setOpaque(true);
                JPanel panel21 = new JPanel();
                //panel21.setLayout(new BoxLayout(panel21, BoxLayout.X_AXIS));
                //panel21.setLayout(new GridLayout(1,8));
                panel21.setLayout(new GridLayout(3,5));
                panel21.setOpaque(true);
                panel21.setBorder(BorderFactory.createLineBorder(Color.black));
                TitledBorder title;
                title = BorderFactory.createTitledBorder("Yleiset ja EP14");
                panel21.setBorder(title);
                
                //EP15
                JPanel panel22 = new JPanel();
                panel22.setLayout(new GridLayout(3,5));
                panel22.setOpaque(true);
                panel22.setBorder(BorderFactory.createLineBorder(Color.black));
                TitledBorder title2;
                title2 = BorderFactory.createTitledBorder("EP15");
                panel22.setBorder(title2);

                //KV
                JPanel panel23 = new JPanel();
                panel23.setLayout(new GridLayout(3,5));
                panel23.setOpaque(true);
                panel23.setBorder(BorderFactory.createLineBorder(Color.black));
                TitledBorder title3;
                title3 = BorderFactory.createTitledBorder("KV, suorasähkö, kompressori ja muut");
                panel23.setBorder(title3);

                JScrollPane scroller = new JScrollPane(kayrat);
                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                panel.add(scroller);

                //piiloon by default
                (kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(7, false, true);
                (kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(8, false, true);
                (kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(9, false, true);
                (kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(10, false, true);
                (kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(11, false, true);
                (kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(12, false, true);
                (kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(13, false, true);
                (kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(14, false, true);
                (kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(15, false, true);
                //EP15
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(16, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(17, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(18, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(19, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(20, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(21, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(22, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(23, false, true);
               	//sälät
            	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(24, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(25, false, true);
               	//KV
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(26, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(27, false, true);
               	//Suorasähkö
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(28, false, true);
               	//BF1 virtaus
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(29, false, true);
               	//Kompressorin nopeus, pois jossei F1x55
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(30, false, true);
               	//teho
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(31, false, true);
               	//GPt
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(32, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(33, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(34, false, true);
               	//uima-allas, aurinko
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(35, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(36, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(37, false, true);
               	//prio
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(38, false, true);
               	//FLM
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(39, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(40, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(41, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(42, false, true);               	
               	
               	//virtamittarit
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(43, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(44, false, true);
               	(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(45, false, true);

                //tehdään valintanappulat
                for (int i = 0; i < ohjain.getKayra_taulukko().size(); i++) 
            	  {
            	   final int suure = i;
		           final JCheckBox chkbox = new JCheckBox(ohjain.getKayra_taulukko_nimet().get(i));
		                if (i >= 0 && i < 7) {
		                	if (Math.abs(Collections.max(ohjain.getKayra_taulukko().get(i)))>0) {
		                		chkbox.setSelected(true);
		                	} else {
		                		(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(i, false, true);
		                	}
		                }
		                if (lblMLPMalli.getText().equalsIgnoreCase("F11/245")) {
		                	if (i >= 15 && i <= 23) chkbox.setEnabled(false);
		                }  else {
		                	if (i >= 15) chkbox.setEnabled(true);
		                }
		                
		                if (lblMLPMalli.getText().contains("PILP")) {
		                	if (i >= 39 && i <= 40) {
		                		chkbox.setSelected(true);
		                		(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(suure, true, true);
		                	}
		                }

		                //checkboksien kuuntelijat
		                chkbox.addItemListener(new ItemListener() {
                	
		                    @Override
		                    public void itemStateChanged(ItemEvent e) {
		                        //System.out.println(e.getStateChange() == ItemEvent.SELECTED
		                        //		? "SELECTED" : "DESELECTED");
		                    	if (!chkbox.isSelected()){
		                    		(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(suure, false, true);
		                    	}
		                    	else if (chkbox.isSelected()){
		                    		(kayrat.getChart().getXYPlot().getRendererForDataset(kayrat.getChart().getXYPlot().getDataset(0))).setSeriesVisible(suure, true, true);

		                    	}
		                    }
		                });
		                


		                if (i >= 0 && i < 15){
		                	panel21.add(chkbox);
		                } else if (i >= 15 && i < 24) {
	                		panel22.add(chkbox);
		                } else if (i >= 26 ) {
		                    if (i==34) { //ep15_gp1 = 34
		                    	if (lblMLPMalli.getText().equalsIgnoreCase("F1345")) {
		                    		panel22.add(chkbox);
		                    	}
		                    } else if (i==42) { //ep15_gp2 = 42
		                    	if (lblMLPMalli.getText().equalsIgnoreCase("F1345")) {
		                    		panel22.add(chkbox);
		                    	}
			                } else {
		                    	if (i!=38 && i!=46) { //prioriteetti = 38, hälytykset = 46
		                    		panel23.add(chkbox);
		                    	}
		                    }
		                }
            	  }
                
                //hälytykset trendin päälle
                final JCheckBox chkbox = new JCheckBox("hälytykset");
                panel23.add(chkbox);
                
            	@SuppressWarnings("unchecked")
            	List<XYPointerAnnotation> annotations = kayrat.getChart().getXYPlot().getAnnotations();
            	if ( annotations.size() > 0 ) {
            		//jos hälytyksiä, aseta valintanappula päälle
            		chkbox.setSelected(true);
            	}
            	
                //checkboksin kuuntelija
                chkbox.addItemListener(new ItemListener() {
                	@Override				
                    public void itemStateChanged(ItemEvent e) {
                        //System.out.println(e.getStateChange() == ItemEvent.SELECTED
                        //		? "SELECTED" : "DESELECTED");

                    	if (!chkbox.isSelected()){
                    		//poistetaan annotaatiot (hälytykset) kuvaajasta
                    		kayrat.getChart().getXYPlot().clearAnnotations();
                    	}
                    	else if (chkbox.isSelected()){
                    		for( XYPointerAnnotation a : annotations) {
                    			//annotaatiot (hälytykset) takaisin
                    			kayrat.getChart().getXYPlot().addAnnotation(a);
                    		}
                    	}
                    }
                });
                
                panel2.add(panel21);
                if (lblMLPMalli.getText().equalsIgnoreCase("F1345")) {
                	panel2.add(panel22);
                }
                panel2.add(panel23);
                
                if (!tietokanta_omahakuaika && tietolahde==1) {
	                //settings
	                JPanel panel24 = new JPanel();
	                panel24.setLayout(new GridLayout(1,1));
	                panel24.setOpaque(true);
	                panel24.setBorder(BorderFactory.createLineBorder(Color.black));
	                TitledBorder title4;
	                title4 = BorderFactory.createTitledBorder("muuta");
	                panel24.setBorder(title4);
	                
	            	final JCheckBox autoupd_chkbox = new JCheckBox("autom. päivitys");
	            	//asetetaan nappulan tila jos autoupdate oli jo päällä
	            	autoupd_chkbox.setSelected(getAutoUpdChkbox());
	                panel24.add(autoupd_chkbox);

	                //checkboksin kuuntelija
	                autoupd_chkbox.addItemListener(new ItemListener() {
	                	
	                    @Override
	                    public void itemStateChanged(ItemEvent e) {
	                    	if (!autoupd_chkbox.isSelected()){
	                    		konsoli.append("Automaattipäivitys pois.\n");
	                    		setAutoUpdChkbox(false);
	                    		
	                    	}
	                    	else if (autoupd_chkbox.isSelected()){
	                    		konsoli.append("Automaattipäivitys päällä.\n");
	                    		setAutoUpdChkbox(true);
	                    		 Thread thread = new Thread(new Updater(kayrat));
	                             thread.setDaemon(true);
	                             thread.start();
	                    	}
	
	                    }//kuuntelija
	
	                });
	                panel2.add(panel24);
                }

                kayrakehys.getContentPane().add(BorderLayout.NORTH, panel);
                kayrakehys.getContentPane().add(BorderLayout.CENTER, panel2);
                kayrakehys.pack();
                kayrakehys.setLocationByPlatform(true);
                kayrakehys.setVisible(true);
                kayrakehys.setResizable(true);
                setCursor(Cursor.getDefaultCursor());
            }
        });

    }
    
    // kysyTallennusta
    // kysytään tallennetaanko paikalliseen tietokantaan
    //
    public boolean kysyTallennusta() {
    	Object[] options = {"Kyllä","Ei"};
    	//int kysyTulos = JOptionPane.showConfirmDialog(this, "Tallennetaanko paikalliseen tietokantaan?", "Tallennetaanko?", kysyTallennus, options);
    	int kysyTulos=JOptionPane.showOptionDialog(this, "Tallennetaanko tiedostologi paikalliseen tietokantaan myöhempää lukua varten?", 
                "Tallennetaanko?", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE, 
                null, options, options[1]);
    	if(kysyTulos == 0) {
    		return true;
    	} else {
    		return false;
    	} 
    }
    
    //SETTERS & GETTERS
    //käyrän taustaväri ja ristikko
    static Color getGraafinTaustanVari() {
		return ohjain.getGraafinTaustanVari();
	}

    static Color getGraafinAsteikonVari() {
		return ohjain.getGraafinAsteikonVari();
	}
	
    public void kirjoitaKonsolille (String teksti){
    	konsoli.append(teksti);
    }
    
    public JRadioButton getRadioNappulaEP14() {
		return radioNappulaEP14;
	}

	public void setRadioNappulaEP14(JRadioButton radioNappulaEP14) {
		this.radioNappulaEP14 = radioNappulaEP14;
	}

	public JRadioButton getRadioNappulaEP15() {
		return radioNappulaEP15;
	}

	public void setRadioNappulaEP15(JRadioButton radioNappulaEP15) {
		this.radioNappulaEP15 = radioNappulaEP15;
	}

	public static JLabel getLblMittausvaliKentta() {
		return lblMittausvaliKentta;
	}

	public static void setLblMittausvaliKentta(JLabel lblMittausvaliKentta) {
		LogintutkijaGUI.lblMittausvaliKentta = lblMittausvaliKentta;
	}


	public JButton getLuoKayrat() {
		return luoKayrat;
	}

	public void setLuoKayrat(JButton luoKayrat) {
		this.luoKayrat = luoKayrat;
	}

	public double getLa() {
		return la;
	}

	public void setLa(double la) {
		this.la = la;
	}

	public double getKv() {
		return kv;
	}

	public void setKv(double kv) {
		this.kv = kv;
	}

	public double getLe() {
		return le;
	}

	public void setLe(double le) {
		this.le = le;
	}

	public boolean isJatkuvatlogit() {
		return jatkuvatlogit;
	}

	public void setJatkuvatlogit(boolean jatkuvatlogit) {
		LogintutkijaGUI.jatkuvatlogit = jatkuvatlogit;
	}

	public JLabel getLblMLPMalli() {
		return lblMLPMalli;
	}

	public void setLblMLPMalli(JLabel lblMLPMalli) {
		this.lblMLPMalli = lblMLPMalli;
	}
	
	public JLabel getLblSuos5() {
		return suos5;
	}

	public void setLblSuos5(JLabel suos5) {
		this.suos5 = suos5;
	}

	public int getTietolahde() {
		return tietolahde;
	}

	public void setTietolahde(int tietolahde) {
		this.tietolahde = tietolahde;
	}

	public String getTietokanta_url() {
	    //tietokanta_url = "jdbc:mysql://" + tietokanta_osoite + ":3306/talologger";
	    tietokanta_url = "jdbc:" + combo.getSelectedItem().toString().toLowerCase() + "://" + tietokanta_osoite + ":3306/talologger";
		return tietokanta_url;
	}

	public String getTietokanta_dbms() {
		if (ekayhteys != true) {
		return combo.getSelectedItem().toString().toLowerCase();
		} else {
			return this.tietokanta_dbms;
		}
	}
	
	public String getTietokanta_nimi() {
		return tietokanta_nimi;
	}

	public String getTietokanta_osoite() {
		return tietokanta_osoite;
	}
	
	public void setTietokanta_dbms(String tietokanta_dbms) {
		this.tietokanta_dbms = tietokanta_dbms;
	}

	public void setTietokanta_osoite(String tietokanta_osoite) {
		this.tietokanta_osoite = tietokanta_osoite;
	}
	
	public void setTietokanta_nimi(String tietokanta_nimi) {
		this.tietokanta_nimi = tietokanta_nimi;
	}
	
	public void setTietokanta_url(String tietokanta_url) {
		this.tietokanta_url = tietokanta_url;
	}

	public String getTietokanta_kayttaja() {
		return tietokanta_kayttaja;
	}

	public void setTietokanta_kayttaja(String tietokanta_kayttaja) {
		this.tietokanta_kayttaja = tietokanta_kayttaja;
	}

	public String getTietokanta_salasana() {
		return tietokanta_salasana;
	}

	public void setTietokanta_salasana(String tietokanta_salasana) {
		this.tietokanta_salasana = tietokanta_salasana;
	}

	public String getTietokanta_pvm_mihin() {
		return tietokanta_pvm_mihin;
	}

	public void setTietokanta_pvm_mihin(String tietokanta_pvm_mihin) {
		this.tietokanta_pvm_mihin = tietokanta_pvm_mihin;
	}

	public String getTietokanta_pvm_mista() {
		return tietokanta_pvm_mista;
	}

	public void setTietokanta_pvm_mista(String tietokanta_pvm_mista) {
		this.tietokanta_pvm_mista = tietokanta_pvm_mista;
	}

	public JLabel getLblLokiVersio() {
		return lblLokiVersio;
	}

	public void setLblLokiVersio(JLabel lblLokiVersio) {
		this.lblLokiVersio = lblLokiVersio;
	}
	public boolean getAutoUpdChkbox() {
		return autoupdate;
	}

	public JLabel getLblLammonKeruu() {
		return lblLammonKeruu;
	}

	public void setLblLammonKeruu(JLabel lblLammonKeruu) {
		this.lblLammonKeruu = lblLammonKeruu;
	}

	public void setAutoUpdChkbox(boolean autoupdate) {
		LogintutkijaGUI.autoupdate = autoupdate;
	}
	
	
	public static String getAikaAloitus() {
		return aikaAloitus;
	}

	public static void setAikaAloitus(String aikaAloitus) {
		LogintutkijaGUI.aikaAloitus = aikaAloitus;
	}


	public static String getAikaLopetus() {
		return aikaLopetus;
	}

	public static void setAikaLopetus(String aikaLopetus) {
		LogintutkijaGUI.aikaLopetus = aikaLopetus;
	}


	public double getSav() {
		return sav;
	}

	public void setSav(double sav) {
		this.sav = sav;
	}

	public double getSal() {
		return sal;
	}

	public void setSal(double sal) {
		this.sal = sal;
	}

	public double getUal() {
		return ual;
	}

	public void setUal(double ual) {
		this.ual = ual;
	}
	
	public double getLaFlm() {
		return la_flm;
	}

	public void setLaFlm(double la_flm) {
		this.la_flm = la_flm;
	}

	public double getKvFlm() {
		return kv_flm;
	}

	public void setKvFlm(double kv_flm) {
		this.kv_flm = kv_flm;
	}
	
	public void setValittu_Tiedosto_Filtteri(String vtf) {
		LogintutkijaGUI.vtf=vtf;
	}
	
	public static String getValittu_Tiedosto_Filtteri() {
		return vtf;
	}

	public class Updater implements Runnable {

        private ChartPanel kayrat;

		public Updater(final ChartPanel kayrat) {
                this.kayrat = kayrat;
        }
        @Override
		public void run() {	
            while (true) {
       			 if (!getAutoUpdChkbox()){
       				 break;	
       			}
    			//muutetaan kellonajat hakua varten
       			tietokanta_pvm_mista = dateFormat.format(jokuaikasitten());
       			tietokanta_pvm_mihin = dateFormat.format(nyt());

        		 //uudet arvot
        		 if (!ohjain.isRunning()) {
            		 ohjain.aloitaSaie();
        		 }
        		 while (!ohjain.isRunning()){
        			 try {
                     	Thread.sleep(500);
                 	} catch (InterruptedException ex) {
                	}
        		 }
         			while (ohjain.isRunning()) {
         				try {
                        	Thread.sleep(100);
                    	} catch (InterruptedException ex) {
                    	}
         			}      		
         		kayrat.getChart().getXYPlot().setDataset(teeTietoJoukko(ohjain.getKayra_taulukko_nimet(), ohjain.getKayra_taulukko(),ohjain.getLogiaika()));
         		kayrat.getChart().getXYPlot().getDomainAxis().setLabel(aikaAloitus + aikaLopetus);
        		//odotetaan mittausvälin verran
                try {
                    Thread.sleep(LogintutkijaOhjain.getMittausvali_ms());
                } catch (InterruptedException ex) {
                }
            }
        }

    }
	
	/**
	 * Inherited FileFilter class to facilitate reuse when
	 * multiple file filter selections are required. For example
	 * purposes, I used a static nested class, which is defined
	 * as below as a member of our original FileChooserExample
	 * class.
	 */
	static class ExtensionFileFilter 
	    extends javax.swing.filechooser.FileFilter {

	    private java.util.List<String> extensions;
	    private String description;

	    public ExtensionFileFilter(String[] exts, String desc) {
	        if (exts != null) {
	            extensions = new java.util.ArrayList<String>();

	            for (String ext : exts) {

	                // Clean array of extensions to remove "."
	                // and transform to lowercase.
	                extensions.add(
	                    ext.replace(".", "").trim().toLowerCase()
	                );
	            }
	        } // No else need; null extensions handled below.

	        // Using inline if syntax, use input from desc or use
	        // a default value.
	        // Wrap with an if statement to default as well as
	        // avoid NullPointerException when using trim().
	        description = (desc != null) ? desc.trim() : "Custom File List";
	    }

	    // Handles which files are allowed by filter.
	    @Override
	    public boolean accept(File f) {
	    
	        // Allow directories to be seen.
	        if (f.isDirectory()) return true;

	        // exit if no extensions exist.
	        if (extensions == null) return false;
			
	        // Allows files with extensions specified to be seen.
	        for (String ext : extensions) {
	            if (f.getName().toLowerCase().endsWith("." + ext))
	                return true;
	        }

	        // Otherwise file is not shown.
	        return false;
	    }

	    // 'Files of Type' description
	    @Override
	    public String getDescription() {
	        return description;
	    }
	}
	
}
