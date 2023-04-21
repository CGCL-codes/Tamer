    public void digestData() throws IOException, PKCS11Exception {
        byte[] buffer = new byte[1024];
        byte[] helpBuffer, testDigest;
        int bytesRead;
        System.out.println("Digest Data");
        myPKCS11Module_.C_DigestInit(session_, digestMechanism_);
        try {
            messageDigest_ = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
            System.out.println(e);
        }
        InputStream dataInput = new FileInputStream(file_);
        while ((bytesRead = dataInput.read(buffer, 0, buffer.length)) >= 0) {
            helpBuffer = new byte[bytesRead];
            System.arraycopy(buffer, 0, helpBuffer, 0, bytesRead);
            myPKCS11Module_.C_DigestUpdate(session_, helpBuffer);
            messageDigest_.update(helpBuffer);
            Arrays.fill(helpBuffer, (byte) 0);
        }
        Arrays.fill(buffer, (byte) 0);
        digest_ = myPKCS11Module_.C_DigestFinal(session_);
        testDigest = messageDigest_.digest();
        System.out.println("PKCS11digest:" + Functions.toHexString(digest_));
        System.out.println("TestDigest  :" + Functions.toHexString(testDigest));
        System.out.println("FINISHED\n");
    }
