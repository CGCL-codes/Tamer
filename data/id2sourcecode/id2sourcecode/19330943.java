    public static int getRemoteSize(String urlPath) {
        URL url;
        int len = 0;
        try {
            url = new URL(urlPath);
            URLConnection con = url.openConnection();
            len = con.getContentLength();
            con.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return len;
    }
