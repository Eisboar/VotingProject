package com.example.votingServer;
import java.net.*;



public class Server extends Thread {

	private GUI gui = null;
	
	public Server(GUI gui){
		this.gui=gui;
	}
	
	public void run() {

        int nreq = 1;
        gui.addText("waiting");
        try
        {
            ServerSocket sock = new ServerSocket (8080);
            for (;;)
            {
                Socket newsock = sock.accept();
                System.out.println("Creating thread ...");
                Thread t = new ThreadHandler(newsock,nreq, gui);
                t.start();
                nreq++;
            }
            
        }
        catch (Exception e)
        {
            System.out.println("IO error " + e);
        }
        System.out.println("End!");
    }
}

