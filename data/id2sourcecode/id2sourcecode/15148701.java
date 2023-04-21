    @Test
    public void defaultPatch() {
        assertEquals(patch.getDetail(0).getDimmer().getChannel().getId(), 0);
        assertEquals(patch.getDetail(23).getDimmer().getChannel().getId(), 23);
        assertNull(patch.getDetail(24).getDimmer().getChannel());
        assertNull(patch.getDetail(31).getDimmer().getChannel());
        assertTrue(context.getShow().getChannels().get(0).isPatched());
        assertTrue(context.getShow().getChannels().get(23).isPatched());
    }
