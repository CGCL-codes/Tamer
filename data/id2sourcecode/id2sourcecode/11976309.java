    public static void main(String[] args) {
        System.out.println("abba".equals(new PalindromeDecoding().decode("ab", new int[] { 0 }, new int[] { 2 })));
        System.out.println("Mississippi".equals(new PalindromeDecoding().decode("Misip", new int[] { 2, 3, 1, 7 }, new int[] { 1, 1, 2, 2 })));
        System.out.println("XYYXXYYXXYYXXYYXXYYXXYYXXYYXXYYX".equals(new PalindromeDecoding().decode("XY", new int[] { 0, 0, 0, 0 }, new int[] { 2, 4, 8, 16 })));
    }
