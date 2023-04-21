    protected byte[] downloadData(String urlS) throws Exception {
        byte[] data = null;
        try {
            URL url = new URL(urlS);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            authenticateRequest(conn);
            int respCode = conn.getResponseCode();
            log.info("response code = " + respCode);
            if (respCode == 200) {
                InputStream in = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    bos.write(buf, 0, len);
                }
                data = bos.toByteArray();
                bos.close();
                conn.disconnect();
                log.info("Response read");
            }
        } catch (Exception ex) {
            throw new Exception("The atachment can not be dowloaded by this URL: " + urlS, ex);
        }
        return data;
    }
