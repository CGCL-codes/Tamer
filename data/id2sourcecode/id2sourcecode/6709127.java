    private String getChannelString() throws IOException {
        String channels = String.valueOf(System.currentTimeMillis()).concat("\n".concat(String.valueOf(sockets.size())).concat("\n"));
        for (int x = 0; x < sockets.size(); x++) {
            channels = channels.concat(sockets.get(x).getIP().concat(" `".concat(sockets.get(x).getChannelName().concat("`".concat(" `".concat(sockets.get(x).getMOTD().concat("`".concat(" `".concat(String.valueOf(sockets.get(x).getServerCount()).concat("`".concat("\n")))))))))));
        }
        return channels;
    }
