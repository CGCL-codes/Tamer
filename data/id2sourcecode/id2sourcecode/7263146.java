    public byte[] getSymmetricKey(int keyBits) {
        if (keyBits < 1 || keyBits > 512) throw new IllegalArgumentException("bit length not positiv");
        if (root.privateKey == null) computeKeys(owner.getParent());
        try {
            MessageDigest md = null;
            if (keyBits <= 160) md = MessageDigest.getInstance("SHA-1"); else if (keyBits <= 256) md = MessageDigest.getInstance("SHA-256"); else md = MessageDigest.getInstance("SHA-512");
            BigInteger rootKey = root.getPrivate().getX();
            byte digest[] = md.digest(rootKey.toByteArray());
            int keyBytes = (keyBits + 7) / 8;
            byte output[] = new byte[keyBytes];
            System.arraycopy(digest, 0, output, 0, output.length);
            output[0] &= 255 >>> 8 * keyBytes - keyBits;
            return output;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
