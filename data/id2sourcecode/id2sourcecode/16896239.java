    public static void main(String[] args) {
        BigInt sum = BigInt.from(0L);
        for (int i = 0; i < 1000000; i++) {
            if (isDecimalPalindrome(i) && isBinaryPalindrome(i)) {
                sum = sum.plus(String.valueOf(i));
            }
        }
        System.out.println(sum);
    }
