    public void init(InputStream inStream, IOType inType, OutputStream outStream, IOType outType) throws IOException {
        this.inType = inType;
        if (inType == null) {
            throw new IOException("Input type not defined.");
        }
        this.outType = outType;
        if (outType == null) {
            throw new IOException("Output type not defined.");
        }
        try {
            in = inStream;
            loader = MoleculeFileHelper.getMolReader(in, inType);
            out = outStream;
            writer = MoleculeFileHelper.getMolWriter(out, outType);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException("Can not get molecule reader/writer pipe instance.");
        }
        if (!loader.readable()) {
            throw new IOException(inType.getRepresentation() + " is not readable.");
        }
        if (!writer.writeable()) {
            throw new IOException(outType.getRepresentation() + " is not writeable.");
        }
        watch = new StopWatch();
        molCounterLoaded = 0;
        molCounterWritten = 0;
        mol = new BasicConformerMolecule(inType, outType);
    }
