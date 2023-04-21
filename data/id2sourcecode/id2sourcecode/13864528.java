    public static Certificate[] getServerCertificates(URL url) throws IOException {
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        return getServerCertificates(conn);
    }
