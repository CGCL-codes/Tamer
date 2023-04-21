    @Test
    public void testActionNewCancel() {
        StartParameters parameters = context.getPreferences().getStartParameters();
        parameters.setChannelCount("5");
        parameters.setSubmasterCount("3");
        parameters.setCueCount("10");
        Show before = context.getShow();
        actionNew();
        NewShowParametersDialogOperator dialog = new NewShowParametersDialogOperator();
        dialog.buttonCancel.push();
        dialog.window.waitClosed();
        assertEquals(parameters.getChannelCount(), "5");
        assertEquals(parameters.getSubmasterCount(), "3");
        assertEquals(parameters.getCueCount(), "10");
        Show after = context.getShow();
        assertEquals(after.getNumberOfChannels(), before.getNumberOfChannels());
        assertEquals(after.getNumberOfSubmasters(), before.getNumberOfSubmasters());
        assertEquals(after.getCues().size(), before.getCues().size());
    }
