    public void testHashingTruncated() {
        int truncateto = 15;
        for (int i = 0; i < input.length; i++) {
            SHA256d hasher = new SHA256d(truncateto);
            hasher.update(input[i]);
            byte[] got = hasher.digest();
            byte[] expected = new byte[truncateto];
            System.arraycopy(Hex.decode(hexoutput[i]), 0, expected, 0, truncateto);
            if (!Arrays.equals(got, expected)) {
                fail("got (" + new String(Hex.encode(got)) + ") but expected (" + new String(Hex.encode(expected)) + ")");
            }
        }
    }
