    private void loadBinary(InputStream is) throws IOException {
        if (is instanceof FileInputStream) {
            FileInputStream fis = (FileInputStream) is;
            FileChannel fc = fis.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());
            bb.load();
            loadBinary(bb);
            is.close();
        } else {
            loadBinary(new DataInputStream(is));
        }
    }
