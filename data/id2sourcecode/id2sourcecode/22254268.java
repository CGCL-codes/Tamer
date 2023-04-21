    @Override
    public String get(final FetchInfos fetchInfos) throws HttpException {
        URL url = null;
        try {
            url = new URL(fetchInfos.getUri());
        } catch (MalformedURLException exception) {
            throw new HttpException("uri is malformed '" + fetchInfos.getUri() + "'", exception);
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException exception) {
            fetchInfos.setResult(FETCHING_RESULT.IO_ERROR);
            throw new HttpException("get '" + fetchInfos.getUri() + "' failed", exception);
        }
        InputStream input = null;
        try {
            connection.setRequestMethod("GET");
            connection.connect();
            input = connection.getInputStream();
            if ("gzip".equals(connection.getHeaderField("content-encoding"))) {
                input = new GZIPInputStream(input);
            }
            if (HttpServiceImpl.LOGGER.isDebugEnabled()) {
                this.logConnection(connection);
                input = new LoggingInputStream(input);
            }
        } catch (SocketTimeoutException exception) {
            fetchInfos.setResult(FETCHING_RESULT.TIME_OUT);
            throw new HttpException("get '" + fetchInfos.getUri() + "' timeout", exception);
        } catch (IOException exception) {
            fetchInfos.setResult(FETCHING_RESULT.IO_ERROR);
            throw new HttpException("get '" + fetchInfos.getUri() + "' failed", exception);
        }
        fetchInfos.setResult(FETCHING_RESULT.OK);
        String response = null;
        try {
            response = this.toString(input);
        } catch (IOException exception) {
            throw new HttpException("converting inputstream to string failed", exception);
        }
        return response;
    }
