    public String retrieveMessage(String messageBufferName, String timeout) throws AppFabricException {
        MessageBufferUtil msgBufferUtilObj = new MessageBufferUtil(solutionName, TokenConstants.getSimpleAuthAuthenticationType());
        String requestUri = msgBufferUtilObj.getRequestUri();
        String sendPath = MessageBufferConstants.getPATH_FOR_RETRIEVE_MESSAGE();
        String timeOutParameter = MessageBufferConstants.getTIMEOUTPARAMETER();
        String messageBufferUri = msgBufferUtilObj.getMessageUri(messageBufferName, sendPath);
        String authorizationToken = "";
        try {
            ACSTokenProvider tp = new ACSTokenProvider(httpWebProxyServer_, httpWebProxyPort_, this.credentials);
            authorizationToken = tp.getACSToken(requestUri, messageBufferUri);
            messageBufferUri = messageBufferUri.replaceAll("http", "https");
            String retrieveUrl = messageBufferUri + "?" + timeOutParameter + "=" + timeout;
            URL urlConn = new URL(retrieveUrl);
            HttpURLConnection connection;
            if (httpWebProxy_ != null) connection = (HttpURLConnection) urlConn.openConnection(httpWebProxy_); else connection = (HttpURLConnection) urlConn.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", MessageBufferConstants.getCONTENT_TYPE_PROPERTY_FOR_TEXT());
            String authStr = TokenConstants.getWrapAuthenticationType() + " " + TokenConstants.getWrapAuthorizationHeaderKey() + "=\"" + authorizationToken + "\"";
            connection.setRequestProperty("Authorization", authStr);
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logRequest(connection, SDKLoggerHelper.RecordType.GetMessage_REQUEST);
            connection.connect();
            String responseCode = "<responseCode>" + connection.getResponseCode() + "</responseCode>";
            if (connection.getResponseCode() == MessageBufferConstants.HTTP_STATUS_CODE_OK) {
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                if (LoggerUtil.getIsLoggingOn()) {
                    StringBuilder responseXML = new StringBuilder();
                    responseXML.append(responseCode);
                    responseXML.append(response.toString());
                    SDKLoggerHelper.logMessage(URLEncoder.encode(responseXML.toString(), "UTF-8"), SDKLoggerHelper.RecordType.GetMessage_RESPONSE);
                }
                return response.toString();
            } else if (connection.getResponseCode() == MessageBufferConstants.HTTP_STATUS_CODE_NORESPONSE) {
                if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logMessage(URLEncoder.encode(responseCode, "UTF-8"), SDKLoggerHelper.RecordType.GetMessage_RESPONSE);
                throw new AppFabricException("No content");
            } else {
                if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logMessage(URLEncoder.encode(responseCode, "UTF-8"), SDKLoggerHelper.RecordType.GetMessage_RESPONSE);
                throw new AppFabricException("Message could not be retrieved . Response code: " + connection.getResponseCode());
            }
        } catch (Exception e) {
            throw new AppFabricException(e.getMessage());
        }
    }
