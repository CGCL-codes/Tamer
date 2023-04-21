    private void procesNonSecure(final HttpServletRequest request, final HttpServletResponse response, String uniekId, URL url) throws IOException, ProtocolException {
        BufferedInputStream webToProxyBuf;
        BufferedOutputStream proxyToClientBuf;
        HttpURLConnection con;
        int statusCode;
        int oneByte;
        String methodName;
        BufferedWriter outputRequest = null;
        String requestString = request.toString();
        outputRequest = openOutputStream(uniekId, "-request.xml");
        writeToFile(outputRequest, requestString, uniekId);
        con = (HttpURLConnection) url.openConnection();
        methodName = request.getMethod();
        con.setRequestMethod(methodName);
        con.setDoOutput(true);
        con.setDoInput(true);
        HttpURLConnection.setFollowRedirects(false);
        con.setUseCaches(true);
        if (timeout > 0) {
            con.setConnectTimeout(timeout);
        }
        for (Enumeration e = request.getHeaderNames(); e.hasMoreElements(); ) {
            String headerName = e.nextElement().toString();
            String headerValue = request.getHeader(headerName);
            con.setRequestProperty(headerName, headerValue);
            writeToFile(outputRequest, headerName + " : " + headerValue + "\n", uniekId);
        }
        con.connect();
        if (methodName.equals("POST")) {
            BufferedInputStream clientToProxyBuf = new BufferedInputStream(request.getInputStream());
            BufferedOutputStream proxyToWebBuf = new BufferedOutputStream(con.getOutputStream());
            writeToFile(outputRequest, "ProxyToWebBuf: \n", uniekId);
            while ((oneByte = clientToProxyBuf.read()) != -1) {
                proxyToWebBuf.write(oneByte);
                outputRequest.write(oneByte);
            }
            proxyToWebBuf.flush();
            outputRequest.flush();
            proxyToWebBuf.close();
            clientToProxyBuf.close();
        }
        closeStream(outputRequest, uniekId);
        statusCode = con.getResponseCode();
        response.setStatus(statusCode);
        BufferedWriter outputResponse = openOutputStream(uniekId, "-response.xml");
        writeToFile(outputResponse, "Response-Statuscode: " + Integer.toString(statusCode) + "\nHTTP Headers:\n", uniekId);
        for (Iterator i = con.getHeaderFields().entrySet().iterator(); i.hasNext(); ) {
            Map.Entry mapEntry = (Map.Entry) i.next();
            if (mapEntry.getKey() != null) {
                String key = mapEntry.getKey().toString();
                String value = ((List) mapEntry.getValue()).get(0).toString();
                writeToFile(outputResponse, key + " : " + value + "\n", uniekId);
                response.setHeader(key, value);
            }
        }
        webToProxyBuf = new BufferedInputStream(con.getInputStream());
        proxyToClientBuf = new BufferedOutputStream(response.getOutputStream());
        writeToFile(outputResponse, "Response:  \n", uniekId);
        while ((oneByte = webToProxyBuf.read()) != -1) {
            proxyToClientBuf.write(oneByte);
            outputResponse.write(oneByte);
        }
        proxyToClientBuf.flush();
        outputResponse.flush();
        closeStream(outputResponse, uniekId);
        proxyToClientBuf.close();
        webToProxyBuf.close();
        con.disconnect();
    }
