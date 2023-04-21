    public static void main(String[] args) {
        Logger logger = Logger.getLogger("edu.mit.tbp.se.chat");
        CommandArgs parsedArgs;
        try {
            parsedArgs = CommandArgs.parseArgs(args);
        } catch (IllegalArgumentException e) {
            System.err.println(e);
            System.err.println(CommandArgs.getUsage());
            System.exit(-1);
            return;
        }
        if (parsedArgs.debug) {
            ConsoleHandler ch = new ConsoleHandler();
            logger.setLevel(parsedArgs.debugLevel);
            logger.addHandler(ch);
            ch.setLevel(parsedArgs.debugLevel);
        } else {
            logger.setLevel(Level.OFF);
        }
        String username = parsedArgs.username;
        String password = parsedArgs.password;
        FLAPConnection fc = new FLAPConnection();
        MessageLayer ml = new MessageLayer(fc);
        TOCMessage message;
        try {
            fc.connect(username);
            ml.login(username, password);
        } catch (Exception e) {
            logger.severe("SignOn failed: " + e);
            e.printStackTrace();
            System.exit(-1);
            return;
        }
        MessageReceiver mr = new MessageReceiver(ml, System.out);
        Thread mrThread = new Thread(mr, "AIM Server Listening Thread");
        mrThread.setDaemon(true);
        mrThread.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        CommandReader cr = new CommandReader(br, System.out, ml);
        cr.run();
    }
