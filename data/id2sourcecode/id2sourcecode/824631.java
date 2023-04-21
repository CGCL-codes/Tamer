    public void testFromByteArray() {
        CurveStructure data = new CurveStructure();
        try {
            data.fromByteArray(ba, 0, 1);
            assertEquals(0x53746141, data.getMeasureUnitSignature().intValue());
            UInt32Number[] cms = data.getChannelMeasures();
            assertEquals(1, cms.length);
            assertEquals(1, cms[0].intValue());
            XYZNumber[] patches = data.getPatchMeasures();
            assertEquals(1, patches.length);
            assertEquals(1.0, patches[0].getCIEX().doubleValue());
            assertEquals(1.0, patches[0].getCIEY().doubleValue());
            assertEquals(1.0, patches[0].getCIEZ().doubleValue());
            Response16Number[] ar = data.getResponseArray();
            assertEquals(1, ar.length);
            byte[] b = ar[0].toByteArray();
            compareBytes(ba, 20, b.length, b);
        } catch (ICCProfileException e) {
            assertFalse(e.getMessage(), true);
        }
        try {
            data.fromByteArray(ba, -1, ba.length);
            assertFalse("index out of bounds, should raise exception", true);
        } catch (ICCProfileException e) {
            assertEquals(e.getMessage(), ICCProfileException.IndexOutOfBoundsException, e.getType());
        }
        try {
            data.fromByteArray(ba, ba.length, ba.length);
            assertFalse("index out of bounds, should raise exception", true);
        } catch (ICCProfileException e) {
            assertEquals(e.getMessage(), ICCProfileException.IndexOutOfBoundsException, e.getType());
        }
        try {
            data.fromByteArray(null, 0, 0);
            assertFalse("should raise null pointer exception", true);
        } catch (ICCProfileException e) {
            assertEquals(e.getMessage(), ICCProfileException.NullPointerException, e.getType());
        }
    }
