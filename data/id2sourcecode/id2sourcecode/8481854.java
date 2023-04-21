    private DiffPart decodeAdd(final int blockSize_S, final int blockSize_L) throws UnsupportedEncodingException, DecodingException {
        if (blockSize_S < 1 || blockSize_L < 1) {
            throw new DecodingException("Invalid value for blockSize_S: " + blockSize_S + " or blockSize_L: " + blockSize_L);
        }
        int s = r.read(blockSize_S);
        int l = r.read(blockSize_L);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (int i = 0; i < l; i++) {
            output.write(r.readByte());
        }
        DiffPart part = new DiffPart(DiffAction.INSERT);
        part.setStart(s);
        part.setText(output.toString(WIKIPEDIA_ENCODING));
        return part;
    }
