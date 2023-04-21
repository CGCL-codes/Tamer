    public BigIntegerEx get_u(byte[] B) {
        byte[] hash = getSHA1().digest(B);
        byte[] u = new byte[4];
        u[0] = hash[3];
        u[1] = hash[2];
        u[2] = hash[1];
        u[3] = hash[0];
        return new BigIntegerEx(BigIntegerEx.LITTLE_ENDIAN, u);
    }
