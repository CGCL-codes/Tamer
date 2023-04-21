    public static String getSampleText(String path) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        FileInputStream inputStream = new FileInputStream(path);
        byte[] bytes = new byte[1028];
        int lenght = -1;
        while ((lenght = inputStream.read(bytes)) > -1) {
            buffer.write(bytes, 0, lenght);
        }
        inputStream.close();
        return new String(buffer.toByteArray());
    }
