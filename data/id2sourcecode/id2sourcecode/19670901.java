    public void receivePush(PushMessage pushMsg) {
        LOG.debug("JTellaAdapter: Recieved PUSH message from: " + pushMsg.getIPAddress() + ":" + pushMsg.getPort() + " Url Prefix: " + pushMsg.getUrlPrefix());
        String localIp;
        try {
            localIp = InetAddress.getByName(pushMsg.getOriginatingConnection().getPublicIP()).getHostAddress();
        } catch (UnknownHostException e) {
            localIp = pushMsg.getOriginatingConnection().getPublicIP();
        }
        String urlString = "http://" + pushMsg.getIPAddress() + ":" + pushMsg.getPort() + "/" + pushMsg.getUrlPrefix() + "/push?" + HttpParams.UP2P_PEERID + "=" + localIp + ":" + adapter.getPort() + "/" + getUrlPrefix();
        LOG.debug("JTellaAdapter: Initiating HTTP connection to: " + urlString);
        HttpURLConnection pushConn = null;
        try {
            URL url = new URL(urlString);
            boolean pushComplete = false;
            while (!pushComplete) {
                pushConn = (HttpURLConnection) url.openConnection();
                pushConn.setDoInput(true);
                pushConn.setDoOutput(true);
                pushConn.setUseCaches(false);
                pushConn.setRequestMethod("GET");
                pushConn.setRequestProperty("Connection", "Keep-Alive");
                pushConn.setRequestProperty("User-Agent", "UP2P");
                pushConn.setRequestProperty("Accept", "[star]/[star]");
                BufferedReader inStream = new BufferedReader(new InputStreamReader(pushConn.getInputStream()));
                String serverResponse = inStream.readLine();
                inStream.close();
                LOG.debug("JTellaAdapter: Received from PUSH servlet: " + serverResponse);
                if (serverResponse.startsWith("OK")) {
                    LOG.debug("JTellaAdapter: PUSH transfers complete.");
                    pushComplete = true;
                } else if (serverResponse.startsWith("GIV")) {
                    serverResponse = serverResponse.substring(serverResponse.indexOf(" ") + 1);
                    String[] splitResponse = serverResponse.split("/");
                    LOG.debug("JTellaAdapter: Got PUSH request for ComId: " + splitResponse[0] + "   ResId: " + splitResponse[1]);
                    try {
                        List<String> filePathList = adapter.lookupFilepaths(splitResponse[0], splitResponse[1]);
                        String resourceFilePath = filePathList.remove(0);
                        pushResource(pushMsg.getIPAddress() + ":" + pushMsg.getPort() + "/" + pushMsg.getUrlPrefix(), splitResponse[0], resourceFilePath, filePathList);
                    } catch (ResourceNotFoundException e) {
                        LOG.error("JTellaAdapter: Could not find resource specified by PUSH message: " + splitResponse[0] + "/" + splitResponse[1]);
                    }
                }
                pushConn.disconnect();
            }
        } catch (IOException e) {
            LOG.error("JTellaAdapter: PUSH file transfer failed: " + e.getMessage());
            if (!this.relayPeerUrl.equals("")) {
                issueRelayMessage(pushMsg);
            }
        }
    }
