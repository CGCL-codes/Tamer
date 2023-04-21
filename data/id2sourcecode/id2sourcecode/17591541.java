    private void runURLRequest(String cache, String ID) {
        String thisURL = null;
        for (int i = 0; i < cacheURLs.length; i++) {
            try {
                thisURL = "http://" + cacheURLs[i] + "/cache/Service?Cache=" + cache + "&ID=" + ID;
                URL url = new URL(thisURL);
                Proxy thisProxy = Proxy.NO_PROXY;
                URLConnection urlConn = url.openConnection(thisProxy);
                urlConn.setUseCaches(false);
                urlConn.connect();
                Reader stream = new java.io.InputStreamReader(urlConn.getInputStream());
                StringBuffer srvOutput = new StringBuffer();
                try {
                    int c;
                    while ((c = stream.read()) != -1) srvOutput.append((char) c);
                } catch (Exception E2) {
                    E2.printStackTrace();
                }
            } catch (IOException E) {
                if (log != null) log.warning("Can't clean cache at:" + thisURL + " be carefull, your deployment server may use invalid or old cache data!");
            }
        }
    }
