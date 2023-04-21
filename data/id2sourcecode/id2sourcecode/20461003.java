    public String computeChecksumSHA256(byte[] message) {
        try {
            byte[] food = message;
            MessageDigest sha256 = MessageDigest.getInstance("SHA256", "BC");
            sha256.update(food);
            byte[] poop = sha256.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < poop.length; i++) {
                sb.append(Integer.toString((poop[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.SEVERE, "Algorithm SHA256 not supported.", ex);
        } catch (NoSuchProviderException ex) {
            logger.log(Level.SEVERE, "Provider BC not supported.", ex);
        }
        return null;
    }
