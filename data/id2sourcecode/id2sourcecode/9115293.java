    private static final String createUUID(final Random random) {
        StringBuffer preMD5 = new StringBuffer();
        long time = System.currentTimeMillis();
        long rand = random.nextLong();
        preMD5.append(RandomUtil.getHostName());
        preMD5.append(":");
        preMD5.append(Long.toString(time));
        preMD5.append(":");
        preMD5.append(Long.toString(rand));
        MessageDigest digest = RandomUtil.getMessageDigest();
        digest.update(preMD5.toString().getBytes());
        byte[] array = digest.digest();
        StringBuffer postMD5 = new StringBuffer();
        for (int j = 0; j < array.length; ++j) {
            int b = array[j] & 0xFF;
            if (b < 0x10) {
                postMD5.append('0');
            }
            postMD5.append(Integer.toHexString(b));
        }
        StringBuffer formatted = new StringBuffer();
        formatted.append(postMD5.substring(0, 8).toUpperCase());
        formatted.append("-");
        formatted.append(postMD5.substring(8, 12).toUpperCase());
        formatted.append("-");
        formatted.append(postMD5.substring(12, 16).toUpperCase());
        formatted.append("-");
        formatted.append(postMD5.substring(16, 20).toUpperCase());
        formatted.append("-");
        formatted.append(postMD5.substring(20).toUpperCase());
        return formatted.toString();
    }
