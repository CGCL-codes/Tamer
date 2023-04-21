    public void init() {
        log.debug("bootstraping jython...");
        exec("Bootstrapping Jython", getClass().getResourceAsStream("bootstrap.py"));
        File bootstrapFile = new File(System.getProperty("user.home") + SystemProperties.FILE_SEPARATOR + ".hermes" + SystemProperties.FILE_SEPARATOR + "hermesrc.py");
        if (bootstrapFile.exists()) {
            try {
                log.debug("reading " + bootstrapFile.getName());
                exec("Reading hermesrc.py", new FileInputStream(bootstrapFile));
                Hermes.ui.getDefaultMessageSink().add("Loaded hermesrc.py");
            } catch (FileNotFoundException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            log.debug("Unable to locate a hermesrc.py in " + System.getProperty("user.home"));
        }
        try {
            if (System.getProperty("hermes.python.url") != null) {
                String url = System.getProperty("hermes.python.url");
                log.debug("reading " + url);
                exec("Reading " + url, new URL(url).openStream());
            } else {
                log.debug("no hermes.python.url set");
            }
        } catch (Exception ex) {
            HermesBrowser.getBrowser().showErrorDialog(ex);
        }
    }
