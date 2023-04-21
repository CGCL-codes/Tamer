    public byte[] getCheckValue() {
        byte[] b = smd.digest();
        if (debug) {
            System.out.print("DigestOutputStream.getCheckValue: ");
            for (int i = 0; i < b.length; i++) {
                System.out.format("%02X", b[i]);
            }
            System.out.println();
        }
        smd.reset();
        return b;
    }
