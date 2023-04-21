    protected int findTheLargestPalindromeProductOfTwo3DigitNumbers2() {
        int palindrome = 0;
        for (int a = 9; a > 0; a--) {
            for (int b = 9; b >= 0; b--) {
                for (int c = 9; c >= 0; c--) {
                    palindrome = 100001 * a + 10010 * b + 1100 * c;
                    for (int k = 90; k >= 10; k--) {
                        if (palindrome % k == 0) {
                            int n = palindrome / k / 11;
                            if (n >= 100 && n <= 999) {
                                return palindrome;
                            }
                        }
                    }
                }
            }
        }
        return palindrome;
    }
