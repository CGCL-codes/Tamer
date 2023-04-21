    private static Configuration readDefaultConfiguration() {
        if (log.isEnabled(LogLevel.DEBUG)) {
            log.debug("readDefaultConfiguration() - start");
        }
        java.net.URL url = Main.class.getResource("DefaultConfiguration.xml");
        Configuration configuration = null;
        try {
            log.info("Unmarshal default configuration to Java structure");
            Unmarshaller um = jc.createUnmarshaller();
            configuration = (Configuration) um.unmarshal(url.openStream());
        } catch (IOException x) {
            log.caught(LogLevel.ERROR, "readDefaultConfiguration()", x);
        } catch (JAXBException x) {
            log.caught(LogLevel.ERROR, "unable to unmarshal configuration", x);
        }
        if (log.isEnabled(LogLevel.DEBUG)) {
            log.debug("readDefaultConfiguration() - end");
        }
        return configuration;
    }
