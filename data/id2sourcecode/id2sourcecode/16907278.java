    private void doSend(Message msg) throws IOException {
        long beginConnectTime = 0;
        long connectTime = 0;
        if (TransportMeterBuildSettings.TRANSPORT_METERING) {
            beginConnectTime = TimeUtils.timeNow();
        }
        WireFormatMessage serialed = WireFormatMessageFactory.toWire(msg, EndpointServiceImpl.DEFAULT_MESSAGE_TYPE, null);
        for (int connectAttempt = 1; connectAttempt <= CONNECT_RETRIES; connectAttempt++) {
            if (connectAttempt > 1) {
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Retrying connection to " + senderURL);
                }
            }
            HttpURLConnection urlConn = (HttpURLConnection) senderURL.openConnection();
            try {
                urlConn.setRequestMethod("POST");
                urlConn.setDoOutput(true);
                urlConn.setDoInput(true);
                urlConn.setAllowUserInteraction(false);
                urlConn.setUseCaches(false);
                urlConn.setConnectTimeout(CONNECT_TIMEOUT);
                urlConn.setReadTimeout(CONNECT_TIMEOUT);
                urlConn.setRequestProperty("content-length", Long.toString(serialed.getByteLength()));
                urlConn.setRequestProperty("content-type", serialed.getMimeType().toString());
                OutputStream out = urlConn.getOutputStream();
                if (TransportMeterBuildSettings.TRANSPORT_METERING && (transportBindingMeter != null)) {
                    connectTime = TimeUtils.timeNow();
                    transportBindingMeter.connectionEstablished(true, connectTime - beginConnectTime);
                }
                serialed.sendToStream(out);
                out.flush();
                int responseCode;
                try {
                    responseCode = urlConn.getResponseCode();
                } catch (SocketTimeoutException expired) {
                    continue;
                } catch (IOException ioe) {
                    if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                        LOG.fine("HTTP 1.0 proxy seems in use");
                    }
                    continue;
                }
                if (responseCode == -1) {
                    if (neverWarned && Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                        LOG.warning("Obsolete HTTP proxy does not issue HTTP_OK response. Assuming OK");
                        neverWarned = false;
                    }
                    responseCode = HttpURLConnection.HTTP_OK;
                }
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    if (TransportMeterBuildSettings.TRANSPORT_METERING && (transportBindingMeter != null)) {
                        transportBindingMeter.dataSent(true, serialed.getByteLength());
                        transportBindingMeter.connectionDropped(true, TimeUtils.timeNow() - beginConnectTime);
                    }
                    throw new IOException("Message not accepted: HTTP status " + "code=" + responseCode + " reason=" + urlConn.getResponseMessage());
                }
                if (TransportMeterBuildSettings.TRANSPORT_METERING && (transportBindingMeter != null)) {
                    long messageSentTime = TimeUtils.timeNow();
                    transportBindingMeter.messageSent(true, msg, messageSentTime - connectTime, serialed.getByteLength());
                    transportBindingMeter.connectionClosed(true, messageSentTime - beginConnectTime);
                }
                lastUsed = TimeUtils.timeNow();
                return;
            } finally {
                urlConn.disconnect();
            }
        }
        throw new IOException("Failed sending " + msg + " to " + senderURL);
    }
