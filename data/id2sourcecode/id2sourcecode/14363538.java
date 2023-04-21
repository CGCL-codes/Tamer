    @Test
    public void testUnzipZipInputStreamFile() throws IOException {
        File targetFolder = _temporaryFolder.newFolder("unpacked4");
        final Pipe zipPipe = Pipe.open();
        final SinkChannel sink = zipPipe.sink();
        final SourceChannel source = zipPipe.source();
        final InputStream sourceIn = Channels.newInputStream(source);
        final OutputStream sourceOut = Channels.newOutputStream(sink);
        final ZipInputStream zis = new ZipInputStream(sourceIn);
        final FileInputStream fis = new FileInputStream(TestResources.SHARD1);
        final AtomicBoolean failed = new AtomicBoolean(false);
        Thread writer = new Thread() {

            @Override
            public void run() {
                try {
                    int b;
                    while ((b = fis.read()) >= 0) {
                        sourceOut.write(b);
                    }
                } catch (IOException e) {
                    System.err.println("shard transfer via pipe failed: " + e);
                    e.printStackTrace(System.err);
                    failed.set(true);
                } finally {
                    try {
                        fis.close();
                    } catch (IOException ignore) {
                    }
                    try {
                        sourceOut.close();
                    } catch (IOException ignore) {
                    }
                }
            }
        };
        writer.start();
        FileUtil.unzip(zis, targetFolder);
        File segment = new File(targetFolder, "segments.gen");
        assertTrue("Unzipped streamed zip to target", segment.exists());
    }
