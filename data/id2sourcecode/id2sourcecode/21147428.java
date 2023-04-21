    private GkcConfig() {
        if (logger.isInfoEnabled()) logger.info("GkcConfig loading gkc-config.properties...");
        try {
            Properties prop;
            prop = new Properties();
            URL url = Thread.currentThread().getContextClassLoader().getResource("gkc-config.properties");
            if (url != null) prop.load(url.openStream());
            if (prop.get("keyBufferConsumerThreadCount") != null) keyBufferConsumerThreadCount = Integer.parseInt((String) prop.get("keyBufferConsumerThreadCount"));
            if (prop.get("keyMinlength") != null) keyMinlength = Integer.parseInt((String) prop.get("keyMinlength"));
            if (prop.get("keyMaxlength") != null) keyMaxlength = Integer.parseInt((String) prop.get("keyMaxlength"));
            if (prop.get("groupkeyConsumerThreadCount") != null) groupkeyConsumerThreadCount = Integer.parseInt((String) prop.get("groupkeyConsumerThreadCount"));
            if (prop.get("groupkeySortInterval") != null) groupkeySortInterval = Integer.parseInt((String) prop.get("groupkeySortInterval"));
            if (prop.get("groupkeySize") != null) groupkeySize = Integer.parseInt((String) prop.get("groupkeySize"));
            if (prop.get("groupkeySamplingMatchfactor") != null) groupkeySamplingMatchfactor = Integer.parseInt((String) prop.get("groupkeySamplingMatchfactor"));
            if (prop.get("isFlush") != null) {
                if (((String) prop.get("isFlush")).equalsIgnoreCase("true")) isFlush = true;
            }
            if (prop.get("isStore") != null) {
                if (((String) prop.get("isStore")).equalsIgnoreCase("false")) isStore = false;
            }
            consumerThreadCount = groupkeyConsumerThreadCount + keyBufferConsumerThreadCount;
            dataDir = new File(new File(url.toURI()).getParentFile(), "gkcfiles" + File.separatorChar + "persistent");
            if (prop.get("isMonitor") != null) {
                if (((String) prop.get("isMonitor")).equalsIgnoreCase("true")) {
                    isMonitor = true;
                    consumerThreadCount = consumerThreadCount + 1;
                }
            }
            if (logger.isInfoEnabled()) logger.info("GkcConfig init complete." + this);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("GkcConfig init error!", e);
        }
    }
