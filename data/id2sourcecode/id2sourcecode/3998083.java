    public Jinx(String[] args) {
        String separator = System.getProperty("file.separator");
        String home = System.getProperty("user.home");
        String landf = UIManager.getSystemLookAndFeelClassName();
        Runtime.getRuntime().addShutdownHook(new ShutdownHandler(this));
        Timer timer = new Timer("Save Timer");
        timer.scheduleAtFixedRate(new JxAutosaveHandler(), 30000, 30000);
        this._log_prefix = AsynchronousEventManager.createLogPrefix("Jinx");
        AsynchronousEventManager.setDebugVerbose(true);
        AsynchronousEventManager.setDebugDepth(5);
        String prefs_log_dir = null;
        try {
            this._jinx_properties_file = new File(home + separator + ".jinx.properties");
            Document document = new SAXBuilder().build(this._jinx_properties_file);
            this._jinx_properties = document.getRootElement();
            prefs_log_dir = this.getLogDirectory();
            Element autosave = this._jinx_properties.getChild("autosave");
            JxAutosaveUpdateEvent aue = new JxAutosaveUpdateEvent();
            aue.setAutosaveOn(autosave.getAttribute("on").getBooleanValue());
            aue.setAutosaveFrequency(autosave.getAttribute("frequency").getIntValue());
            aue.setAutosaveDirectory(autosave.getAttributeValue("path"));
            aue.send();
        } catch (Exception e) {
            this._jinx_properties = new Element("Properties");
            Element autosave = new Element("autosave");
            this._jinx_properties.addContent(autosave);
            this.autosaveUpdateXML(true, 300, System.getProperty("user.dir"));
        }
        String[] directories = { prefs_log_dir, System.getProperty("jinx.logdir"), home, System.getProperty("user.dir") };
        String log_dir = null;
        for (int i = 0; i < directories.length; i++) if (directories[i] != null) try {
            log_dir = directories[i];
            String path = log_dir + separator + this._log_prefix + ".event";
            AsynchronousEventManager.setDebugFile(path);
            File[] list = new File(log_dir).listFiles(new ListFilter());
            if (list.length > 5) {
                Arrays.sort(list);
                ArrayList<File> temp = new ArrayList<File>();
                for (int l = 0; l < list.length - 1; l++) temp.add(list[l]);
                list = temp.toArray(new File[temp.size()]);
                new JxEventFileCleanupDialog(list);
            }
            break;
        } catch (FileNotFoundException fnfe) {
        }
        if (log_dir == null) {
            System.err.println("Unable to open log file.  Quitting.");
            System.exit(1);
        } else this.editLogDirectory(log_dir);
        AsynchronousEventManager.setDebug(true);
        new JxLogEvent().send("Jinx Version " + Jinx.getVersion() + " Deployed: " + JxDeployed.deployed());
        String hostname = "Unknown.";
        try {
            hostname = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException ex) {
            new JxLogEvent().send(ex);
        }
        new JxLogEvent().send("Host Name: " + hostname + " OS Name: " + System.getProperty("os.name") + " Version: " + System.getProperty("os.version") + " System Architecture: " + System.getProperty("os.arch"));
        new JxLogEvent().send("Look and Feel: " + landf);
        Runtime runtime = Runtime.getRuntime();
        long maxmem = runtime.maxMemory();
        long totmem = runtime.totalMemory();
        new JxLogEvent().send("Maximum Memory: " + maxmem + " " + (float) (maxmem / (1024.0 * 1024.0)) + "MB" + ", Total Memory:  " + totmem + " " + (float) (totmem / (1024.0 * 1024.0)) + "MB");
        new JxLogEvent().send("Processors: " + runtime.availableProcessors());
        Map<String, String> env = System.getenv();
        Set<String> keys = env.keySet();
        String environment = "";
        for (String key : keys) environment += "\n\t" + key + "\t'" + env.get(key) + "'";
        new JxLogEvent().send("environment:" + environment);
        Properties properties = System.getProperties();
        String props = "";
        for (Enumeration pn = properties.propertyNames(); pn.hasMoreElements(); ) {
            String name = pn.nextElement().toString();
            props += "\n\t" + name + "\t'" + properties.getProperty(name) + "'";
        }
        new JxLogEvent().send("properties:" + props);
        try {
            UIManager.setLookAndFeel(landf);
            ClassLoader cl = this.getClass().getClassLoader();
            String resources = "";
            for (Enumeration e = cl.getResources("META-INF/"); e.hasMoreElements(); ) {
                URL url = (URL) e.nextElement();
                JarURLConnection jc = (JarURLConnection) url.openConnection();
                JarFile jf = jc.getJarFile();
                for (Enumeration<JarEntry> ej = jf.entries(); ej.hasMoreElements(); ) {
                    JarEntry je = ej.nextElement();
                    if (je.toString().equals("META-INF/MANIFEST.MF")) {
                        String[] path = url.getFile().split("/");
                        String[] file = path[path.length - 2].split("!");
                        String date = new Date(je.getTime()).toString();
                        resources += "\n\t" + date + "\t\t" + file[0];
                        break;
                    }
                }
            }
            new JxLogEvent().send("resources:" + resources);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.launchListeners();
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        JxMainFrame main_window = new JxMainFrame();
        String host = "Unknown???";
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
            new JxErrorEvent().send(ex);
        }
        new JxLogEvent().send("Startup on host " + host);
        String filename = home + separator + ".jinx";
        new JxFileChooserEventListener(main_window, filename + ".chooser").enable();
        if (System.getProperty("jinx.demo") != null) new LaunchDemo(); else {
            String session = System.getProperty("jinx.session");
            if (session != null) try {
                URI uri = new URI(session);
                GeneralFile file = GeneralFileFactory.createFile(uri);
                if ((file != null) && (file.exists())) {
                    JxLoaderEvent le;
                    if (session.endsWith(".jnx")) le = new JxSessionLoaderEvent(); else le = new JxVolumeLoaderEvent();
                    le.parseLocation(System.getProperty("jinx.location"));
                    le.parseSize(System.getProperty("jinx.size"));
                    le.parseObjectName(System.getProperty("jinx.object"));
                    le.parseQuaternion(System.getProperty("jinx.quaternion"));
                    le.parseView(System.getProperty("jinx.view"));
                    le.send(file);
                } else if (file == null) new JxErrorEvent().send("No file: bad uri? " + uri); else new JxErrorEvent().send("File " + uri + " doesn't exist.");
            } catch (Exception ex) {
                new JxErrorEvent().send(ex);
            }
        }
    }
