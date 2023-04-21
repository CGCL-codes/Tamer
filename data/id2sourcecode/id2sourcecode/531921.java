    public static void main(String[] args) {
        int largestPalindrome = 0;
        for (int i = 100; i < 1000; i++) {
            for (int j = 100; j < 1000; j++) {
                int product = i * j;
                if (isPalindrome(String.valueOf(product))) {
                    System.out.println(product);
                    if (product > largestPalindrome) {
                        largestPalindrome = product;
                    }
                }
            }
        }
        System.out.println(largestPalindrome);
    }
