    public void init() throws ServletException {
        String uri = Settings.get("locations.modelingQueue");
        String agent = "Mozilla/4.0";
        String type = "text/plain; charset=UTF-8";
        try {
            URL url = new URL(uri);
            for (int i = 0; i < _initQueues.length; i++) {
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoOutput(true);
                urlConn.setDoInput(true);
                urlConn.setUseCaches(false);
                urlConn.setAllowUserInteraction(false);
                urlConn.setRequestMethod("POST");
                urlConn.setRequestProperty("User-Agent", agent);
                urlConn.setRequestProperty("Content-Type", type);
                OutputStreamWriter urlOut = new OutputStreamWriter(urlConn.getOutputStream(), "UTF-8");
                urlOut.write(_initQueues[i]);
                urlOut.flush();
                urlOut.close();
            }
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }
