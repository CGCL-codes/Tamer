    public static String readFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1000);
        streamData(fileInputStream, byteArrayOutputStream);
        return new String(byteArrayOutputStream.toByteArray());
    }
