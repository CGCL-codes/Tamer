    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        stillNeedsRun = false;
        if (this.socket == null) {
            LOGGER.info(threadName + ": socket null, cleanup+return");
            doCleanup();
            return;
        }
        if (this.failedException != null) {
            LOGGER.info(threadName + ": failedException set, cleanup+return");
            doCleanup();
            return;
        }
        LOGGER.info(threadName + ": everything normal, going to run loop!");
        String fromServer = null;
        boolean done = false;
        boolean forcedLogout = false;
        try {
            while (!done && (fromServer = getOneLine()) != null) {
                String[] tokens = fromServer.split(sep, -1);
                String command = tokens[0];
                if (fromServer.startsWith("ACK: ")) {
                    command = tokens[0].substring(5);
                    handleAckNack(command, tokens);
                } else if (fromServer.startsWith("NACK: ")) {
                    command = tokens[0].substring(6);
                    handleAckNack(command, tokens);
                } else if (fromServer.equals(IWebClient.connectionClosed)) {
                    done = true;
                } else if (fromServer.equals(IWebClient.forcedLogout)) {
                    forcedLogout = true;
                    done = true;
                } else if (command.equals(IWebClient.gameInfo)) {
                    GameInfo gi = restoreGameInfo(tokens);
                    webClient.gameInfo(gi);
                } else if (command.equals(IWebClient.userInfo)) {
                    int loggedin = Integer.parseInt(tokens[1]);
                    int enrolled = Integer.parseInt(tokens[2]);
                    int playing = Integer.parseInt(tokens[3]);
                    int dead = Integer.parseInt(tokens[4]);
                    long ago = Long.parseLong(tokens[5]);
                    String text = tokens[6];
                    webClient.userInfo(loggedin, enrolled, playing, dead, ago, text);
                } else if (command.equals(IWebClient.didEnroll)) {
                    String gameId = tokens[1];
                    String user = tokens[2];
                    webClient.didEnroll(gameId, user);
                } else if (command.equals(IWebClient.didUnenroll)) {
                    String gameId = tokens[1];
                    String user = tokens[2];
                    webClient.didUnenroll(gameId, user);
                } else if (command.equals(IWebClient.gameCancelled)) {
                    String gameId = tokens[1];
                    String byUser = tokens[2];
                    webClient.gameCancelled(gameId, byUser);
                } else if (command.equals(IWebClient.gameStartsSoon)) {
                    String gameId = tokens[1];
                    String startUser = tokens[2];
                    confirmCommand(command, gameId, startUser, "nothing");
                    webClient.gameStartsSoon(gameId, startUser);
                } else if (command.equals(IWebClient.gameStartsNow)) {
                    String gameId = tokens[1];
                    int port = Integer.parseInt(tokens[2]);
                    String host = tokens[3];
                    confirmCommand(command, gameId, port + "", host);
                    webClient.gameStartsNow(gameId, port, host);
                } else if (command.equals(IWebClient.chatDeliver)) {
                    String chatId = tokens[1];
                    long when = Long.parseLong(tokens[2]);
                    String sender = tokens[3];
                    String message = tokens[4];
                    boolean resent = Boolean.valueOf(tokens[5]).booleanValue();
                    webClient.chatDeliver(chatId, when, sender, message, resent);
                } else if (command.equals(IWebClient.pingRequest)) {
                    String arg1 = tokens[1];
                    String arg2 = tokens[2];
                    String arg3 = tokens[3];
                    pingResponse(arg1, arg2, arg3);
                } else if (command.equals(IWebClient.generalMessage)) {
                    long when = Long.parseLong(tokens[1]);
                    boolean error = Boolean.valueOf(tokens[2]).booleanValue();
                    String title = tokens[3];
                    String message = tokens[4];
                    webClient.deliverGeneralMessage(when, error, title, message);
                } else if (command.equals(IWebClient.requestAttention)) {
                    long when = Long.parseLong(tokens[1]);
                    String byUser = tokens[2];
                    boolean byAdmin = Boolean.valueOf(tokens[3]).booleanValue();
                    String message = tokens[4];
                    int beepCount = Integer.parseInt(tokens[5]);
                    long beepInterval = Long.parseLong(tokens[6]);
                    boolean windows = Boolean.valueOf(tokens[7]).booleanValue();
                    webClient.requestAttention(when, byUser, byAdmin, message, beepCount, beepInterval, windows);
                } else if (command.equals(IWebClient.grantAdmin)) {
                    webClient.grantAdminStatus();
                } else {
                    if (webClient != null) {
                        if (webClient instanceof WebClient) {
                            ((WebClient) webClient).showAnswer(fromServer);
                        }
                    }
                }
            }
            writeLog("End of SocketClientThread while loop, done = " + done + " readline " + (fromServer == null ? " null " : "'" + fromServer + "'"));
            if (loggedIn) {
                webClient.connectionReset(forcedLogout);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "WebClientSocketThread IOException!");
            webClient.connectionReset(false);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "WebClientSocketThread whatever Exception!", e);
            Thread.dumpStack();
            webClient.connectionReset(false);
        }
        doCleanup();
    }
