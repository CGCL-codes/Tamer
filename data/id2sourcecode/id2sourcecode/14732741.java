    public void fromBrokenBlastXML(final InputStream input) throws BioDOMException {
        int c;
        final int buffSize = 1024;
        final byte buff[] = new byte[buffSize];
        final OutputStream os = new ByteArrayOutputStream(buffSize);
        try {
            while ((c = input.read(buff)) != -1) os.write(buff, 0, c);
            fromBrokenBlastXML(os.toString());
        } catch (IOException ex) {
            throw new BioDOMException(ex.toString());
        }
    }
