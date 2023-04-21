    @Test
    public final void testClearing() throws Exception {
        initiator.write(this, TARGET_DRIVE_NAME, writeBuffer, LOGICAL_BLOCK_ADDRESS, writeBuffer.remaining());
        writeBuffer.flip();
        initiator.read(this, TARGET_DRIVE_NAME, readBuffer, LOGICAL_BLOCK_ADDRESS, readBuffer.remaining());
        readBuffer.flip();
        assertTrue(writeBuffer.equals(readBuffer));
    }
