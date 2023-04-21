    public void execute() {
        Date startedRequestAt = new Date();
        ProbeResult result;
        InetAddress ia = null;
        HttpURLConnection connection = null;
        try {
            ia = InetAddress.getByName(url.getHost());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            result = new ProbeResult(url, connection.getResponseCode(), -1, null);
            resultTable.put(ia, result);
        } catch (IOException ioe) {
            Date requestEndedAt = new Date();
            result = new ProbeResult(url, -1, requestEndedAt.getTime() - startedRequestAt.getTime(), ioe);
            resultTable.put(ia, result);
        }
        if (connection != null) connection.disconnect();
    }
