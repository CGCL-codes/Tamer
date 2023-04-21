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
        socketOut.println("JOIN " + getChannel());
        Logger.info(this, "Logged in an joined channel " + getChannel() + ".");
        Logger.info(this, "Requesting user list");
        socketOut.println("NAMES " + getChannel());
        String line;
        while ((line = socketIn.readLine()) != null) {
            Logger.debug(this, "Received from server: " + line);
            String nameLine = ".*? 353 .*? = " + getChannel() + " :(.*)";
            String endLine = ".*? 366 .*? " + getChannel() + " :End of /NAMES list.";
            if (line.matches(nameLine)) {
                for (String name : line.replaceAll(nameLine, "$1").split(" ")) {
                    users.add(name);
                }
            } else if (line.matches(endLine)) {
                Logger.info(this, "User list received: " + users.toString());
                break;
            }
        }
    }
