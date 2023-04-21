    public void testSequentialChannelToChannelTransfer() throws IOException {
        ReadableByteChannel rbc = Channels.newChannel(new ByteArrayInputStream(data1));
        ByteArrayOutputStream baos = new ByteArrayOutputStream(DATALEN);
        WritableByteChannel wbc = Channels.newChannel(baos);
        pipe.transferFrom(rbc);
        pipe.setAutoWriteChannel(wbc, null);
        pipe.autoTransferTo();
        byte[] data2 = baos.toByteArray();
        assertTrue(Arrays.equals(data1, data2));
    }
