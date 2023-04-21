    public EndianInputStream(final File file, final String charsetName) throws IOException {
        this.charsetName = charsetName;
        FileInputStream fin = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream((int) file.length());
        byte[] buf = new byte[64 * 1024];
        while (true) {
            int len = fin.read(buf);
            if (len < 0) {
                break;
            }
            out.write(buf, 0, len);
        }
        fin.close();
        data = out.toByteArray();
        pos = 0;
    }
