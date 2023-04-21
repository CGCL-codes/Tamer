    private void startPdsImageRetrieval(double lat, double lon) {
        imageDone = false;
        goodConnection = false;
        URL url = null;
        url = getPDSURL(lat, lon);
        HttpURLConnection urlCon;
        try {
            urlCon = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        new PDSConnectionManager(urlCon, this);
    }
