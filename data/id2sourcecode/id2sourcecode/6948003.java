    @Override
    public int read() throws IOException {
        int read = theBase.read();
        if (read >= 0) {
            if (theLog != null) theLog.write(read); else System.out.print((char) read);
        }
        return read;
    }
