    private void uploadFileToServer(final OutputStream streamToServer, final File testFile, final boolean isGoodCase) throws Exception {
        final long fileLength = testFile.length();
        final Database db = new Database(testFile);
        final String fullName = db.getFullName();
        long numOfBytesTransmitted = 0;
        final byte[] fileBuffer = new byte[BUF_SIZE];
        final InputStream testFileIn = new FileInputStream(testFile);
        try {
            final String testQuery = "PUT /databases/" + URLEncoder.encode(fullName, "UTF-8") + "?version=5&device=Aladin HTTP/1.1\r\n" + "Host: localhost\r\n" + "Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==\r\n" + "Content-Type: application/octet-stream\r\n" + "Content-Length: " + fileLength + "\r\n" + "\r\n";
            streamToServer.write(testQuery.getBytes());
            try {
                long readCount;
                while (fileLength - numOfBytesTransmitted > 0) {
                    readCount = fileLength - numOfBytesTransmitted;
                    if (readCount > fileBuffer.length) {
                        readCount = fileBuffer.length;
                    }
                    readCount = testFileIn.read(fileBuffer, 0, (int) readCount);
                    if (readCount >= 0) {
                        streamToServer.write(fileBuffer, 0, (int) readCount);
                        numOfBytesTransmitted += readCount;
                    }
                }
                streamToServer.flush();
            } catch (final IOException e) {
                System.err.println("uploadFileToServer: IOException, reason = " + e.getMessage());
                Assert.fail("Writing of a file to server may not fail");
            }
        } finally {
            testFileIn.close();
        }
        if (isGoodCase) {
            Assert.assertEquals("Number of bytes transmitted has to be the size of the file.", numOfBytesTransmitted, fileLength);
        }
    }
