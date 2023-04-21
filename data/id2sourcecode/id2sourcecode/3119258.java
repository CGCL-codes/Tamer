    @Test(dataProvider = "data")
    public void roundtrip(long plainValue, int minimumEncodedValueLength, long[] encodedValues) {
        EbmlEncoder encoder = new EbmlEncoder();
        EbmlDecoder decoder = new EbmlDecoder();
        ByteBuffer buffer = ByteBuffer.allocate(32);
        for (int i = minimumEncodedValueLength; i <= 8; i++) {
            buffer.clear();
            long encodedValue = encodedValues[i - minimumEncodedValueLength];
            VariableLengthInteger write = VariableLengthInteger.fromEncoded(encodedValue, i);
            encoder.encodeVariableLengthInteger(buffer, write);
            buffer.flip();
            VariableLengthInteger read = decoder.decodeVariableLengthInteger(buffer);
            assertThat(read, isEqualTo(write));
        }
    }
