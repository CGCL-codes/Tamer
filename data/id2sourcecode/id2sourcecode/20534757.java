    private static void allocateBlock(File cstore, int new_key) throws ContentStoreException {
        boolean exception_thrown = false;
        File key_file = new File(cstore, "keyfile.txt");
        RandomAccessFile out = null;
        try {
            out = new RandomAccessFile(key_file, "rw");
            for (int i = 0; i < FileContentStore.MAX_SPINS; i++) {
                FileLock l = out.getChannel().tryLock();
                if (l == null) {
                    try {
                        Thread.sleep(FileContentStore.SPIN_TIMEOUT);
                    } catch (InterruptedException e) {
                    }
                    continue;
                } else {
                    out.setLength(0);
                    out.writeUTF(Integer.toString(new_key));
                    return;
                }
            }
            throw new ContentStoreException("Block allocation timed out.");
        } catch (ContentStoreException e) {
            exception_thrown = true;
            throw e;
        } catch (Throwable t) {
            exception_thrown = true;
            throw new ContentStoreException(t);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    if (!exception_thrown) throw new ContentStoreException("Problems occurred when closing content store.", e);
                }
            }
        }
    }
