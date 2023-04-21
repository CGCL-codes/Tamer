    public CSSFile(File file, String mode, int exp) throws IOException {
        RandomAccessFile ras = new RandomAccessFile(file, mode);
        fc = ras.getChannel();
        if (fc.size() == 0) {
            long count = nextPrime(1L << exp);
            if (mode.indexOf('w') >= 0) {
                inflate(ras, count * BYTES_PER_BUCKET);
            } else {
                throw new IOException("zero size file: " + file);
            }
        } else {
            long count = fc.size() / BYTES_PER_BUCKET;
            if (factor(count) != count) {
                throw new IOException("invalid file size: " + count + " % " + factor(count) + " = 0");
            }
        }
    }
