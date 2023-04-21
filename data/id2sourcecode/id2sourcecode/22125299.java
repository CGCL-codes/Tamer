    public BayesNet(URL url) throws IFException, IOException {
        this();
        InputStream istream = url.openStream();
        InterchangeFormat ifo = new InterchangeFormat(istream);
        ifo.CompilationUnit();
        translate(ifo);
        istream.close();
    }
