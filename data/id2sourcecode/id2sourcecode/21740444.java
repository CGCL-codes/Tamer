    private void writeFile(File file, ByteBuffer buffer) throws FileNotFoundException, IOException {
        IOException ex = null;
        RandomAccessFile rfile = null;
        FileChannel chan = null;
        try {
            rfile = new RandomAccessFile(file, "rw");
            chan = rfile.getChannel();
            chan.write(buffer);
        } catch (IOException ioe) {
            ex = ioe;
        } finally {
            try {
                if (chan != null) chan.close();
                if (rfile != null) rfile.close();
            } catch (IOException ioe) {
                if (ex == null) ex = ioe;
            }
            rfile = null;
            chan = null;
        }
        if (ex != null) throw ex;
    }
