    @Test
    public void get() {
        PatchParameters parameters = new PatchParameters();
        parameters.add(1, 2);
        parameters.add(3, 4);
        assertEquals(parameters.getDimmerId(0), 1);
        assertEquals(parameters.getChannelId(0), 2);
        assertEquals(parameters.getDimmerId(1), 3);
        assertEquals(parameters.getChannelId(1), 4);
    }
