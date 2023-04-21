    public InputStream sendKVPRequest(Map<String, ? extends Object> query) throws TAPIRException {
        if (!Encoding.isKVP(encoding)) {
            throw new TAPIRException(this, "sendKVPRequest: invalid request");
        }
        clearLastData();
        HttpURLConnection connection = null;
        if (encoding == Encoding.KVP_GET) {
            try {
                connection = (HttpURLConnection) getKVPGetURL(accessPoint, query, UTF8).openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.connect();
            } catch (Throwable t) {
                throw new TAPIRException(this, "sendKVPRequest [GET]", t);
            }
        } else if (encoding == Encoding.KVP_POST) {
            try {
                if (url == null) {
                    url = new URL(accessPoint);
                }
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                if (agentName != null) {
                    connection.setRequestProperty("User-Agent", agentName);
                }
                StringBuffer postData = new StringBuffer();
                for (Iterator<String> keys = query.keySet().iterator(); keys.hasNext(); ) {
                    String key = keys.next();
                    Object value = query.get(key);
                    if (value instanceof Collection) {
                        Iterator i = ((Collection) value).iterator();
                        while (i.hasNext()) {
                            Object v = i.next();
                            if (v != null) {
                                if (postData.length() > 0) {
                                    postData.append("&");
                                }
                                postData.append(key).append("=").append(v.toString());
                            }
                        }
                    } else {
                        if (postData.length() > 0) {
                            postData.append("&");
                        }
                        postData.append(key).append("=").append(value.toString());
                    }
                }
                PrintStream out = new PrintStream(connection.getOutputStream());
                out.print(postData.toString());
                out.close();
            } catch (Throwable t) {
                throw new TAPIRException(this, "sendKVPRequest [POST]", t);
            }
        } else {
            throw new TAPIRException(this, "sendKVPRequest: Incompatible TAPIR encoding to use with KVP requests");
        }
        return sendHTTPRequest(connection);
    }
