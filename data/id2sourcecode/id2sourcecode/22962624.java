    private void procesSecure(final HttpServletRequest request, final HttpServletResponse response, String uniekId, URL url) throws IOException, ProtocolException {
        BufferedInputStream webToProxyBuf = null;
        BufferedOutputStream proxyToClientBuf = null;
        HttpsURLConnection cons = null;
        int statusCode = 0;
        int oneByte = 0;
        String methodName = null;
        MyVerifier myVerifier = null;
        BufferedWriter outputRequest = null;
        String requestString = request.toString();
        outputRequest = openOutputStream(uniekId, "-request.xml");
        writeToFile(outputRequest, requestString, uniekId);
        cons = (HttpsURLConnection) url.openConnection();
        methodName = request.getMethod();
        cons.setRequestMethod(methodName);
        cons.setDoOutput(true);
        cons.setDoInput(true);
        HttpsURLConnection.setFollowRedirects(false);
        cons.setUseCaches(true);
        if (timeout > 0) {
            cons.setConnectTimeout(timeout);
        }
        if (verifyHostName) {
            myVerifier = new MyVerifier();
            cons.setHostnameVerifier(myVerifier);
        }
        for (Enumeration e = request.getHeaderNames(); e.hasMoreElements(); ) {
            String headerName = e.nextElement().toString();
            String headerValue = request.getHeader(headerName);
            cons.setRequestProperty(headerName, headerValue);
            writeToFile(outputRequest, headerName + " : " + headerValue + "\n", uniekId);
        }
        cons.connect();
        if (methodName.equals("POST")) {
            BufferedInputStream clientToProxyBuf = new BufferedInputStream(request.getInputStream());
            BufferedOutputStream proxyToWebBuf = new BufferedOutputStream(cons.getOutputStream());
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
        statusCode = cons.getResponseCode();
        response.setStatus(statusCode);
        BufferedWriter outputResponse = openOutputStream(uniekId, "-response.xml");
        writeToFile(outputResponse, "Response-Statuscode: " + Integer.toString(statusCode) + "\nHTTP Headers:\n", uniekId);
        for (Iterator i = cons.getHeaderFields().entrySet().iterator(); i.hasNext(); ) {
            Map.Entry mapEntry = (Map.Entry) i.next();
            if (mapEntry.getKey() != null) {
                String key = mapEntry.getKey().toString();
                String value = ((List) mapEntry.getValue()).get(0).toString();
                writeToFile(outputResponse, key + " : " + value + "\n", uniekId);
                response.setHeader(key, value);
            }
        }
        webToProxyBuf = new BufferedInputStream(cons.getInputStream());
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
        cons.disconnect();
    }
