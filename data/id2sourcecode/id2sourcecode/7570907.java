    public static byte[] getImageBytesFromFileSpec(String imageFileSpec) throws FileNotFoundException, IOException {
        InputStream inputStream = new FileInputStream(new File(imageFileSpec));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
        byte[] bytes = new byte[2048];
        int readBytes;
        while ((readBytes = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, readBytes);
        }
        byte[] byteData = outputStream.toByteArray();
        inputStream.close();
        outputStream.close();
        return byteData;
    }
