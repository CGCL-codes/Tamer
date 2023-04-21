    public Response execute(Request req) throws Exception {
        try {
            setTotalBytes(-1);
            setBytesSoFar(0);
            setState(State.CONNECTING);
            StringBuffer surl = new StringBuffer(req.getUrl());
            if (surl.length() == 0) {
                setState(State.FAILED);
                throw new IllegalStateException("Cannot excecute a request that has no URL specified");
            }
            char delim = '?';
            for (Parameter p : req.getParameters()) {
                surl.append(delim);
                delim = '&';
                String name = URLEncoder.encode(p.getName(), "UTF-8");
                String value = URLEncoder.encode(p.getValue(), "UTF-8");
                surl.append(name + "=" + value);
            }
            URL url = createURL(surl.toString());
            URLConnection conn = url.openConnection();
            if (!(conn instanceof HttpURLConnection)) {
                setState(State.FAILED);
                throw new IllegalStateException("Must be an HTTP or HTTPS based URL");
            }
            HttpURLConnection http = (HttpURLConnection) conn;
            http.setRequestMethod(req.getMethod().name());
            http.setInstanceFollowRedirects(req.getFollowRedirects());
            for (Header h : req.getHeaders()) {
                http.setRequestProperty(h.getName(), h.getValue());
            }
            if (http instanceof HttpsURLConnection) {
                HttpsURLConnection https = (HttpsURLConnection) http;
                https.setSSLSocketFactory(createSocketFactory(url.getHost()));
            }
            long contentLength = -1;
            Header contentLengthHeader = req.getHeader("Content-Length");
            if (contentLengthHeader != null) {
                try {
                    contentLength = Long.parseLong(contentLengthHeader.getValue().trim());
                } catch (NumberFormatException ex) {
                    contentLength = -1;
                }
            }
            setTotalBytes(contentLength);
            setState(State.SENDING);
            OutputStream out = null;
            InputStream body = req.getBody();
            if (body != null) {
                try {
                    http.setDoOutput(true);
                    out = http.getOutputStream();
                    byte[] buffer = new byte[8096];
                    int length = -1;
                    while ((length = body.read(buffer)) != -1) {
                        out.write(buffer, 0, length);
                        setBytesSoFar(bytesSoFar + length);
                    }
                } catch (Exception e) {
                    setState(State.FAILED);
                    throw e;
                } finally {
                    if (out != null) out.close();
                    body.close();
                }
            }
            setState(State.SENT);
            http.connect();
            setBytesSoFar(0);
            setTotalBytes(http.getContentLength());
            setState(State.RECEIVING);
            Set<Header> headers = new HashSet<Header>();
            Header contentType = null;
            for (Map.Entry<String, List<String>> entry : http.getHeaderFields().entrySet()) {
                String headerKey = entry.getKey();
                String headerValue = http.getHeaderField(headerKey);
                if (headerKey == null) continue;
                List<String> values = entry.getValue();
                Header.Element[] elements = new Header.Element[values.size()];
                for (int j = 0; j < elements.length; j++) {
                    elements[j] = new Header.Element(new Parameter(values.get(j), values.get(j)));
                }
                Header h = new Header(headerKey, headerValue, elements);
                headers.add(h);
                if ("Content-Type".equalsIgnoreCase(headerKey)) contentType = h;
            }
            byte[] responseBody = null;
            StatusCode responseCode = StatusCode.INTERNAL_SERVER_ERROR;
            InputStream responseStream = null;
            try {
                responseStream = http.getInputStream();
                responseCode = StatusCode.valueOf(http.getResponseCode());
                String contentEncoding = http.getContentEncoding();
                if ("gzip".equals(contentEncoding)) {
                    responseStream = new GZIPInputStream(responseStream);
                }
                responseBody = readFully(responseStream);
            } catch (FileNotFoundException e) {
                responseStream = http.getErrorStream();
                responseBody = readFully(responseStream);
            } catch (HttpRetryException e) {
                setState(State.FAILED);
                return new Response(StatusCode.NOT_FOUND, "HttpRetryException: " + e.getMessage(), null, null, null, req.getUrl());
            } catch (UnknownHostException e) {
                setState(State.FAILED);
                return new Response(StatusCode.NOT_FOUND, "Unknown host", null, null, null, req.getUrl());
            } catch (IOException ex) {
                String msg = ex.getMessage();
                if (msg.contains("Server returned HTTP response code:")) {
                    int startIndex = msg.indexOf("code: ") + 6;
                    String s = msg.substring(startIndex, startIndex + 3);
                    responseCode = StatusCode.valueOf(Integer.parseInt(s));
                    responseStream = http.getErrorStream();
                    responseBody = readFully(responseStream);
                } else {
                    throw ex;
                }
            } finally {
                if (responseStream != null) responseStream.close();
            }
            String foo = "foo";
            URI uri = new URI(req.getUrl());
            URI uu = uri.resolve(new URI(foo));
            String baseUrl = uu.toString().substring(0, uu.toString().length() - foo.length());
            String charset = null;
            if (contentType != null) {
                String tmp = contentType.getValue();
                int index = tmp.indexOf(";");
                if (index >= 0) {
                    index = tmp.indexOf("=", index + 1);
                    if (index > 0) charset = contentType.getValue().substring(index + 1);
                }
            }
            Response response = new Response(responseCode, http.getResponseMessage(), responseBody, charset, headers, baseUrl);
            setState(State.DONE);
            return response;
        } catch (InterruptedException ex) {
            setState(State.ABORTED);
            throw ex;
        } finally {
        }
    }
