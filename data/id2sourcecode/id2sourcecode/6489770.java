    private static int getNextID_HTTP(String TableName, String website, String prm_USER, String prm_PASSWORD, String prm_TABLE, String prm_ALTKEY, String prm_COMMENT, String prm_PROJECT) {
        StringBuffer read = new StringBuffer();
        int retValue = -1;
        try {
            String completeUrl = website + "?" + "USER=" + URLEncoder.encode(prm_USER, "UTF-8") + "&PASSWORD=" + URLEncoder.encode(prm_PASSWORD, "UTF-8") + "&PROJECT=" + URLEncoder.encode(prm_PROJECT, "UTF-8") + "&TABLE=" + URLEncoder.encode(prm_TABLE, "UTF-8") + "&ALTKEY=" + URLEncoder.encode(prm_ALTKEY, "UTF-8") + "&COMMENT=" + URLEncoder.encode(prm_COMMENT, "UTF-8");
            URL url = new URL(completeUrl);
            String protocol = url.getProtocol();
            if (!protocol.equals("http")) throw new IllegalArgumentException("URL must use 'http:' protocol");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setAllowUserInteraction(false);
            InputStream is = conn.getInputStream();
            byte[] buffer = new byte[4096];
            int bytes_read;
            while ((bytes_read = is.read(buffer)) != -1) {
                for (int i = 0; i < bytes_read; i++) {
                    if (buffer[i] != 10) read.append((char) buffer[i]);
                }
            }
            conn.disconnect();
            retValue = Integer.parseInt(read.toString());
            if (retValue <= 0) retValue = -1;
        } catch (Exception e) {
            System.err.println(e);
            retValue = -1;
        }
        s_log.log(Level.INFO, "getNextID_HTTP - " + TableName + "=" + read + "(" + retValue + ")");
        return retValue;
    }
