    public void saveTo(URL url) throws IOException {
        OutputStream stream = url.openConnection().getOutputStream();
        store(stream, "connection dictionary");
    }
