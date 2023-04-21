    public static void printUsage() {
        output_.println("Usage: GenerateKeyPair <PKCS#11 module> <X.509 encoded public key output file>");
        output_.println(" e.g.: GenerateKeyPair pk2priv.dll publicKey.xpk");
        output_.println("The given DLL must be in the search path of the system.");
    }
