    public void destroy() throws KrbCredCacheException {
        File file;
        try {
            file = new File(this.file);
        } catch (NullPointerException ne) {
            throw new KrbCredCacheException(ne);
        }
        try {
            if (!file.isFile() || !file.exists()) {
                return;
            }
        } catch (SecurityException se) {
            throw new KrbCredCacheException("permission denied", se);
        }
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            FileLock lock = raf.getChannel().lock();
            int length = (int) raf.length();
            byte[] b = new byte[length];
            raf.write(b, 0, length);
            lock.release();
            raf.close();
        } catch (Exception e) {
            System.err.println("unable to zero cred file");
            e.printStackTrace();
        }
        try {
            file.delete();
        } catch (SecurityException se) {
            throw new KrbCredCacheException("permission denied", se);
        }
        return;
    }
