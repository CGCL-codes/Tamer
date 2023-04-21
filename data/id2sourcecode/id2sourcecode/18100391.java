    private void readAndAssertBuffer(URL url, String expected) throws IOException {
        InputStream stream = null;
        try {
            stream = url.openStream();
            int available = stream.available();
            byte[] buffer = new byte[available];
            stream.read(buffer);
            String res = new String(buffer);
            assertEquals(expected, res);
        } finally {
            stream.close();
        }
    }
