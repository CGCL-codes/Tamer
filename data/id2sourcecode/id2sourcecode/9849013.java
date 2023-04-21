    public static Properties readProperties(String resourceName) throws Exception {
        Properties defaultProps = new Properties();
        try {
            Utils utils = new Utils();
            Enumeration<URL> urls = utils.getClass().getClassLoader().getResources(resourceName);
            boolean first = true;
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (first) {
                    defaultProps.load(url.openStream());
                    first = false;
                } else {
                    Properties props = new Properties(defaultProps);
                    props.load(url.openStream());
                    defaultProps = props;
                }
            }
        } catch (Exception ex) {
            System.err.println("Warning, unable to load properties file(s) from " + "system resource (Utils.java): " + resourceName);
        }
        int slInd = resourceName.lastIndexOf('/');
        if (slInd != -1) {
            resourceName = resourceName.substring(slInd + 1);
        }
        Properties userProps = new Properties(defaultProps);
        if (!WekaPackageManager.PROPERTIES_DIR.exists()) {
            WekaPackageManager.PROPERTIES_DIR.mkdir();
        }
        File propFile = new File(WekaPackageManager.PROPERTIES_DIR.toString() + File.separator + resourceName);
        if (propFile.exists()) {
            try {
                userProps.load(new FileInputStream(propFile));
            } catch (Exception ex) {
                throw new Exception("Problem reading user properties: " + propFile);
            }
        }
        Properties localProps = new Properties(userProps);
        propFile = new File(resourceName);
        if (propFile.exists()) {
            try {
                localProps.load(new FileInputStream(propFile));
            } catch (Exception ex) {
                throw new Exception("Problem reading local properties: " + propFile);
            }
        }
        return localProps;
    }
