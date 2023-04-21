        public static int getURLContentLength(URL url) throws IOException {
            URLConnection con = url.openConnection();
            con.setConnectTimeout(ShareConstants.connectTimeout);
            con.setReadTimeout(ShareConstants.connectTimeout);
            con.setUseCaches(false);
            con.connect();
            return con.getContentLength();
        }
