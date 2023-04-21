    private void expectEmptyConfidenceData(InputStream mockInputStream) throws IOException {
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq((int) encodedBases.getLength()))).andAnswer(EasyMockUtil.writeArrayToInputStream(EMPTY_CONFIDENCE));
    }
