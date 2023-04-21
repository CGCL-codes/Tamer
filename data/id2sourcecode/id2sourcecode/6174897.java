    public void set(BigInteger integer) {
        byte[] result = integer.toByteArray();
        if ((result.length == NUM_BYTES + 1) && (result[0] == 0)) {
            data = new byte[NUM_BYTES];
            for (int i = 0; i < NUM_BYTES; i++) data[i] = result[i + 1];
        } else if (result.length == NUM_BYTES) {
            data = result;
        } else if (result.length < NUM_BYTES) {
            data = new byte[NUM_BYTES];
            for (int i = 0; i < result.length; i++) data[i + NUM_BYTES - result.length] = result[i];
        } else {
            System.out.println("Enccard.set: result.length=" + result.length);
        }
    }
