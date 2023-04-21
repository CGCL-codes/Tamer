    public void shouldNotAllowWriteOperations() throws Exception {
        TFileFactory factory = factory();
        TFile file = factory.file("tmp/test.txt");
        file.save("content");
        file.read(new InputProcessor() {

            public void process(InputManager inputManager) throws IOException {
                final FileChannel channel = inputManager.channel();
                assertNotSupported(new Block() {

                    public void run() throws Exception {
                        channel.write(ByteBuffer.allocate(3));
                    }
                });
                assertNotSupported(new Block() {

                    public void run() throws Exception {
                        channel.write(new ByteBuffer[] { ByteBuffer.allocate(3) }, 0, 1);
                    }
                });
                assertNotSupported(new Block() {

                    public void run() throws Exception {
                        channel.write(new ByteBuffer[] { ByteBuffer.allocate(3) });
                    }
                });
                assertNotSupported(new Block() {

                    public void run() throws Exception {
                        channel.write(ByteBuffer.allocate(3), 0);
                    }
                });
                assertNotSupported(new Block() {

                    public void run() throws Exception {
                        channel.truncate(3);
                    }
                });
                assertNotSupported(new Block() {

                    public void run() throws Exception {
                        channel.transferFrom(AccesssUtil.createInMemoryOutputChannel(), 0, 1);
                    }
                });
                assertNotSupported(new Block() {

                    public void run() throws Exception {
                        channel.map(FileChannel.MapMode.READ_WRITE, 0, 10);
                    }
                });
                assertNotSupported(new Block() {

                    public void run() throws Exception {
                        channel.map(FileChannel.MapMode.PRIVATE, 0, 10);
                    }
                });
            }
        });
    }
