    private boolean init() throws IOException {
        urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setRequestMethod("HEAD");
        urlCon.setRequestProperty("User-Agent", useragent);
        if (urlCon.getResponseCode() != HttpURLConnection.HTTP_OK) return false;
        if ((savedLength = urlCon.getHeaderFieldInt("Content-Length", -1)) < 0) {
            return false;
        }
        String s;
        if ((s = urlCon.getHeaderField("Accept-Ranges")) == null || !s.equals("bytes")) {
            System.err.println("Server does not accept HTTP range requests." + " Aborting!");
            return false;
        }
        return true;
    }
