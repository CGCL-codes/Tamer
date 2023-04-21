    private String copyTestResourceToFile(String testResourceName) throws IOException, FileNotFoundException {
        InputStream testIniStream = TestUtils.getTestResource(this.getClass(), testResourceName);
        ReadableByteChannel testIniChannel = Channels.newChannel(testIniStream);
        File tempFile = File.createTempFile("openfire", "iniDataTest");
        FileOutputStream testFileStream = new FileOutputStream(tempFile);
        FileChannel fileChannel = testFileStream.getChannel();
        fileChannel.transferFrom(testIniChannel, 0, testIniStream.available());
        testIniChannel.close();
        fileChannel.close();
        return tempFile.getAbsolutePath();
    }
