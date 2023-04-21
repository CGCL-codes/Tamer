    public static Image createImage(String filename) throws IOException {
        InputStream input = new FileInputStream(filename);
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int blockSize = 1024;
        byte[] block = new byte[blockSize];
        int bytes;
        while ((bytes = input.read(block, 0, blockSize)) > -1) bytestream.write(block, 0, bytes);
        return Toolkit.getDefaultToolkit().createImage(bytestream.toByteArray());
    }
