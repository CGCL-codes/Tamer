    public static void main(String[] args) throws Exception {
        char[] pass = { 'p' };
        String userName = "p";
        System.out.println("Entering key gen mode");
        File dirr = new File("/usr/lib/rt-credential/");
        for (int i = 0; i < 200; ++i) {
            System.out.print(i + " ");
            KeyGenerator.generateKeyPair(userName, userName, pass, i, dirr);
        }
        System.exit(0);
        String dir = null;
        profileDir = null;
        if (args.length == 2) {
            dir = args[1];
            if (dir.startsWith("\"")) {
                profileDir = new File(dir.substring(1, dir.length() - 1));
            } else {
                profileDir = new File(dir);
            }
            if (!(profileDir.exists() && profileDir.canRead() && profileDir.canWrite())) {
                System.err.println("Could not find profiles directory: " + dir + " does not exist or does not have proper permissions.");
            }
        }
        launch(CredentialAuthorApp.class, args);
    }
