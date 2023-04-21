        static void updateDigest(MessageDigest md, byte[] pad1, byte[] pad2, SecretKey masterSecret) {
            byte[] keyBytes = "RAW".equals(masterSecret.getFormat()) ? masterSecret.getEncoded() : null;
            if (keyBytes != null) {
                md.update(keyBytes);
            } else {
                digestKey(md, masterSecret);
            }
            md.update(pad1);
            byte[] temp = md.digest();
            if (keyBytes != null) {
                md.update(keyBytes);
            } else {
                digestKey(md, masterSecret);
            }
            md.update(pad2);
            md.update(temp);
        }
