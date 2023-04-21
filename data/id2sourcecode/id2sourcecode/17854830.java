    private void setMaximumRetries() {
        Parameter maxRetriesParam = getParameter(TribesConstants.MAX_RETRIES);
        int maxRetries = 10;
        if (maxRetriesParam != null) {
            maxRetries = Integer.parseInt((String) maxRetriesParam.getValue());
        }
        ReplicationTransmitter replicationTransmitter = (ReplicationTransmitter) channel.getChannelSender();
        MultiPointSender multiPointSender = replicationTransmitter.getTransport();
        multiPointSender.setMaxRetryAttempts(maxRetries);
    }
