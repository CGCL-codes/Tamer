    protected R doRequest(URL url, String method, String content, String contentType, String accept) throws IOException {
        boolean debug = logger.isDebugEnabled();
        if (debug) {
            logger.debug("HTTP " + method + " request: URL=" + url);
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(content != null);
        byte[] bytes = null;
        try {
            conn.setInstanceFollowRedirects(false);
            conn.setDefaultUseCaches(false);
            conn.setUseCaches(false);
            if (accept != null) {
                if (debug) {
                    logger.debug("HTTP " + method + " request: accept=" + accept);
                }
                conn.setRequestProperty("Accept", accept);
            }
            if (content != null) {
                bytes = content.getBytes();
                if (debug) {
                    logger.debug("HTTP " + method + " request: content-length=" + bytes.length);
                }
                conn.setRequestProperty("Content-Length", Integer.toString(bytes.length));
                if (contentType != null) {
                    if (debug) {
                        logger.debug("HTTP " + method + " request: content-type=" + contentType);
                    }
                    conn.setRequestProperty("Content-Type", contentType);
                }
            }
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                if (debug) {
                    logger.debug("HTTP " + method + " request: " + entry.getKey() + "=" + entry.getValue());
                }
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
            conn.connect();
            if (bytes != null) {
                OutputStream os = conn.getOutputStream();
                os.write(bytes);
                os.close();
            }
            int rc = conn.getResponseCode();
            InputStream is = (rc >= 400) ? conn.getErrorStream() : conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int count;
            while ((count = is.read(buffer)) > 0) {
                bos.write(buffer, 0, count);
            }
            is.close();
            return createResponse(conn, bos.toByteArray());
        } finally {
            conn.disconnect();
        }
    }
