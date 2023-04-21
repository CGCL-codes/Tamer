    public HttpResponseMessage execute(HttpMessage request, Map<String, Object> parameters) throws IOException {
        final String httpMethod = request.method;
        final Collection<Map.Entry<String, String>> addHeaders = request.headers;
        final URL url = request.url;
        final URLConnection connection = url.openConnection();
        connection.setDoInput(true);
        if (connection instanceof HttpURLConnection) {
            HttpURLConnection http = (HttpURLConnection) connection;
            http.setRequestMethod(httpMethod);
            for (Map.Entry<String, Object> p : parameters.entrySet()) {
                String name = p.getKey();
                String value = p.getValue().toString();
                if (FOLLOW_REDIRECTS.equals(name)) {
                    http.setInstanceFollowRedirects(Boolean.parseBoolean(value));
                } else if (CONNECT_TIMEOUT.equals(name)) {
                    http.setConnectTimeout(Integer.parseInt(value));
                } else if (READ_TIMEOUT.equals(name)) {
                    http.setReadTimeout(Integer.parseInt(value));
                }
            }
        }
        StringBuilder headers = new StringBuilder(httpMethod);
        {
            headers.append(" ").append(url.getPath());
            String query = url.getQuery();
            if (query != null && query.length() > 0) {
                headers.append("?").append(query);
            }
            headers.append(EOL);
            for (Map.Entry<String, List<String>> header : connection.getRequestProperties().entrySet()) {
                String key = header.getKey();
                for (String value : header.getValue()) {
                    headers.append(key).append(": ").append(value).append(EOL);
                }
            }
        }
        String contentLength = null;
        for (Map.Entry<String, String> header : addHeaders) {
            String key = header.getKey();
            if (HttpMessage.CONTENT_LENGTH.equalsIgnoreCase(key) && connection instanceof HttpURLConnection) {
                contentLength = header.getValue();
            } else {
                connection.setRequestProperty(key, header.getValue());
            }
            headers.append(key).append(": ").append(header.getValue()).append(EOL);
        }
        byte[] excerpt = null;
        final InputStream body = request.getBody();
        if (body != null) {
            try {
                if (contentLength != null) {
                    ((HttpURLConnection) connection).setFixedLengthStreamingMode(Integer.parseInt(contentLength));
                }
                connection.setDoOutput(true);
                OutputStream output = connection.getOutputStream();
                try {
                    final ExcerptInputStream ex = new ExcerptInputStream(body);
                    byte[] b = new byte[1024];
                    for (int n; 0 < (n = ex.read(b)); ) {
                        output.write(b, 0, n);
                    }
                    excerpt = ex.getExcerpt();
                } finally {
                    output.close();
                }
            } finally {
                body.close();
            }
        }
        return new URLConnectionResponse(request, headers.toString(), excerpt, connection);
    }
