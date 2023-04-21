    public byte[] calculate(byte[] buf) {
        tiger.reset();
        tiger.update((byte) 0);
        tiger.update(buf, 0, 0);
        Object dig = (Object) tiger.digest();
        return (byte[]) dig;
    }
