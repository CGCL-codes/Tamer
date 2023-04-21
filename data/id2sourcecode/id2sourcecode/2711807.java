    private static void initProfili() {
        try {
            URL url = new URL(Conf.XML_FILE_URL);
            XStream xstream = new XStream(new DomDriver());
            xstream.alias("profili", ArrayList.class);
            xstream.alias("profilo", Profilo.class);
            xstream.alias("userType", String.class);
            xstream.alias("functions", ArrayList.class);
            xstream.alias("function", String.class);
            profili = (ArrayList) xstream.fromXML(url.openStream());
            logger.info("Loading profili from " + Conf.XML_FILE_URL);
        } catch (Exception e) {
            logger.error("Unable to load profili from " + Conf.XML_FILE_URL + ". " + e.getMessage());
        }
    }
