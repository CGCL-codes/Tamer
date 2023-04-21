    public void assertBodyEqualsLocalFile(String relativePath) throws Exception {
        String directoryPrefix = getReferenceFilePath(relativePath);
        File file = new File(directoryPrefix);
        log.info("Open file " + file.getPath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream stream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        while (stream.available() > 0) {
            int read = stream.read(buffer);
            baos.write(buffer, 0, read);
        }
        stream.close();
        byte[] data = baos.toByteArray();
        if (!Arrays.equals(httpMethod.getResponseBody(), data)) {
            String expected = new String(data, "ISO-8859-1");
            String actual = new String(httpMethod.getResponseBody(), "ISO-8859-1");
            int index = 0;
            int size = Math.min(expected.length(), actual.length());
            int line = 1;
            int character = 1;
            String expectedLine = "";
            String actualLine = "";
            while (index < size) {
                if (expected.charAt(index) == '\n' && actual.charAt(index) == '\n') {
                    line++;
                    character = 1;
                    expectedLine = "";
                    actualLine = "";
                } else {
                    character++;
                    expectedLine += expected.charAt(index);
                    actualLine += actual.charAt(index);
                }
                if (expected.charAt(index) != actual.charAt(index)) {
                    String message = "Body of " + getMethodURI() + " must be equal to reference\n";
                    message += "Error line " + line + " character " + character + "\n";
                    index++;
                    while (expected.charAt(index) != '\n' && actual.charAt(index) != '\n' && index < size) {
                        expectedLine += expected.charAt(index);
                        actualLine += actual.charAt(index);
                        index++;
                    }
                    message += "Expected: " + expectedLine + "\n";
                    message += "Actual:   " + actualLine + "\n";
                    throw new AssertionFailedError(message);
                }
                index++;
            }
        }
    }
