    protected static MappedByteBuffer map_file(String filename, boolean writable) {
        try {
            FileChannel.MapMode mode = (writable) ? FileChannel.MapMode.READ_WRITE : FileChannel.MapMode.READ_ONLY;
            String open_mode = (writable) ? "rw" : "r";
            FileChannel chnl = new RandomAccessFile(filename, open_mode).getChannel();
            MappedByteBuffer buf = chnl.map(mode, 0, (int) chnl.size());
            buf.load();
            return buf;
        } catch (java.io.FileNotFoundException fnfe) {
            throw new IllegalArgumentException("Cannot find data file: " + filename);
        } catch (java.io.IOException ioe) {
            throw new IllegalArgumentException("IO exception mapping data file: " + filename);
        }
    }
