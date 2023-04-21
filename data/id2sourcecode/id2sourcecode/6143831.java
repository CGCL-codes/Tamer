    private void doTestPatchActions(final int dimmerCount) {
        Show show = ShowBuilder.build(dimmerCount, 0, dimmerCount, "");
        context.setShow(show);
        Patch patch = new Patch(context);
        final Patcher patcher = new Patcher(context, patch);
        patcher.setUpdateLanbox(true);
        Dimmer first = context.getShow().getDimmers().get(0);
        Dimmer last = context.getShow().getDimmers().get(dimmerCount - 1);
        patcher.defaultPatchAndWait();
        patcher.loadPatchAndWait(true);
        assertTrue(first.isPatched());
        assertEquals(first.getChannelId(), 0);
        assertEquals(first.getLanboxChannelId(), 0);
        assertTrue(last.isPatched());
        assertEquals(last.getChannelId(), dimmerCount - 1);
        assertEquals(last.getLanboxChannelId(), dimmerCount - 1);
        patcher.clearPatchAndWait();
        patcher.loadPatchAndWait(true);
        assertFalse(first.isPatched());
        assertFalse(last.isPatched());
        if (dimmerCount <= Patcher.PRE_PATCH_START) {
            assertEquals(first.getLanboxChannelId(), Patcher.PRE_PATCH_START);
            assertEquals(last.getLanboxChannelId(), Patcher.PRE_PATCH_START + dimmerCount - 1);
        } else {
            assertEquals(first.getLanboxChannelId(), -1);
            assertEquals(last.getLanboxChannelId(), -1);
        }
    }
