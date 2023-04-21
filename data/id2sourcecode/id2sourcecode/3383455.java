    @Test
    public void validMustSkipToStartOfCommentSection() throws Exception {
        final String scfCommentAsString = convertPropertiesToSCFComment(expectedComments);
        int distanceToSkip = 200;
        InputStream mockInputStream = createMock(InputStream.class);
        expect(mockInputStream.skip(distanceToSkip)).andReturn((long) distanceToSkip);
        expect(mockInputStream.read(isA(byte[].class), eq(0), eq(scfCommentAsString.length()))).andAnswer(EasyMockUtil.writeArrayToInputStream(scfCommentAsString.getBytes()));
        expect(mockHeader.getCommentOffset()).andReturn(distanceToSkip);
        expect(mockHeader.getCommentSize()).andReturn(scfCommentAsString.length());
        replay(mockHeader, mockInputStream);
        long newOffset = sut.decode(new DataInputStream(mockInputStream), currentOffset, mockHeader, chromaStruct);
        verify(mockHeader, mockInputStream);
        assertEquals(expectedComments, chromaStruct.properties());
        assertEquals(scfCommentAsString.length() + distanceToSkip, newOffset);
    }
