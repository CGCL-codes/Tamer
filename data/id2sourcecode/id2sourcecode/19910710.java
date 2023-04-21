    public static void main(String[] args) {
        Integer max = null;
        for (int x = 999; x > 0; x--) {
            for (int y = 999; y > 0; y--) {
                int possiblePalindrome = x * y;
                if (PalindromeChecker.test("" + possiblePalindrome)) {
                    if (max == null) {
                        max = possiblePalindrome;
                    }
                    if (possiblePalindrome > max) {
                        max = possiblePalindrome;
                    }
                }
            }
        }
        System.out.println(max);
    }
