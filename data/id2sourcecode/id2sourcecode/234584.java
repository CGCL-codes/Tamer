    public void testPalindrome() {
        assertFalse(isPalindrome("abcdef"));
        assertFalse(isPalindrome("abccda"));
        assertTrue(isPalindrome("abccba"));
        assertFalse(isPalindrome("abcxba"));
        assertTrue(isPalindrome("a"));
        assertTrue(isPalindrome("aa"));
        assertFalse(isPalindrome("ab"));
        assertTrue(isPalindrome(""));
        assertTrue(isPalindrome("aaa"));
        assertTrue(isPalindrome("aba"));
        assertTrue(isPalindrome("abbba"));
        assertTrue(isPalindrome("abba"));
        assertFalse(isPalindrome("abbaa"));
        assertFalse(isPalindrome("abcda"));
    }
