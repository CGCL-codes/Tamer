    protected int findTheLargestPalindromeProductOfTwo3DigitNumbers() {
        int palindrome = 0;
        int max3DigitNumber = 999;
        int min3DigitNumber = 900;
        int max = max3DigitNumber * max3DigitNumber;
        int min = min3DigitNumber * min3DigitNumber;
        for (int i = max; i >= min; i--) {
            if (isPalindrome(i)) {
                for (int j = min3DigitNumber; j <= max3DigitNumber; j++) {
                    if (i % j == 0 && i / j <= max3DigitNumber) {
                        return i;
                    }
                }
            }
        }
        return palindrome;
    }
