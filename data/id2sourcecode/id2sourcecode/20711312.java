    public void loadErrorCodeExtensions(String resourceName) {
        try {
            Enumeration<URL> resources = loader.getResources(resourceName);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                Properties props = new Properties();
                props.load(url.openStream());
                for (Object key : props.keySet()) {
                    int code = Integer.parseInt(key.toString());
                    String message = props.get(key).toString();
                    if (!errorCodes.containsKey(code)) {
                        errorCodes.put(code, message);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Unable to access classpath resource to load " + "core error codes, these will be ignored");
        }
    }
