    protected String getIpAddress(String host) {
        String url = String.format(CAPACITY_URL, host);
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
                    StringTokenizer toks = new StringTokenizer(out.toString(), "\r\n");
                    if (toks.countTokens() <= 0) {
                        log.warn("Failed getting tokens for: " + url);
                        return null;
                    }
                    String coloCapacityString = getTokenValue(COLO_CAPACITY, toks);
                    if (coloCapacityString == null || coloCapacityString.isEmpty()) {
                        log.error("No colo capacity found for: " + host);
                    } else {
                        Integer coloCapacity = new Integer(coloCapacityString);
                        log.info("Colo Capacity is: " + coloCapacity + " for: " + host);
                    }
                    String ipAddress = getTokenValue(CS_IP_ADDRESS, toks);
                    if (ipAddress == null || ipAddress.isEmpty()) {
                        log.error("No ipAddress found for: " + host);
                        return null;
                    } else {
                        log.info("ipAddress is: " + ipAddress + " for: " + host);
                        return ipAddress;
                    }
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
