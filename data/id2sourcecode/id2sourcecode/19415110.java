    public static void readFile(DataInputStream dis, File outfile) throws IOException {
        long len = dis.readLong();
        if (len > MAXFILE) {
            throw new IOException("Too large file! " + MAXFILE);
        }
        FileOutputStream fos = new FileOutputStream(outfile);
        byte[] buffer = new byte[128];
        int readlen = 0;
        while (len > 0) {
            readlen = dis.read(buffer, 0, (int) Math.min(len, buffer.length));
            if (readlen > 0) {
                fos.write(buffer, 0, readlen);
                len -= readlen;
            }
        }
        fos.close();
    }
