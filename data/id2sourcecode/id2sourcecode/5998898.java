    private void digest(String msg, MessageDigest digest) {
        System.out.println("Using algorithm: " + digest.getAlgorithm());
        digest.reset();
        byte[] bytes = msg.getBytes();
        byte[] out = digest.digest(bytes);
        BASE64Encoder enc = new BASE64Encoder();
        System.out.println(enc.encode(out));
    }
