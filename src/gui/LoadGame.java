package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LoadGame extends JInternalFrame
{
    private static final long serialVersionUID = -8299862914609921412L;

    private JTextField TFCesta;
    private JButton BtBrowse;
    //private JButton BtOk;

    public LoadGame(String nazevOkna)
    {


            super("" ,
                  true, //resizable
                  true, //closable
                  false, //maximizable
                  false);//iconifiable

            System.out.println("\t\tKonstruktor LG");

           this.setSize(450,120);
           this.title=nazevOkna;

        getContentPane().setLayout(null);

        this.TFCesta = new JTextField();
        this.TFCesta.setBounds(54, 11, 231, 20);
        getContentPane().add(this.TFCesta);
        //this.cesta.setColumns(10);

        this.BtBrowse = new JButton("Browse");
        this.BtBrowse.setBounds(294, 11, 140, 20);
        this.BtBrowse.addActionListener(new OpenL());
        getContentPane().add(this.BtBrowse);

        JLabel popisek = new JLabel("File:");
        popisek.setBounds(26, 15, 34, 14);
        getContentPane().add(popisek);

        /*
        this.BtOk = new JButton("OK");
        this.BtOk.setBounds(272, 39, 89, 23);
        getContentPane().add(this.BtOk);
         */

        System.out.println("\t\t/Konstruktor LG");
    }




      class OpenL implements ActionListener
      {
            public void actionPerformed(ActionEvent e)
            {
                  //System.out.println("Action listener:" + e.getActionCommand());
                  JFileChooser c = new JFileChooser(".\\examples");
                  FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
                  c.setFileFilter(xmlfilter);
                  FileNameExtensionFilter xmlfilter2 = new FileNameExtensionFilter("standast notation (.txt)", "txt");
                  c.setFileFilter(xmlfilter2);

                  int rVal = c.showOpenDialog(LoadGame.this);
                  if (rVal == JFileChooser.APPROVE_OPTION)
                  {
                        //TFCesta.setText(c.getCurrentDirectory().toString() + "\\" + c.getSelectedFile().getName());
                      TFCesta.setText((String)c.getSelectedFile().getAbsolutePath());
                  }
            }
      }


      public String GetPath()
      {
          return this.TFCesta.getText();
      }
}

