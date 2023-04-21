    public void testIsPalindrome() {
        assertTrue(PalindromeChecker.test("123321"));
        assertFalse(PalindromeChecker.test("123421"));
        assertTrue(PalindromeChecker.test("1234321"));
        assertTrue(PalindromeChecker.test("racecar"));
    }
