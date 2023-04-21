    private String getHash(int punteggio3) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        byte[] points = Integer.toString(punteggio3).getBytes();
        md.update(points);
        byte[] hashish = md.digest();
        byte[] hash = new byte[16 + points.length];
        for (int i = 0; i < hashish.length; i++) {
            hash[i] = hashish[i];
        }
        hash[16] = '-';
        for (int i = 0; i < points.length; i++) {
            hash[i + 16] = points[i];
        }
        md.reset();
        md.update(hash);
        byte[] hashish_final = md.digest();
        StringBuffer hexString = new StringBuffer();
        for (byte element : hashish_final) {
            String hex = Integer.toHexString(0xFF & element);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        String hash_final = hexString.toString();
        return hash_final;
    }
