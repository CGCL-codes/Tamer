    public void deleteLockedMessage(URL messageUri, String lockId) throws AppFabricException {
        MessageBufferUtil msgBufferUtilObj = new MessageBufferUtil(solutionName, TokenConstants.getSimpleAuthAuthenticationType());
        String requestUri = msgBufferUtilObj.getRequestUri();
        String lockIdParameter = MessageBufferConstants.getLOCKIDPARAMETER();
        String authorizationToken = "";
        try {
            ACSTokenProvider tp = new ACSTokenProvider(httpWebProxyServer_, httpWebProxyPort_, this.credentials);
            String messageUriStr = messageUri.toString();
            authorizationToken = tp.getACSToken(requestUri, messageUriStr.replaceAll("https", "http"));
            String query = messageUriStr + "?" + lockIdParameter + "=" + lockId;
            URL urlConn = new URL(query);
            HttpURLConnection connection;
            if (httpWebProxy_ != null) connection = (HttpURLConnection) urlConn.openConnection(httpWebProxy_); else connection = (HttpURLConnection) urlConn.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-type", MessageBufferConstants.getCONTENT_TYPE_PROPERTY_FOR_ATOM_XML());
            String authStr = TokenConstants.getWrapAuthenticationType() + " " + TokenConstants.getWrapAuthorizationHeaderKey() + "=\"" + authorizationToken + "\"";
            connection.setRequestProperty("Authorization", authStr);
            if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logRequest(connection, SDKLoggerHelper.RecordType.DeleteLockedMessage_REQUEST);
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
                throw new AppFabricException("Messages could not be deleted.");
            }
            if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logMessage(URLEncoder.encode(responseCode, "UTF-8"), SDKLoggerHelper.RecordType.DeleteLockedMessage_RESPONSE);
        } catch (Exception e) {
            throw new AppFabricException(e.getMessage());
        }
    }
