    public static boolean SHA1(int piece, Files files) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        MappedByteBuffer[] mbb = files.mmap(piece);
        for (MappedByteBuffer b : mbb) md.update(b);
        for (int i = 0; i < mbb.length; ++i) mbb[i] = null;
        mbb = null;
        return Arrays.equals(md.digest(), files.metafile.pieceHash(piece));
    }
