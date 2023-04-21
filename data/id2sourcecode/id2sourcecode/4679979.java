    private static String _s_updateDigest(MessageDigest mdtMessageDigest, InputStream ism) {
        String strMethod = KTLKprOpenSignAbs._f_s_strClass + "_s_updateDigest(...)";
        if (mdtMessageDigest == null || ism == null) MySystem.s_printOutExit(strMethod, "nil arg");
        byte[] bytsBuffer = new byte[com.google.code.p.keytooliui.shared.util.jar.S_JarOutputStream.f_s_intLengthBytsBuffer];
        int intChr = 0;
        try {
            while ((intChr = ism.read(bytsBuffer)) > 0) mdtMessageDigest.update(bytsBuffer, 0, intChr);
            ism.close();
        } catch (IOException excIO) {
            excIO.printStackTrace();
            MySystem.s_printOutError(strMethod, "excIO caught");
            return null;
        }
        return KTLKprOpenSignAbs._s_b64.encode(mdtMessageDigest.digest());
    }
