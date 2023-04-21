    @Test
    public void testNickCaseInsensitivity() {
        Channel chan = session.getChannel("#ubuntu");
        assertTrue(chan.getNicks().contains("Rosco"));
        assertTrue(chan.getNicks().contains("RoScO"));
        assertTrue(chan.getNicks().contains("ROSCO"));
        assertTrue(chan.getNicks().indexOf("Rosco") != -1);
        assertTrue(chan.getNicks().indexOf("RoScO") != -1);
        assertTrue(chan.getNicks().indexOf("ROSCO") != -1);
    }
