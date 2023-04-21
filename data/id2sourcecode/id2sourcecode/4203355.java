    @Override
    public Object call(Object... args) {
        Args.massage(FunctionSpec.value, args);
        String s = Ops.toStr(args[0]);
        byte[] data = Util.stringToBytes(s);
        MessageDigest digest;
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new SuException("Can't access MD5", e);
        }
        digest.update(data);
        return Util.bytesToString(digest.digest());
    }
