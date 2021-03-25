package com.dkit.gd2.johnloane.client;

import com.dkit.gd2.johnloane.core.ComboServiceDetails;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ComboServiceClient
{
    public static void main(String[] args)
    {
        try
        {
            //Step 1: Establish a connection to the server
            // Like a phone call, first need to dial the number
            // before you can talk
            Socket dataSocket = new Socket(ComboServiceDetails.HOST, ComboServiceDetails.LISTENING_PORT);

            //Step 2: Build input and output streams
            OutputStream out = dataSocket.getOutputStream();
            //Decorator pattern
            PrintWriter output = new PrintWriter(new OutputStreamWriter(out));

            InputStream in = dataSocket.getInputStream();
            Scanner input = new Scanner(new InputStreamReader(in));

            Scanner keyboard = new Scanner(System.in);

            String message = "";

            while(!message.equals(ComboServiceDetails.END_SESSION))
            {
                displayMenu();
                int choice = getNumber(keyboard);
                String response = "";

                if(choice >= 0 && choice < 4)
                {
                    switch(choice)
                    {
                        case 0:
                            message = ComboServiceDetails.END_SESSION;
                            //send message
                            output.println(message);
                            output.flush();

                            //Listen for response
                            response = input.nextLine();
                            if(response.equals(ComboServiceDetails.SESSION_TERMINATED))
                            {
                                System.out.println("Session ended");
                            }
                            break;
                        case 1:
                            message = generateEcho(keyboard);

                            //send message
                            output.println(message);
                            output.flush();
                            //listen for response
                            response = input.nextLine();
                            System.out.println("Received response: " + response);
                            break;

                        case 2:
                            message = ComboServiceDetails.DAYTIME;
                            //send message
                            output.println(message);
                            output.flush();
                            //listen for response
                            response = input.nextLine();
                            System.out.println("The current date and time is: " + response);
                            break;
                        case 3:
                            message = ComboServiceDetails.STATS;
                            //send message
                            output.println(message);
                            output.flush();
                            //listen for response
                            response = input.nextLine();
                            System.out.println("The stats value is: " + response);
                            break;
                    }
                    if(response.equals(ComboServiceDetails.UNRECOGNISED))
                    {
                        System.out.println("Sorry, the request is not recognised. Please enter a number between 0 and 3 inclusive");
                    }
                }
                else
                {
                    System.out.println("Please select and option from the menu");
                }
            }
            System.out.println("Thanks for using the TCP Combo Service");
            dataSocket.close();
        }
        catch (UnknownHostException e)
        {
            System.out.println(e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static String generateEcho(Scanner keyboard)
    {
        StringBuffer message = new StringBuffer(ComboServiceDetails.ECHO);
        message.append(ComboServiceDetails.COMMAND_SEPARATOR);
        System.out.print("Enter message to echo:> ");
        String echo = keyboard.nextLine();
        message.append(echo);

        return message.toString();
    }

    private static int getNumber(Scanner keyboard)
    {
        boolean numberEntered = false;
        int number = 0;
        while(!numberEntered)
        {
            try
            {
                number = keyboard.nextInt();
                numberEntered = true;
            }
            catch(InputMismatchException e)
            {
                System.out.println("Please enter an integer (0-3)");
                keyboard.nextLine();
            }
        }
        keyboard.nextLine();
        return number;
    }

    private static void displayMenu()
    {
        System.out.println("0) To quit");
        System.out.println("1) To echo a message");
        System.out.println("2) To get server date and time");
        System.out.println("3) To get stats");
    }
}
