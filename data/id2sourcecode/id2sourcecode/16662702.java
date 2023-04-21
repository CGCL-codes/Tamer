    private boolean checkOmicBrowse(String server) {
        URL url = null;
        HttpURLConnection connection;
        if (server.indexOf("/gps") == -1) server = server + "/gps";
        try {
            url = new URL(server);
        } catch (MalformedURLException e) {
            try {
                url = new URL("http://" + server);
            } catch (MalformedURLException me) {
                DialogPane.showMessageDialog(RegisterOtherServersFrame.this, "Invalid Server Name", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        try {
            if (url != null) {
                connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    DialogPane.showMessageDialog(RegisterOtherServersFrame.this, "The server has been found successfully.", "Register a New Server", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                }
            }
        } catch (IOException e) {
            logger.debug("The server was not found");
        }
        return DialogPane.showConfirmDialog(RegisterOtherServersFrame.this, "<html>The server was not found.<br>Do you really want to register this server?</html>", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }
