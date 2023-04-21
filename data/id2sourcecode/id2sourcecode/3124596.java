    static int mix_it(CharPtr address, int offset, int len) {
        char[] buf;
        CharPtr storeaddress = new CharPtr();
        CharPtr pom = new CharPtr();
        int i;
        if ((buf = new char[2 * len]) == null) return 1;
        storeaddress = address;
        pom.set(buf, 0);
        for (i = 0; i < len; i++) {
            pom.write(address.read());
            pom.inc();
            pom.write(address.read() + offset);
            pom.inc();
            address.inc();
        }
        pom.set(buf, 0);
        for (i = 0; i < 2 * len; i++) {
            storeaddress.writeinc(pom.readinc());
        }
        buf = null;
        return 0;
    }
