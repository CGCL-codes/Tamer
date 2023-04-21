    private boolean checkHash(Index index, String installTo) {
        File localFile = new File(installTo + index.getURL());
        if (!localFile.exists()) {
            return true;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            InputStream is = new FileInputStream(localFile);
            byte[] buffer = new byte[8192];
            int read = 0;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            is.close();
            System.out.println("File: " + index.getURL());
            System.out.println("Index: " + index.getHash());
            System.out.println("Output: " + output);
            if (!output.equals(index.getHash())) {
                System.out.println("return true");
                return true;
            }
            return false;
        } catch (Exception e) {
            Logger.getInstance().logException(e, false);
        }
        return true;
    }
