    @Test
    public void palindromeTest() {
        assertEquals(0, 1 / 2);
        assertTrue(LlqUtil.isPalindrome(""));
        assertTrue(LlqUtil.isPalindrome("a"));
        assertTrue(LlqUtil.isPalindrome("aa"));
        assertTrue(LlqUtil.isPalindrome("aaa"));
        assertTrue(LlqUtil.isPalindrome("aaaa"));
        assertTrue(LlqUtil.isPalindrome("aba"));
        assertTrue(LlqUtil.isPalindrome("abba"));
        assertTrue(LlqUtil.isPalindrome("abcba"));
        assertTrue(LlqUtil.isPalindrome("abccba"));
        assertFalse(LlqUtil.isPalindrome("ab"));
        assertFalse(LlqUtil.isPalindrome("abc"));
        assertFalse(LlqUtil.isPalindrome("abca"));
        assertFalse(LlqUtil.isPalindrome("abab"));
        assertFalse(LlqUtil.isPalindrome("abb"));
        assertFalse(LlqUtil.isPalindrome("abbaa"));
        assertFalse(LlqUtil.isPalindrome("abcdef"));
    }
