    public void testPalindrome() {
        assertFalse(Loop.isPalindrome("abcdef"));
        assertFalse(Loop.isPalindrome("abccda"));
        assertFalse(Loop.isPalindrome("abccda"));
        assertFalse(Loop.isPalindrome("abcxba"));
        assertTrue(Loop.isPalindrome("a"));
        assertTrue(Loop.isPalindrome("aa"));
        assertFalse(Loop.isPalindrome("ab"));
        assertTrue(Loop.isPalindrome(""));
        assertTrue(Loop.isPalindrome("aaa"));
        assertTrue(Loop.isPalindrome("aba"));
        assertFalse(Loop.isPalindrome("abbba"));
        assertFalse(Loop.isPalindrome("abba"));
        assertFalse(Loop.isPalindrome("abbas"));
    }
