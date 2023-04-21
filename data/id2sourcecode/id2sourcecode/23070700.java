    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testOutOfRangeData() throws BadParameterException {
        @SuppressWarnings("unused") float f = mono.getChannelData(0)[100];
    }
