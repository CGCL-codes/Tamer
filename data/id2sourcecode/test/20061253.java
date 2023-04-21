    private void setOneAuth(String secret) throws Exception {
        String oneSecret = secret;
        try {
            if (oneSecret == null) {
                String oneAuthEnv = System.getenv("ONE_AUTH");
                File authFile;
                if (oneAuthEnv != null && oneAuthEnv.length() != 0) {
                    authFile = new File(oneAuthEnv);
                } else {
                    authFile = new File(System.getenv("HOME") + "/.one/one_auth");
                }
                oneSecret = (new BufferedReader(new FileReader(authFile))).readLine();
            }
            String[] token = oneSecret.split(":");
            if (token.length != 2) {
                throw new Exception("Wrong format for authorization string: " + oneSecret + "\nFormat expected is user:password");
            }
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(token[1].getBytes());
            String hash = "";
            for (byte aux : digest) {
                int b = aux & 0xff;
                if (Integer.toHexString(b).length() == 1) {
                    hash += "0";
                }
                hash += Integer.toHexString(b);
            }
            oneAuth = token[0] + ":" + hash;
        } catch (FileNotFoundException e) {
            throw new Exception("ONE_AUTH file not present");
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("Error initializing MessageDigest with SHA-1");
        }
    }
