    public void releaseLock(URL lockUri) throws AppFabricException {
        MessageBufferUtil msgBufferUtilObj = new MessageBufferUtil(solutionName, TokenConstants.getSimpleAuthAuthenticationType());
        String requestUri = msgBufferUtilObj.getRequestUri();
        String authorizationToken = "";
        try {
            ACSTokenProvider tp = new ACSTokenProvider(httpWebProxyServer_, httpWebProxyPort_, this.credentials);
            String lockUriStr = lockUri.toString().replaceAll("https", "http");
            authorizationToken = tp.getACSToken(requestUri, lockUriStr);
            lockUriStr = lockUriStr.replaceAll("http", "https");
            URL urlConn = new URL(lockUriStr);
            HttpURLConnection connection;
            if (httpWebProxy_ != null) connection = (HttpURLConnection) urlConn.openConnection(httpWebProxy_); else connection = (HttpURLConnection) urlConn.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-type", MessageBufferConstants.getCONTENT_TYPE_PROPERTY_FOR_ATOM_XML());
            String authStr = TokenConstants.getWrapAuthenticationType() + " " + TokenConstants.getWrapAuthorizationHeaderKey() + "=\"" + authorizationToken + "\"";
            connection.setRequestProperty("Authorization", authStr);
            if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logRequest(connection, SDKLoggerHelper.RecordType.ReleaseLock_REQUEST);
            String responseCode = "<responseCode>" + connection.getResponseCode() + "</responseCode>";
            if ((connection.getResponseCode() == MessageBufferConstants.HTTP_STATUS_CODE_OK)) {
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
            } else {
                throw new AppFabricException("Lock could not be released.Error.... Response code:  " + connection.getResponseCode());
            }
            if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logMessage(URLEncoder.encode(responseCode, "UTF-8"), SDKLoggerHelper.RecordType.ReleaseLock_RESOPONSE);
        } catch (Exception e) {
            throw new AppFabricException(e.getMessage());
        }
    }
