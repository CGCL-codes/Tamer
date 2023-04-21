    public BlowfishEasy(String sPassword) {
        MessageDigest mds = null;
        byte[] hash;
        try {
            mds = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException nsae) {
        }
        for (int nI = 0, nC = sPassword.length(); nI < nC; nI++) {
            mds.update((byte) (sPassword.charAt(nI) & 0x0ff));
        }
        hash = mds.digest();
        m_bfish = new BlowfishCBC(hash, 0, hash.length, 0);
    }
