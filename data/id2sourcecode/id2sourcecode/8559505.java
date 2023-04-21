    public synchronized String submitCommand(String command, String params) {
        if (!isLogged()) {
            return null;
        }
        URLConnection connection = null;
        try {
            connection = urlGenerator(command, params).openConnection();
        } catch (IOException ex) {
            Logger.getLogger(DreamHostConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (null == connection) {
            return null;
        }
        return getResponse(connection);
    }
