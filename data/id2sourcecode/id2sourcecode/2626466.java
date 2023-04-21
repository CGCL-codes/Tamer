    public static String getPrductStatus(String siteUrl) throws Exception {
        String httpSttusCd = null;
        BufferedReader buf = null;
        try {
            URL url = new URL(siteUrl);
            url.openStream();
            httpSttusCd = "01";
        } catch (Exception e) {
            httpSttusCd = "02";
        }
        return httpSttusCd;
    }
