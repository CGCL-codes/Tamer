    @Test
    public void readMultipleBytesSpecificInLoop() throws IOException {
        Random random = new Random();
        byte testData[] = new byte[1024];
        random.nextBytes(testData);
        final ByteBuffer buf = ByteBuffer.wrap(testData);
        InputStream is = new ByteBufferInputStream(new TestableByteBufferAccessor(buf));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte readBuf[] = new byte[56];
        int read = -1;
        while ((read = is.read(readBuf, 0, readBuf.length)) > -1) {
            assertLessThanOrEqual(readBuf.length, read);
            baos.write(readBuf, 0, read);
        }
        assertArrayEquals(testData, baos.toByteArray());
    }
