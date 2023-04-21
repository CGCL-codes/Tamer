    @Test
    public void TestReadWriteCompare10() {
        CDB cdb1 = new Write10(0, false, false, false, false, false, 10, NUM_BLOCKS_TRANSFER);
        Command cmd1 = new Command(this.transport.createNexus(this.cmdRef), cdb1, TaskAttribute.ORDERED, this.cmdRef, 0);
        this.transport.createReadData(NUM_BLOCKS_TRANSFER * STORE_BLOCK_SIZE, this.cmdRef);
        lu.enqueue(this.transport, cmd1);
        this.cmdRef++;
        CDB cdb2 = new Read10(0, false, false, false, false, false, 10, NUM_BLOCKS_TRANSFER);
        Command cmd2 = new Command(this.transport.createNexus(this.cmdRef), cdb2, TaskAttribute.ORDERED, this.cmdRef, 0);
        lu.enqueue(this.transport, cmd2);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        byte[] readBuf = this.transport.getReadDataMap().get(cmdRef - 1).array();
        byte[] writeBuf = this.transport.getWriteDataMap().get(cmdRef).array();
        Assert.assertTrue("inconsistent read/write comparison", Arrays.equals(readBuf, writeBuf));
    }
