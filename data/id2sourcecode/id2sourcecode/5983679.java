    public static Properties loadProperties(String name) throws JediException {
        URL url = null;
        InputStream conf = null;
        Properties prop = new Properties();
        BufferedInputStream bconf = null;
        if (name == null) {
            JediLog.log(JediLog.LOG_TECHNICAL, JediLog.ERROR, "jedi_msg_parameter_error", "", null);
            throw new JediException("URI nulle");
        }
        try {
            url = new URL(name);
            conf = url.openStream();
        } catch (MalformedURLException e) {
            try {
                conf = new FileInputStream(name);
            } catch (FileNotFoundException ex) {
                JediLog.log(JediLog.LOG_TECHNICAL, JediLog.ERROR, "jedi_msg_internal_error", "", null);
                throw new JediException("Le fichier de configuration est introuvable");
            }
        } catch (IOException ex) {
            JediLog.log(JediLog.LOG_TECHNICAL, JediLog.ERROR, "jedi_msg_internal_error", "", null);
            throw new JediException("Le fichier de configuration est inaccessible");
        }
        if (conf == null) {
            return null;
        }
        try {
            bconf = new BufferedInputStream(conf);
            prop.load(bconf);
            conf.close();
        } catch (IOException ex) {
            JediLog.log(JediLog.LOG_TECHNICAL, JediLog.ERROR, "jedi_msg_internal_error", "", null);
            throw new JediException("Erreur en cours de lecture du fichier de configuration");
        }
        return prop;
    }
