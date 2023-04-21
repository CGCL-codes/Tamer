    public static void main(String[] args) {
        boolean gotValidPort = false;
        String portName = "";
        evm = EventManager.getInstance();
        TwoWaySerialComm port = new TwoWaySerialComm();
        String configPath = "config.xml";
        if (args.length > 0) {
            configPath = args[0];
        }
        File configFile = new File(configPath);
        if (!configFile.canRead()) {
            System.err.println("Cannot read config file!");
            System.err.println("Tried to used config @ " + configFile.getAbsolutePath());
            System.exit(1);
        }
        config = new ConfigParse(configFile);
        System.out.println("Using port " + config.getPort());
        System.out.println("Using music library " + config.getMusicLibLocation());
        System.out.println("Using prebuilt library XML " + config.getXMLMusicLib() + "(parsed value)");
        File musicLibFolder = new File(config.getMusicLibLocation());
        if (!musicLibFolder.exists() || !musicLibFolder.canRead()) {
            System.err.println("Cannot read Music library location!");
            System.err.println("Tried to use folder @ " + musicLibFolder.getAbsolutePath());
            System.exit(3);
        }
        File musicFile = new File(config.getXMLMusicLib());
        if (!musicFile.canRead()) {
            if (!musicFile.canWrite()) {
                System.err.println("Cannot read or write XML music library!");
                System.err.println("Tried to use XML file @ " + musicFile.getAbsolutePath());
                System.exit(2);
            } else {
                Indexer tmp = new Indexer(config.getMusicLibLocation());
                new Thread(tmp).start();
            }
        }
        allMusic = MusicParse.parse(musicFile);
        plists = PlistParse.parse(musicFile);
        CommPortIdentifier portIdentifier = null;
        boolean portExists = true;
        try {
            portIdentifier = CommPortIdentifier.getPortIdentifier(config.getPort());
        } catch (NoSuchPortException ex) {
            System.err.println("INVALID PORT IN CONFIG");
            gotValidPort = false;
            portExists = false;
        }
        if (portExists) {
            if (!portIdentifier.isCurrentlyOwned()) {
                gotValidPort = true;
                portName = config.getPort();
            }
        }
        if (!gotValidPort) {
            System.out.println("Getting and testing ports... (COULD TAKE A MOMENT....)");
            ArrayList<CommPortIdentifier> avl = getAvailableSerialPorts();
            System.out.println("Choose a port : ");
            for (int i = 0; i < avl.size(); i++) {
                System.out.println(i + " -> " + avl.get(i).getName());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int ptu = -1;
            System.out.println();
            System.out.println("Enter port number :");
            try {
                ptu = Integer.parseInt(br.readLine());
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Trying to open" + avl.get(ptu).getName());
            portName = avl.get(ptu).getName();
        }
        try {
            port.connect(portName);
            CDEmulator cde = new CDEmulator();
            mid = new MID();
            pc = new PlayerController();
            ChangeToCDInput ctci = new ChangeToCDInput();
            evm.addListener("FM", ctci);
            evm.addListener("FM", cde);
            evm.addListener("FM", mid);
            evm.addListener("FM", pc);
            new Thread(cde).run();
            new Thread(mid).run();
            System.out.println("Serial port opened. Setting up menu and waiting.");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(3);
        }
    }
