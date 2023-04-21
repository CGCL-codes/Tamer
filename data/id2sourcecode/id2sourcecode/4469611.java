    public static void main(String[] args) {
        Map<String, String> versions = new HashMap<String, String>();
        String versionsString = "";
        versionsString = HexTD.getVersions(versions);
        Properties defaults = new Properties();
        defaults.setProperty("port", "4567");
        Properties config = new Properties(defaults);
        try {
            config.load(new FileInputStream("HexTD.conf"));
        } catch (FileNotFoundException ex) {
            try {
                System.out.println("Creating config file, " + defaults.size() + " keys");
                defaults.store(new FileWriter("HexTD.conf"), "Default settings.");
            } catch (IOException ex1) {
                LOGGER.warn("Error!", ex1);
            }
        } catch (IOException ex) {
            LOGGER.warn("Error!", ex);
        }
        Console c = System.console();
        if (c == null) {
            try {
                System.err.println("No console.");
                Server server = new Server(config);
                server.startServer();
            } catch (IOException ex) {
                LOGGER.warn("Error!", ex);
            }
        } else {
            try {
                Server server = new Server(config);
                server.startServer();
                boolean quit = false;
                do {
                    String line = c.readLine(">> ");
                    if (line.matches("^i$")) {
                        for (Entry<String, String> es : versions.entrySet()) {
                            System.out.println(es.getKey() + " " + es.getValue());
                        }
                        System.out.println("Players Auth: " + server.getAuthedPlayerCount() + " Players notAuth: " + server.getUnauthedPlayerCount() + " Channels: " + server.getChannelCount());
                        System.out.println("Unchecked logs: " + server.logsToCheck.size());
                    }
                    if (line.matches("^m$")) {
                        server.checkMaps();
                    }
                    if (line.matches("^p$")) {
                        System.out.println("Authenticated players:");
                        for (ServerUser p : server.getPlayers()) {
                            System.out.println(" * " + p.getUserInfo().getName() + " " + p.getConnection().getRemoteAddress() + " " + p.getChannel());
                        }
                        System.out.println("Unauthenticated players:");
                        for (ServerUser p : server.getPlayersNew()) {
                            System.out.println(" * " + p.getConnection().getRemoteAddress());
                        }
                    }
                    quit = line.matches("^q(uit)?$");
                } while (!quit);
                server.stop();
                System.exit(0);
            } catch (IOException ex) {
                LOGGER.warn("Error!", ex);
            }
        }
    }
