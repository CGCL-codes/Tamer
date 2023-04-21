    private LogConfig() {
        Properties prop;
        try {
            prop = new Properties();
            URL url = Thread.currentThread().getContextClassLoader().getResource(LOG_CONFIG);
            if (url != null) prop.load(url.openStream());
            if (prop.get("flushInterval") != null) flushInterval = Integer.parseInt((String) prop.get("flushInterval"));
            if (prop.get("createBundleServiceThreadCount") != null) createBundleServiceThreadCount = Integer.parseInt((String) prop.get("createBundleServiceThreadCount"));
            if (prop.get("bundleMaxCount") != null) bundleMaxCount = Integer.parseInt((String) prop.get("bundleMaxCount"));
            if (prop.get("writerMaxCount") != null) writerMaxCount = Integer.parseInt((String) prop.get("writerMaxCount"));
            localAddress = InetAddress.getLocalHost().getHostAddress().toString();
            if (localAddress == null || "".equals(localAddress)) localAddress = InetAddress.getLocalHost().getHostName().toString();
        } catch (Exception ex) {
            Logger.error("Load AsynWriter error!", ex);
            throw new java.lang.RuntimeException(ex);
        }
    }
