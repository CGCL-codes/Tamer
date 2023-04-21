    public boolean lockAndLoadFile(float mmb, boolean append) {
        if (!file.exists()) return false;
        try {
            if (append) {
                fout = new FileOutputStream(file, append);
                out = fout.getChannel();
            } else {
                file.delete();
                faout = new RandomAccessFile(file, "rw");
                out = faout.getChannel();
            }
            lock = out.tryLock();
            if (lock == null) return false;
            mb = ByteBuffer.allocate((int) (1024 * 1024 * mmb));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return false;
        }
    }
