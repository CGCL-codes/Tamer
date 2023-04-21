    @Test
    @Bench
    public void test() {
        try {
            int numBlocks = 50;
            int address = 12345;
            String target = "testing-xen2-disk2";
            ByteBuffer writeData = ByteBuffer.allocate(512 * numBlocks);
            ByteBuffer readData = ByteBuffer.allocate(512 * numBlocks);
            Random random = new Random(System.currentTimeMillis());
            random.nextBytes(writeData.array());
            Initiator initiator = new Initiator(Configuration.create());
            initiator.createSession(target);
            initiator.write(this, target, writeData, address, writeData.capacity());
            initiator.read(this, target, readData, address, writeData.capacity());
            initiator.closeSession(target);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
