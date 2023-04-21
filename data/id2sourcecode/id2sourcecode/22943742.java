    public ConfigReader(String confNode) {
        try {
            URL url = ConfigReader.class.getResource("/config.xml");
            StringBuffer buildpage = new StringBuffer();
            int c;
            try {
                BufferedInputStream page = new BufferedInputStream(url.openStream());
                while ((c = page.read()) != -1) {
                    buildpage.append((char) c);
                }
            } catch (Exception exe) {
                log.error("Initialization error", exe);
            }
            String configfile = buildpage.toString();
            this.setConfdoc(XercesHelper.string2Dom(configfile));
            this.setRelativeNodePath(confNode);
        } catch (Exception exe) {
            log.error("Initialization error", exe);
        }
    }
