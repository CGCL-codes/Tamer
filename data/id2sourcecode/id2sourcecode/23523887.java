    public void run(Connection con) {
        mycon = con;
        myio = mycon.getTerminalIO();
        mycon.addConnectionListener(this);
        Reader reader = new mvTelnetReader(myio);
        PrintWriter writer = new PrintWriter(new mvTelnetWriter(myio));
        Properties prop = new Properties();
        String prop_file = System.getProperty("maverick.config");
        if (prop_file != null) prop.loadFile(prop_file);
        Session session = new Session(prop, reader, writer);
        mvString[] args = new mvString[0];
        SH shell = new SH();
        shell.run(session, args);
    }
