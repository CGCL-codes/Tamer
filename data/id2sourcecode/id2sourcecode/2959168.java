    public String confirmationDigest(Confirmation confirmation) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(confirmation.getOwner().getEmail()).append(";");
        buffer.append(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(confirmation.getOwner().getCreated())).append(";");
        buffer.append(confirmation.getKey());
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(hashAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest;
        byte[] message = buffer.toString().getBytes();
        md.update(message);
        digest = md.digest();
        return new String(Hex.encodeHex(digest));
    }
