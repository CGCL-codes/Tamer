    public static Identifier createIdentifier(String str, int len) {
        byte[] identifier;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] input = str.getBytes("UTF-8");
            identifier = md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        byte[] wrapper = new byte[len];
        if (wrapper.length >= identifier.length) {
            System.arraycopy(identifier, 0, wrapper, 0, identifier.length);
        } else {
            for (int i = 0; i < wrapper.length; i++) {
                wrapper[i] = identifier[i];
            }
        }
        return new ByteArrayIdentifier(wrapper);
    }
