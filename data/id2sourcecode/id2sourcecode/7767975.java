    public void open() throws HTTPSRequestException {
        try {
            url = new URL(APJP.APJP_REMOTE_HTTPS_SERVER_REQUEST_URL[i]);
            Proxy proxy = Proxy.NO_PROXY;
            if (url.getProtocol().equalsIgnoreCase("HTTP") == true) {
                if (APJP.APJP_HTTP_PROXY_SERVER_ADDRESS.equalsIgnoreCase("") == false) {
                    proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(APJP.APJP_HTTP_PROXY_SERVER_ADDRESS, APJP.APJP_HTTP_PROXY_SERVER_PORT));
                }
            } else {
                if (url.getProtocol().equalsIgnoreCase("HTTPS") == true) {
                    if (APJP.APJP_HTTPS_PROXY_SERVER_ADDRESS.equalsIgnoreCase("") == false) {
                        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(APJP.APJP_HTTPS_PROXY_SERVER_ADDRESS, APJP.APJP_HTTPS_PROXY_SERVER_PORT));
                    }
                }
            }
            urlConnection = url.openConnection(proxy);
            if (urlConnection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) urlConnection).setHostnameVerifier(new HostnameVerifier() {

                    public boolean verify(String hostname, SSLSession sslSession) {
                        String value1 = APJP.APJP_REMOTE_HTTPS_SERVER_REQUEST_URL[i];
                        String[] values1 = value1.split("/", -1);
                        String value2 = values1[2];
                        String[] values2 = value2.split(":");
                        String value3 = values2[0];
                        if (value3.equalsIgnoreCase(hostname)) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
            }
            if (url.getProtocol().equalsIgnoreCase("HTTP") == true) {
                if (APJP.APJP_HTTP_PROXY_SERVER_ADDRESS.equalsIgnoreCase("") == false && APJP.APJP_HTTP_PROXY_SERVER_USERNAME.equalsIgnoreCase("") == false) {
                    urlConnection.setRequestProperty("Proxy-Authorization", "Basic " + new String(BASE64.encode((APJP.APJP_HTTP_PROXY_SERVER_USERNAME + ":" + APJP.APJP_HTTP_PROXY_SERVER_PASSWORD).getBytes())));
                }
            } else {
                if (url.getProtocol().equalsIgnoreCase("HTTPS") == true) {
                    if (APJP.APJP_HTTPS_PROXY_SERVER_ADDRESS.equalsIgnoreCase("") == false && APJP.APJP_HTTPS_PROXY_SERVER_USERNAME.equalsIgnoreCase("") == false) {
                        urlConnection.setRequestProperty("Proxy-Authorization", "Basic " + new String(BASE64.encode((APJP.APJP_HTTPS_PROXY_SERVER_USERNAME + ":" + APJP.APJP_HTTPS_PROXY_SERVER_PASSWORD).getBytes())));
                    }
                }
            }
            for (int j = 0; j < APJP.APJP_REMOTE_HTTPS_SERVER_REQUEST_PROPERTY_KEY[i].length; j = j + 1) {
                if (APJP.APJP_REMOTE_HTTPS_SERVER_REQUEST_PROPERTY_KEY[i][j].equalsIgnoreCase("") == false) {
                    urlConnection.setRequestProperty(APJP.APJP_REMOTE_HTTPS_SERVER_REQUEST_PROPERTY_KEY[i][j], APJP.APJP_REMOTE_HTTPS_SERVER_REQUEST_PROPERTY_VALUE[i][j]);
                }
            }
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (Exception e) {
            throw new HTTPSRequestException("HTTPS_REQUEST/OPEN", e);
        }
    }
