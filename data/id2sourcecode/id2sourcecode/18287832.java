    @Test
    public void getInputStream() throws XPathException, IOException {
        final String testData = "test data";
        final String base64TestData = Base64.encodeBase64String(testData.getBytes()).trim();
        BinaryValue binaryValue = new BinaryValueFromBinaryString(new Base64BinaryValueType(), base64TestData);
        InputStream is = binaryValue.getInputStream();
        int read = -1;
        byte buf[] = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((read = is.read(buf)) > -1) {
            baos.write(buf, 0, read);
        }
        assertArrayEquals(testData.getBytes(), baos.toByteArray());
    }
