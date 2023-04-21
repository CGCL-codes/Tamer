    @Test
    public void transferFrom() throws Exception {
        InputStream in = new ByteArrayInputStream("asdf".getBytes("utf-8"));
        ByteBufferOutputStream buffer = new ByteBufferOutputStream(4);
        int n = buffer.transferFrom(Channels.newChannel(in));
        assertEquals(4, n);
        assertEquals("asdf", Charset.forName("utf-8").decode(buffer.getByteBuffer()).toString());
    }
