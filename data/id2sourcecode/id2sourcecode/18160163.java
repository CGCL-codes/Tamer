    public void testAppendToExistingFile() throws IOException, TaskQueueException, InterruptedException {
        testWrite();
        assertTrue(writeFile.exists());
        final ReadFileTask readTask1 = new ReadFileTask(writeFile, null, "test");
        io.getTaskQueue().enqueue(readTask1);
        final ReadFileEvent readEvent1 = (ReadFileEvent) testChannel.take();
        final byte[] data1 = readEvent1.getData();
        assertEquals(14, data1.length);
        assertTrue("this is a test".equals(new String(data1)));
        String appendString = "append";
        WriteFileTask task = new WriteFileTask(writeFile, null, "test", appendString.getBytes());
        task.setAppend(true);
        io.getTaskQueue().enqueue(task);
        WriteFileEvent event = (WriteFileEvent) testChannel.take();
        assertTrue(event.isSuccesful());
        final ReadFileTask readTask2 = new ReadFileTask(writeFile, null, "test");
        io.getTaskQueue().enqueue(readTask2);
        final ReadFileEvent readEvent2 = (ReadFileEvent) testChannel.take();
        final byte[] data2 = readEvent2.getData();
        assertEquals(20, data2.length);
        assertTrue("this is a testappend".equals(new String(data2)));
    }
