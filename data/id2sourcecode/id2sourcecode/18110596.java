    @Test
    public void valid() throws IOException {
        SFFReadHeader readHeader = new DefaultSFFReadHeader(numberOfBases, qualityClip, adapterClip, name);
        byte[] expectedEncodedBytes = encodeReadHeader(readHeader);
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        SffWriter.writeReadHeader(readHeader, actual);
        assertArrayEquals(expectedEncodedBytes, actual.toByteArray());
    }
