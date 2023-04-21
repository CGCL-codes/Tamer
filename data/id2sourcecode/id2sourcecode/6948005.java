    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int read = theBase.read(cbuf, off, len);
        if (theLog != null) theLog.write(cbuf, off, read); else System.out.print(new String(cbuf, off, read));
        return read;
    }
