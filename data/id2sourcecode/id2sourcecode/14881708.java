    public boolean copy(final Object[] r) throws IOException {
        Reader is = (Reader) r[0];
        Writer os = (Writer) r[1];
        char[] buffer = new char[sChunk];
        int length;
        while ((length = is.read(buffer, 0, sChunk)) != -1) os.write(buffer, 0, length);
        is.close();
        os.close();
        return true;
    }
