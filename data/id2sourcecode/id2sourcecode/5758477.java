    public void handle(String pathInContext, String pathParams, HttpRequest request, HttpResponse response) throws HttpException, IOException {
        URI uri = request.getURI();
        if (HttpRequest.__CONNECT.equalsIgnoreCase(request.getMethod())) {
            response.setField(HttpFields.__Connection, "close");
            handleConnect(pathInContext, pathParams, request, response);
            return;
        }
        try {
            URL url = isProxied(uri);
            if (url == null) {
                if (isForbidden(uri)) sendForbid(request, response, uri);
                return;
            }
            if (log.isDebugEnabled()) log.debug("PROXY URL=" + url);
            URLConnection connection = url.openConnection();
            connection.setAllowUserInteraction(false);
            HttpURLConnection http = null;
            if (connection instanceof HttpURLConnection) {
                http = (HttpURLConnection) connection;
                http.setRequestMethod(request.getMethod());
                http.setInstanceFollowRedirects(false);
            }
            String connectionHdr = request.getField(HttpFields.__Connection);
            if (connectionHdr != null && (connectionHdr.equalsIgnoreCase(HttpFields.__KeepAlive) || connectionHdr.equalsIgnoreCase(HttpFields.__Close))) connectionHdr = null;
            boolean xForwardedFor = false;
            boolean hasContent = false;
            Enumeration enm = request.getFieldNames();
            while (enm.hasMoreElements()) {
                String hdr = (String) enm.nextElement();
                if (_DontProxyHeaders.containsKey(hdr) || !_chained && _ProxyAuthHeaders.containsKey(hdr)) continue;
                if (connectionHdr != null && connectionHdr.indexOf(hdr) >= 0) continue;
                if (HttpFields.__ContentType.equals(hdr)) hasContent = true;
                Enumeration vals = request.getFieldValues(hdr);
                while (vals.hasMoreElements()) {
                    String val = (String) vals.nextElement();
                    if (val != null) {
                        connection.addRequestProperty(hdr, val);
                        xForwardedFor |= HttpFields.__XForwardedFor.equalsIgnoreCase(hdr);
                    }
                }
            }
            if (!_anonymous) connection.setRequestProperty("Via", "1.1 (jetty)");
            if (!xForwardedFor) connection.addRequestProperty(HttpFields.__XForwardedFor, request.getRemoteAddr());
            String cache_control = request.getField(HttpFields.__CacheControl);
            if (cache_control != null && (cache_control.indexOf("no-cache") >= 0 || cache_control.indexOf("no-store") >= 0)) connection.setUseCaches(false);
            customizeConnection(pathInContext, pathParams, request, connection);
            try {
                connection.setDoInput(true);
                InputStream in = request.getInputStream();
                if (hasContent) {
                    connection.setDoOutput(true);
                    IO.copy(in, connection.getOutputStream());
                }
                connection.connect();
            } catch (Exception e) {
                LogSupport.ignore(log, e);
            }
            InputStream proxy_in = null;
            int code = HttpResponse.__500_Internal_Server_Error;
            if (http != null) {
                proxy_in = http.getErrorStream();
                code = http.getResponseCode();
                response.setStatus(code);
                response.setReason(http.getResponseMessage());
            }
            if (proxy_in == null) {
                try {
                    proxy_in = connection.getInputStream();
                } catch (Exception e) {
                    LogSupport.ignore(log, e);
                    proxy_in = http.getErrorStream();
                }
            }
            response.removeField(HttpFields.__Date);
            response.removeField(HttpFields.__Server);
            int h = 0;
            String hdr = connection.getHeaderFieldKey(h);
            String val = connection.getHeaderField(h);
            while (hdr != null || val != null) {
                if (hdr != null && val != null && !_DontProxyHeaders.containsKey(hdr) && (_chained || !_ProxyAuthHeaders.containsKey(hdr))) response.addField(hdr, val);
                h++;
                hdr = connection.getHeaderFieldKey(h);
                val = connection.getHeaderField(h);
            }
            if (!_anonymous) response.setField("Via", "1.1 (jetty)");
            request.setHandled(true);
            if (proxy_in != null) IO.copy(proxy_in, response.getOutputStream());
        } catch (Exception e) {
            log.warn(e.toString());
            LogSupport.ignore(log, e);
            if (!response.isCommitted()) response.sendError(HttpResponse.__400_Bad_Request);
        }
    }
