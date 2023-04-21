    public synchronized void store(String header) throws IOException {
        super.store(url.openConnection().getOutputStream(), header);
    }
