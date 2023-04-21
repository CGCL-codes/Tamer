    private void init(InputStream _in, IOType _inType, OutputStream _out, IOType _outType) throws IOException {
        inType = _inType;
        if (inType == null) {
            throw new IOException("Input type not defined.");
        }
        outType = _outType;
        if (outType == null) {
            throw new IOException("Output type not defined.");
        }
        try {
            in = _in;
            loader = JOEFileFormat.getMolReader(in, inType);
            out = _out;
            writer = JOEFileFormat.getMolWriter(out, outType);
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
        mol = new JOEMol(inType, outType);
    }
