    public void testMag() {
        String key = "Brazil#Sao Paulo#Sao Paulo";
        byte[] hashed = digester.digest(key.getBytes());
        BigInteger bigInt = new BigInteger(hashed);
        byte[] fromBigInt = bigInt.toByteArray();
        for (int i = 0; i < fromBigInt.length; i++) assertEquals(hashed[i], fromBigInt[i]);
        byte[] array = new byte[20];
        for (int pos = 0; pos < 20; pos++) {
            for (int pos2 = 0; pos2 <= pos; pos2++) {
                for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
                    if (i != 0) {
                        array[pos2] = i;
                        BigInteger bi = new BigInteger(array);
                        byte[] fromBi = normalizeArray(bi.toByteArray(), array.length);
                        assertEquals(array.length, fromBi.length);
                        for (int j = 0; j < fromBi.length; j++) assertEquals(array[j], fromBi[j]);
                    }
                }
            }
        }
    }
