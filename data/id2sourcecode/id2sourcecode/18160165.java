    public void testOverWrite() throws IOException, TaskQueueException, InterruptedException {
        File writeFile = new File(xmlDir, "write.txt");
        if (writeFile.exists()) {
            writeFile.delete();
        }
        assertFalse(writeFile.exists());
        assertTrue(writeFile.createNewFile());
        assertTrue(writeFile.exists());
        String writeString = "this is a test";
        WriteFileTask writeTask = new WriteFileTask(writeFile, null, "test", writeString.getBytes());
        io.getTaskQueue().enqueue(writeTask);
        WriteFileEvent writeFileEvent = (WriteFileEvent) testChannel.take();
        assertTrue(writeFileEvent.isSuccesful());
        ReadFileTask readTask1 = new ReadFileTask(writeFile, null, "test");
        io.getTaskQueue().enqueue(readTask1);
        ReadFileEvent readEvent1 = (ReadFileEvent) testChannel.take();
        byte[] data1 = readEvent1.getData();
        assertEquals(14, data1.length);
        assertTrue(writeString.equals(new String(data1)));
        assertTrue(writeFile.exists());
        assertEquals(14, writeFile.length());
        assertEquals(0, testChannel.size());
        String string = "this is an overwrite test";
        WriteFileTask task = new WriteFileTask(writeFile, null, "test", string.getBytes());
        io.getTaskQueue().enqueue(task);
        WriteFileEvent event = (WriteFileEvent) testChannel.take();
        assertTrue(event.isSuccesful());
        ReadFileTask readTask = new ReadFileTask(writeFile, null, "test");
        io.getTaskQueue().enqueue(readTask);
        ReadFileEvent readEvent = (ReadFileEvent) testChannel.take();
        byte[] data = readEvent.getData();
        assertEquals(25, data.length);
        assertTrue(string.equals(new String(data)));
    }
