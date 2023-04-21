    public String processServerResponse() {
        try {
            String str;
            int rc = conn.getResponseCode();
            if (rc == HttpURLConnection.HTTP_OK) {
                int length = conn.getContentLength();
                if (length != -1) {
                    byte servletData[] = new byte[length];
                    is.read(servletData);
                    str = new String(servletData);
                } else {
                    ByteArrayOutputStream bStrm = new ByteArrayOutputStream();
                    int ch;
                    while ((ch = is.read()) != -1) bStrm.write(ch);
                    str = new String(bStrm.toByteArray());
                    bStrm.close();
                }
                return str;
            } else throw new IOException("HTTP response code: " + rc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
