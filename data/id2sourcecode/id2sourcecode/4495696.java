    public static String fingerPrintFile(File file, int length) throws IOException {
        long size = file.length();
        int space = 0;
        FileInputStream fin = null;
        RandomAccessFile raf = null;
        try {
            if (size <= length) {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                fin = new FileInputStream(file);
                IOUtils.pipe(fin, bout, 1024);
                return new String(bout.toByteArray());
            } else {
                space = (int) Math.floor((size / length));
                raf = new RandomAccessFile(file, "r");
                StringBuffer buffer = new StringBuffer();
                for (long i = 0; i < size; i += space) {
                    try {
                        buffer.append(raf.readByte());
                        raf.skipBytes(space);
                    } catch (EOFException e) {
                        i = size;
                    }
                }
                buffer.append(size);
                return buffer.toString();
            }
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException ignore) {
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
