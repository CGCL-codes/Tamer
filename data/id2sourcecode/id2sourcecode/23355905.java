    public static boolean checkPalindrome(BigInteger currentSum) {
        if (currentSum == null) {
            return false;
        }
        return checkPalindrome(currentSum.toString());
    }
