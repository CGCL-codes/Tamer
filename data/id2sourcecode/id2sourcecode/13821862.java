    @Test
    public void testActionNew() {
        StartParameters parameters = context.getPreferences().getStartParameters();
        parameters.setChannelCount("5");
        parameters.setSubmasterCount("3");
        parameters.setCueCount("10");
        actionNew();
        NewShowParametersDialogOperator dialog = new NewShowParametersDialogOperator();
        dialog.channelCount.setText("10");
        dialog.submasterCount.setText("6");
        dialog.cueCount.setText("20");
        dialog.buttonOk.push();
        dialog.window.waitClosed();
        Util.sleep(100);
        assertEquals(parameters.getChannelCount(), "10");
        assertEquals(parameters.getSubmasterCount(), "6");
        assertEquals(parameters.getCueCount(), "20");
        Show show = context.getShow();
        assertEquals(show.getNumberOfChannels(), 10);
        assertEquals(show.getNumberOfSubmasters(), 6);
        assertEquals(show.getCues().size(), 20);
    }
