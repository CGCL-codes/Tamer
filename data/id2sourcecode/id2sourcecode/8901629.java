    private void readPolling() {
        String urlString = processUrl(pollReadUrl);
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException mue) {
            errorMsg("The polling url is malformed:\n" + urlString);
            isConnected = false;
            return;
        }
        pollErrorCount = 0;
        try {
            initConnection();
            while (isConnected) {
                URLConnection connection = url.openConnection();
                connection.setDoInput(true);
                readFromConnection(connection, mainBuffer);
                debug("readPolling: sleeping for: " + pollInterval);
                try {
                    Thread.currentThread().sleep(pollInterval);
                } catch (Exception exc) {
                }
            }
        } catch (Exception exc) {
            errorMsg("An error  occurred polling the server:\n" + exc);
        }
    }
