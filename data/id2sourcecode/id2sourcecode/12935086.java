    public static String getMD5HashString(String inputString) {
        String hashString = "";
        MessageDigest md5;
        byte[] digest;
        int curNum;
        final char[] hexTable = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            md5 = MessageDigest.getInstance("MD5");
            digest = md5.digest(inputString.getBytes());
            for (int i = 0; i < digest.length; i++) {
                curNum = digest[i];
                if (curNum < 0) curNum = curNum + 256;
                hashString = hashString + hexTable[curNum / 16] + hexTable[curNum % 16];
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5 not supported by current Java VM!");
            System.exit(1);
        }
        return hashString;
    }
