package logintutkija;

import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class Logintutkija {
	
	/************************************
	 * Logintutkija Main
	 * 
	 ************************************/
	
    /******************
     * LUOKKAMUUTTUJAT
     ******************/
	private static String sovNimi = "LP logintutkija";
	private static String sovVersio = "v1.3.14";
	private static String sovNimiVersio = sovNimi + " " + sovVersio;
	
	
	/******************************************************************
     * Luodaan ohjelman päänäkymä, ajetaan tapahtumankäsittelysäikeessä
     * MVC:n mukaiset malli, ikkuna ja ohjain oliot
     ******************************************************************/
    private static void luoJaNaytaGUI() throws IOException {
		final LogintutkijaMalli malli = new LogintutkijaMalli();
		final LogintutkijaGUI ikkuna = new LogintutkijaGUI();
		LogintutkijaOhjain ohjain = new LogintutkijaOhjain(malli, ikkuna);
		ikkuna.rekisteröiOhjain(ohjain);
		malli.rekisteröiOhjain(ohjain);
		
		//luetaan konfiguraatio
		if (malli.haeKonfiggi() == false)
			malli.haeKonfiggi();
		
		//haetaan lämmitystarveluvut
		//if (malli.haeHDDGet() == false)
		//	malli.haeHDDGet();
		
        //Luodaan ikkuna
        final JFrame kehys = new JFrame(sovNimiVersio);
        //jotta voidaan kysyä halutaanko lopettaa vai ei
        kehys.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        kehys.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            	UIManager.put("OptionPane.yesButtonText", "Kyllä");
            	UIManager.put("OptionPane.noButtonText", "Ei");
                if (JOptionPane.showConfirmDialog(kehys, 
                    "Haluatko poistua?", "Lopetetaanko?", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                		malli.tallennaKonfiggi();
                		kehys.dispose();
                		System.exit(0);
                }
            }
        });
        //Luodaan sisältöalue
        kehys.getContentPane().add(ikkuna);
        //Näytetään ikkuna
        kehys.setPreferredSize(new java.awt.Dimension(850, 720));
        kehys.pack();
        kehys.setVisible(true);  
    }
    
    /*************
     * PÄÄOHJELMA
     *************/
	public static void main(String[] args) {		
        //Tässä tehdään säie jossa näytetään käyttöliittymä ja kuunnellaan tapahtumia
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
			public void run() {
            	try {
            		luoJaNaytaGUI();
				} catch (IOException e) {
					// ei pystytty luomaan säiettä - mikä tilanne tuo tähän?
					e.printStackTrace();
				}
            }
        });
    }
}
