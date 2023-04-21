    private boolean sendLogToRemoteServer(final LoggingEvent event) throws java.io.IOException {
        boolean logMessageDispatched = false;
        ArrayList allRemoteAddresses = new ArrayList();
        allRemoteAddresses.addAll(this.remoteLoggingAddresses.getAddressList());
        allRemoteAddresses.addAll(this.serviceListenerSupport.getRemoteServiceAddresses());
        if (allRemoteAddresses.size() > 0) {
            StringBuffer queryString = new StringBuffer();
            queryString.append(LOG_DATE_KEY);
            queryString.append("=");
            queryString.append(UrlEncodingUtils.encode(dateFormat.format(new Date(event.timeStamp))));
            queryString.append("&");
            if (JServer.getJServer() != null) {
                queryString.append(SERVER_NAME_KEY);
                queryString.append("=");
                queryString.append(UrlEncodingUtils.encode(JServer.getJServer().getName()));
                queryString.append("&");
            }
            queryString.append(LOG_ORIGIN_KEY);
            queryString.append("=");
            queryString.append(UrlEncodingUtils.encode(event.getThreadName()));
            queryString.append("&");
            queryString.append(LOG_MESSAGE_KEY);
            queryString.append("=");
            queryString.append(UrlEncodingUtils.encode(super.getLayout().format(event)));
            String logMessageId = getLogMessageId(event);
            if (logMessageId != null) {
                queryString.append("&");
                queryString.append(LOG_MESSAGE_ID_KEY);
                queryString.append("=");
                queryString.append(logMessageId);
            }
            TcpEndPointIdentifier address;
            for (int i = 0; (!logMessageDispatched && (i < allRemoteAddresses.size())); i++) {
                address = (TcpEndPointIdentifier) allRemoteAddresses.get(i);
                try {
                    StringBuffer urlString = new StringBuffer();
                    urlString.append("http://");
                    urlString.append(address.getAddressAsString());
                    urlString.append("/CriticalErrorLogger?");
                    urlString.append(queryString.toString());
                    URL url = new URL(urlString.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    if (conn.getResponseCode() == 200) {
                        logMessageDispatched = true;
                    } else {
                        logError("Failed to dispatch critical error message to " + address + "! Response was: " + conn.getResponseCode() + " - " + conn.getResponseMessage() + ".");
                    }
                    try {
                        conn.disconnect();
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                    logError("Error while dispatching critical error message to " + address + "!", e);
                }
            }
            return logMessageDispatched;
        } else {
            return false;
        }
    }
