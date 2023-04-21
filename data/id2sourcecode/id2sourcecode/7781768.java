    public void selectAudio(Audio audio) throws IOException {
        String location = "/cgi-bin/setAudio?channel=" + audio.getChannel() + "&language=" + audio.getPid();
        URLConnection urlConn = getConnection(location);
        Utils.validateResponse(urlConn);
    }
