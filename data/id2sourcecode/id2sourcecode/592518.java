    public void testIsPalindrome() {
        System.out.println("isPalindrome");
        assertEquals(Algorithms.isPalindrome("A"), true);
        assertEquals(Algorithms.isPalindrome("BA"), false);
        assertEquals(Algorithms.isPalindrome("BB"), true);
        assertEquals(Algorithms.isPalindrome("BAB"), true);
        assertEquals(Algorithms.isPalindrome("BAA"), false);
        assertEquals(Algorithms.isPalindrome("ABB"), false);
    }
