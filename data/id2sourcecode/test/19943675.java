    public Map<String, ?> getAttributes(URI uri, Map<?, ?> options) {
        Map<String, Object> result = new HashMap<String, Object>();
        Set<String> requestedAttributes = getRequestedAttributes(options);
        try {
            URL url = new URL(uri.toString());
            URLConnection urlConnection = null;
            if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_READ_ONLY)) {
                urlConnection = url.openConnection();
                if (urlConnection instanceof HttpURLConnection) {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                    httpURLConnection.setRequestMethod("OPTIONS");
                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String allow = httpURLConnection.getHeaderField("Allow");
                        result.put(URIConverter.ATTRIBUTE_READ_ONLY, allow == null || !allow.contains("PUT"));
                    }
                    urlConnection = null;
                } else {
                    result.put(URIConverter.ATTRIBUTE_READ_ONLY, true);
                }
            }
            if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_TIME_STAMP)) {
                if (urlConnection == null) {
                    urlConnection = url.openConnection();
                    if (urlConnection instanceof HttpURLConnection) {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                        httpURLConnection.setRequestMethod("HEAD");
                        httpURLConnection.getResponseCode();
                    }
                }
                if (urlConnection.getHeaderField("last-modified") != null) {
                    result.put(URIConverter.ATTRIBUTE_TIME_STAMP, urlConnection.getLastModified());
                }
            }
            if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_LENGTH)) {
                if (urlConnection == null) {
                    urlConnection = url.openConnection();
                    if (urlConnection instanceof HttpURLConnection) {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                        httpURLConnection.setRequestMethod("HEAD");
                        httpURLConnection.getResponseCode();
                    }
                }
                if (urlConnection.getHeaderField("content-length") != null) {
                    result.put(URIConverter.ATTRIBUTE_LENGTH, urlConnection.getContentLength());
                }
            }
        } catch (IOException exception) {
        }
        return result;
    }
