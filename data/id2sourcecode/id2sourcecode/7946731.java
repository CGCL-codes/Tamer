    @Override
    public void connect() throws IOException {
        super.connect();
        users = new LinkedList<String>();
        loadPass();
        Logger.info(this, "Sending login information.");
        Logger.debug(this, "Sending »PASS " + pass + "«");
        socketOut.println("PASS " + pass);
        Logger.debug(this, "Sending »USER " + user + "«");
        socketOut.println("USER " + user);
        Logger.debug(this, "Sending »NICK " + getName() + "«");
        socketOut.println("NICK " + getName());
        String fromServer;
        int noticecounter = 0, authcounter = 0;
        while ((fromServer = socketIn.readLine()) != null) {
            Logger.debug(this, "Received from server: " + fromServer);
            if (fromServer.startsWith("PING :")) {
                socketOut.println("PONG :" + fromServer.substring(6));
            }
            if (fromServer.startsWith("NOTICE AUTH :***")) {
                authcounter++;
            }
            if (fromServer.startsWith(":Notice!")) {
                noticecounter++;
            }
            if (authcounter >= 4 || noticecounter >= 3) {
                break;
            }
        }
        for (String channel : channels) {
            socketOut.println("JOIN " + channel);
        }
        Logger.info(this, "Logged in an joined channels " + Arrays.toString(channels) + ".");
        Logger.info(this, "Requesting user list");
        for (String channel : channels) {
            socketOut.println("NAMES " + channel);
        }
        String line;
        int counter = 0;
        while ((line = socketIn.readLine()) != null) {
            Logger.debug(this, "Received from server: " + line);
            String nameLine = ".*? 353 .*? = " + getChannels() + " :(.*)";
            String endLine = ".*? 366 .*? " + getChannels() + " :End of /NAMES list.";
            if (line.matches(nameLine)) {
                for (String name : line.replaceAll(nameLine, "$2").split(" ")) {
                    users.add(name);
                }
            } else if (line.matches(endLine)) {
                counter++;
                if (counter == channels.length) {
                    Logger.info(this, "User list received: " + users.toString());
                    break;
                }
            }
        }
    }
