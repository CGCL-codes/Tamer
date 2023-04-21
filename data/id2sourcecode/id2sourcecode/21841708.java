    public static ByteBuffer decode(PDFObject dict, ByteBuffer buf, PDFObject params) throws IOException {
        Inflater inf = new Inflater(false);
        int bufSize = buf.remaining();
        byte[] data = new byte[bufSize];
        buf.get(data);
        inf.setInput(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] decomp = new byte[bufSize];
        int loc = 0;
        int read = 0;
        try {
            while (!inf.finished()) {
                read = inf.inflate(decomp);
                if (read <= 0) {
                    if (inf.needsDictionary()) {
                        throw new PDFParseException("Don't know how to ask for a dictionary in FlateDecode");
                    } else {
                        return ByteBuffer.allocate(0);
                    }
                }
                baos.write(decomp, 0, read);
            }
        } catch (DataFormatException dfe) {
            throw new PDFParseException("Data format exception:" + dfe.getMessage());
        }
        ByteBuffer outBytes = ByteBuffer.wrap(baos.toByteArray());
        if (params != null && params.getDictionary().containsKey("Predictor")) {
            Predictor predictor = Predictor.getPredictor(params);
            if (predictor != null) {
                outBytes = predictor.unpredict(outBytes);
            }
        }
        return outBytes;
    }
