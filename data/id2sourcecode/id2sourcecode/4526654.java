    public String getAllChannels() {
        StringBuffer sb = new StringBuffer();
        for (int n = 0; n < scopes.length; n++) {
            sb.append(getChannel(n));
        }
        return sb.toString();
    }
