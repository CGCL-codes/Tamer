    private ErrorItemFactory(ClassLoader loader) {
        this.loader = loader;
        errorCodes = new HashMap<Integer, String>();
        try {
            Enumeration<URL> resources = loader.getResources("META-INF/magetab/errorcodes-core.properties");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                log.info("Loading core MAGE-TAB error codes from " + url.toString());
                Properties props = new Properties();
                props.load(url.openStream());
                for (Object key : props.keySet()) {
                    int code = Integer.parseInt(key.toString());
                    String message = props.get(key).toString();
                    if (!errorCodes.containsKey(code)) {
                        log.debug("Adding error code " + code + " to known list");
                        errorCodes.put(code, message);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Unable to access classpath resource to load " + "core error codes, these will be ignored");
        }
        try {
            Enumeration<URL> resources = loader.getResources("META-INF/magetab/errorcodes.properties");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                log.info("Loading extension MAGE-TAB error codes from " + url.toString());
                Properties props = new Properties();
                props.load(url.openStream());
                for (Object key : props.keySet()) {
                    int code = Integer.parseInt(key.toString());
                    String message = props.get(key).toString();
                    if (!errorCodes.containsKey(code)) {
                        log.debug("Adding custom error code " + code + " to known list");
                        errorCodes.put(code, message);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Unable to access classpath resource to load " + "extension error codes, these will be ignored");
        }
    }
