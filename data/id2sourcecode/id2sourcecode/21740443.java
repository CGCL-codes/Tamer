    private ByteBuffer readFile(File file) throws FileNotFoundException, IOException {
        IOException ex = null;
        ByteBuffer contents = null;
        RandomAccessFile rfile = null;
        FileChannel chan = null;
        try {
            rfile = new RandomAccessFile(file, "r");
            chan = rfile.getChannel();
            int size = (int) chan.size();
            contents = ByteBuffer.allocate(size);
            while (contents.hasRemaining()) {
                int read = chan.read(contents);
                if (read == 0) throw new IOException("Read failure: " + file.getPath());
            }
            contents.flip();
        } catch (FileNotFoundException fnfe) {
            ex = fnfe;
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
        return contents;
    }
