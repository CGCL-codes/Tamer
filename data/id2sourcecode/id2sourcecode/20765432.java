    public void testWriteArrayBigDDouble() {
        Double[] expected = { 0d, 1.2, 83.0, -932.108E-6, Math.E };
        assertEqualsArray(expected, ArrayValueHandler.readArray(Double[].class.getName(), ArrayValueHandler.writeArray(expected, ','), ','));
    }
