    public HttpStream getStream(URI uri, Date lastModifiedDate, String eTag, boolean deltaEncoding) throws HttpException {
        HttpStream stream = null;
        logManager.addLog(LogLevel.VERBOSE, "URI: " + uri);
        final HttpHost target = new HttpHost(uri.getHost(), uri.getPort(), "http");
        try {
            final HttpRequest req = new HttpGet(uri);
            req.setHeader("Accept-Encoding", "gzip");
            if (eTag != null) req.setHeader("If-None-Match", eTag);
            if (lastModifiedDate != null) req.setHeader("If-Modified-Since", DateUtils.formatDate(lastModifiedDate));
            if (deltaEncoding) req.setHeader("A-IM", "feed");
            final HttpResponse response = client.execute(target, req);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) throw new HttpServerException(response.getStatusLine());
            stream = new HttpStream(response);
        } catch (ClientProtocolException exception) {
            throw new HttpException(exception);
        } catch (IOException exception) {
            throw new HttpException(exception);
        }
        logManager.addLog(LogLevel.VERBOSE, "fini pour l'URI: " + uri);
        return stream;
    }
