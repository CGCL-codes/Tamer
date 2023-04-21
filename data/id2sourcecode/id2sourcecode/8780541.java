    public void access() {
        logger.info("accessing [" + name + "] with URL " + url);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[512];
        try {
            try {
                InetAddress addr = InetAddress.getByName(url.getHost());
                logger.info("Connecting to " + addr);
            } catch (IOException ignore) {
            }
            HTTPConnection conn = new HTTPConnection(url);
            conn.setDefaultHeaders(USER_AGENT);
            HTTPResponse resp = conn.Get(url.getFile());
            if (resp.getStatusCode() >= 400) {
                throw new IOException(resp.getStatusCode() + resp.getReasonLine());
            }
            InputStream in = resp.getInputStream();
            int len = 0;
            while ((len = in.read(buf)) != -1) out.write(buf, 0, len);
        } catch (HTTPClient.ModuleException me) {
            logger.warn(me.toString());
            return;
        } catch (IOException ioe) {
            logger.warn(ioe.toString());
            return;
        }
        synchronized (lock) {
            basis = out.toByteArray();
            lastAccess = System.currentTimeMillis();
        }
        update(true, true);
    }
