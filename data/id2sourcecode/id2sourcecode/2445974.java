    public static String readAllFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int aByte;
        while ((aByte = inputStream.read()) != -1) outStream.write(aByte);
        inputStream.close();
        return outStream.toString();
    }
