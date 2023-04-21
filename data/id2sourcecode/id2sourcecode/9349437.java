    void encode(InputStream mockInputStream, SffReadData readData) throws IOException {
        int basesLength = (int) readData.getBasecalls().getLength();
        int numberOfFlows = readData.getFlowgramValues().length;
        int readDataLength = numberOfFlows * 2 + 3 * numberOfBases;
        long padding = SffUtil.caclulatePaddedBytes(readDataLength);
        for (int i = 0; i < numberOfFlows; i++) {
            EasyMockUtil.putShort(mockInputStream, readData.getFlowgramValues()[i]);
        }
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(basesLength))).andAnswer(EasyMockUtil.writeArrayToInputStream(readData.getFlowIndexPerBase()));
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(basesLength))).andAnswer(EasyMockUtil.writeArrayToInputStream(readData.getBasecalls().toString().getBytes(IOUtil.UTF_8)));
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(basesLength))).andAnswer(EasyMockUtil.writeArrayToInputStream(readData.getQualities()));
        expect(mockInputStream.skip(padding)).andReturn(padding);
    }
