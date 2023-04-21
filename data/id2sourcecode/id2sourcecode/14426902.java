    private byte[] toByte(File file) throws IOException {
        FileInputStream fin = null;
        FileChannel ch = null;
        try {
            fin = new FileInputStream(file);
            ch = fin.getChannel();
            int size = (int) ch.size();
            MappedByteBuffer buf = ch.map(MapMode.READ_ONLY, 0, size);
            byte[] bytes = new byte[size];
            buf.get(bytes);
            return bytes;
        } finally {
            if (fin != null) fin.close();
            if (ch != null) ch.close();
        }
    }
