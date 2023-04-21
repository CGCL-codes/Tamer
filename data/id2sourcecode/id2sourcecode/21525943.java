    public void testHashing() {
        for (int i = 0; i < input.length; i++) {
            SHA256d hasher = new SHA256d();
            hasher.update(input[i]);
            byte[] got = hasher.digest();
            byte[] expected = Hex.decode(hexoutput[i]);
            if (!Arrays.equals(got, expected)) {
                fail("got (" + new String(Hex.encode(got)) + ") but expected (" + hexoutput[i] + ")");
            }
        }
    }
