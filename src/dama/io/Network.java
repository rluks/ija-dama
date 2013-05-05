package dama.io;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Třída Network zapouzdřuje síťovou komunikaci.
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Network
{








    public static void main(String[] args)
    {
        String s = "testovací datagram";

        byte ba[] = null;

        try
        {
            ba = s.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
          System.out.println("pojebany kodovani");
        }

        //DatagramPacket dgp1 = new DatagramPacket(ba, ba.length);
        //DatagramPacket dgp2 = new DatagramPacket(ba, 2, 3);

        DatagramPacket dgp3 = null;
        //DatagramPacket dgp4 = null;

        try
        {
            dgp3 = new DatagramPacket(ba, ba.length, InetAddress.getByName("localhost"), 12345);
            //dgp4 = new DatagramPacket(ba, ba.length, new InetSocketAddress("pocitac.domena.cz", 12345));
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }

        try
        {
            DatagramSocket sock1 = new DatagramSocket();

            //sock1.send(dgp1);  // chyba - datagram ani socket nemá cíl. adresu a port
            sock1.send(dgp3);  // funguje

            /*
            DatagramSocket sock2 = new DatagramSocket(55555);
            sock2.connect(InetAddress.getByName("pocitac.domena.org"), 44444);

            sock2.send(dgp2);  // funguje
            sock2.send(dgp4);  // chyba - výjimka kvůli neshodě adres
            */
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }




    }



}