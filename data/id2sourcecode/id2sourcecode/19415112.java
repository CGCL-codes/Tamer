    public static void writeRawFile(DataOutputStream dos, File infile) throws IOException {
        if (infile.length() > MAXFILE) {
            throw new IOException("File too large to write!");
        }
        byte[] buffer = new byte[128];
        int readlen = 0;
        FileInputStream fis = new FileInputStream(infile);
        readlen = fis.read(buffer);
        while (readlen >= 0) {
            if (readlen > 0) {
                dos.write(buffer, 0, readlen);
            }
            readlen = fis.read(buffer);
        }
        fis.close();
    }
