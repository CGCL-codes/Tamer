    public static String getEncoding(URL url) throws IOException {
        final String encoding = getEncoding(url.openStream());
        System.out.println("url:" + url + " encoding:" + encoding);
        return encoding;
    }
