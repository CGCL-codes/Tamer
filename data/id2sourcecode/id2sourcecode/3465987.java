    public static HttpURLConnection getUrlConnection(URL url, String proxyHost, String proxyPort, String aclUsername, String aclPassword, Map additionnalHeaders) throws IOException {
        Properties systemSettings = System.getProperties();
        String oldHttpProxyHost = systemSettings.getProperty("http.proxyHost");
        String oldHttpProxyPort = systemSettings.getProperty("http.proxyPort");
        String oldHttpsProxyHost = systemSettings.getProperty("https.proxyHost");
        String oldHttpsProxyPort = systemSettings.getProperty("https.proxyPort");
        String oldHandler = systemSettings.getProperty("java.protocol.handler.pkgs");
        systemSettings.remove("http.proxyHost");
        systemSettings.remove("http.proxyPort");
        systemSettings.remove("https.proxyHost");
        systemSettings.remove("https.proxyPort");
        systemSettings.remove("java.protocol.handler.pkgs");
        System.setProperties(systemSettings);
        String protocol = url.getProtocol();
        if (proxyHost != null && proxyPort != null) {
            systemSettings.put("proxySet", "true");
            if ("https".equals(protocol)) {
                systemSettings.put("https.proxyHost", proxyHost);
                systemSettings.put("https.proxyPort", proxyPort);
            } else if ("http".equals(protocol)) {
                systemSettings.put("http.proxyHost", proxyHost);
                systemSettings.put("http.proxyPort", proxyPort);
            }
        }
        if ("https".equals(protocol)) {
            systemSettings.put("java.protocol.handler.pkgs", "sun.net.www.protocol.https");
        } else if ("http".equals(protocol)) {
            systemSettings.put("java.protocol.handler.pkgs", "sun.net.www.protocol.http");
        }
        systemSettings.put("sun.net.client.defaultConnectTimeout", "5000");
        systemSettings.put("sun.net.client.defaultReadTimeout", "30000");
        System.setProperties(systemSettings);
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            if (aclUsername != null || aclPassword != null) {
                String authorization = ((aclUsername != null) ? aclUsername : "") + ":" + ((aclPassword != null) ? aclPassword : "");
                BASE64Encoder encoder = new BASE64Encoder();
                String encodedAuthorization = encoder.encode(authorization.getBytes());
                con.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
            }
            if (additionnalHeaders != null) {
                Iterator it = additionnalHeaders.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    String value = (String) additionnalHeaders.get(key);
                    con.setRequestProperty(key, value);
                }
            }
            return con;
        } catch (IOException e) {
            throw e;
        } finally {
            if (oldHttpProxyHost != null) {
                systemSettings.put("http.proxyHost", oldHttpProxyHost);
            }
            if (oldHttpProxyPort != null) {
                systemSettings.put("http.proxyPort", oldHttpProxyPort);
            }
            if (oldHttpsProxyHost != null) {
                systemSettings.put("https.proxyHost", oldHttpsProxyHost);
            }
            if (oldHttpsProxyPort != null) {
                systemSettings.put("https.proxyPort", oldHttpsProxyPort);
            }
            if (oldHandler != null) {
                systemSettings.put("java.protocol.handler.pkgs", oldHandler);
            }
            System.setProperties(systemSettings);
        }
    }
