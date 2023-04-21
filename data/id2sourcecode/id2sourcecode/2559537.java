    private URLConnection getConnection(String location) throws IOException {
        URL url = new URL(getBase() + location);
        URLConnection uc = url.openConnection();
        uc.setConnectTimeout(3000);
        return uc;
    }
