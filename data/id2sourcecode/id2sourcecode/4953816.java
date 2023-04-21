    private WADOResponseObject getRemoteWADOObject(AEDTO aedto, WADORequestObject req) {
        if (log.isInfoEnabled()) log.info("WADO request redirected to aedto:" + aedto.getHostName() + " WADO URL:" + aedto.getWadoURL());
        URL url = null;
        try {
            url = getRedirectURL(aedto, req);
            if (log.isDebugEnabled()) log.debug("redirect url:" + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String authHeader = (String) req.getRequestHeaders().get("Authorization");
            if (authHeader != null) {
                conn.addRequestProperty("Authorization", authHeader);
            }
            conn.connect();
            if (log.isDebugEnabled()) log.debug("conn.getResponseCode():" + conn.getResponseCode());
            if (conn.getResponseCode() != HttpServletResponse.SC_OK) {
                if (log.isInfoEnabled()) log.info("Remote WADO server responses with:" + conn.getResponseMessage());
                return new WADOStreamResponseObjectImpl(null, conn.getContentType(), conn.getResponseCode(), conn.getResponseMessage());
            }
            InputStream is = conn.getInputStream();
            if (WADOCacheImpl.getWADOCache().isRedirectCaching() && CONTENT_TYPE_JPEG.equals(conn.getContentType())) {
                String suffix = req.getFrameNumber();
                if (suffix != null && suffix.equals("0")) suffix = null;
                File file = WADOCacheImpl.getWADOCache().putStream(is, req.getStudyUID(), req.getSeriesUID(), req.getObjectUID(), req.getRows(), req.getColumns(), req.getRegion(), req.getWindowWidth(), req.getWindowCenter(), req.getImageQuality(), suffix);
                is = new FileInputStream(file);
            }
            return new WADOStreamResponseObjectImpl(is, conn.getContentType(), HttpServletResponse.SC_OK, null);
        } catch (Exception e) {
            log.error("Can't connect to remote WADO service:" + url, e);
            return new WADOStreamResponseObjectImpl(null, CONTENT_TYPE_JPEG, HttpServletResponse.SC_NOT_FOUND, "Redirect to find requested object failed! (Can't connect to remote WADO service:" + url + ")!");
        }
    }
