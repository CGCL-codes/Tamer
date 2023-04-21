    @Test
    public void testValidCreateShowParameters() {
        parameters.setChannelCount("120");
        parameters.setSubmasterCount("12");
        parameters.setCueCount("5");
        ShowParametersDialogOperator dialog = openDialog();
        assertTrue(dialog.buttonOk.isEnabled());
        assertTrue(dialog.buttonCancel.isEnabled());
        assertTrue(dialog.channelCount.isEnabled());
        assertTrue(dialog.submasterCount.isEnabled());
        assertEquals(dialog.channelCount.getText(), "120");
        assertEquals(dialog.submasterCount.getText(), "12");
        dialog.channelCount.setText("555");
        assertFalse(dialog.buttonOk.isEnabled());
        dialog.channelCount.setText("60");
        dialog.submasterCount.setText("6");
        assertTrue(dialog.buttonOk.isEnabled());
        dialog.buttonOk.push();
        dialog.window.waitClosed();
        assertFalse(target.isCancelled());
        assertEquals(parameters.getChannelCount(), "60");
        assertEquals(parameters.getSubmasterCount(), "6");
    }
