    protected byte[] engineSign() {
        if (SigDebug.isActive()) SigDebug.write("MSSHARSASignFactoryImpl: engineSign: entered");
        if (!signOpInProgress) {
            System.out.println("MSSHARSASignFactoryImpl: error - throw exception");
            return null;
        }
        byte[] hash = MD.digest();
        byte[] mssig = MSF.MSrsaSignHash(hash, (byte[]) null, MessageDigestType, certAlias, pin);
        signOpInProgress = false;
        return mssig;
    }
