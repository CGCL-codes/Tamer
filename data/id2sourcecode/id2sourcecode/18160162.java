    public void testWrite() throws IOException, TaskQueueException, InterruptedException {
        File writeFile = new File(xmlDir, "write.txt");
        assertFalse(writeFile.exists());
        assertTrue(writeFile.createNewFile());
        assertTrue(writeFile.exists());
        String string = "this is a test";
        WriteFileTask task = new WriteFileTask(writeFile, null, "test", string.getBytes());
        io.getTaskQueue().enqueue(task);
        WriteFileEvent event = (WriteFileEvent) testChannel.take();
        assertTrue(event.isSuccesful());
        ReadFileTask readTask = new ReadFileTask(writeFile, null, "test");
        io.getTaskQueue().enqueue(readTask);
        ReadFileEvent readEvent = (ReadFileEvent) testChannel.take();
        byte[] data = readEvent.getData();
        assertEquals(14, data.length);
        assertTrue(string.equals(new String(data)));
    }
