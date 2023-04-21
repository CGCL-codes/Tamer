    public LockedMessageInfo peekLock(String messageBufferName, String timeout, String lockDuration) throws AppFabricException {
        MessageBufferUtil msgBufferUtilObj = new MessageBufferUtil(solutionName, TokenConstants.getSimpleAuthAuthenticationType());
        String requestUri = msgBufferUtilObj.getRequestUri();
        String sendPath = MessageBufferConstants.getPATH_FOR_RETRIEVE_MESSAGE();
        String timeOutParameter = MessageBufferConstants.getTIMEOUTPARAMETER();
        String lockDurationParameter = MessageBufferConstants.getLOCKDURATIONPARAMETER();
        String messageBufferUri = msgBufferUtilObj.getMessageUri(messageBufferName, sendPath);
        String authorizationToken = "";
        try {
            ACSTokenProvider tp = new ACSTokenProvider(httpWebProxyServer_, httpWebProxyPort_, this.credentials);
            authorizationToken = tp.getACSToken(requestUri, messageBufferUri);
            String tempMessageBufferUri = messageBufferUri;
            tempMessageBufferUri = tempMessageBufferUri.replaceAll("http", "https");
            String retrieveUrl = tempMessageBufferUri + "?" + timeOutParameter + "=" + timeout + "&" + lockDurationParameter + "=" + lockDuration;
            URL urlConn = new URL(retrieveUrl);
            HttpURLConnection connection;
            if (httpWebProxy_ != null) connection = (HttpURLConnection) urlConn.openConnection(httpWebProxy_); else connection = (HttpURLConnection) urlConn.openConnection();
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-type", MessageBufferConstants.getCONTENT_TYPE_PROPERTY_FOR_TEXT());
            connection.setRequestProperty("Expect", "100-continue");
            connection.setRequestProperty("Accept", "*/*");
            String authStr = TokenConstants.getWrapAuthenticationType() + " " + TokenConstants.getWrapAuthorizationHeaderKey() + "=\"" + authorizationToken + "\"";
            connection.setRequestProperty("Authorization", authStr);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.flush();
            wr.close();
            if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logRequest(connection, SDKLoggerHelper.RecordType.PeekLock_REQUEST);
            String responseCode = "<responseCode>" + connection.getResponseCode() + "</responseCode>";
            if ((connection.getResponseCode() == MessageBufferConstants.HTTP_STATUS_CODE_ACCEPTED) || (connection.getResponseCode() == MessageBufferConstants.HTTP_STATUS_CODE_CREATED) || (connection.getResponseCode() == MessageBufferConstants.HTTP_STATUS_CODE_OK)) {
                String uriString = connection.getHeaderField("X-MS-MESSAGE-LOCATION");
                if (uriString == null) {
                    throw new AppFabricException("Unable to retrieve 'X-MS-MESSAGE-LOCATION' header from message.");
                }
                URL messageUri = new URL(uriString);
                String lockId = connection.getHeaderField("X-MS-LOCK-ID");
                if (lockId == null) {
                    throw new AppFabricException("Unable to retrieve 'X-MS-LOCK-ID' header from message.");
                }
                String lockLocation = connection.getHeaderField("X-MS-LOCK-LOCATION");
                if (lockLocation == null) {
                    throw new AppFabricException("Unable to retrieve 'X-MS-LOCK-LOCATION' header from message.");
                }
                if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logMessage(responseCode, SDKLoggerHelper.RecordType.PeekLock_REQUEST);
                return new LockedMessageInfo(lockId, new URL(lockLocation), messageUri);
            } else if (connection.getResponseCode() == MessageBufferConstants.HTTP_STATUS_CODE_NORESPONSE) {
                if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logMessage(URLEncoder.encode(responseCode, "UTF-8"), SDKLoggerHelper.RecordType.PeekLock_REQUEST);
                throw new AppFabricException("No content");
            } else {
                if (LoggerUtil.getIsLoggingOn()) SDKLoggerHelper.logMessage(URLEncoder.encode(responseCode, "UTF-8"), SDKLoggerHelper.RecordType.PeekLock_REQUEST);
                throw new AppFabricException("Message could not be PeekLocked. Error .... Response code: " + connection.getResponseCode());
            }
        } catch (Exception e) {
            throw new AppFabricException(e.getMessage());
        }
    }
