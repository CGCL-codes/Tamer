    protected void initialize() throws Exception {
        final ServletContext context = getServletContext();
        String guiProp = null;
        try {
            FileInputStream propsFile = new FileInputStream(context.getRealPath(STREAMSICLE_CONFIG_FILE));
            props = new Properties();
            props.load(propsFile);
            propsFile.close();
            String configProp = props.getProperty(CONFIG_PROPERTY);
            guiProp = props.getProperty(GUI_PROPERTY);
            if ((configProp == null) || (CONFIG_FLAG.equals(configProp))) {
                if (GUI_FLAG.equals(guiProp)) {
                    ConfigurationWizard wiz = new ConfigurationWizard(context.getRealPath(STREAMSICLE_CONFIG_FILE));
                    try {
                        wiz.run();
                    } catch (Exception e) {
                        log.error("Problem with configuration: ");
                    }
                    propsFile = new FileInputStream(context.getRealPath(STREAMSICLE_CONFIG_FILE));
                    props.load(propsFile);
                    propsFile.close();
                    setConfigured(true);
                } else {
                    setConfigured(false);
                }
            } else {
                setConfigured(true);
            }
        } catch (IOException e) {
            log.error("Problem with config file: " + e);
        }
        if (isConfigured()) {
            try {
                URL url = new URL(UPGRADE_CHECK_URL);
                URLConnection connection = url.openConnection();
                Object contents = connection.getContent();
                if (contents instanceof InputStream) {
                    if (connection.getHeaderField(0).indexOf("4") == -1) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) contents));
                        String line;
                        while (null != (line = reader.readLine())) {
                            upgradeLink += line;
                        }
                    }
                    ((InputStream) contents).close();
                }
            } catch (Exception e) {
            }
            if (props.getProperty("streamsicle.requesttree.showsongtimes").toLowerCase().equals("true")) {
                getServletContext().setAttribute(Constants.SHOW_SONG_TIMES_ATTRIBUTE, "true");
            }
            int maxClients = -1;
            try {
                maxClients = Integer.parseInt(System.getProperty("streamsicle.maxclients"));
            } catch (NumberFormatException nfe) {
            }
            getServletContext().setAttribute(Constants.MAX_CLIENTS_ATTRIBUTE, new Integer(maxClients));
            setReloading(true);
            Thread initThread = new Thread(new Runnable() {

                public void run() {
                    server = new Server(context, props);
                    log.debug("Trying to initialize streaming engine.");
                    try {
                        server.initialize();
                    } catch (StreamingEngineException e) {
                        log.debug("Had an engine exception.");
                        setReloading(false);
                        setConfigured(false, e.toString());
                        return;
                    }
                    setConfigured(true, "");
                    stream = server.getStream();
                    setReloading(false);
                }
            }, "Init Thread");
            initThread.start();
            if (GUI_FLAG.equals(guiProp)) {
                log.info("Initializing GUI...");
                MainWindow gui = new MainWindow(getServletContext().getRealPath(STREAMSICLE_CONFIG_FILE), this);
                gui.start();
            }
        }
    }
