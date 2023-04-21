    @Test
    public void testFileUploadSuccess() throws Exception {
        final int testPort = TestHelpers.findUnboundSocket(DEFAULT_TEST_PORT);
        final String testDirName = TestHelpers.prepareDataDirectory();
        Class.forName("org.sqlite.JDBC");
        Assert.assertNotNull("Driver has to be loaded.", DriverManager.getDriver("jdbc:sqlite:"));
        final DatabaseLibrarian databaseLibrary = new DatabaseLibrarian(testDirName);
        databaseLibrary.addDatabasesFromDirectory(testDirName);
        final UserAuthenticator deviceAuthenticator = new UserAuthenticator();
        deviceAuthenticator.addUser(new User(1, "Aladdin", "open sesame"));
        final SyncServer serverRunnable = new SyncServer(testPort, 0, databaseLibrary, deviceAuthenticator);
        Assert.assertNotNull("Server runnable has to be allocated.", serverRunnable);
        serverRunnable.start();
        Assert.assertTrue("Server should be running now.", serverRunnable.isRunning());
        final String testFileName = TestHelpers.getTestStorePath() + File.separatorChar + "l_test3.db";
        final File testFile = new File(testFileName);
        final long testFileByteCount = testFile.length();
        final InputStream testFileIn = new FileInputStream(testFileName);
        final Socket testSocket = new Socket("localhost", testPort);
        try {
            final InputStream testIn = testSocket.getInputStream();
            final OutputStream testOut = testSocket.getOutputStream();
            final String testQuery = "PUT /upload?version=5&device=Aladin HTTP/1.0\r\n" + "Host: localhost\r\n" + "Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==\r\n" + "Content-Type: application/octet-stream\r\n" + "Content-Length: " + testFileByteCount + "\r\n" + "\r\n";
            testOut.write(testQuery.getBytes());
            final byte[] fileBuffer = new byte[BUF_SIZE];
            int readCount = 0;
            long writeCount = 0;
            while ((readCount = testFileIn.read(fileBuffer, 0, fileBuffer.length)) >= 0) {
                try {
                    testOut.write(fileBuffer, 0, readCount);
                } catch (final Exception e) {
                    Assert.fail("Writing of a file to server may not fail:" + e);
                    writeCount -= readCount;
                }
                writeCount += readCount;
            }
            testOut.flush();
            testFileIn.close();
            final StringBuilder resultBuffer = new StringBuilder();
            while ((readCount = testIn.read(fileBuffer, 0, fileBuffer.length)) >= 0) {
                resultBuffer.append(new String(fileBuffer, 0, readCount));
            }
            final String testResult = resultBuffer.toString();
            Assert.assertEquals("Count of bytes written has to be the size of the file.", writeCount, testFileByteCount);
            Assert.assertEquals("Response to be 200 OK.", 0, testResult.indexOf("HTTP/1.0 200 OK\r\n"));
            final String newFilePath = testDirName + File.separatorChar + "l_test3.db";
            final byte[] refFileBuffer = new byte[1000];
            final byte[] newFileBuffer = new byte[1000];
            final InputStream refFileIn = new FileInputStream(testFileName);
            final InputStream newFileIn = new FileInputStream(newFilePath);
            int refFileReadCount;
            while ((refFileReadCount = refFileIn.read(refFileBuffer, 0, 1000)) >= 0) {
                final int newFileReadCount = newFileIn.read(newFileBuffer, 0, 1000);
                Assert.assertEquals("Reference and uploaded file have to have the same size.", newFileReadCount, refFileReadCount);
                for (int n = 0; n < refFileReadCount; n++) {
                    if (refFileBuffer[n] != newFileBuffer[n]) {
                        Assert.fail("Byte in reference and uploaded file at " + n + " have to have the same value.");
                    }
                }
            }
            refFileIn.close();
            newFileIn.close();
            testIn.close();
            testOut.close();
        } finally {
            testSocket.close();
        }
        serverRunnable.stop();
        TestHelpers.removeDataDirectory();
    }
