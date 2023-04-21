    public static synchronized String getSHA1Hash(String msg, String algorithm, boolean hash) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] msgBytes = msg.getBytes("UTF-8");
        String toEncodeString = "{" + algorithm + "}";
        int size = 32;
        if (algorithm == "SHA") {
            size = 40;
        } else if (algorithm == "MD5") {
            size = 32;
        }
        byte[] output = new byte[size / 2];
        if (hash) {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(msgBytes);
            byte[] digest = md.digest();
            toEncodeString += Base64.encodeBytes(digest);
        } else {
            char[] c = new char[2];
            String s = "";
            int j = 0;
            for (int i = 0; i < size; i += 2) {
                c[0] = msg.charAt(i);
                c[1] = msg.charAt(i + 1);
                s = new String(c);
                output[j] = (byte) Integer.parseInt(s, 16);
                j++;
            }
            toEncodeString += Base64.encodeBytes(output);
        }
        return toEncodeString;
    }
