    private void takeWebDriverScreenshot(String saveTo) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(saveTo));
            scrFile.delete();
        } catch (IOException e) {
        }
    }
