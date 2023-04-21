    public void test_load() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(tmpFile);
        FileChannel fileChannelRead = fileInputStream.getChannel();
        MappedByteBuffer mmbRead = fileChannelRead.map(MapMode.READ_ONLY, 0, fileChannelRead.size());
        assertEquals(mmbRead, mmbRead.load());
        RandomAccessFile randomFile = new RandomAccessFile(tmpFile, "rw");
        FileChannel fileChannelReadWrite = randomFile.getChannel();
        MappedByteBuffer mmbReadWrite = fileChannelReadWrite.map(FileChannel.MapMode.READ_WRITE, 0, fileChannelReadWrite.size());
        assertEquals(mmbReadWrite, mmbReadWrite.load());
        fileChannelRead.close();
        fileChannelReadWrite.close();
    }
