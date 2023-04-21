    private JarRepository(Path path) throws IOException {
        super(path);
        this.url = new URL(path.toURLString());
        this.connection = (JarURLConnection) url.openConnection();
        this.ns = Names.getNamespace(path);
        this.idNumber = 0;
    }
