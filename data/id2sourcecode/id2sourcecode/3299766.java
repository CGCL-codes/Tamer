    public ByteArrayDataReader(File inputFile) throws FileNotFoundException, IOException {
        InputStream in = new FileInputStream(inputFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int nextByte;
        while ((nextByte = in.read()) != -1) {
            baos.write(nextByte);
        }
        data = baos.toByteArray();
    }
