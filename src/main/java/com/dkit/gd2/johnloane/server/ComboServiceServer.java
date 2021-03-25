package com.dkit.gd2.johnloane.server;

import com.dkit.gd2.johnloane.core.ComboServiceDetails;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ComboServiceServer
{
    public static void main(String[] args)
    {
        try
        {
            //Step 1: Set up a listening socket - this just listens for connections
            //Once a socket connects we create a dataSocket for the rest of the
            //communication
            ServerSocket listeningSocket = new ServerSocket(ComboServiceDetails.LISTENING_PORT);
            Socket dataSocket = new Socket();

            boolean continueRunning = true;

            while(continueRunning)
            {
                //Step 2
                //Once a connection is accepted on the listening socket
                //spawn a dataSocket for the rest of the communication
                dataSocket = listeningSocket.accept();

                //Step 3 - set up input and output streams
                OutputStream out = dataSocket.getOutputStream();
                //Decorator pattern
                PrintWriter output = new PrintWriter(new OutputStreamWriter(out));

                InputStream in = dataSocket.getInputStream();
                Scanner input = new Scanner(new InputStreamReader(in));

                String incomingMessage = "";
                String response;

                while(true)
                {

                        response = null;

                        //Take the information from the client
                        incomingMessage = input.nextLine();
                        System.out.println("Received message: " + incomingMessage);

                        String[] messageComponents = incomingMessage.split(ComboServiceDetails.COMMAND_SEPARATOR);
                        //echo%%Hello World, echo%%Hello%%World
                        //0%%1                  0%%1%%2
                        if (messageComponents[0].equals(ComboServiceDetails.ECHO))
                        {
                            StringBuffer echoMessage = new StringBuffer("");
                            if (messageComponents.length > 1)
                            {
                                echoMessage.append(messageComponents[1]);
                            }
                            for (int i = 2; i < messageComponents.length; i++)
                            {
                                echoMessage.append(ComboServiceDetails.COMMAND_SEPARATOR);
                                echoMessage.append(messageComponents[i]);
                            }
                            response = echoMessage.toString();
                        } else if (messageComponents[0].equals(ComboServiceDetails.DAYTIME))
                        {
                            response = new Date().toString();
                        } else if (messageComponents[0].equals(ComboServiceDetails.STATS))
                        {
                            //TODO Home work
                            response = "This has not been implemented yet. This is for homework";
                        } else if (messageComponents[0].equals(ComboServiceDetails.END_SESSION))
                        {
                            response = ComboServiceDetails.SESSION_TERMINATED;
                        } else
                        {
                            response = ComboServiceDetails.UNRECOGNISED;
                        }

                        //Send response back
                        output.println(response);
                        output.flush();
                    }

                }
            }
        catch (IOException e)
        {
            System.out.println(e.getMessage());;
        }
        catch(NoSuchElementException e)
        {
            System.out.println("Shutting down. I think we need threads");
            System.exit(1);
        }
    }
}
