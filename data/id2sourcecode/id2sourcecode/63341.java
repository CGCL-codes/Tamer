    static String get(String fn) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(1024);
        FileChannel fc = new FileInputStream(fn).getChannel();
        try {
            bb.clear();
            if (fc.read(bb) < 0) throw new IOException("Could not read any bytes");
            bb.flip();
            int minor = bb.getShort(4);
            int major = bb.getShort(6);
            return major + "." + minor;
        } finally {
            fc.close();
        }
    }
