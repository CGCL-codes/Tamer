    public void makeRequest() {
        activeThreads++;
        int count = 0;
        HttpURLConnection con = null;
        try {
            logger.debug("Making a connection to " + url);
            con = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                count++;
            }
            logger.debug("Got " + count + " lines back from " + url.toString() + " Response code " + con.getResponseCode());
            if (con != null && con.getResponseCode() == 200 && count > 1) {
                completedThreads++;
            }
        } catch (IOException ex) {
            logger.debug(ex, ex);
        } finally {
            try {
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception ex) {
                logger.warn(ex, ex);
            }
            activeThreads--;
        }
    }
