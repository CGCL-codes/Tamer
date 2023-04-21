    private byte[] makeClientKey(long authKey, long introKey) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        md5.update((byte) ((authKey >> 24) & 0xff));
        md5.update((byte) ((authKey >> 16) & 0xff));
        md5.update((byte) ((authKey >> 8) & 0xff));
        md5.update((byte) (authKey & 0xff));
        md5.update((byte) ((introKey >> 24) & 0xff));
        md5.update((byte) ((introKey >> 16) & 0xff));
        md5.update((byte) ((introKey >> 8) & 0xff));
        md5.update((byte) (introKey & 0xff));
        Random random = new Random();
        int randomNumber = random.nextInt();
        md5.update((byte) ((randomNumber >> 24) & 0xff));
        md5.update((byte) ((randomNumber >> 16) & 0xff));
        md5.update((byte) ((randomNumber >> 8) & 0xff));
        md5.update((byte) (randomNumber & 0xff));
        int time = (int) System.currentTimeMillis();
        md5.update((byte) ((time >> 24) & 0xff));
        md5.update((byte) ((time >> 16) & 0xff));
        md5.update((byte) ((time >> 8) & 0xff));
        md5.update((byte) (time & 0xff));
        return md5.digest();
    }
