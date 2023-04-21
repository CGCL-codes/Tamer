    public void testChunkedChannelToChannelTransfer() throws IOException {
        ReadableByteChannel rbc = Channels.newChannel(new ByteArrayInputStream(data1));
        ByteArrayOutputStream baos = new ByteArrayOutputStream(DATALEN);
        WritableByteChannel wbc = Channels.newChannel(baos);
        pipe.setAutoWriteChannel(wbc, null);
        int transferCount = 0;
        while (transferCount < DATALEN) {
            int chunkSize = Math.min(65531, DATALEN - transferCount);
            int transferSize = pipe.transferFrom(rbc, chunkSize);
            assertEquals(chunkSize, transferSize);
            transferCount += transferSize;
        }
        pipe.closeForWriting();
        byte[] data2 = baos.toByteArray();
        for (int i = 0; i < DATALEN; ++i) {
            if (data1[i] != data2[i]) {
                System.err.println(i);
                break;
            }
        }
        assertTrue(Arrays.equals(data1, data2));
    }
