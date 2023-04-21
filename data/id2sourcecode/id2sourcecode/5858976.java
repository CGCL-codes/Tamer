    public Login80(int u, int seed, String password, Status initialStatus) {
        super(TYPE_LOGIN80);
        version = App.getInstance().getGGClientVersion();
        uin = u;
        description = initialStatus.getDescription();
        status = initialStatus.getGGStatus();
        try {
            MessageDigest alg = MessageDigest.getInstance("SHA1");
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.putInt(Integer.reverseBytes(seed));
            byte[] seedBytes = bb.array();
            byte[] passwordBytes;
            passwordBytes = password.getBytes("UTF-8");
            alg.update(passwordBytes);
            alg.update(seedBytes);
            byte[] result = alg.digest();
            Arrays.fill(hash, (byte) 0);
            ByteBuffer hashBuffer = ByteBuffer.wrap(hash);
            hashBuffer.put(result);
        } catch (Exception e) {
            ;
        }
        lang[0] = 'p';
        lang[1] = 'l';
    }
