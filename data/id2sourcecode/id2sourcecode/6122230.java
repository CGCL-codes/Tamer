    public String digest() {
        byte[] hash = message.digest();
        String d = "";
        for (int i = 0; i < hash.length; i++) {
            int v = hash[i] & 0xff;
            if (v < 16) d += "0";
            d += Integer.toString(v, 16).toUpperCase() + defaultSeparator;
        }
        return d;
    }
