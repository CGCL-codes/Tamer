    public WebResponse getResponse(final WebRequest webRequest) throws IOException {
        final long startTime = System.currentTimeMillis();
        final URL url = webRequest.getUrl();
        if (LOG.isTraceEnabled()) {
            LOG.trace("about to fetch URL " + url);
        }
        final WebResponse response = produceWebResponseForGAEProcolHack(url);
        if (response != null) {
            return response;
        }
        try {
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(webClient_.getTimeout());
            connection.addRequestProperty("User-Agent", webClient_.getBrowserVersion().getUserAgent());
            for (final Entry<String, String> header : webRequest.getAdditionalHeaders().entrySet()) {
                connection.addRequestProperty(header.getKey(), header.getValue());
            }
            final HttpMethod httpMethod = webRequest.getHttpMethod();
            connection.setRequestMethod(httpMethod.name());
            if (HttpMethod.POST == httpMethod || HttpMethod.PUT == httpMethod) {
                connection.setDoOutput(true);
                final String charset = webRequest.getCharset();
                connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                final OutputStream outputStream = connection.getOutputStream();
                try {
                    final List<NameValuePair> pairs = webRequest.getRequestParameters();
                    final org.apache.http.NameValuePair[] httpClientPairs = NameValuePair.toHttpClient(pairs);
                    final String query = URLEncodedUtils.format(Arrays.asList(httpClientPairs), charset);
                    outputStream.write(query.getBytes(charset));
                    if (webRequest.getRequestBody() != null) {
                        IOUtils.write(webRequest.getRequestBody().getBytes(charset), outputStream);
                    }
                } finally {
                    outputStream.close();
                }
            }
            final int responseCode = connection.getResponseCode();
            if (LOG.isTraceEnabled()) {
                LOG.trace("fetched URL " + url);
            }
            final List<NameValuePair> headers = new ArrayList<NameValuePair>();
            for (final Map.Entry<String, List<String>> headerEntry : connection.getHeaderFields().entrySet()) {
                final String headerKey = headerEntry.getKey();
                if (headerKey != null) {
                    final StringBuilder sb = new StringBuilder();
                    for (final String headerValue : headerEntry.getValue()) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(headerValue);
                    }
                    headers.add(new NameValuePair(headerKey, sb.toString()));
                }
            }
            final InputStream is = responseCode < 400 ? connection.getInputStream() : connection.getErrorStream();
            final byte[] byteArray;
            try {
                byteArray = IOUtils.toByteArray(is);
            } finally {
                is.close();
            }
            final long duration = System.currentTimeMillis() - startTime;
            final WebResponseData responseData = new WebResponseData(byteArray, responseCode, connection.getResponseMessage(), headers);
            return new WebResponse(responseData, webRequest, duration);
        } catch (final IOException e) {
            LOG.error("Exception while tyring to fetch " + url, e);
            throw new RuntimeException(e);
        }
    }
