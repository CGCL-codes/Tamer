    public void testConcurrentChannelToChannelTransfer() throws IOException {
        ReadableByteChannel rbc = Channels.newChannel(new ByteArrayInputStream(data1));
        ByteArrayOutputStream baos = new ByteArrayOutputStream(DATALEN);
        WritableByteChannel wbc = Channels.newChannel(baos);
        pipe.setAutoWriteChannel(wbc, null);
        pipe.transferFrom(rbc);
        pipe.closeForWriting();
        byte[] data2 = baos.toByteArray();
        assertTrue(Arrays.equals(data1, data2));
    }
