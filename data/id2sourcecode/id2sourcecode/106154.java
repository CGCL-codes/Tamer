    public static boolean isPalindrome(BigInteger number) {
        return number.toString().equals(new StringBuffer(number.toString()).reverse().toString());
    }
