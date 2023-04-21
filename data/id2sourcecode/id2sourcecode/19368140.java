    public String getFileHash(File f) {
        try {
            byte[] bytes = getBytesFromFile(f);
            MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
            mdAlgorithm.update(bytes);
            byte[] digest = mdAlgorithm.digest();
            StringBuffer hexString = new StringBuffer();
            String plainText;
            for (int i = 0; i < digest.length; i++) {
                plainText = Integer.toHexString(0xFF & digest[i]);
                if (plainText.length() < 2) {
                    plainText = "0" + plainText;
                }
                hexString.append(plainText);
            }
            return hexString.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
