    private static int tryIterations(int number, BigInteger currentSum, int iterationNumber) {
        if (iterationNumber > iterationLimit) {
            return -1;
        }
        if (PalindromeUtil.checkPalindrome(currentSum) && currentSum.compareTo(BigInteger.valueOf(9)) > 0 && iterationNumber != 1) {
            return iterationNumber;
        }
        BigInteger reverse = PalindromeUtil.reverse(currentSum);
        BigInteger reverseSum = currentSum.add(reverse);
        if (PalindromeUtil.checkPalindrome(reverseSum) && currentSum.compareTo(BigInteger.valueOf(9)) > 0) {
            return iterationNumber;
        }
        int attempt = tryIterations(number, reverseSum, iterationNumber + 1);
        if (attempt != -1) {
            if (reverseSum.compareTo(BigInteger.valueOf(10001l)) < 0) {
                int ival = reverseSum.intValue();
                attempts[ival] = (attempt - iterationNumber);
            }
        }
        return attempt;
    }
