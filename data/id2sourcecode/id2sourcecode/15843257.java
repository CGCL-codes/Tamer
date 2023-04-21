    public CartServerImpl() throws RemoteException {
        super();
        boolean gotSocket = false;
        for (int i = 0; i < 10; i++) {
            try {
                socket = new Socket(host, port);
                socket.setSoTimeout(500);
                gotSocket = true;
                break;
            } catch (IOException ioe) {
                System.err.println(prg + ": Error opening socket: " + ioe);
                System.exit(-1);
            }
        }
        if (!gotSocket) {
            System.err.println(prg + ": Error opening socket");
            System.exit(-1);
        }
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException ioe) {
            System.err.println(prg + ": Error creating reader/writer: " + ioe);
            System.exit(-1);
        }
        if (useSick) {
            if (verbose) System.out.println("Attempting to get Sick LRF client...");
            lrf = getClient("SickLRFServerImpl");
        }
        if (useUrg) {
            if (verbose) System.out.println("Attempting to get Urg LRF client...");
            lrf = getClient("UrgLRFServerImpl");
        }
        u = new Updater();
        u.start();
        if (runTest) test();
    }
