    public void crawl(String site, String[] parsers) throws MalformedURLException, IOException {
        if (proxy) {
            System.setProperty("http.proxySet", "true");
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", proxyPort);
            System.setProperty("http.proxyType", proxyType);
            Authenticator def = new Authenticator();
            java.net.Authenticator.setDefault(def);
        }
        java.net.URL connection = new java.net.URL(site);
        java.net.URLConnection connected = connection.openConnection();
        java.io.InputStream raw_data = connected.getInputStream();
        byte[] buffer = new byte[10240];
        java.io.ByteArrayOutputStream contents = new java.io.ByteArrayOutputStream();
        int num_read = 0;
        while ((num_read = raw_data.read(buffer)) > 0) {
            contents.write(buffer, 0, num_read);
        }
        raw_data.close();
        contents.close();
        buffer = contents.toByteArray();
        try {
            doParse(buffer, parsers, site);
        } catch (Exception ex) {
            Logger.getLogger(CrawlerBase.class.getName()).log(Level.SEVERE, "Exception in File " + site + ": " + ex.getMessage());
            ex.printStackTrace();
        }
        raw_data.close();
    }
