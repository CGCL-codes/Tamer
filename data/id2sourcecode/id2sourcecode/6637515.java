    public String getChannelName() {
        String encodedAddr = "";
        if (inet != null && !"0.0.0.0".equals(inet.getHostAddress())) {
            encodedAddr = getAddress();
            if (encodedAddr.startsWith("/")) encodedAddr = encodedAddr.substring(1);
            encodedAddr = URLEncoder.encode(encodedAddr) + "-";
        }
        return ("jk-" + encodedAddr + port);
    }
