    @Test
    public void clearPatch() {
        patch.clearPatch();
        assertNull(patch.getDetail(0).getDimmer().getChannel());
        assertNull(patch.getDetail(23).getDimmer().getChannel());
        assertNull(patch.getDetail(24).getDimmer().getChannel());
        assertNull(patch.getDetail(31).getDimmer().getChannel());
    }
