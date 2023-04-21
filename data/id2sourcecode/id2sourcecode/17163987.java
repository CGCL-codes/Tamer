    private void handleStreamCall(ServiceCall serviceCall, UOSMessageContext messageContext) throws NetworkException, IOException {
        if (serviceCall.getServiceType().equals(ServiceType.STREAM)) {
            NetworkDevice networkDevice = messageContext.getCallerDevice();
            String host = connectionManagerControlCenter.getHost(networkDevice.getNetworkDeviceName());
            for (int i = 0; i < serviceCall.getChannels(); i++) {
                ClientConnection con = connectionManagerControlCenter.openActiveConnection(host + ":" + serviceCall.getChannelIDs()[i], serviceCall.getChannelType());
                messageContext.addDataStreams(con.getDataInputStream(), con.getDataOutputStream());
            }
        }
    }
