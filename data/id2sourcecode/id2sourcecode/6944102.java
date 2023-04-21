    public static void rebuildFile(File oldFile, File newFile, List deltas) throws IOException {
        if (oldFile.equals(newFile)) {
            throw new IOException("cannot read and write to the same file");
        }
        RandomAccessFile out = new RandomAccessFile(newFile, "rw");
        RandomAccessFile in = null;
        try {
            in = new RandomAccessFile(oldFile, "r");
        } catch (IOException ignore) {
        }
        for (Iterator i = deltas.iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (o instanceof DataBlock) {
                long off = ((DataBlock) o).getOffset();
                out.seek(off);
                out.write(((DataBlock) o).getData());
            } else if (o instanceof Offsets) {
                if (in == null) {
                    throw new IOException("original file does not exist or not readable");
                }
                int len = ((Offsets) o).getBlockLength();
                long off1 = ((Offsets) o).getOldOffset();
                long off2 = ((Offsets) o).getNewOffset();
                byte[] buf = new byte[len];
                in.seek(off1);
                in.read(buf);
                out.seek(off2);
                out.write(buf);
            }
        }
        if (in != null) in.close();
        out.close();
    }
