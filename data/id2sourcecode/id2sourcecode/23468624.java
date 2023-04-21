    @Override
    public CompressionOutputStream create(File file) throws IOException, InterruptedException {
        if (out == null) {
            FileOutputStream fout = new FileOutputStream(file);
            lock = fout.getChannel().tryLock();
            long start = System.currentTimeMillis();
            while (lock == null) {
                lock = fout.getChannel().tryLock();
                if (acquireLockTimeout > (System.currentTimeMillis() - start)) {
                    throw new IOException("Could not obtain exclusive lock on file " + file.getAbsolutePath() + " some other java process might be using this file");
                }
            }
            out = pool.create(fout, waitForCompressionResource, TimeUnit.MILLISECONDS);
        }
        return out;
    }
