    public void init(String redirdHost, String realIrcServerHostName, int redirdPort, String nickName, String userName, String loginPassword, String realName, IRCListener irclistener, MN2Factory MN2Factory) throws IOException {
        Lang.ASSERT_NOT_NULL(redirdHost, "redirdHost");
        Lang.ASSERT_NOT_NULL_NOR_TRIMMED_EMPTY(loginPassword, "loginPassword");
        System.err.println("ircclient.init: redirdHost=" + redirdHost);
        ircServerAddress = redirdHost;
        irclistener.connecting();
        try {
            socket = new Socket(redirdHost, redirdPort);
        } catch (Throwable throwable) {
            System.err.println("connect ex");
            if (throwable.getClass().getName().indexOf("Security") != -1) {
                throw new RuntimeException(throwable);
            } else {
                if (throwable instanceof IOException) throw (IOException) throwable; else {
                    Logger.printException(throwable);
                    throw new RuntimeException("" + throwable);
                }
            }
        }
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()), 4096);
        PrintWriter printwriter = new PrintWriter(new BufferedOutputStream(socket.getOutputStream(), 1024), true);
        init(bufferedreader, printwriter, irclistener);
        irclistener.registering();
        register(loginPassword, nickName, userName, realIrcServerHostName, realIrcServerHostName, realName);
        irclistener.connected();
        queryClient = new IRCController(nickName, realName, userName, loginPassword, MN2Factory);
    }
