    private DiffPart decodeFullRevision(final int blockSize_L) throws UnsupportedEncodingException, DecodingException {
        if (blockSize_L < 1) {
            throw new DecodingException("Invalid value for blockSize_L: " + blockSize_L);
        }
        int l = r.read(blockSize_L);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (int i = 0; i < l; i++) {
            output.write(r.readByte());
        }
        DiffPart part = new DiffPart(DiffAction.FULL_REVISION_UNCOMPRESSED);
        part.setText(output.toString(WIKIPEDIA_ENCODING));
        return part;
    }
