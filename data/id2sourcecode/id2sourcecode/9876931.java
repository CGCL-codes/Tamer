    public void testShouldReturnSameChannelObject() throws Exception {
        File file = new File("D:\\Java\\workspace\\jade-stone\\JS.Testing\\src\\jdk\\collection\\test\\MapTest.java");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel1 = fileInputStream.getChannel();
        FileChannel channel2 = fileInputStream.getChannel();
        assertSame(channel1, channel2);
    }
