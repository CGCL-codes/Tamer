    @Test
    public void testCaseInsensitivity() {
        assertTrue(session.getChannel("#FOO") != null);
        assertTrue(session.getChannel("#foo") != null);
        assertTrue(session.getChannel("#FoO") != null);
        assertTrue(session.getChannel("#fOO") != null);
    }
