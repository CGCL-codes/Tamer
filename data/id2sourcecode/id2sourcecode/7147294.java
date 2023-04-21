    public void testTransferToFileChannel() throws Exception {
        byte[] a = generateBytes();
        File f = File.createTempFile("test", null);
        RandomAccessFile rf = new RandomAccessFile(f, "rw");
        rf.write(a);
        rf.seek(0);
        ReadableByteChannel in = Channels.newChannel(new ByteArrayInputStream(a));
        WritableByteChannel out = rf.getChannel();
        PipeTask task = new PipeTask(in, true, out, false, 8192, true);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        task.awaitTermination(5, TimeUnit.SECONDS);
        assertTrue(out.isOpen());
        byte[] b = new byte[a.length];
        rf.seek(0);
        rf.readFully(b);
        rf.close();
        f.delete();
        assertEquals(a.length, b.length);
        assertTrue(Arrays.equals(a, b));
        assertFalse(in.isOpen());
    }
