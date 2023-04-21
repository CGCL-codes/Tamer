    public Reader getURLStream(String source, int timeout, String username, String password, String realm) throws Exception {
        long start = System.currentTimeMillis();
        logger.info("Get URL Stream: " + source);
        BufferedReader in = null;
        logger.info("New HTTP Connection: " + source);
        HTTPConnection con = null;
        HTTPResponse rsp = null;
        try {
            if (source == null) {
                throw new Exception("Source is null");
            }
            URL url = null;
            if (source.startsWith("/")) {
                url = this.getContext().getResource(source);
                in = new BufferedReader(new InputStreamReader(url.openStream()));
            } else {
                url = new URL(source);
                con = new HTTPConnection(url);
                CookieModule.setCookiePolicyHandler(null);
                con.setDefaultHeaders(new NVPair[] { new NVPair("Connection", "close") });
                if (timeout > 0) {
                    logger.info("Timeout Set To: " + timeout + " milliseconds");
                    con.setTimeout(timeout);
                }
                if ((username != null) && (username.trim().length() > 0)) {
                    logger.info("Adding basic authentication: " + username + "(realm: " + realm + ")");
                    con.addBasicAuthorization(realm, username, password);
                }
                String request = url.getPath();
                if (url.getQuery() != null) {
                    request += "?" + url.getQuery();
                }
                logger.info("Perform GET Http request " + request);
                rsp = con.Get(request);
                logger.info("Start reading response after " + (System.currentTimeMillis() - start) + " millis");
                if (rsp.getStatusCode() >= 300) {
                    logger.error("Received Error: " + rsp.getReasonLine());
                    logger.error(rsp.getText());
                    throw new Exception("Received HTTP Error Status " + rsp.getStatusCode() + ": " + rsp.getReasonLine() + " - " + rsp.getText());
                }
                in = new BufferedReader(new InputStreamReader(rsp.getInputStream()));
            }
        } catch (Exception ex) {
            logger.error("Failed to get URL Stream for source: " + source + " - " + ex.getClass().getName() + ": " + ex.getMessage(), ex);
            if (con != null) {
                logger.info("Stopping HTTP Connection");
                con.stop();
            }
            throw new Exception("Exception: " + ex.getClass().getName() + ": " + ex.getMessage());
        }
        logger.info("Got URL Stream in " + (System.currentTimeMillis() - start) + " millis");
        return in;
    }
