    @Test
    public void copyDimmerNamesToChannels() {
        patch.copyDimmerNamesToChannels();
        assertEquals(context.getShow().getChannels().get(0).getName(), "Dimmer 1");
    }
