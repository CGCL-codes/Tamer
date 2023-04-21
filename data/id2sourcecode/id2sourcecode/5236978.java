    public Obj(URL url) throws EOFException, IOException {
        this();
        this.readMesh(new InputStreamReader(url.openStream()));
    }
