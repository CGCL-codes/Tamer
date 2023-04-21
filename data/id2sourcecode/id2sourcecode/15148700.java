    @Test
    public void showContextListener() {
        patch.clearPatch();
        assertEquals(patch.getDimmerCount(), 32);
        assertEquals(patch.getDetails().size(), 32);
        PatchDetail detail = patch.getDetail(0);
        assertEquals(detail.getDimmer().getId(), 0);
        assertEquals(detail.getDimmer().getChannel(), null);
        Show show = ShowBuilder.build(512, 0, 512, "TestShow");
        context.setShow(show);
        assertEquals(patch.getDimmerCount(), 512);
        detail = patch.getDetail(511);
        assertEquals(detail.getDimmer().getId(), 511);
        assertEquals(detail.getDimmer().getChannel(), null);
    }
