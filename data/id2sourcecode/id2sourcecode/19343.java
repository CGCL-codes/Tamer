    public static int[] hashCalculator(byte[] hayStack, byte[] needle) {
        MessageDigest hash = null;
        try {
            hash = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
        }
        for (int i = 0; i <= hayStack.length; i++) {
            for (int j = 0; j <= hayStack.length - i; j++) {
                hash.update(hayStack, j, i);
                byte[] dig = hash.digest();
                if (Arrays.equals(dig, needle)) return new int[] { j, i };
            }
        }
        return null;
    }
