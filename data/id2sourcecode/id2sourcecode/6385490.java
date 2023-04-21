    private String getChannelTypeXML(String type, String docId) {
        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        if (type.equals(CHANNEL_MAIN)) {
            result += "<ace><channel type=\"" + type + "\"/></ace>";
        } else if (type.equals(CHANNEL_DISCOVERY)) {
            NetworkServiceImpl service = NetworkServiceImpl.getInstance();
            String userid = service.getUserId();
            String username = service.getUserDetails().getUsername();
            ServerInfo info = service.getServerInfo();
            String address = info.getAddress().getHostAddress();
            String port = Integer.toString(info.getPort());
            String user = "<user id=\"" + userid + "\" name=\"" + username + "\" address=\"" + address + "\" port=\"" + port + "\"/>";
            result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<ace><channel type=\"" + CHANNEL_MAIN + "\">" + user + "</channel></ace>";
        } else if (type.equals(CHANNEL_SESSION)) {
            String id = NetworkServiceImpl.getInstance().getUserId();
            result += "<ace><channel type=\"" + type + "\" docId=\"" + docId + "\" userid=\"" + id + "\"/></ace>";
        }
        return result;
    }
