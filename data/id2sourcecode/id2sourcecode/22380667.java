    @SuppressWarnings("unchecked")
    public void handle_WriteURL(AppEvent evt) throws ScriptException {
        int i = 0;
        try {
            String urlName = getStringParameter(evt, "url", "");
            Object response = evt.getConnection().getResponse();
            while (response != null && response instanceof ScriptResponse) {
                response = ((ScriptResponse) response).getResponse();
            }
            if (!(response instanceof HttpServletResponse)) {
                throw new Exception("Failed to extract HttpServletResponse from Application Event");
            }
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            OutputStream out = httpResponse.getOutputStream();
            URL url = new URL(urlName);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDefaultUseCaches(true);
            urlConnection.setUseCaches(true);
            urlConnection.setFollowRedirects(true);
            Connection connection = evt.getConnection();
            while (connection != null) {
                evt.getApplication().getLogger().logMessage(8, "writeURL: Found connection: " + connection.getClass().getCanonicalName());
                Object request = connection.getRequest();
                if (request != null && request instanceof HttpServletRequest) {
                    Enumeration<String> headers = ((HttpServletRequest) request).getHeaderNames();
                    while (headers.hasMoreElements()) {
                        String headerName = headers.nextElement();
                        evt.getApplication().getLogger().logMessage(8, "writeURL: Setting HTTP request header: " + headerName + ": " + ((HttpServletRequest) request).getHeader(headerName));
                        urlConnection.setRequestProperty(headerName, ((HttpServletRequest) request).getHeader(headerName));
                    }
                    break;
                }
                connection = connection.getParentConnection();
            }
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            httpResponse.setStatus(statusCode);
            for (int j = 1; j < 64; j++) {
                String key = urlConnection.getHeaderFieldKey(j);
                String value = urlConnection.getHeaderField(j);
                if (value == null) break;
                httpResponse.addHeader(key, value);
                evt.getApplication().getLogger().logMessage(8, "writeURL: HTTP response header: " + key + ": " + value);
            }
            evt.getApplication().getLogger().logMessage(8, "writeURL: HTTP response status: " + statusCode);
            httpResponse.flushBuffer();
            InputStream is = urlConnection.getInputStream();
            int ch;
            boolean clientClosedStream = false;
            while (((ch = is.read()) != -1) && !clientClosedStream) {
                i++;
                try {
                    out.write(ch);
                    if (i % 1024 == 0) {
                        out.flush();
                        httpResponse.flushBuffer();
                    }
                } catch (SocketException e) {
                    evt.getApplication().getLogger().logMessage(4, "writeURL: Client closed HTTP connection after " + i + " bytes written.");
                    clientClosedStream = true;
                }
            }
            evt.getApplication().getLogger().logMessage(4, "writeURL: Wrote bytes to HTTP response: " + i);
            if (i < 1) {
                throw new IOException("The URL stream is empty");
            }
            out.flush();
            out.close();
            urlConnection.disconnect();
        } catch (IOException e) {
            evt.getApplication().getLogger().logMessage(10, "writeURL: Client closed HTTP connection after " + i + " bytes written.");
        } catch (Exception e) {
            throw new ScriptException(evt, e);
        }
    }
