    public Object[] encrypt(InputStream in, OutputStream out, long fs) throws Exception {
        log.debug("Building Message Digest");
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance(VALIDATIONDIGEST);
        } catch (NoSuchAlgorithmException algException) {
            throw new Exception("Failed to get the defined file validation digest \"" + VALIDATIONDIGEST + "\" - ERROR: " + algException);
        }
        algorithm.reset();
        log.debug("Building Cipher Stream");
        CipherOutputStream cos = new CipherOutputStream(out, ecipher);
        byte[] buffer = new byte[2048];
        int bytesRead;
        log.debug("Starting file read");
        long fileSize = 0L;
        while (true) {
            if (fs >= 0L) {
                int foo;
                if (buffer.length < fs) {
                    foo = buffer.length;
                } else {
                    foo = (int) fs;
                }
                bytesRead = in.read(buffer, 0, foo);
                fs -= foo;
            } else {
                bytesRead = in.read(buffer);
            }
            if (bytesRead == -1) {
                break;
            }
            cos.write(buffer, 0, bytesRead);
            algorithm.update(buffer, 0, bytesRead);
            fileSize = fileSize + bytesRead;
            if (fs == 0L) {
                break;
            }
        }
        cos.close();
        log.debug("File Size is: " + fileSize);
        java.util.Arrays.fill(buffer, (byte) 0);
        byte messageDigest[] = algorithm.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        String MD5String = hexString.toString().toLowerCase();
        log.debug("File MD5 Hash: " + MD5String);
        Object[] returnValues = { MD5String, fileSize };
        return returnValues;
    }
