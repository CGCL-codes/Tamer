    protected static byte[] doDownload(String url, boolean isProxySet, String proxyHost, int proxyPort) {
        if (Config.LOGD) Log.d(TAG, "Downloading XTRA data from " + url);
        AndroidHttpClient client = null;
        try {
            client = AndroidHttpClient.newInstance("Android");
            HttpUriRequest req = new HttpGet(url);
            if (isProxySet) {
                HttpHost proxy = new HttpHost(proxyHost, proxyPort);
                ConnRouteParams.setDefaultProxy(req.getParams(), proxy);
            }
            req.addHeader("Accept", "*/*, application/vnd.wap.mms-message, application/vnd.wap.sic");
            req.addHeader("x-wap-profile", "http://www.openmobilealliance.org/tech/profiles/UAPROF/ccppschema-20021212#");
            HttpResponse response = client.execute(req);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != 200) {
                if (Config.LOGD) Log.d(TAG, "HTTP error: " + status.getReasonPhrase());
                return null;
            }
            HttpEntity entity = response.getEntity();
            byte[] body = null;
            if (entity != null) {
                try {
                    if (entity.getContentLength() > 0) {
                        body = new byte[(int) entity.getContentLength()];
                        DataInputStream dis = new DataInputStream(entity.getContent());
                        try {
                            dis.readFully(body);
                        } finally {
                            try {
                                dis.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Unexpected IOException.", e);
                            }
                        }
                    }
                } finally {
                    if (entity != null) {
                        entity.consumeContent();
                    }
                }
            }
            return body;
        } catch (Exception e) {
            if (Config.LOGD) Log.d(TAG, "error " + e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }
