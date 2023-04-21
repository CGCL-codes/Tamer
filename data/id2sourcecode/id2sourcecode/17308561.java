    private void expectFullConfidenceRead(InputStream mockInputStream, byte[] confidence) throws IOException {
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(confidence.length))).andAnswer(EasyMockUtil.writeArrayToInputStream(confidence));
    }
