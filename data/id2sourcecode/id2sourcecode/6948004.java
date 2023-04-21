    @Override
    public int read(char[] cbuf) throws IOException {
        int read = theBase.read(cbuf);
        if (theLog != null) theLog.write(cbuf, 0, read); else System.out.print(new String(cbuf, 0, read));
        return read;
    }
