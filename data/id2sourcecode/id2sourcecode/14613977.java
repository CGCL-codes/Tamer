    private static String _s_getFingerprint(byte[] bytsCertificate, String strAlgorithm) {
        String strMethod = UtilCrtX509._f_s_strClass + "." + "_s_getFingerprint(bytsCertificate, strAlgorithm)";
        if (bytsCertificate == null || strAlgorithm == null) {
            MySystem.s_printOutExit(strMethod, "nil arg");
        }
        String str = new String("");
        MessageDigest mdt = null;
        try {
            mdt = MessageDigest.getInstance(strAlgorithm);
        } catch (NoSuchAlgorithmException excNoSuchAlgorithm) {
            excNoSuchAlgorithm.printStackTrace();
            MySystem.s_printOutWarning(strMethod, "excNoSuchAlgorithm caught");
            return null;
        }
        mdt.update(bytsCertificate);
        byte[] bytsDigest = mdt.digest();
        for (int i = 0; i < bytsDigest.length; i++) {
            if (i != 0) str += ":";
            int b = bytsDigest[i] & 0xff;
            String strHexa = Integer.toHexString(b).toUpperCase();
            if (strHexa.length() == 1) str += "0";
            str += strHexa;
        }
        return str;
    }
