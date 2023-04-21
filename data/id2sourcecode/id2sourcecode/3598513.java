    private byte[] getKeySeed() {
        byte[] ks = null;
        if (bapSHA1.isSelected()) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                byte[] t = md.digest(keyseed.getText().getBytes());
                ks = new byte[16];
                System.arraycopy(t, 0, ks, 0, 16);
            } catch (NoSuchAlgorithmException nsae) {
            }
        } else if (keyseed.getText().length() == 16) {
            ks = keyseed.getText().getBytes();
        }
        return ks;
    }
