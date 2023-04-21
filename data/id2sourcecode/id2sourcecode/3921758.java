    public String retrieveSessionId(String adress) throws IOException {
        String jSessionId = "";
        URL url = new URL(adress);
        URLConnection connection = url.openConnection(proxy);
        jSessionId = connection.getHeaderField("Set-Cookie");
        jSessionId = jSessionId.substring(0, jSessionId.indexOf(";"));
        this.jSessionId = jSessionId;
        return jSessionId;
    }
