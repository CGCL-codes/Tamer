    public void loadFile(float mmb, boolean append) {
        try {
            if (append) {
                fout = new FileOutputStream(file, append);
                out = fout.getChannel();
            } else {
                file.delete();
                faout = new RandomAccessFile(file, "rw");
                out = faout.getChannel();
            }
            mb = ByteBuffer.allocate((int) (1024 * 1024 * mmb));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
