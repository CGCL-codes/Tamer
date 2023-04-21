    public void testWriteArrayDouble() {
        double[] expected = { 0d, 1.2, 83, -932.108E-6, Math.E };
        assertEqualsArray(expected, ArrayValueHandler.readArray(double[].class.getName(), ArrayValueHandler.writeArray(expected, ','), ','));
    }
