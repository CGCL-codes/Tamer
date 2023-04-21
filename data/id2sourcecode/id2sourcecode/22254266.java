    public InputStream fetch(final FetchInfos fetchInfos) throws HttpException {
        URL url = null;
        try {
            url = new URL(fetchInfos.getUri());
        } catch (MalformedURLException exception) {
            throw new HttpException("what the fuck '" + fetchInfos.getUri() + "'", exception);
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException exception) {
            fetchInfos.setResult(FETCHING_RESULT.IO_ERROR);
            throw new HttpException("fetch '" + fetchInfos.getUri() + "' failed", exception);
        }
        if (fetchInfos.getETag() != null) {
            connection.setRequestProperty("If-None-Match", fetchInfos.getETag());
        }
        if (fetchInfos.getLastFetch() != null) {
            synchronized (this.dateFormater) {
                final String lastUpdate = this.dateFormater.format(fetchInfos.getLastFetch());
                connection.setRequestProperty("If-Modified-Since", lastUpdate);
            }
        }
        if (fetchInfos.isDeltaEncoding()) {
            connection.setRequestProperty("A-IM", "feed");
        }
        InputStream input = null;
        try {
            connection.connect();
            fetchInfos.setLastFetch(new Date());
            final int responseCode = connection.getResponseCode();
            if ((responseCode >= 200) && (responseCode < 300)) {
                fetchInfos.setResult(FETCHING_RESULT.OK);
            } else if ((responseCode >= 300) && (responseCode < 400)) {
                if (responseCode == 304) {
                    fetchInfos.setResult(FETCHING_RESULT.NOT_MODIFIED);
                } else {
                    fetchInfos.setResult(FETCHING_RESULT.OK);
                }
            } else if ((responseCode >= 400) && (responseCode < 500)) {
                fetchInfos.setResult(FETCHING_RESULT.PATH_ERROR);
            } else if ((responseCode >= 500) && (responseCode < 600)) {
                fetchInfos.setResult(FETCHING_RESULT.SITE_ERROR);
            } else {
                fetchInfos.setResult(FETCHING_RESULT.IO_ERROR);
            }
            fetchInfos.setLastSiteAnswer(connection.getResponseCode() + "-" + connection.getResponseMessage());
            if (FETCHING_RESULT.OK.equals(fetchInfos.getResult())) {
                input = connection.getInputStream();
                if ("gzip".equals(connection.getHeaderField("content-encoding"))) {
                    input = new GZIPInputStream(input);
                }
                if (HttpServiceImpl.LOGGER.isDebugEnabled()) {
                    this.logConnection(connection);
                    input = new LoggingInputStream(input);
                }
            }
        } catch (SocketTimeoutException exception) {
            fetchInfos.setResult(FETCHING_RESULT.TIME_OUT);
            throw new HttpException("fetch '" + fetchInfos.getUri() + "' timeout", exception);
        } catch (IOException exception) {
            fetchInfos.setResult(FETCHING_RESULT.IO_ERROR);
            throw new HttpException("fetch '" + fetchInfos.getUri() + "' failed", exception);
        }
        if (connection.getHeaderField("ETag") != null) {
            fetchInfos.setETag(connection.getHeaderField("ETag"));
        }
        if (connection.getLastModified() != 0) {
            fetchInfos.setLastResourceModification(new Date(connection.getLastModified()));
        }
        if ((connection.getHeaderField("IM") != null) && connection.getHeaderField("IM").contains("feed")) {
            fetchInfos.setDeltaEncoding(true);
        }
        if ((connection.getHeaderField("Cache-Control") != null) && connection.getHeaderField("Cache-Control").contains("max-age")) {
            final String maxAge = this.explodeHeader(connection.getHeaderField("Cache-Control")).get("max-age");
            try {
                fetchInfos.setUpdateInterval(new Duration(Integer.parseInt(maxAge) * 1000));
            } catch (NumberFormatException exception) {
            }
        }
        return input;
    }
