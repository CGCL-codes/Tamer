    @Test
    public void testActionSetup() {
        Dirty dirty = context.getDirtyShow();
        Show before = ShowBuilder.build(dirty, 10, 5, 10, "");
        context.setShow(before);
        actionSetup();
        ShowParametersDialogOperator dialog = new ShowParametersDialogOperator();
        assertEquals(dialog.channelCount.getText(), "10");
        assertEquals(dialog.submasterCount.getText(), "5");
        dialog.channelCount.setText("12");
        dialog.submasterCount.setText("7");
        dialog.buttonOk.push();
        dialog.window.waitClosed();
        StartParameters parameters = context.getPreferences().getStartParameters();
        assertEquals(parameters.getChannelCount(), "12");
        assertEquals(parameters.getSubmasterCount(), "7");
        Show after = context.getShow();
        assertEquals(after.getNumberOfChannels(), 12);
        assertEquals(after.getNumberOfSubmasters(), 7);
    }
