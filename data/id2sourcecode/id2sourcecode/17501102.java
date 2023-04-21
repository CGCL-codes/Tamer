    public static CSSFile create(File file, long size) throws IOException {
        long count = size / BYTES_PER_BUCKET;
        if (count * BYTES_PER_BUCKET != size) {
            throw new IllegalArgumentException("not a muitiple of " + BYTES_PER_BUCKET + ": " + size);
        } else if (factor(count) != count) {
            throw new IllegalArgumentException("not prime: " + count);
        }
        RandomAccessFile ras = new RandomAccessFile(file, "rw");
        inflate(ras, size);
        return new CSSFile(ras.getChannel());
    }
