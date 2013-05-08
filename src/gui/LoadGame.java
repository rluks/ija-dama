package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Trida ktera ma na starost internal frame s nabidkou pro otevreni/ulozeni souboru.
 * @author Pavel Hala
 * @author Roman Luks
 */
public class LoadGame extends JInternalFrame
{
    private static final long serialVersionUID = -8299862914609921412L;

    private JTextField TFCesta;
    private JButton BtBrowse;

    /**
     * Konstruktor. Vytvori novy internal frame
     * @param nazevOkna    jak se ma okno jmenovat (pouzivame "Save game" a "Load game")
     */
    public LoadGame(String nazevOkna)
    {
            super("" ,
                  false, //resizable
                  true, //closable
                  false, //maximizable
                  false);//iconifiable

       this.setSize(450,120);        //velikost okna
       this.title=nazevOkna;        //nazev

       getContentPane().setLayout(null);    //zadny layout ->absolutni pozicovani

       this.TFCesta = new JTextField();            //textfiled s cestou
       this.TFCesta.setBounds(54, 11, 231, 20);
       getContentPane().add(this.TFCesta);

       this.BtBrowse = new JButton("Browse");        //tlacitko "browse", otevre dialog s vyberem souboru
       this.BtBrowse.setBounds(294, 11, 140, 20);
       this.BtBrowse.addActionListener(new OpenL());
       getContentPane().add(this.BtBrowse);

       JLabel popisek = new JLabel("File:");    //jen popisek
       popisek.setBounds(26, 15, 34, 14);
       getContentPane().add(popisek);
    }

     /**
     * Trida ktera ma na starost otevreni nabidky pro vyber souboru
     * @author Pavel Hala
     * @author Roman Luks
     */
    class OpenL implements ActionListener
      {
            public void actionPerformed(ActionEvent e)
            {
                  //System.out.println("Action listener:" + e.getActionCommand());
                  JFileChooser c = new JFileChooser(".\\examples");
                  FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
                  c.setFileFilter(xmlfilter);
                  FileNameExtensionFilter xmlfilter2 = new FileNameExtensionFilter("standart notation (.txt)", "txt");
                  c.setFileFilter(xmlfilter2);

                  int rVal = c.showOpenDialog(LoadGame.this);
                  if (rVal == JFileChooser.APPROVE_OPTION)
                  {
                        //TFCesta.setText(c.getCurrentDirectory().toString() + "\\" + c.getSelectedFile().getName());
                      TFCesta.setText((String)c.getSelectedFile().getAbsolutePath());
                  }
            }
      }


     /**
     * Vrati obsah textfieldu s cestou k souboru
     * @return cesta k souboru
     */
    public String GetPath()
      {
          return this.TFCesta.getText();
      }
}

