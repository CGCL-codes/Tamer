    public URLConnection openConnection(URL url) throws ParserException {
        boolean repeat;
        int repeated;
        Properties sysprops;
        Hashtable properties;
        Enumeration enumeration;
        String key;
        String value;
        String set = null;
        String host = null;
        String port = null;
        String host2 = null;
        String port2 = null;
        HttpURLConnection http;
        String auth;
        String encoded;
        int code;
        String uri;
        URLConnection ret;
        repeated = 0;
        do {
            repeat = false;
            try {
                try {
                    if ((null != getProxyHost()) && (0 != getProxyPort())) {
                        sysprops = System.getProperties();
                        set = (String) sysprops.put("proxySet", "true");
                        host = (String) sysprops.put("proxyHost", getProxyHost());
                        port = (String) sysprops.put("proxyPort", Integer.toString(getProxyPort()));
                        host2 = (String) sysprops.put("http.proxyHost", getProxyHost());
                        port2 = (String) sysprops.put("http.proxyPort", Integer.toString(getProxyPort()));
                        System.setProperties(sysprops);
                    }
                    ret = url.openConnection();
                    if (ret instanceof HttpURLConnection) {
                        http = (HttpURLConnection) ret;
                        if (getRedirectionProcessingEnabled()) http.setInstanceFollowRedirects(false);
                        properties = getRequestProperties();
                        if (null != properties) for (enumeration = properties.keys(); enumeration.hasMoreElements(); ) {
                            key = (String) enumeration.nextElement();
                            value = (String) properties.get(key);
                            ret.setRequestProperty(key, value);
                        }
                        if ((null != getProxyUser()) && (null != getProxyPassword())) {
                            auth = getProxyUser() + ":" + getProxyPassword();
                            encoded = encode(auth.getBytes("ISO-8859-1"));
                            ret.setRequestProperty("Proxy-Authorization", encoded);
                        }
                        if ((null != getUser()) && (null != getPassword())) {
                            auth = getUser() + ":" + getPassword();
                            encoded = encode(auth.getBytes("ISO-8859-1"));
                            ret.setRequestProperty("Authorization", "Basic " + encoded);
                        }
                        if (getCookieProcessingEnabled()) addCookies(ret);
                        if (null != getMonitor()) getMonitor().preConnect(http);
                    } else http = null;
                    try {
                        ret.connect();
                        if (null != http) {
                            if (null != getMonitor()) getMonitor().postConnect(http);
                            if (getCookieProcessingEnabled()) parseCookies(ret);
                            code = http.getResponseCode();
                            if ((3 == (code / 100)) && (repeated < 20)) if (null != (uri = getLocation(http))) {
                                url = new URL(uri);
                                repeat = true;
                                repeated++;
                            }
                        }
                    } catch (UnknownHostException uhe) {
                        int message = (int) (Math.random() * FOUR_OH_FOUR.length);
                        throw new ParserException(FOUR_OH_FOUR[message], uhe);
                    } catch (IOException ioe) {
                        throw new ParserException(ioe.getMessage(), ioe);
                    }
                } finally {
                    if ((null != getProxyHost()) && (0 != getProxyPort())) {
                        sysprops = System.getProperties();
                        if (null != set) sysprops.put("proxySet", set); else sysprops.remove("proxySet");
                        if (null != host) sysprops.put("proxyHost", host); else sysprops.remove("proxyHost");
                        if (null != port) sysprops.put("proxyPort", port); else sysprops.remove("proxyPort");
                        if (null != host2) sysprops.put("http.proxyHost", host2); else sysprops.remove("http.proxyHost");
                        if (null != port2) sysprops.put("http.proxyPort", port2); else sysprops.remove("http.proxyPort");
                        System.setProperties(sysprops);
                    }
                }
            } catch (IOException ioe) {
                String msg = "Error in opening a connection to " + url.toExternalForm();
                ParserException ex = new ParserException(msg, ioe);
                throw ex;
            }
        } while (repeat);
        return (ret);
    }
