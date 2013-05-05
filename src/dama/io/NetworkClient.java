package dama.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class NetworkClient
{

    public static void main(String[] args)
    {
        try
        {
            System.out.println("Client");
            Socket connectionToTheServer = new Socket("localhost", 8888);

            OutputStream out = connectionToTheServer.getOutputStream();
            PrintStream ps = new PrintStream(out, true); // Second param: auto-flush on write = true
            String odesilam="a3-b4";
            ps.println(odesilam);
            System.out.println("Client: posilam: \"" + odesilam + "\"");

            InputStream in = connectionToTheServer.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            Turn turn=Turn.NotStringToTurn(line, 'W');
            if (turn==null)System.out.println("Klient: od serveru prislo: \"" + line + "\" - NEVALIDNI TAH");
            else System.out.println("Klient: od serveru prislo: \"" + line + "\" - validni tah");

            connectionToTheServer.close();

        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
