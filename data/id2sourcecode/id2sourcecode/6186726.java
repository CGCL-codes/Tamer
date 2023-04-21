    public static String getURLContents(URL url) throws Exception {
        URLConnection con = url.openConnection();
        HttpURLConnection hcon = null;
        if (con != null && con instanceof HttpURLConnection) {
            hcon = (HttpURLConnection) con;
        }
        con.setAllowUserInteraction(false);
        con.connect();
        InputStream is = con.getInputStream();
        StringBuffer contents = new StringBuffer();
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(is);
            int len = -1;
            char[] ca = new char[1024];
            while ((len = isr.read(ca, 0, 1024)) != -1) {
                contents.append(ca, 0, len);
            }
            return contents.toString();
        } finally {
            CloseUtils.close(isr);
            if (hcon != null) hcon.disconnect();
        }
    }
