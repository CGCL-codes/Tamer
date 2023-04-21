    public byte[] get_special_map_digest() {
        byte[] result = sha_digest.digest();
        return result;
    }
