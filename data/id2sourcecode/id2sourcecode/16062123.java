    @Override
    public LOCKSSDaemonStatusTableTO getDataFromDaemonStatusTable() throws HttpResponseException {
        LOCKSSPlatformStatusReader ldstxp = null;
        LOCKSSPlatformStatusHtmlParser ldsthp = null;
        LOCKSSDaemonStatusTableTO ldstTO = null;
        HttpEntity entity = null;
        HttpGet httpget = null;
        String headerTimeString = null;
        try {
            httpClient.getParams().setParameter("http.connection.timeout", 100000);
            httpClient.getParams().setParameter("http.socket.timeout", 100000);
            httpget = new HttpGet(dataUrl);
            logger.log(Level.INFO, "executing request {0}", httpget.getURI());
            HttpResponse resp = httpClient.execute(httpget);
            HeaderElementIterator it = new BasicHeaderElementIterator(resp.headerIterator());
            while (it.hasNext()) {
                HeaderElement elem = it.nextElement();
                logger.log(Level.INFO, "name({0})=value({1})", new Object[] { elem.getName(), elem.getValue() });
                if (elem.getName().endsWith("GMT")) {
                    headerTimeString = elem.getName();
                }
                NameValuePair[] params = elem.getParameters();
                for (int i = 0; i < params.length; i++) {
                    logger.log(Level.FINE, "parampair:name = {0}", params[i].getName());
                }
            }
            logger.log(Level.INFO, "headerTimeString={0}", headerTimeString);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                logger.log(Level.WARNING, "response to the request is not OK: skip this IP: status code={0}", statusCode);
                httpget.abort();
                ldstTO = new LOCKSSDaemonStatusTableTO();
                ldstTO.setBoxHttpStatusOK(false);
                return ldstTO;
            }
            entity = resp.getEntity();
            InputStream is = entity.getContent();
            ldsthp = new LOCKSSPlatformStatusHtmlParser();
            ldsthp.getPlatformStatusData(is);
            ldstTO = ldsthp.getLOCKSSDaemonStatusTableTO();
            ldstTO.setIpAddress(this.ip);
            logger.log(Level.FINE, "After parsing {0}: contents of ldstTO:\n{1}", new Object[] { this.tableId, ldstTO });
            String currenttimeTimestamp = ldstTO.getBoxInfoMap().get("time");
            logger.log(Level.INFO, "headerTimeString={0} : currenttimeTimestamp={1}", new Object[] { headerTimeString, currenttimeTimestamp });
            String timezoneOffset = DaemonStatusDataUtil.calculateTimezoneOffset(headerTimeString, currenttimeTimestamp);
            ldstTO.setTimezoneOffset(timezoneOffset);
            logger.log(Level.INFO, "timezone offset={0}", ldstTO.getTimezoneOffset());
        } catch (ConnectTimeoutException ce) {
            logger.log(Level.WARNING, "ConnectTimeoutException occurred", ce);
            ldstTO = new LOCKSSDaemonStatusTableTO();
            ldstTO.setBoxHttpStatusOK(false);
            if (httpget != null) {
                httpget.abort();
            }
            return ldstTO;
        } catch (SocketTimeoutException se) {
            logger.log(Level.WARNING, "SocketTimeoutException occurred", se);
            ldstTO = new LOCKSSDaemonStatusTableTO();
            ldstTO.setBoxHttpStatusOK(false);
            if (httpget != null) {
                httpget.abort();
            }
            return ldstTO;
        } catch (ConnectException ce) {
            logger.log(Level.SEVERE, "connection to this box is refused:{0}", this.ip);
            if (httpget != null) {
                httpget.abort();
            }
            ldstTO = new LOCKSSDaemonStatusTableTO();
            ldstTO.setBoxHttpStatusOK(false);
            return ldstTO;
        } catch (ClientProtocolException pe) {
            logger.log(Level.SEVERE, "The protocol was not http; https is suspected", pe);
            ldstTO = new LOCKSSDaemonStatusTableTO();
            ldstTO.setBoxHttpStatusOK(false);
            ldstTO.setHttpProtocol(false);
            if (httpget != null) {
                httpget.abort();
            }
            return ldstTO;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IO exception occurs", ex);
            ldstTO = new LOCKSSDaemonStatusTableTO();
            ldstTO.setBoxHttpStatusOK(false);
            if (httpget != null) {
                httpget.abort();
            }
            return ldstTO;
        } finally {
            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "io exception when entity was to be" + "consumed", ex);
                }
            }
        }
        return ldstTO;
    }
