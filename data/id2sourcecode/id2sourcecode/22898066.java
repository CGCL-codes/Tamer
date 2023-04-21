    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getChannel", args = {  })
    public void test_getChannel() {
        FileChannel channel;
        byte[] buffer = new byte[100];
        byte[] stringBytes;
        final int offset = 5;
        boolean equal = true;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            channel = fis.getChannel();
            assertNotNull(channel);
            assertTrue("Channel is closed.", channel.isOpen());
            channel.position(offset);
            fis.read(buffer, 0, 10);
            stringBytes = fileString.getBytes();
            for (int i = 0; i < 10; i++) {
                equal &= (buffer[i] == stringBytes[i + offset]);
            }
            assertTrue("Channel is not associated with this stream.", equal);
            fis.close();
            assertFalse("Channel has not been closed.", channel.isOpen());
        } catch (FileNotFoundException e) {
            fail("Could not find : " + fileName);
        } catch (IOException e) {
            fail("Exception during test : " + e.getMessage());
        }
    }
