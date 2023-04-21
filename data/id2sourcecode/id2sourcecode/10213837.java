    public void screenshot(IUIContext ui, String title) {
        logEntry2(title);
        TestCase.assertNotNull(ui);
        TestCase.assertNotNull(title);
        TestCase.assertTrue("THE TITLE <" + title + "> IS NOT A VALID FILE NAME", Pattern.matches("[a-zA-Z]*[a-zA-Z0-9\\.]*[a-zA-Z0-9]+", title));
        ScreenCapture.createScreenCapture(title);
        logExit2();
    }
