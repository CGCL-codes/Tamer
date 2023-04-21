    public void testTransferFromFileChannel() throws Exception {
        byte[] a = generateBytes();
        File f = File.createTempFile("test", null);
        RandomAccessFile rf = new RandomAccessFile(f, "rw");
        rf.write(a);
        rf.seek(0);
        ReadableByteChannel in = rf.getChannel();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(a.length);
        WritableByteChannel out = Channels.newChannel(outStream);
        PipeTask task = new PipeTask(in, out);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        task.awaitTermination(5, TimeUnit.SECONDS);
        byte[] b = outStream.toByteArray();
        assertEquals(a.length, b.length);
        assertTrue(Arrays.equals(a, b));
        assertFalse(in.isOpen());
        f.delete();
        assertFalse(out.isOpen());
    }
