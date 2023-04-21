    public static final HugeShortArray fromFile(final File file) throws IOException {
        final FileChannel channel = new FileInputStream(file).getChannel();
        final long flen = channel.size();
        if (flen % BYTESPERELEMENT != 0) throw new IOException("File size not compatible with HugeShortArray");
        final HugeShortArray a = new HugeShortArray(flen / BYTESPERELEMENT);
        a.read(file, 0, flen / BYTESPERELEMENT, 0);
        return a;
    }
