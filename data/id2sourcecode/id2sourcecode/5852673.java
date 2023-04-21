    public String digest(byte[] msg) {
        try {
            final MessageDigest dig = MessageDigest.getInstance("MD2");
            final BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(dig.digest(msg));
        } catch (Exception e) {
            LOG.fatal("Digest MD2 not supported");
            return "digest not supported";
        }
    }
