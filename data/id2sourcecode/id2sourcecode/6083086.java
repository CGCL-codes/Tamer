    protected void engineInit(int opmode, Key key, SecureRandom random) throws InvalidKeyException {
        if (SigDebug.isActive()) SigDebug.write("MSRSACipherFactoryImpl: engineInit entered");
        buffer.reset();
        if (opmode != Cipher.ENCRYPT_MODE && opmode != Cipher.DECRYPT_MODE) throw new InvalidKeyException("MSRSA opmode must be either encrypt or decrypt");
        ciphermode = opmode;
    }
