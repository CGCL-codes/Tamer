    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if ("CONNECT".equalsIgnoreCase(request.getMethod())) {
            handleConnect(request, response);
        } else {
            String uri = request.getRequestURI();
            if (request.getQueryString() != null) {
                uri += "?" + request.getQueryString();
            }
            URL url = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), uri);
            context.log("URL=" + url);
            context.log("  method: " + request.getMethod());
            if ("1.1 (jetty)".equals(request.getHeader("Via"))) {
                throw new FileNotFoundException("Can't proxy local url: " + url);
            }
            if (handleJnlpCacheFile(request, response)) {
                return;
            }
            URLConnection remoteConn = url.openConnection();
            remoteConn.setAllowUserInteraction(false);
            HttpURLConnection remoteHttpConn = null;
            if (remoteConn instanceof HttpURLConnection) {
                remoteHttpConn = (HttpURLConnection) remoteConn;
                remoteHttpConn.setRequestMethod(request.getMethod());
                remoteHttpConn.setInstanceFollowRedirects(false);
            }
            String reqHdrConnection = request.getHeader("Connection");
            if (reqHdrConnection != null) {
                reqHdrConnection = reqHdrConnection.toLowerCase();
                if (reqHdrConnection.equals("keep-alive") || reqHdrConnection.equals("close")) {
                    reqHdrConnection = null;
                }
            }
            boolean xForwardedFor = false;
            boolean hasContent = false;
            Enumeration reqHdrNames = request.getHeaderNames();
            while (reqHdrNames.hasMoreElements()) {
                String reqHdrName = (String) reqHdrNames.nextElement();
                String lReqHdrName = reqHdrName.toLowerCase();
                String reqHdrStrVal = request.getHeader(reqHdrName);
                context.log("   " + reqHdrName + ": " + reqHdrStrVal);
                if (_DontProxyHeaders.contains(lReqHdrName)) {
                    continue;
                }
                if (reqHdrConnection != null && reqHdrConnection.indexOf(lReqHdrName) >= 0) {
                    continue;
                }
                if ("x-forwarded-for".equals(lReqHdrName)) {
                    xForwardedFor = true;
                }
                if ("content-type".equals(lReqHdrName)) {
                    hasContent = true;
                }
                Enumeration reqHdrVals = request.getHeaders(reqHdrName);
                while (reqHdrVals.hasMoreElements()) {
                    String reqHdrVal = (String) reqHdrVals.nextElement();
                    if (reqHdrVal != null) {
                        remoteConn.addRequestProperty(reqHdrName, reqHdrVal);
                    }
                }
            }
            if (!"POST".equalsIgnoreCase(request.getMethod())) {
                hasContent = false;
            }
            remoteConn.setRequestProperty("Via", "1.1 (jetty)");
            if (!xForwardedFor) {
                remoteConn.addRequestProperty("X-Forwarded-For", request.getRemoteAddr());
            }
            String cache_control = request.getHeader("Cache-Control");
            if (cache_control != null && (cache_control.indexOf("no-cache") >= 0 || cache_control.indexOf("no-store") >= 0)) remoteConn.setUseCaches(false);
            try {
                if (hasContent) {
                    remoteConn.setDoInput(true);
                    InputStream requestIn = request.getInputStream();
                    remoteConn.setDoOutput(true);
                    IO.copy(requestIn, remoteConn.getOutputStream());
                }
                remoteConn.connect();
            } catch (Exception e) {
                context.log("proxy", e);
            }
            InputStream remoteIn = null;
            int code = 500;
            if (remoteHttpConn != null) {
                remoteIn = remoteHttpConn.getErrorStream();
                code = remoteHttpConn.getResponseCode();
                response.setStatus(code, remoteHttpConn.getResponseMessage());
                context.log("response = " + remoteHttpConn.getResponseCode());
            }
            if (remoteIn == null) {
                try {
                    remoteIn = remoteConn.getInputStream();
                } catch (Exception e) {
                    context.log("stream", e);
                    remoteIn = remoteHttpConn.getErrorStream();
                }
            }
            response.setHeader("Date", null);
            response.setHeader("Server", null);
            context.log("response -----------");
            int remoteHdrIndex = 0;
            String remoteHdrName = remoteConn.getHeaderFieldKey(remoteHdrIndex);
            String val = remoteConn.getHeaderField(remoteHdrIndex);
            while (remoteHdrName != null || val != null) {
                String lhdr = remoteHdrName != null ? remoteHdrName.toLowerCase() : null;
                if (remoteHdrName != null && val != null && !_DontProxyHeaders.contains(lhdr)) response.addHeader(remoteHdrName, val);
                context.log("  " + remoteHdrName + ": " + val);
                remoteHdrIndex++;
                remoteHdrName = remoteConn.getHeaderFieldKey(remoteHdrIndex);
                val = remoteConn.getHeaderField(remoteHdrIndex);
            }
            response.addHeader("Via", "1.1 (jetty)");
            if (remoteIn != null) IO.copy(remoteIn, response.getOutputStream());
        }
    }
