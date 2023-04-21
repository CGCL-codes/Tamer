    public static String fileToSting(String filename) throws IOException {
        InputStream in = new FileInputStream(filename);
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] aoBuffer = new byte[512];
        int nBytesRead;
        while ((nBytesRead = in.read(aoBuffer)) > 0) {
            out.write(aoBuffer, 0, nBytesRead);
        }
        in.close();
        return new String(out.toByteArray());
    }
