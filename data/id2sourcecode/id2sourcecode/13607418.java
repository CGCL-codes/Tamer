    public static void printUsage() {
        output_.println("Usage: GenerateKeyPair2048 <PKCS#11 module> <X.509 encoded public key file>");
        output_.println(" e.g.: GenerateKeyPair2048 pk2priv.dll publicKey.xpk");
        output_.println("The given DLL must be in the search path of the system.");
    }
