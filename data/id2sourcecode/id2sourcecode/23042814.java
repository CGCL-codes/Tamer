    protected void setServerAndPort(String aServerName, InterfaceType aInterfaceType) throws IOException {
        URL url = Connector.class.getResource(SERVERS_PROPERTIES);
        if (url == null) {
            throw new NullPointerException("Problem reading " + SERVERS_PROPERTIES + " file.");
        }
        InputStream inStream = url.openStream();
        if (inStream == null) {
            throw new NullPointerException("Problem reading " + SERVERS_PROPERTIES + " file.");
        }
        Properties properties = new Properties();
        properties.load(inStream);
        String addressStr = properties.getProperty(aServerName + ".host");
        String portStr = properties.getProperty(aServerName + "." + aInterfaceType.name().toLowerCase() + ".port");
        if (addressStr == null) {
            throw new IllegalArgumentException("No host defined for " + aServerName);
        }
        if (portStr == null) {
            throw new IllegalArgumentException("No port defined for " + aServerName);
        }
        if (portStr != null) {
            portStr = portStr.trim();
            if (!portStr.matches("\\d+")) {
                throw new IllegalArgumentException("Port (" + portStr + ") is not a number for " + aServerName);
            }
        }
        address = addressStr;
        port = Integer.parseInt(portStr);
    }
