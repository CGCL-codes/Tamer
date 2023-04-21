    public static String printSkey(PrivateKey skey) {
        if (!init) {
            init();
        }
        md.update(skey.getEncoded());
        byte[] hash = md.digest();
        return ByteUtils.print_bytes(hash, 0, AntiquityUtils.num_bytes_to_print);
    }
