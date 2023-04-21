    @Override
    public int read(CharBuffer target) throws IOException {
        int read = theBase.read(target);
        if (theLog != null) theLog.write(target.array(), 0, read); else System.out.print(target.toString().substring(0, read));
        return read;
    }
