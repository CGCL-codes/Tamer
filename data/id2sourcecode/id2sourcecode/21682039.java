    public void checkMD5() throws Exception {
        showMessage("Checking authenticy of downloaded file");
        FileInputStream fis = new FileInputStream(frinikaFile);
        MessageDigest md = MessageDigest.getInstance("MD5");
        DigestInputStream dis = new DigestInputStream(fis, md);
        progressBar.setValue(0);
        progressBar.setMinimum(0);
        progressBar.setMaximum((int) frinikaFile.length());
        byte[] b = new byte[BUFSIZE];
        int c;
        while ((c = dis.read(b)) != -1) {
            progressBar.setValue(progressBar.getValue() + c);
        }
        BigInteger calculatedMd5 = new BigInteger(md.digest());
        System.out.println("Expected MD5 sum = '" + md5.toString(16) + "'");
        System.out.println("MD5 sum = '" + calculatedMd5.toString(16) + "'");
        if (!md5.equals(calculatedMd5)) {
            frinikaFile.delete();
            throw new InvalidMD5Exception("Downloaded file is not authentic, and is now deleted - please check your download sources");
        }
    }
