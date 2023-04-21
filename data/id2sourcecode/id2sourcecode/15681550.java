    private void DeleteFile(File f) throws IOException {
        if (!f.delete() && f.exists()) {
            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            raf.getChannel().force(true);
            raf.close();
            if (!f.delete()) {
                throw new RuntimeException("Could not delete file: " + f.getPath());
            }
        }
    }
