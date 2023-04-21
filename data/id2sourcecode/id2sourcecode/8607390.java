    public ServerConnexion(String adr) throws Exception {
        url = new URL(adr);
        c = url.openConnection();
    }
