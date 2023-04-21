    protected int engineSign(byte[] outbuf, int offset, int len) {
        if (SigDebug.isActive()) SigDebug.write("MSSHARSASignFactoryImpl: engineSign: entered");
        if (!signOpInProgress) {
            System.out.println("MSSHARSASignFactoryImpl: error - throw exception");
            return 0;
        }
        byte[] hash = MD.digest();
        byte[] mssig = MSF.MSrsaSignHash(hash, (byte[]) null, MessageDigestType, certAlias, pin);
        java.lang.System.arraycopy((Object) mssig, 0, (Object) outbuf, offset, mssig.length);
        signOpInProgress = false;
        return mssig.length;
    }
