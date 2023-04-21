    private void getChannels(DataOutputStream outToClient) throws IOException {
        String servers = String.valueOf(sockets.size()).concat("\r\n");
        for (int x = 0; x < sockets.size(); x++) {
            servers = servers.concat(sockets.get(x).getIP().concat(" `".concat(sockets.get(x).getChannelName().concat("`".concat(" `".concat(sockets.get(x).getMOTD().concat("`".concat(" `".concat(String.valueOf(sockets.get(x).getServerCount()).concat("`".concat("\r\n")))))))))));
        }
        outToClient.writeBytes(servers);
    }
