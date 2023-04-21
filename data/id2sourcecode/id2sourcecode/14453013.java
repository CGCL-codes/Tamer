    @Test
    public void testGetObject() throws Exception {
        _conn.putObject(_testBucketName, _fileObj, AccessControlList.StandardPolicy.PRIVATE);
        S3Object obj = _conn.getObject(_testBucketName, _fileObj.getKey());
        S3ObjectTest.testEquals(_fileObj, obj);
        assertTrue(String.format("S3Object modified date: %d Original modified date: %d --- Is your clock correct?", obj.lastModified(), _fileObj.lastModified()), obj.lastModified() >= (_fileObj.lastModified() - (10 * 60 * 1000)));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = obj.getInputStream();
        byte[] data = new byte[1024];
        int nread;
        while ((nread = input.read(data)) > 0) {
            output.write(data, 0, nread);
            if (output.size() > 2048) {
                break;
            }
        }
        input.close();
        assertEquals(TEST_DATA, output.toString("utf8"));
    }
