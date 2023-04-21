    public void testDeflate() throws IOException {
        byte[] compressed = IOUtil.deflate(randomData);
        assertTrue(compressed.length < randomData.length);
        InflaterInputStream stream = new InflaterInputStream(new ByteArrayInputStream(compressed));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int read = 0;
        do {
            read = stream.read(buf);
            if (read > 0) {
                out.write(buf, 0, read);
            }
        } while (read > 0);
        assertTrue(Arrays.equals(out.toByteArray(), randomData));
    }
