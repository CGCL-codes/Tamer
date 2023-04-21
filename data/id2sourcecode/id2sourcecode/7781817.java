    @ThreadSafe
    private void loadIndex(@NotNull File serFile) {
        ObjectInputStream in = null;
        try {
            FileInputStream fin = new FileInputStream(serFile);
            FileLock lock = fin.getChannel().lock(0, Long.MAX_VALUE, true);
            LuceneIndex index;
            try {
                in = new ObjectInputStream(fin);
                index = (LuceneIndex) in.readObject();
            } finally {
                lock.release();
            }
            addIndex(index, serFile.lastModified());
        } catch (Exception e) {
            Util.printErr(e);
        } finally {
            Closeables.closeQuietly(in);
        }
    }
