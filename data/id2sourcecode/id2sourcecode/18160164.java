    public void testAppendToNewFileFile() throws IOException, TaskQueueException, InterruptedException {
        assertFalse(writeFile.exists());
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
        assertEquals(6, data2.length);
        assertTrue(appendString.equals(new String(data2)));
    }
