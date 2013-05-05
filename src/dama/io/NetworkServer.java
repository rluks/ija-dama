package dama.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Třída Network zapouzdřuje síťovou komunikaci.
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class NetworkServer
{

    public static void main(String[] args)
    {
        //iniciator
        try
        {
            System.out.println("Server");
            ServerSocket server = new ServerSocket(8888);
            Socket connectionToTheClient = server.accept();
            System.out.println("Server po accept");

            InputStream in = connectionToTheClient.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            Turn turn=Turn.NotStringToTurn(line, 'W');
            if (turn==null)System.out.println("Server: od klienta prislo: \"" + line + "\" - NEVALIDNI TAH");
            else System.out.println("Server: od klienta prislo: \"" + line + "\" - validni tah");

            OutputStream out = connectionToTheClient.getOutputStream();
            PrintStream ps = new PrintStream(out, true); // Second param: auto-flush on write = true
            String odesilam="b6-c5";
            ps.println(odesilam);
            System.out.println("Server: posilam: \"" + odesilam + "\"");

            server.close();
        }
        catch (Exception e)
        {
            System.out.println("Sit se pojebala!");
        }

    }



}