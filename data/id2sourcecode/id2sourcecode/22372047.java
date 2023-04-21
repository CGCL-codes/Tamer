    public static String grep(File f, String regExp) throws IOException {
        FileChannel fc = null;
        try {
            Pattern pattern = Pattern.compile(regExp);
            FileInputStream fis = new FileInputStream(f);
            fc = fis.getChannel();
            int sz = (int) fc.size();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
            CharBuffer cb = decoder.decode(bb);
            return grep(f, cb, pattern);
        } finally {
            if (fc != null) {
                try {
                    fc.close();
                } catch (Exception e) {
                }
                fc = null;
            }
        }
    }
