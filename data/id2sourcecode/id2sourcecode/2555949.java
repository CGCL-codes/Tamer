    public static final HugeByteArray fromFile(final File file) throws IOException {
        final FileChannel channel = new FileInputStream(file).getChannel();
        final long flen = channel.size();
        if (flen % BYTESPERELEMENT != 0) throw new IOException("File size not compatible with HugeByteArray");
        final HugeByteArray a = new HugeByteArray(flen / BYTESPERELEMENT);
        a.read(file, 0, flen / BYTESPERELEMENT, 0);
        return a;
    }
