    public void generateKeypair() throws IOException, InterruptedException {
        Rand.initialize();
        String tempKeyringPublic;
        String tempKeyringSecret;
        String rand;
        do {
            rand = Rand.getString(10, "ABCDEF0123456789");
            tempKeyringPublic = pathTempDir + "/" + rand + ".pub";
            tempKeyringSecret = pathTempDir + "/" + rand + ".sec";
        } while (FileOperation.exists(tempKeyringPublic) || FileOperation.exists(tempKeyringSecret));
        if (!FileOperation.exists(pathKeyringPublic)) FileOperation.create(pathKeyringPublic);
        if (!FileOperation.exists(pathKeyringSecret)) FileOperation.create(pathKeyringSecret);
        String name = "OPP Autokey [" + rand + "]";
        String keyInfo = "Key-Type: DSA\n" + "Key-Length: 1024\n" + "Subkey-Type: ELG-E\n" + "Subkey-Length: 2048\n" + "Expire-Date: 0\n" + "Name-Real: " + name + "\n" + "%pubring " + tempKeyringPublic + "\n" + "%secring " + tempKeyringSecret + "\n" + "%commit\n" + "%echo " + name + " created!\n";
        runGnuPG(pathGPG + " --batch --gen-key", keyInfo, Stream.STDOUT);
        String sPublicKeyCode = runGnuPG(pathGPG + " -a --no-default-keyring --keyring " + tempKeyringPublic + " --export", "", Stream.STDOUT);
        String sSecretKeyCode = runGnuPG(pathGPG + " -a --no-default-keyring --secret-keyring " + tempKeyringSecret + " --export-secret-keys", "", Stream.STDOUT);
        importKey(sPublicKeyCode);
        importKey(sSecretKeyCode);
        FileOperation.delete(tempKeyringPublic);
        FileOperation.delete(tempKeyringSecret);
    }
