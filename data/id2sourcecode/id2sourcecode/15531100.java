    @Test
    public final void testWriteRead() throws Exception {
        initiator.write(this, TARGET_DRIVE_NAME, writeBuffer, LOGICAL_BLOCK_ADDRESS, writeBuffer.remaining());
        initiator.read(this, TARGET_DRIVE_NAME, readBuffer, LOGICAL_BLOCK_ADDRESS, readBuffer.remaining());
        writeBuffer.flip();
        readBuffer.flip();
        assertTrue(writeBuffer.equals(readBuffer));
    }
