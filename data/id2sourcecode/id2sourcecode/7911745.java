    public static final HugeIntArray fromFile(final File file) throws IOException {
        final FileChannel channel = new FileInputStream(file).getChannel();
        final long flen = channel.size();
        if (flen % BYTESPERELEMENT != 0) throw new IOException("File size not compatible with HugeIntArray");
        final HugeIntArray a = new HugeIntArray(flen / BYTESPERELEMENT);
        a.read(file, 0, flen / BYTESPERELEMENT, 0);
        return a;
    }
