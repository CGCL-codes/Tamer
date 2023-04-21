    public BayesNet(URL context, String spec) throws IFException, IOException {
        this();
        URL url = new URL(context, spec);
        InputStream istream = url.openStream();
        InterchangeFormat ifo = new InterchangeFormat(istream);
        ifo.CompilationUnit();
        translate(ifo);
        istream.close();
    }
