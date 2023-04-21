    @Test
    @Bench
    public void testMultiThreaded() {
        try {
            int numBlocks = 50;
            int address = 12345;
            String target1 = "testing-xen2-disk1";
            String target2 = "testing-xen2-disk2";
            final ByteBuffer writeData = ByteBuffer.allocate(512 * numBlocks);
            final ByteBuffer readData = ByteBuffer.allocate(512 * numBlocks);
            final ByteBuffer writeData2 = ByteBuffer.allocate(512 * numBlocks);
            final ByteBuffer readData2 = ByteBuffer.allocate(512 * numBlocks);
            Random random = new Random(System.currentTimeMillis());
            random.nextBytes(writeData.array());
            random.nextBytes(writeData2.array());
            Initiator initiator = new Initiator(Configuration.create());
            initiator.createSession(target1);
            initiator.createSession(target2);
            System.out.println("Buffer Size Test write1 t1: " + writeData.remaining());
            final Future<Void> write1 = initiator.multiThreadedWrite(this, target1, writeData, address, writeData.capacity());
            System.out.println("Buffer Size Test read1 t1: " + readData.remaining());
            final Future<Void> read1 = initiator.multiThreadedRead(this, target1, readData, address, readData.capacity());
            System.out.println("Buffer Size test write1 t2: " + writeData2.remaining());
            final Future<Void> write2 = initiator.multiThreadedWrite(this, target2, writeData2, address, writeData2.capacity());
            System.out.println("Buffer Size Test read1 t2: " + readData2.remaining());
            final Future<Void> read2 = initiator.multiThreadedRead(this, target2, readData2, address, readData2.capacity());
            write1.get();
            read1.get();
            write2.get();
            read2.get();
            assertEquals(writeData, readData);
            assertEquals(writeData2, readData2);
            readData.clear();
            writeData2.clear();
            final Future<Void> write3 = initiator.multiThreadedWrite(this, target2, writeData2, address, writeData2.capacity());
            final Future<Void> read3 = initiator.multiThreadedRead(this, target1, readData, address, readData.capacity());
            write3.get();
            read3.get();
            initiator.closeSession(target1);
            initiator.closeSession(target2);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
