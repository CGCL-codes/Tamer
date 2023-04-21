    @Override
    public FileOutputStream create(File file) throws IOException, InterruptedException {
        if (out == null) {
            out = new FileOutputStream(file, true);
            lock = out.getChannel().tryLock();
            long start = System.currentTimeMillis();
            while (lock == null) {
                lock = out.getChannel().tryLock();
                if (acquireLockTimeout > (System.currentTimeMillis() - start)) {
                    throw new IOException("Could not obtain exclusive lock on file " + file.getAbsolutePath() + " some other java process might be using this file");
                }
            }
        }
        return out;
    }
