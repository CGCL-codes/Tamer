    private String getIpAddress(String host) {
        String url = String.format(NetworkConstants.CAPACITY_URL_FORMAT, host);
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            if (uc instanceof HttpURLConnection) {
                int responseCode = ((HttpURLConnection) uc).getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = uc.getInputStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    int read = -1;
                    byte[] buff = new byte[256];
                    while ((read = in.read(buff)) != -1) {
                        out.write(buff, 0, read);
                    }
                    in.close();
                    return readIpAddress(host, url, out);
                } else {
                    log.error("Failed opening url: " + url + " return code: " + responseCode);
                }
            } else {
                Class<? extends URLConnection> ucType = null;
                if (uc != null) {
                    ucType = uc.getClass();
                }
                log.error("Failed opening  url: " + url + " returns: " + ucType);
            }
        } catch (Exception e) {
            log.error("Failed url: " + url, e);
        }
        return null;
    }
