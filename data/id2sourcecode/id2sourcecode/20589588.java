    private void _runStreamTest(final int length) throws Exception {
        final byte[] aData = _createData(length);
        final NonBlockingByteArrayOutputStream aOutBytes = new NonBlockingByteArrayOutputStream();
        final Base64.OutputStream aOS = new Base64.OutputStream(aOutBytes);
        aOS.write(aData);
        aOS.suspendEncoding();
        aOS.resumeEncoding();
        aOS.close();
        final byte[] aEncoded = aOutBytes.toByteArray();
        byte[] aDecoded = Base64.decode(aEncoded);
        assertArrayEquals(aData, aDecoded);
        aOutBytes.reset();
        final Base64.InputStream aIS = new Base64.InputStream(new NonBlockingByteArrayInputStream(aEncoded));
        final byte[] aBuffer = new byte[3];
        for (int n = aIS.read(aBuffer); n > 0; n = aIS.read(aBuffer)) aOutBytes.write(aBuffer, 0, n);
        aOutBytes.close();
        aIS.close();
        aDecoded = aOutBytes.toByteArray();
        assertArrayEquals(aData, aDecoded);
    }
