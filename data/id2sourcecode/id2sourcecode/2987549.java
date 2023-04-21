    public static byte[] readBytes(File f) throws IOException {
        FileInputStream fin = null;
        FileChannel ch = null;
        try {
            fin = new FileInputStream(f);
            ch = fin.getChannel();
            int size = (int) ch.size();
            MappedByteBuffer buf = ch.map(MapMode.READ_ONLY, 0, size);
            byte[] bytes = new byte[size];
            buf.get(bytes);
            return bytes;
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
                if (ch != null) {
                    ch.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
