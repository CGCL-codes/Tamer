    protected byte[] getByteArrayForBundleLocation(String location) throws MalformedURLException, IOException {
        URL url = new URL(location);
        InputStream inStream = url.openStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final int arraySize = 4096;
        byte[] tempArray = new byte[arraySize];
        int readBytes = 0;
        do {
            readBytes = inStream.read(tempArray);
            outStream.write(tempArray, 0, readBytes);
        } while (readBytes == arraySize);
        byte[] bundleArray = outStream.toByteArray();
        return bundleArray;
    }
