    public final long[] writeTo(String destPath) throws IOException {
        FileChannel fc = new RandomAccessFile(destPath, "rw").getChannel();
        long[] ret = writeTo(fc);
        fc.force(false);
        fc.close();
        return ret;
    }
