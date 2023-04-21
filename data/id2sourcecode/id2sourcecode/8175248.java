    protected void tryServerConnection(String service, String urlStr) {
        URL url;
        try {
            url = new URL(urlStr);
            System.err.println(" -- " + service + " : trying " + urlStr + "...");
        } catch (MalformedURLException e1) {
            String msg = e1.toString();
            System.err.println(" -- " + service + " : " + msg + " => local");
            releaseWait(service);
            return;
        }
        try {
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.err.println(" -- " + service + " : ok => remote");
                properties_.put("net.access." + service, "remote");
                releaseWait(service);
            }
        } catch (IOException e2) {
            String msg = e2.toString();
            System.err.println(" -- " + service + " : " + msg + " => local");
            releaseWait(service);
        }
    }
