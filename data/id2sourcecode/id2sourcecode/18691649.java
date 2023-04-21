    public void read(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();
        buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, f.length());
        int o = 0;
        do {
            int l = buffer.getInt(o);
            if (l <= STRING_OFFSET) {
                throw new IOException("Corrupted file : invalid packet length");
            }
            addOffset(o);
            o += l;
        } while (o < buffer.limit());
    }
