    public static String printPkey(PublicKey pkey) {
        if (!init) {
            init();
        }
        md.update(pkey.getEncoded());
        byte[] hash = md.digest();
        return ByteUtils.print_bytes(hash, 0, AntiquityUtils.num_bytes_to_print);
    }
