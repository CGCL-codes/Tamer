    public static boolean isCurrentVersion() {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        HttpGet httpget = new HttpGet("http://neembuuuploader.sourceforge.net/version.xml");
        NULogger.getLogger().info("Checking for new version...");
        try {
            HttpResponse response = httpclient.execute(httpget);
            String respxml = EntityUtils.toString(response.getEntity());
            availablever = getVersionFromXML(respxml);
            NULogger.getLogger().log(Level.INFO, "Available version: {0}", availablever);
            currentver = NeembuuUploader.version;
            NULogger.getLogger().log(Level.INFO, "Current version: {0}", currentver);
            if (availablever > currentver) {
                return false;
            }
        } catch (Exception ex) {
            NULogger.getLogger().log(Level.INFO, "Exception while checking update\n{0}", ex);
        }
        return true;
    }
