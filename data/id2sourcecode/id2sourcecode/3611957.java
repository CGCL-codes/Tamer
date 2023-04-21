    private void mkMaster() throws IOException {
        byte[] expansion[] = { { (byte) 0x41 }, { (byte) 0x42, (byte) 0x42 }, { (byte) 0x43, (byte) 0x43, (byte) 0x43 } };
        MessageDigest md = null;
        MessageDigest sd = null;
        byte[] tmp = new byte[preMaster.length + crand.length + srand.length];
        System.arraycopy(preMaster, 0, tmp, 0, preMaster.length);
        System.arraycopy(crand, 0, tmp, preMaster.length, crand.length);
        System.arraycopy(srand, 0, tmp, preMaster.length + crand.length, srand.length);
        try {
            md = MessageDigest.getInstance("MD5");
            sd = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No MD5 or SHA");
        }
        master = new byte[48];
        try {
            for (int i = 0; i < 3; i++) {
                md.update(preMaster, 0, preMaster.length);
                sd.update(expansion[i], 0, expansion[i].length);
                byte[] res = new byte[SHA_SIZE];
                sd.update(tmp, 0, tmp.length);
                sd.digest(res, 0, res.length);
                md.update(res, 0, res.length);
                md.digest(master, i << 4, MD5_SIZE);
            }
        } catch (DigestException e) {
            throw new RuntimeException("digest exception");
        }
    }
