    public void test_force() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(tmpFile);
        FileChannel fileChannelRead = fileInputStream.getChannel();
        MappedByteBuffer mmbRead = fileChannelRead.map(MapMode.READ_ONLY, 0, fileChannelRead.size());
        mmbRead.force();
        FileInputStream inputStream = new FileInputStream(tmpFile);
        FileChannel fileChannelR = inputStream.getChannel();
        MappedByteBuffer resultRead = fileChannelR.map(MapMode.READ_ONLY, 0, fileChannelR.size());
        assertEquals("Invoking force() should have no effect when this buffer was not mapped in read/write mode", mmbRead, resultRead);
        RandomAccessFile randomFile = new RandomAccessFile(tmpFile, "rw");
        FileChannel fileChannelReadWrite = randomFile.getChannel();
        MappedByteBuffer mmbReadWrite = fileChannelReadWrite.map(FileChannel.MapMode.READ_WRITE, 0, fileChannelReadWrite.size());
        mmbReadWrite.put((byte) 'o');
        mmbReadWrite.force();
        RandomAccessFile random = new RandomAccessFile(tmpFile, "rw");
        FileChannel fileChannelRW = random.getChannel();
        MappedByteBuffer resultReadWrite = fileChannelRW.map(FileChannel.MapMode.READ_WRITE, 0, fileChannelRW.size());
        assertFalse(mmbReadWrite.equals(resultReadWrite));
        fileChannelRead.close();
        fileChannelR.close();
        fileChannelReadWrite.close();
        fileChannelRW.close();
    }
