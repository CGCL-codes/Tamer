    public static boolean isLychrel(BigInteger number) {
        BigInteger numberTemp = number;
        for (int i = 0; i < 50; i++) {
            BigInteger sum = numberTemp.add(new BigInteger(new StringBuffer(numberTemp.toString()).reverse().toString()));
            if (isPalindrome(sum)) {
                System.out.println(number + " " + sum);
                return false;
            } else {
                numberTemp = sum;
            }
        }
        return true;
    }
