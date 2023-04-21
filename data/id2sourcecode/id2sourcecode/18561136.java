    @Test
    public void should_get_file_channel() throws IOException {
        is = new FileInputStream(new File("res/input.data"));
        FileChannel channel = is.getChannel();
        assertEquals(0, channel.position());
        ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
        channel.read(buffer);
        assertTrue(new String(buffer.array(), "utf-8").startsWith("《藏地密码》"));
        assertEquals(channel.size(), channel.position());
    }
