package com.dkit.gd2.johnloane.core;

public class ComboServiceDetails
{
    public static final int LISTENING_PORT = 50010;
    public static final String COMMAND_SEPARATOR = "%%";
    public static final String HOST = "localhost";

    //Command strings
    public static final String END_SESSION = "QUIT";
    public static final String ECHO = "ECHO";
    public static final String DAYTIME = "DAYTIME";
    public static final String STATS = "STATS";

    //Response strings
    public static final String UNRECOGNISED = "UNKNOWN_COMMAND";
    public static final String SESSION_TERMINATED = "GOODBYE";


}
