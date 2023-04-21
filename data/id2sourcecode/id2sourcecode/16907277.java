    private EndpointAddress retreiveLogicalDestinationAddress() throws IOException {
        long beginConnectTime = 0;
        long connectTime = 0;
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("Ping (" + senderURL + ")");
        }
        if (TransportMeterBuildSettings.TRANSPORT_METERING) {
            beginConnectTime = TimeUtils.timeNow();
        }
        HttpURLConnection urlConn = (HttpURLConnection) senderURL.openConnection();
        urlConn.setRequestMethod("GET");
        urlConn.setDoOutput(true);
        urlConn.setDoInput(true);
        urlConn.setAllowUserInteraction(false);
        urlConn.setUseCaches(false);
        urlConn.setConnectTimeout(CONNECT_TIMEOUT);
        urlConn.setReadTimeout(CONNECT_TIMEOUT);
        try {
            int code = urlConn.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                if (TransportMeterBuildSettings.TRANSPORT_METERING) {
                    transportBindingMeter = servletHttpTransport.getTransportBindingMeter(null, getDestinationAddress());
                    if (transportBindingMeter != null) {
                        transportBindingMeter.connectionFailed(true, TimeUtils.timeNow() - beginConnectTime);
                    }
                }
                throw new IOException("Message not accepted: HTTP status " + "code=" + code + " reason=" + urlConn.getResponseMessage());
            }
            int msglength = urlConn.getContentLength();
            if (msglength <= 0) {
                throw new IOException("Ping response was empty.");
            }
            InputStream inputStream = urlConn.getInputStream();
            byte[] uniqueIdBytes = new byte[msglength];
            int bytesRead = 0;
            while (bytesRead < msglength) {
                int thisRead = inputStream.read(uniqueIdBytes, bytesRead, msglength - bytesRead);
                if (thisRead < 0) {
                    break;
                }
                bytesRead += thisRead;
            }
            if (bytesRead < msglength) {
                throw new IOException("Content ended before promised Content length");
            }
            String uniqueIdString;
            try {
                uniqueIdString = new String(uniqueIdBytes, "UTF-8");
            } catch (UnsupportedEncodingException never) {
                uniqueIdString = new String(uniqueIdBytes);
            }
            if (TransportMeterBuildSettings.TRANSPORT_METERING) {
                connectTime = TimeUtils.timeNow();
                transportBindingMeter = servletHttpTransport.getTransportBindingMeter(uniqueIdString, getDestinationAddress());
                if (transportBindingMeter != null) {
                    transportBindingMeter.connectionEstablished(true, connectTime - beginConnectTime);
                    transportBindingMeter.ping(connectTime);
                    transportBindingMeter.connectionClosed(true, connectTime - beginConnectTime);
                }
            }
            EndpointAddress remoteAddress = new EndpointAddress("jxta", uniqueIdString.trim(), null, null);
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("Ping (" + senderURL + ") -> " + remoteAddress);
            }
            return remoteAddress;
        } catch (IOException failure) {
            if (TransportMeterBuildSettings.TRANSPORT_METERING) {
                connectTime = TimeUtils.timeNow();
                transportBindingMeter = servletHttpTransport.getTransportBindingMeter(null, getDestinationAddress());
                if (transportBindingMeter != null) {
                    transportBindingMeter.connectionFailed(true, connectTime - beginConnectTime);
                }
            }
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Ping (" + senderURL + ") -> failed");
            }
            throw failure;
        }
    }
