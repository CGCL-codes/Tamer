    @Override
    protected void runImpl() {
        L2GameClient client = this.getClient();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] result = md.digest(_reply);
            if (Arrays.equals(result, VALID)) {
                client.setGameGuardOk(true);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
