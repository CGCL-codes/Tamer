    @Override
    public void flatten(OutputStream out, NSOFEncoder encoder) throws IOException {
        out.write(NSOF_LARGE_BINARY);
        String companderName = getCompanderName();
        byte[] companderNameBytes = (companderName == null) ? null : companderName.getBytes();
        byte[] args = getCompanderArguments();
        Blob data = getBlob();
        int numBytesData = 0;
        if (data != null) {
            try {
                numBytesData = (int) data.length();
            } catch (SQLException se) {
                throw new IOException(se);
            }
        }
        encoder.flatten(getObjectClass(), out);
        out.write(isCompressed() ? COMPRESSED : UNCOMPRESSED);
        htonl(numBytesData, out);
        htonl((companderNameBytes == null) ? 0 : companderNameBytes.length, out);
        htonl((args == null) ? 0 : args.length, out);
        htonl(0, out);
        if (companderNameBytes != null) {
            out.write(companderNameBytes);
        }
        if (args != null) {
            out.write(args);
        }
        if (data != null) {
            InputStream in;
            try {
                in = data.getBinaryStream();
            } catch (SQLException se) {
                throw new IOException(se);
            }
            for (int i = 0; i < numBytesData; i++) out.write(in.read());
        }
    }
