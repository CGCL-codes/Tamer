        @Override
        public HttpResponseMessage execute(HttpMessage request, Map<String, Object> stringObjectMap) throws IOException {
            String body = readInputStream(request.getBody());
            OutputStreamWriter out = null;
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) request.url.openConnection();
            conn.setReadTimeout(URLFETCH_TIMEOUT_IN_MS);
            conn.setRequestMethod(request.method);
            if (request.headers != null) {
                for (java.util.Map.Entry<String, String> header : request.headers) {
                    conn.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            boolean doOutput = body != null && (HTTP_POST_METHOD.equalsIgnoreCase(request.method) || HTTP_PUT_METHOD.equalsIgnoreCase(request.method));
            if (doOutput) {
                conn.setDoOutput(true);
            }
            conn.connect();
            if (doOutput) {
                out = new OutputStreamWriter(conn.getOutputStream(), UTF_8);
                try {
                    out.write(body);
                    out.flush();
                } finally {
                    out.close();
                }
            }
            return new HttpResponse(request.method, request.url, conn.getResponseCode(), conn.getInputStream());
        }
