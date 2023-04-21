    public static void main(String[] args) {
        long maxProduct = 0;
        String result = null;
        for (int i = 999; i > 99; i--) {
            for (int j = 999; j > 99; j--) {
                long product = i * j;
                boolean isPalindrome = PalindromeUtil.checkPalindrome(product);
                if (isPalindrome) {
                    if (product > maxProduct) {
                        maxProduct = product;
                        result = "Numbers are: " + i + ", " + j + " with product: " + product;
                    }
                }
            }
        }
        System.out.println(result);
    }
