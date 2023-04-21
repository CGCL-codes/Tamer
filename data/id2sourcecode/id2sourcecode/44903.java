    public void computeDigest(byte[] b) {
        currentAlgorithm.reset();
        currentAlgorithm.update(b);
        byte[] hash = currentAlgorithm.digest();
        String d = "";
        for (int i = 0; i < hash.length; i++) {
            int v = hash[i] & 0xFF;
            if (v < 16) d += "0";
            d += Integer.toString(v, 16).toUpperCase() + " ";
        }
        digest.setText(d.toLowerCase());
    }
