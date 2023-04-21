    void encode(InputStream mockInputStream, SFFReadData readData) throws IOException {
        int basesLength = readData.getBasecalls().length();
        int numberOfFlows = readData.getFlowgramValues().length;
        int readDataLength = numberOfFlows * 2 + 3 * numberOfBases;
        long padding = SFFUtil.caclulatePaddedBytes(readDataLength);
        for (int i = 0; i < numberOfFlows; i++) {
            EasyMockUtil.putShort(mockInputStream, readData.getFlowgramValues()[i]);
        }
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(basesLength))).andAnswer(EasyMockUtil.writeArrayToInputStream(readData.getFlowIndexPerBase()));
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(basesLength))).andAnswer(EasyMockUtil.writeArrayToInputStream(readData.getBasecalls().getBytes()));
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(basesLength))).andAnswer(EasyMockUtil.writeArrayToInputStream(readData.getQualities()));
        expect(mockInputStream.skip(padding)).andReturn(padding);
    }
