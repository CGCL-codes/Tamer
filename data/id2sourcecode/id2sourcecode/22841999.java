    protected void loadPropertyFiles(String fname) {
        try {
            URL url = ResourceLocator.getResource(this, fname);
            if (url == null) throw new IOException();
            props = new Properties();
            props.load(url.openStream());
            PropertyConfigurator.configure(props);
            props = new Properties();
            url = ResourceLocator.getResource(this, "/jbofh.properties");
            props.load(url.openStream());
        } catch (IOException e) {
            showMessage("Error reading property files", true);
            System.exit(1);
        }
    }
